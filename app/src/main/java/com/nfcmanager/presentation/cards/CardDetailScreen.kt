package com.nfcmanager.presentation.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nfcmanager.domain.model.NfcCard
import com.nfcmanager.presentation.scan.cardTypeLabel
import com.nfcmanager.presentation.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailScreen(
    card: NfcCard,
    isEmulating: Boolean,
    onBack: () -> Unit,
    onEmulate: () -> Unit,
    onStop: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(card.name, color = TextPrimary, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Назад", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkSurface)
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Emulation control
            Surface(
                color = if (isEmulating) TealContainer else DarkCard,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            if (isEmulating) "Эмуляция активна" else "Эмуляция выключена",
                            style = MaterialTheme.typography.titleMedium,
                            color = if (isEmulating) AccentGreen else TextSecondary
                        )
                        Text(
                            if (isEmulating) "Телефон работает как ISO-DEP карта" else "Нажмите Play для активации",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextDisabled
                        )
                    }
                    if (isEmulating) {
                        Button(onClick = onStop, colors = ButtonDefaults.buttonColors(containerColor = AccentRed)) {
                            Icon(Icons.Default.Stop, null, Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Стоп", color = DarkBackground)
                        }
                    } else {
                        Button(onClick = onEmulate, colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)) {
                            Icon(Icons.Default.PlayArrow, null, Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Play", color = DarkBackground)
                        }
                    }
                }
            }

            // UID — оба формата
            InfoSection("UID карты") {
                // Android / RF формат (MSB)
                Text("Android / RF порядок (MSB)", style = MaterialTheme.typography.labelSmall, color = TextDisabled)
                Text(
                    card.uidFormatted.uppercase(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TealPrimary,
                    letterSpacing = 2.sp
                )
                Spacer(Modifier.height(8.dp))
                // СКУД формат (LSB)
                Text("СКУД считыватель (LSB, обратный порядок)", style = MaterialTheme.typography.labelSmall, color = TextDisabled)
                Text(
                    card.uidLsbFormatted.uppercase(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentAmber,
                    letterSpacing = 2.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Десятичный: ${card.uidDecimal}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            // HCE compatibility warning
            Surface(
                color = AccentAmber.copy(alpha = 0.08f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Default.Info, null, tint = AccentAmber, modifier = Modifier.size(20.dp).padding(top = 2.dp))
                    Column {
                        Text(
                            "О совместимости эмуляции",
                            style = MaterialTheme.typography.labelLarge,
                            color = AccentAmber
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "HCE (эмуляция) работает только с считывателями ISO 14443-4 / IsoDep (APDU-протокол). " +
                            "Большинство турникетов и домофонов на Mifare Classic читают UID на уровне RF ISO 14443-3 " +
                            "до любого обмена данными — с такими считывателями HCE не работает без root-доступа.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Card info
            InfoSection("Информация о карте") {
                InfoRow("Тип", cardTypeLabel(card.cardType))
                InfoRow("Технологии", card.techList.joinToString(", ") { it.substringAfterLast(".") })
                InfoRow("Добавлена", dateFormat.format(Date(card.createdAt)))
            }

            // Sector data
            if (card.sectorData.isNotEmpty()) {
                InfoSection("Данные секторов (${card.sectorData.size})") {
                    card.sectorData.forEach { (sector, blocks) ->
                        Text("Сектор $sector", style = MaterialTheme.typography.labelLarge, color = TealPrimary,
                            modifier = Modifier.padding(vertical = 4.dp))
                        blocks.forEachIndexed { i, block ->
                            Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Блок ${sector * 4 + i}", style = MaterialTheme.typography.bodySmall, color = TextDisabled)
                                Text(block.chunked(2).joinToString(" "), fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = TextSecondary)
                            }
                        }
                        HorizontalDivider(color = DarkSurfaceVariant, modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }

            if (card.pages.isNotEmpty()) {
                InfoSection("Страницы Ultralight (${card.pages.size})") {
                    card.pages.forEachIndexed { index, page ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Стр. $index", style = MaterialTheme.typography.bodySmall, color = TextDisabled)
                            Text(page.chunked(2).joinToString(" "), fontFamily = FontFamily.Monospace, fontSize = 12.sp, color = AccentBlue)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Surface(color = DarkCard, shape = RoundedCornerShape(12.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.labelLarge, color = TextSecondary)
            HorizontalDivider(color = DarkSurfaceVariant)
            content()
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextDisabled)
        Text(value, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
    }
}
