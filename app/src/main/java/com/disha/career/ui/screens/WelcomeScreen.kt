package com.disha.career.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disha.career.model.Lang
import com.disha.career.ui.Strings

@Composable
fun WelcomeScreen(
    s: Strings,
    lang: Lang,
    hasResult: Boolean,
    onLang: (Lang) -> Unit,
    onStart: () -> Unit,
    onLastResult: () -> Unit,
) {
    Page {
        Spacer(Modifier.height(32.dp))
        Text("🧭", fontSize = 64.sp)
        Text(s.appName, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text(s.tagline, style = MaterialTheme.typography.titleLarge)

        Text(s.language, style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ChoiceButton("हिंदी", lang == Lang.HI, { onLang(Lang.HI) }, Modifier.weight(1f))
            ChoiceButton("English", lang == Lang.EN, { onLang(Lang.EN) }, Modifier.weight(1f))
        }

        Spacer(Modifier.height(8.dp))
        Button(onClick = onStart, modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text(s.startTest, style = MaterialTheme.typography.titleMedium)
        }
        if (hasResult) {
            OutlinedButton(onClick = onLastResult, modifier = Modifier.fillMaxWidth()) { Text(s.seeLastResult) }
        }
        Text(s.privacyNote, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
