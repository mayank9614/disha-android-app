package com.disha.career.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.disha.career.model.Stage
import com.disha.career.ui.screens.ProfileScreen
import com.disha.career.ui.screens.QuizScreen
import com.disha.career.ui.screens.ReportScreen
import com.disha.career.ui.screens.ResultScreen
import com.disha.career.ui.screens.WelcomeScreen

@Composable
fun DishaApp(
    vm: AppViewModel,
    price: String?,
    onBuy: () -> Boolean,
    onDebugUnlock: (() -> Unit)?,
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val s = stringsFor(state.lang)

    BackHandler(enabled = state.screen != Screen.WELCOME) { vm.back() }

    Scaffold { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            val result = state.result
            when (state.screen) {
                Screen.WELCOME -> WelcomeScreen(
                    s = s,
                    lang = state.lang,
                    hasResult = result != null,
                    onLang = vm::setLang,
                    onStart = vm::startTest,
                    onLastResult = vm::showResult,
                )
                Screen.PROFILE -> ProfileScreen(s, state.profile, vm::submitProfile)
                Screen.QUIZ -> {
                    val question = vm.questions[state.questionIndex]
                    QuizScreen(
                        s = s,
                        lang = state.lang,
                        question = question,
                        index = state.questionIndex,
                        total = vm.questions.size,
                        selected = state.answers[question.id],
                        onAnswer = vm::answer,
                    )
                }
                Screen.RESULT -> if (result != null) ResultScreen(
                    s = s,
                    lang = state.lang,
                    result = result,
                    unlocked = state.unlocked,
                    price = price,
                    onBuy = onBuy,
                    onDebugUnlock = onDebugUnlock,
                    onOpenReport = vm::openReport,
                    onRetake = vm::startTest,
                )
                Screen.REPORT -> if (result != null) ReportScreen(
                    s = s,
                    lang = state.lang,
                    result = result,
                    stage = state.profile?.stage ?: Stage.AFTER_10,
                )
            }
        }
    }
}
