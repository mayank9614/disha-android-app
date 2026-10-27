package com.disha.career.model

import kotlinx.serialization.Serializable

enum class Lang { EN, HI }

@Serializable
data class Bilingual(val en: String, val hi: String) {
    fun of(lang: Lang): String = if (lang == Lang.HI) hi else en
}

@Serializable
enum class Stage { AFTER_10, AFTER_12 }

@Serializable
enum class Stream { SCIENCE, COMMERCE, ARTS }

/** Ordered cheapest first: the scoring engine compares ordinals. */
@Serializable
enum class Budget { LOW, MEDIUM, HIGH }

@Serializable
data class Profile(
    val stage: Stage,
    /** Only set for students who passed 12th. */
    val stream: Stream? = null,
    val budget: Budget,
    val canRelocate: Boolean,
    val wantsGovtJob: Boolean,
)

@Serializable
enum class QuestionKind { INTEREST, APTITUDE }

/**
 * INTEREST questions use the shared three-point scale from the UI and score [dim].
 * APTITUDE questions carry their own [options]; picking [answer] scores [dim].
 */
@Serializable
data class Question(
    val id: String,
    val kind: QuestionKind,
    val dim: String,
    val text: Bilingual,
    val options: List<Bilingual> = emptyList(),
    val answer: Int = -1,
)

@Serializable
data class Career(
    val id: String,
    val title: Bilingual,
    val summary: Bilingual,
    /** Interest weights (R, I, A, S, E, C), roughly 0–3. */
    val riasec: Map<String, Double>,
    /** Aptitude weights (NUM, VERB, LOGIC); empty means aptitude doesn't matter much. */
    val aptitude: Map<String, Double> = emptyMap(),
    val stages: List<Stage>,
    /** For 12th-pass students only; empty means any stream. */
    val streams: List<Stream> = emptyList(),
    val cost: Budget,
    val needsRelocation: Boolean = false,
    val govt: Boolean = false,
    val aiProof: Int,
    val salary: Bilingual,
    val roadmap: List<Bilingual>,
    val exams: List<Bilingual> = emptyList(),
    val skills: List<Bilingual>,
    val aiNote: Bilingual,
)
