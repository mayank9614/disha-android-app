package com.disha.career.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.disha.career.engine.CareerMatch
import com.disha.career.engine.AssessmentResult
import com.disha.career.model.Budget
import com.disha.career.model.Lang
import com.disha.career.model.Stage
import com.disha.career.ui.Strings

/** How many careers the paid report covers. */
private const val REPORT_SIZE = 8

@Composable
fun ReportScreen(s: Strings, lang: Lang, result: AssessmentResult, stage: Stage) {
    Page {
        Text(s.reportTitle, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        if (result.matches.isEmpty()) Text(s.noMatches)
        result.matches.take(REPORT_SIZE).forEach { CareerDetail(s, lang, it) }

        Card {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(s.planTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Bullets(s.plan.getValue(stage))
            }
        }
        Text(s.disclaimer, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CareerDetail(s: Strings, lang: Lang, match: CareerMatch) {
    val career = match.career
    Card {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            MatchHeader(s, lang, match)
            val tags = buildList {
                if (career.govt) add(s.govtTag)
                if (career.cost == Budget.LOW) add(s.lowCostTag)
                if (!career.needsRelocation) add(s.nearHomeTag)
            }
            if (tags.isNotEmpty()) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    tags.forEach { Tag(it) }
                }
            }
            Text(career.summary.of(lang), style = MaterialTheme.typography.bodyMedium)
            Section(s.whyFits) { Bullets(match.reasons.map(s.reasonText)) }
            Section(s.roadmap) { Bullets(career.roadmap.map { it.of(lang) }, numbered = true) }
            if (career.exams.isNotEmpty()) Section(s.exams) { Bullets(career.exams.map { it.of(lang) }) }
            Section(s.salary) { Text(career.salary.of(lang), style = MaterialTheme.typography.bodyMedium) }
            Section(s.skills) { Bullets(career.skills.map { it.of(lang) }) }
            Section(s.aiFuture) { Text(career.aiNote.of(lang), style = MaterialTheme.typography.bodyMedium) }
        }
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        content()
    }
}

@Composable
private fun Tag(text: String) {
    Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.secondaryContainer) {
        Text(text, Modifier.padding(horizontal = 10.dp, vertical = 4.dp), style = MaterialTheme.typography.labelMedium)
    }
}
