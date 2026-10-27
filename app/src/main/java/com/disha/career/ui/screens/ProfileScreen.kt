package com.disha.career.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.disha.career.model.Budget
import com.disha.career.model.Profile
import com.disha.career.model.Stage
import com.disha.career.model.Stream
import com.disha.career.ui.Strings

@Composable
fun ProfileScreen(s: Strings, initial: Profile?, onSubmit: (Profile) -> Unit) {
    var stage by rememberSaveable { mutableStateOf(initial?.stage) }
    var stream by rememberSaveable { mutableStateOf(initial?.stream) }
    var budget by rememberSaveable { mutableStateOf(initial?.budget) }
    var relocate by rememberSaveable { mutableStateOf(initial?.canRelocate) }
    var govt by rememberSaveable { mutableStateOf(initial?.wantsGovtJob) }

    val needsStream = stage == Stage.AFTER_12
    val complete = stage != null && budget != null && relocate != null && govt != null &&
        (!needsStream || stream != null)
    val yesNo = listOf(true to s.yes, false to s.no)

    Page {
        Text(s.profileTitle, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

        Field(s.qClass, listOf(Stage.AFTER_10 to s.class10, Stage.AFTER_12 to s.class12), stage) { stage = it }
        if (needsStream) {
            Field(s.qStream, Stream.entries.map { it to s.streamNames.getValue(it) }, stream) { stream = it }
        }
        Field(s.qBudget, Budget.entries.map { it to s.budgetNames.getValue(it) }, budget) { budget = it }
        Field(s.qRelocate, yesNo, relocate) { relocate = it }
        Field(s.qGovt, yesNo, govt) { govt = it }

        Button(
            onClick = {
                onSubmit(
                    Profile(
                        stage = stage!!,
                        stream = if (needsStream) stream else null,
                        budget = budget!!,
                        canRelocate = relocate!!,
                        wantsGovtJob = govt!!,
                    )
                )
            },
            enabled = complete,
            modifier = Modifier.fillMaxWidth().height(56.dp),
        ) { Text(s.continueBtn, style = MaterialTheme.typography.titleMedium) }
    }
}

@Composable
private fun <T> Field(label: String, options: List<Pair<T, String>>, selected: T?, onSelect: (T) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        options.forEach { (value, text) -> ChoiceButton(text, value == selected, { onSelect(value) }) }
    }
}
