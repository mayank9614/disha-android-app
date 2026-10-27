package com.disha.career.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.disha.career.model.Lang
import com.disha.career.model.Question
import com.disha.career.model.QuestionKind
import com.disha.career.ui.Strings

@Composable
fun QuizScreen(
    s: Strings,
    lang: Lang,
    question: Question,
    index: Int,
    total: Int,
    selected: Int?,
    onAnswer: (Int) -> Unit,
) {
    val isInterest = question.kind == QuestionKind.INTEREST
    val options = if (isInterest) s.likert else question.options.map { it.of(lang) }

    Page {
        Text(s.questionOf(index + 1, total), style = MaterialTheme.typography.labelLarge)
        LinearProgressIndicator(progress = { index / total.toFloat() }, modifier = Modifier.fillMaxWidth())
        Text(
            if (isInterest) s.sectionInterest else s.sectionAptitude,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(question.text.of(lang), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
        options.forEachIndexed { i, option -> ChoiceButton(option, selected == i, { onAnswer(i) }) }
    }
}
