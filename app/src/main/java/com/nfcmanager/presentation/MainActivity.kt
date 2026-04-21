package com.nfcmanager.presentation

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.MifareUltralight
import android.nfc.tech.NfcA
import android.nfc.tech.NfcB
import android.nfc.tech.IsoDep
import android.nfc.tech.Ndef
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nfcmanager.domain.repository.NfcCardRepository
import com.nfcmanager.presentation.cards.CardDetailScreen
import com.nfcmanager.presentation.cards.CardsScreen
import com.nfcmanager.presentation.cards.CardsViewModel
import com.nfcmanager.presentation.scan.ScanScreen
import com.nfcmanager.presentation.scan.ScanViewModel
import com.nfcmanager.presentation.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: NfcCardRepository

    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null
    private var scanViewModel: ScanViewModel? = null

    private val techFilters = arrayOf(
        arrayOf(MifareClassic::class.java.name),
        arrayOf(MifareUltralight::class.java.name),
        arrayOf(NfcA::class.java.name),
        arrayOf(NfcB::class.java.name),
        arrayOf(IsoDep::class.java.name),
        arrayOf(Ndef::class.java.name)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC не поддерживается на этом устройстве", Toast.LENGTH_LONG).show()
        }

        pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE
        )

        setContent {
            NFCManagerTheme {
                MainApp(
                    onScanViewModelReady = { scanViewModel = it }
                )
            }
        }

        // Handle tag if app was launched from NFC intent
        intent?.let { handleNfcIntent(it) }
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(
            this, pendingIntent, null, techFilters
        )
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNfcIntent(intent)
    }

    private fun handleNfcIntent(intent: Intent) {
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
        scanViewModel?.onTagDiscovered(tag)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(onScanViewModelReady: (ScanViewModel) -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val sharedCardsViewModel: CardsViewModel = hiltViewModel()

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.CardDetail.route) {
                NavigationBar(containerColor = DarkSurface) {
                    NavigationBarItem(
                        selected = currentRoute == Screen.Cards.route,
                        onClick = {
                            navController.navigate(Screen.Cards.route) {
                                popUpTo(Screen.Cards.route) { inclusive = true }
                            }
                        },
                        icon = { Icon(Icons.Default.CreditCard, null) },
                        label = { Text("Мои карты") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TealPrimary,
                            selectedTextColor = TealPrimary,
                            indicatorColor = TealContainer,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Scan.route,
                        onClick = {
                            navController.navigate(Screen.Scan.route) {
                                popUpTo(Screen.Scan.route) { inclusive = true }
                            }
                        },
                        icon = { Icon(Icons.Default.NearMe, null) },
                        label = { Text("Сканировать") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TealPrimary,
                            selectedTextColor = TealPrimary,
                            indicatorColor = TealContainer,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                }
            }
        },
        containerColor = DarkBackground
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Cards.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Cards.route) {
                CardsScreen(
                    onCardDetail = { cardId -> navController.navigate(Screen.CardDetail.createRoute(cardId)) },
                    viewModel = sharedCardsViewModel
                )
            }
            composable(Screen.Scan.route) {
                val scanViewModel: ScanViewModel = hiltViewModel()
                LaunchedEffect(scanViewModel) { onScanViewModelReady(scanViewModel) }
                ScanScreen(
                    onCardSaved = { navController.navigate(Screen.Cards.route) },
                    viewModel = scanViewModel
                )
            }
            composable(
                Screen.CardDetail.route,
                arguments = listOf(navArgument("cardId") { type = NavType.LongType })
            ) { backStack ->
                val cardId = backStack.arguments?.getLong("cardId") ?: return@composable
                val uiState by sharedCardsViewModel.uiState.collectAsState()
                val card = uiState.cards.find { it.id == cardId }
                card?.let {
                    CardDetailScreen(
                        card = it,
                        isEmulating = uiState.isEmulating && uiState.selectedCard?.id == cardId,
                        onBack = { navController.popBackStack() },
                        onEmulate = { sharedCardsViewModel.onSelectCard(it) },
                        onStop = { sharedCardsViewModel.onStopEmulation() }
                    )
                }
            }
        }
    }
}
