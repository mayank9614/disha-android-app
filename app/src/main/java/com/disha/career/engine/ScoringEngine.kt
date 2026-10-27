package com.disha.career.engine

import com.disha.career.model.Career
import com.disha.career.model.Profile
import com.disha.career.model.Question
import com.disha.career.model.QuestionKind
import com.disha.career.model.Stage
import kotlin.math.roundToInt
import kotlin.math.sqrt

val INTEREST_DIMS = listOf("R", "I", "A", "S", "E", "C")
val APTITUDE_DIMS = listOf("NUM", "VERB", "LOGIC")

/** Points for the three interest options, in the order the UI shows them. */
private val LIKERT_POINTS = listOf(2, 1, 0)

/** Each user dimension is normalised to 0.0–1.0. */
data class UserScores(val interest: Map<String, Double>, val aptitude: Map<String, Double>)

sealed interface Reason {
    data class Interest(val dim: String) : Reason
    data class Aptitude(val dim: String) : Reason
    data object LowCost : Reason
    data object NearHome : Reason
    data object GovtMatch : Reason
    data object BudgetStretch : Reason
    data object NeedsMove : Reason
}

data class CareerMatch(val career: Career, val score: Int, val reasons: List<Reason>)

data class AssessmentResult(
    /** The strongest interest dimension, e.g. "R". */
    val persona: String,
    val scores: UserScores,
    /** Eligible careers, best match first. */
    val matches: List<CareerMatch>,
)

/**
 * Rule-based matcher: 60% interest fit, 25% aptitude fit, 15% fit with the student's
 * real-life limits (budget, moving away). Runs fully on the phone.
 */
class ScoringEngine(private val questions: List<Question>, private val careers: List<Career>) {

    fun score(profile: Profile, answers: Map<String, Int>): AssessmentResult {
        val user = userScores(answers)
        val matches = careers
            .filter { isEligible(it, profile) }
            .map { match(it, profile, user) }
            .sortedByDescending { it.score }
        val persona = INTEREST_DIMS.maxBy { user.interest.getValue(it) }
        return AssessmentResult(persona, user, matches)
    }

    fun userScores(answers: Map<String, Int>): UserScores {
        val interest = INTEREST_DIMS.associateWith { dim ->
            val qs = questions.filter { it.kind == QuestionKind.INTEREST && it.dim == dim }
            if (qs.isEmpty()) 0.0
            else qs.sumOf { LIKERT_POINTS.getOrElse(answers[it.id] ?: -1) { 0 } }.toDouble() / (qs.size * 2)
        }
        val aptitude = APTITUDE_DIMS.associateWith { dim ->
            val qs = questions.filter { it.kind == QuestionKind.APTITUDE && it.dim == dim }
            if (qs.isEmpty()) 0.0 else qs.count { answers[it.id] == it.answer }.toDouble() / qs.size
        }
        return UserScores(interest, aptitude)
    }

    private fun isEligible(career: Career, profile: Profile): Boolean {
        if (profile.stage !in career.stages) return false
        if (profile.stage == Stage.AFTER_12 && career.streams.isNotEmpty()) {
            return profile.stream in career.streams
        }
        return true
    }

    private fun match(career: Career, profile: Profile, user: UserScores): CareerMatch {
        val interestFit = cosine(career.riasec, user.interest)
        val aptitudeFit =
            if (career.aptitude.isEmpty()) 0.7 else weightedAverage(career.aptitude, user.aptitude)

        val budgetGap = career.cost.ordinal - profile.budget.ordinal
        val mustMove = career.needsRelocation && !profile.canRelocate
        var constraintFit = 1.0
        if (budgetGap > 0) constraintFit -= 0.4 * budgetGap
        if (mustMove) constraintFit -= 0.3
        constraintFit = constraintFit.coerceIn(0.0, 1.0)

        val govtMatch = career.govt && profile.wantsGovtJob
        var total = 0.6 * interestFit + 0.25 * aptitudeFit + 0.15 * constraintFit
        if (govtMatch) total += 0.05

        val reasons = buildList {
            career.riasec.filter { (dim, w) -> w >= 2 && user.interest.getValue(dim) >= 0.5 }
                .keys.sortedByDescending { user.interest.getValue(it) }
                .take(2).forEach { add(Reason.Interest(it)) }
            career.aptitude.keys.filter { user.aptitude.getValue(it) >= 0.5 }
                .maxByOrNull { career.aptitude.getValue(it) }
                ?.let { add(Reason.Aptitude(it)) }
            if (govtMatch) add(Reason.GovtMatch)
            if (career.cost.ordinal == 0) add(Reason.LowCost)
            if (!career.needsRelocation) add(Reason.NearHome)
            if (budgetGap > 0) add(Reason.BudgetStretch)
            if (mustMove) add(Reason.NeedsMove)
        }

        return CareerMatch(career, (total * 100).roundToInt().coerceIn(0, 99), reasons)
    }

    private fun cosine(weights: Map<String, Double>, user: Map<String, Double>): Double {
        var dot = 0.0
        var weightNorm = 0.0
        var userNorm = 0.0
        for (dim in INTEREST_DIMS) {
            val w = weights[dim] ?: 0.0
            val u = user[dim] ?: 0.0
            dot += w * u
            weightNorm += w * w
            userNorm += u * u
        }
        return if (weightNorm == 0.0 || userNorm == 0.0) 0.0 else dot / (sqrt(weightNorm) * sqrt(userNorm))
    }

    private fun weightedAverage(weights: Map<String, Double>, values: Map<String, Double>): Double {
        val totalWeight = weights.values.sum()
        return if (totalWeight == 0.0) 0.0
        else weights.entries.sumOf { (dim, w) -> w * (values[dim] ?: 0.0) } / totalWeight
    }
}
