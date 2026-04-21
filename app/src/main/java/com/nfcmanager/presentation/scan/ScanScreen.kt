package com.nfcmanager.presentation.scan

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nfcmanager.domain.model.CardType
import com.nfcmanager.domain.model.NfcCard
import com.nfcmanager.presentation.ui.theme.*

@Composable
fun ScanScreen(
    onCardSaved: () -> Unit,
    viewModel: ScanViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is ScanUiState.WaitingForCard -> WaitingForCardContent()
        is ScanUiState.Reading -> ReadingContent()
        is ScanUiState.CardRead -> CardReadContent(
            card = state.card,
            onSave = { name -> viewModel.onSaveCard(state.card, name) },
            onDiscard = { viewModel.resetState() }
        )
        is ScanUiState.Saved -> {
            LaunchedEffect(Unit) { onCardSaved() }
        }
        is ScanUiState.Error -> ErrorContent(
            message = state.message,
            onRetry = { viewModel.resetState() }
        )
    }
}

@Composable
private fun WaitingForCardContent() {
    val infiniteTransition = rememberInfiniteTransition(label = "scan_pulse")
    val ring1Scale by infiniteTransition.animateFloat(
        initialValue = 0.6f, targetValue = 1.4f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Restart),
        label = "ring1"
    )
    val ring2Scale by infiniteTransition.animateFloat(
        initialValue = 0.6f, targetValue = 1.4f,
        animationSpec = infiniteRepeatable(tween(2000, delayMillis = 600), RepeatMode.Restart),
        label = "ring2"
    )
    val ring3Scale by infiniteTransition.animateFloat(
        initialValue = 0.6f, targetValue = 1.4f,
        animationSpec = infiniteRepeatable(tween(2000, delayMillis = 1200), RepeatMode.Restart),
        label = "ring3"
    )
    val ring1Alpha by infiniteTransition.animateFloat(
        initialValue = 0.8f, targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Restart),
        label = "alpha1"
    )
    val ring2Alpha by infiniteTransition.animateFloat(
        initialValue = 0.8f, targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(2000, delayMillis = 600), RepeatMode.Restart),
        label = "alpha2"
    )
    val ring3Alpha by infiniteTransition.animateFloat(
        initialValue = 0.8f, targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(2000, delayMillis = 1200), RepeatMode.Restart),
        label = "alpha3"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        // Pulse rings
        Box(
            modifier = Modifier
                .size(240.dp)
                .scale(ring1Scale)
                .background(TealPrimary.copy(alpha = ring1Alpha * 0.15f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .scale(ring2Scale)
                .background(TealPrimary.copy(alpha = ring2Alpha * 0.2f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(160.dp)
                .scale(ring3Scale)
                .background(TealPrimary.copy(alpha = ring3Alpha * 0.3f), CircleShape)
        )

        // Center card icon
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        Brush.radialGradient(listOf(TealContainer, DarkSurface)),
                        CircleShape
                    )
                    .border(2.dp, TealPrimary.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CreditCard,
                    contentDescription = null,
                    tint = TealPrimary,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(Modifier.height(32.dp))
            Text(
                "Поднесите NFC карту",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Поддерживаются Mifare Classic и Ultralight",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ReadingContent() {
    val infiniteTransition = rememberInfiniteTransition(label = "reading")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing)),
        label = "rot"
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(24.dp)) {
            CircularProgressIndicator(color = TealPrimary, strokeWidth = 3.dp, modifier = Modifier.size(64.dp))
            Text("Считывание карты...", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
        }
    }
}

@Composable
private fun CardReadContent(card: NfcCard, onSave: (String) -> Unit, onDiscard: () -> Unit) {
    var cardName by remember { mutableStateOf(card.name) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(8.dp))

        // Success banner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AccentGreen.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentGreen, modifier = Modifier.size(28.dp))
            Column {
                Text("Карта успешно считана", style = MaterialTheme.typography.titleMedium, color = AccentGreen)
                Text(cardTypeLabel(card.cardType), style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }

        // UID display
        Surface(color = DarkCard, shape = RoundedCornerShape(12.dp)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("UID", style = MaterialTheme.typography.labelLarge, color = TextSecondary)
                Text(
                    card.uid.chunked(2).joinToString(" "),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TealPrimary,
                    letterSpacing = 2.sp
                )
            }
        }

        // Card data preview
        if (card.sectorData.isNotEmpty()) {
            SectorDataPreview(card.sectorData)
        } else if (card.pages.isNotEmpty()) {
            PagesPreview(card.pages)
        }

        // Name input
        OutlinedTextField(
            value = cardName,
            onValueChange = { cardName = it },
            label = { Text("Название карты") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSave(cardName) }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TealPrimary,
                focusedLabelColor = TealPrimary,
                cursorColor = TealPrimary,
                unfocusedTextColor = TextPrimary,
                focusedTextColor = TextPrimary
            )
        )

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = onDiscard,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentRed),
                border = ButtonDefaults.outlinedButtonBorder.copy()
            ) {
                Icon(Icons.Default.Delete, null, Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Отменить")
            }
            Button(
                onClick = { onSave(cardName) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary, contentColor = DarkBackground)
            ) {
                Icon(Icons.Default.Save, null, Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Сохранить")
            }
        }
    }
}

@Composable
private fun SectorDataPreview(sectorData: Map<Int, List<String>>) {
    Surface(color = DarkCard, shape = RoundedCornerShape(12.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Данные секторов (${sectorData.size})", style = MaterialTheme.typography.labelLarge, color = TextSecondary)
            sectorData.entries.take(4).forEach { (sector, blocks) ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Сектор $sector", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    Text(
                        "${blocks.size} блоков",
                        style = MaterialTheme.typography.bodySmall,
                        color = TealPrimary,
                        fontFamily = FontFamily.Monospace
                    )
                }
                blocks.take(1).forEach { block ->
                    Text(
                        block.chunked(2).joinToString(" "),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = TextSecondary.copy(alpha = 0.7f)
                    )
                }
                Divider(color = DarkSurfaceVariant)
            }
            if (sectorData.size > 4) {
                Text("... и ещё ${sectorData.size - 4} секторов", style = MaterialTheme.typography.bodySmall, color = TextDisabled)
            }
        }
    }
}

@Composable
private fun PagesPreview(pages: List<String>) {
    Surface(color = DarkCard, shape = RoundedCornerShape(12.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Страницы Ultralight (${pages.size})", style = MaterialTheme.typography.labelLarge, color = TextSecondary)
            pages.take(8).forEachIndexed { index, page ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Страница $index", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    Text(
                        page.chunked(2).joinToString(" "),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = AccentBlue
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize().background(DarkBackground), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(Icons.Default.ErrorOutline, null, tint = AccentRed, modifier = Modifier.size(64.dp))
            Text("Ошибка", style = MaterialTheme.typography.titleLarge, color = AccentRed)
            Text(message, style = MaterialTheme.typography.bodyMedium, color = TextSecondary, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 32.dp))
            Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)) {
                Text("Повторить", color = DarkBackground)
            }
        }
    }
}

fun cardTypeLabel(type: CardType) = when (type) {
    CardType.MIFARE_CLASSIC_1K -> "Mifare Classic 1K"
    CardType.MIFARE_CLASSIC_4K -> "Mifare Classic 4K"
    CardType.MIFARE_ULTRALIGHT -> "Mifare Ultralight"
    CardType.UNKNOWN -> "Неизвестный тип"
}
