package com.disha.career.data

import android.content.Context
import com.disha.career.model.Lang
import com.disha.career.model.Profile
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** Everything the app remembers stays on the phone, in SharedPreferences. */
class AppPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("disha", Context.MODE_PRIVATE)

    var lang: Lang
        get() = prefs.getString(KEY_LANG, null)
            ?.let { runCatching { Lang.valueOf(it) }.getOrNull() }
            ?: Lang.HI
        set(value) = prefs.edit().putString(KEY_LANG, value.name).apply()

    /** Set once the full-report purchase is confirmed. Billing restores it after a reinstall. */
    var unlocked: Boolean
        get() = prefs.getBoolean(KEY_UNLOCKED, false)
        set(value) = prefs.edit().putBoolean(KEY_UNLOCKED, value).apply()

    val profile: Profile?
        get() = prefs.getString(KEY_PROFILE, null)
            ?.let { runCatching { Json.decodeFromString<Profile>(it) }.getOrNull() }

    val answers: Map<String, Int>
        get() = prefs.getString(KEY_ANSWERS, null)
            ?.let { runCatching { Json.decodeFromString<Map<String, Int>>(it) }.getOrNull() }
            ?: emptyMap()

    fun saveAttempt(profile: Profile, answers: Map<String, Int>) {
        prefs.edit()
            .putString(KEY_PROFILE, Json.encodeToString(profile))
            .putString(KEY_ANSWERS, Json.encodeToString(answers))
            .apply()
    }

    private companion object {
        const val KEY_LANG = "lang"
        const val KEY_UNLOCKED = "unlocked"
        const val KEY_PROFILE = "profile"
        const val KEY_ANSWERS = "answers"
    }
}
