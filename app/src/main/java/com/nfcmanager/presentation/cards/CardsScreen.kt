package com.nfcmanager.presentation.cards

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nfcmanager.domain.model.CardType
import com.nfcmanager.domain.model.NfcCard
import com.nfcmanager.presentation.scan.cardTypeLabel
import com.nfcmanager.presentation.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CardsScreen(
    onCardDetail: (Long) -> Unit,
    viewModel: CardsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = DarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Emulation status banner
            AnimatedVisibility(
                visible = uiState.isEmulating && uiState.selectedCard != null,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                EmulationStatusBanner(
                    card = uiState.selectedCard,
                    onStop = viewModel::onStopEmulation
                )
            }

            if (uiState.cards.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.cards, key = { it.id }) { card ->
                        NfcCardItem(
                            card = card,
                            isSelected = card.id == uiState.selectedCard?.id,
                            onSelect = { viewModel.onSelectCard(card) },
                            onDelete = { viewModel.onDeleteCard(card.id) },
                            onDetail = { onCardDetail(card.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmulationStatusBanner(card: NfcCard?, onStop: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "emul")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.7f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "blink"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(listOf(TealPrimaryDark, TealContainer))
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(AccentGreen.copy(alpha = alpha), CircleShape)
        )
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(
                "Эмуляция активна",
                style = MaterialTheme.typography.labelLarge,
                color = AccentGreen,
                fontWeight = FontWeight.Bold
            )
            card?.let {
                Text(
                    it.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        IconButton(onClick = onStop) {
            Icon(Icons.Default.Stop, "Остановить", tint = AccentRed)
        }
    }
}

@Composable
private fun NfcCardItem(
    card: NfcCard,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    onDetail: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Удалить карту?") },
            text = { Text("Карта «${card.name}» будет удалена из базы данных.") },
            confirmButton = {
                TextButton(onClick = { onDelete(); showDeleteDialog = false }) {
                    Text("Удалить", color = AccentRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Отмена")
                }
            },
            containerColor = DarkSurface,
            titleContentColor = TextPrimary,
            textContentColor = TextSecondary
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onDetail() }
            .then(
                if (isSelected) Modifier.border(1.5.dp, TealPrimary, RoundedCornerShape(16.dp))
                else Modifier
            ),
        color = if (isSelected) DarkCard else DarkSurface,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Card type icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (isSelected) TealContainer else DarkSurfaceVariant,
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (card.cardType) {
                        CardType.MIFARE_ULTRALIGHT -> Icons.Default.Memory
                        else -> Icons.Default.CreditCard
                    },
                    contentDescription = null,
                    tint = if (isSelected) TealPrimary else TextSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    card.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    card.uid.chunked(2).joinToString(":"),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = if (isSelected) TealPrimary else TextSecondary,
                    letterSpacing = 1.sp
                )
                Text(
                    cardTypeLabel(card.cardType),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextDisabled
                )
            }

            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (isSelected) {
                    Surface(color = TealPrimary, shape = RoundedCornerShape(8.dp)) {
                        Text(
                            "АКТИВНА",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = DarkBackground,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    IconButton(
                        onClick = onSelect,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, "Эмулировать", tint = TealPrimary, modifier = Modifier.size(20.dp))
                    }
                }
                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.Delete, "Удалить", tint = AccentRed.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Default.CreditCard, null, tint = TextDisabled, modifier = Modifier.size(72.dp))
            Text("Нет сохранённых карт", style = MaterialTheme.typography.titleMedium, color = TextSecondary)
            Text(
                "Перейдите на вкладку «Сканировать»\nи поднесите карту к телефону",
                style = MaterialTheme.typography.bodyMedium,
                color = TextDisabled,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
