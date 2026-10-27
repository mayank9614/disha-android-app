package com.disha.career.data

import android.content.Context
import com.disha.career.model.Career
import com.disha.career.model.Question
import kotlinx.serialization.json.Json

/** All content ships inside the APK under assets/, so the app needs no server. */
class ContentRepository(context: Context) {
    val questions: List<Question> = parseQuestions(context.readAsset("questions.json"))
    val careers: List<Career> = parseCareers(context.readAsset("careers.json"))

    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        fun parseQuestions(text: String): List<Question> = json.decodeFromString(text)
        fun parseCareers(text: String): List<Career> = json.decodeFromString(text)
    }
}

private fun Context.readAsset(name: String): String =
    assets.open(name).bufferedReader().use { it.readText() }
