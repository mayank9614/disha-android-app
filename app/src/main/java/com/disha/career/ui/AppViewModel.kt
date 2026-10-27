package com.disha.career.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.disha.career.data.AppPrefs
import com.disha.career.data.ContentRepository
import com.disha.career.engine.AssessmentResult
import com.disha.career.engine.ScoringEngine
import com.disha.career.model.Lang
import com.disha.career.model.Profile
import com.disha.career.model.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class Screen { WELCOME, PROFILE, QUIZ, RESULT, REPORT }

data class UiState(
    val lang: Lang,
    val screen: Screen = Screen.WELCOME,
    val profile: Profile? = null,
    val answers: Map<String, Int> = emptyMap(),
    val questionIndex: Int = 0,
    val result: AssessmentResult? = null,
    val unlocked: Boolean = false,
)

class AppViewModel(app: Application) : AndroidViewModel(app) {
    private val prefs = AppPrefs(app)
    private val content = ContentRepository(app)
    private val engine = ScoringEngine(content.questions, content.careers)

    val questions: List<Question> = content.questions

    private val _state = MutableStateFlow(restoreState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private fun restoreState(): UiState {
        val profile = prefs.profile
        val answers = prefs.answers
        val result = if (profile != null && answers.isNotEmpty()) engine.score(profile, answers) else null
        return UiState(lang = prefs.lang, profile = profile, answers = answers, result = result, unlocked = prefs.unlocked)
    }

    fun setLang(lang: Lang) {
        prefs.lang = lang
        _state.update { it.copy(lang = lang) }
    }

    fun startTest() = _state.update { it.copy(screen = Screen.PROFILE) }

    fun submitProfile(profile: Profile) = _state.update {
        it.copy(profile = profile, answers = emptyMap(), questionIndex = 0, screen = Screen.QUIZ)
    }

    fun answer(option: Int) {
        val current = _state.value
        val question = questions[current.questionIndex]
        val answers = current.answers + (question.id to option)
        if (current.questionIndex < questions.lastIndex) {
            _state.update { it.copy(answers = answers, questionIndex = it.questionIndex + 1) }
            return
        }
        val profile = current.profile ?: return
        prefs.saveAttempt(profile, answers)
        _state.update {
            it.copy(answers = answers, result = engine.score(profile, answers), screen = Screen.RESULT)
        }
    }

    fun showResult() = _state.update { it.copy(screen = Screen.RESULT) }

    fun openReport() = _state.update { if (it.unlocked) it.copy(screen = Screen.REPORT) else it }

    /** Called by billing (possibly on restore) or by the debug-only button. */
    fun markUnlocked() {
        prefs.unlocked = true
        _state.update { it.copy(unlocked = true) }
    }

    fun back() = _state.update {
        when (it.screen) {
            Screen.WELCOME -> it
            Screen.PROFILE -> it.copy(screen = Screen.WELCOME)
            Screen.QUIZ ->
                if (it.questionIndex > 0) it.copy(questionIndex = it.questionIndex - 1)
                else it.copy(screen = Screen.PROFILE)
            Screen.RESULT -> it.copy(screen = Screen.WELCOME)
            Screen.REPORT -> it.copy(screen = Screen.RESULT)
        }
    }
}
