package com.disha.career.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disha.career.engine.AssessmentResult
import com.disha.career.engine.CareerMatch
import com.disha.career.model.Lang
import com.disha.career.ui.FALLBACK_PRICE
import com.disha.career.ui.PERSONA_EMOJI
import com.disha.career.ui.Strings

private const val PLAY_LINK = "https://play.google.com/store/apps/details?id=com.disha.career"

@Composable
fun ResultScreen(
    s: Strings,
    lang: Lang,
    result: AssessmentResult,
    unlocked: Boolean,
    price: String?,
    onBuy: () -> Boolean,
    onDebugUnlock: (() -> Unit)?,
    onOpenReport: () -> Unit,
    onRetake: () -> Unit,
) {
    val context = LocalContext.current
    val personaName = s.personaNames.getValue(result.persona)
    val top = result.matches.take(3)

    Page {
        Text(s.resultTitle, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(PERSONA_EMOJI.getValue(result.persona), fontSize = 48.sp)
                Text(s.personaHeadline(personaName), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(s.personaDesc.getValue(result.persona), style = MaterialTheme.typography.bodyLarge)
            }
        }

        Text(s.topMatches, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        if (top.isEmpty()) Text(s.noMatches)
        top.forEachIndexed { i, match ->
            // The #1 match shows its reasons for free: a taste of what the full report has.
            FreeMatchCard(s, lang, match, showReasons = i == 0)
        }

        if (unlocked) {
            Button(onClick = onOpenReport, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                Text(s.openReport, style = MaterialTheme.typography.titleMedium)
            }
        } else {
            LockedCard(s, price) {
                if (!onBuy()) Toast.makeText(context, s.paymentUnavailable, Toast.LENGTH_LONG).show()
            }
            if (onDebugUnlock != null) {
                TextButton(onClick = onDebugUnlock) { Text("Unlock (debug build only)") }
            }
        }

        if (top.isNotEmpty()) {
            OutlinedButton(
                onClick = {
                    val first = top.first()
                    val text = s.shareText(personaName, first.career.title.of(lang), first.score, PLAY_LINK)
                    val send = Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, text)
                    context.startActivity(Intent.createChooser(send, null))
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Text(s.share, Modifier.padding(start = 8.dp))
            }
        }
        TextButton(onClick = onRetake, modifier = Modifier.fillMaxWidth()) { Text(s.retake) }
    }
}

@Composable
private fun FreeMatchCard(s: Strings, lang: Lang, match: CareerMatch, showReasons: Boolean) {
    Card {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            MatchHeader(s, lang, match)
            if (showReasons && match.reasons.isNotEmpty()) {
                Text(s.whyFits, style = MaterialTheme.typography.labelLarge)
                Bullets(match.reasons.take(2).map(s.reasonText))
            }
        }
    }
}

@Composable
private fun LockedCard(s: Strings, price: String?, onUnlock: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lock, contentDescription = null)
                Text(
                    s.lockedTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
            s.lockedPoints.forEach { Text("✅  $it", style = MaterialTheme.typography.bodyMedium) }
            Button(
                onClick = onUnlock,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) {
                Text(s.unlockFor(price ?: FALLBACK_PRICE), style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
