package com.disha.career.engine

import com.disha.career.data.ContentRepository
import com.disha.career.model.Budget
import com.disha.career.model.Profile
import com.disha.career.model.QuestionKind
import com.disha.career.model.Stage
import com.disha.career.model.Stream
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/** Runs against the real bundled content, so a broken JSON edit fails here first. */
class ScoringEngineTest {
    // Gradle runs unit tests from the module directory.
    private val questions = ContentRepository.parseQuestions(File("src/main/assets/questions.json").readText())
    private val careers = ContentRepository.parseCareers(File("src/main/assets/careers.json").readText())
    private val engine = ScoringEngine(questions, careers)

    private val after10Low = Profile(Stage.AFTER_10, null, Budget.LOW, canRelocate = false, wantsGovtJob = false)

    /** Loves every interest question in [lovedDims], rejects the rest, aces aptitude. */
    private fun answersLoving(vararg lovedDims: String): Map<String, Int> = questions.associate { q ->
        q.id to when (q.kind) {
            QuestionKind.INTEREST -> if (q.dim in lovedDims) 0 else 2
            QuestionKind.APTITUDE -> q.answer
        }
    }

    @Test
    fun contentIsConsistent() {
        assertEquals(questions.size, questions.map { it.id }.toSet().size)
        questions.filter { it.kind == QuestionKind.INTEREST }.forEach { assertTrue(it.id, it.dim in INTEREST_DIMS) }
        questions.filter { it.kind == QuestionKind.APTITUDE }.forEach {
            assertTrue(it.id, it.dim in APTITUDE_DIMS)
            assertTrue(it.id, it.answer in it.options.indices)
        }
        careers.forEach { c ->
            assertTrue(c.id, c.riasec.keys.all { it in INTEREST_DIMS })
            assertTrue(c.id, c.aptitude.keys.all { it in APTITUDE_DIMS })
            assertTrue(c.id, c.stages.isNotEmpty() && c.roadmap.isNotEmpty())
        }
    }

    @Test
    fun handsOnStudentGetsHandsOnCareers() {
        val result = engine.score(after10Low, answersLoving("R"))
        assertEquals("R", result.persona)
        val top3 = result.matches.take(3).map { it.career.id }
        assertTrue(top3.toString(), "solar_ev_tech" in top3 || "mobile_repair" in top3)
        assertTrue(result.matches.first().reasons.contains(Reason.Interest("R")))
    }

    @Test
    fun caringScienceStudentGetsNursing() {
        val profile = Profile(Stage.AFTER_12, Stream.SCIENCE, Budget.MEDIUM, canRelocate = true, wantsGovtJob = true)
        val result = engine.score(profile, answersLoving("S"))
        // Nursing and teaching both fit a caring profile closely; either may rank first.
        assertTrue("nurse" in result.matches.take(2).map { it.career.id })
    }

    @Test
    fun eligibilityFiltersByClassAndStream() {
        val after10 = engine.score(after10Low, answersLoving("S")).matches.map { it.career.id }
        assertFalse("nurse" in after10)

        val commerce = Profile(Stage.AFTER_12, Stream.COMMERCE, Budget.LOW, canRelocate = false, wantsGovtJob = false)
        val ids = engine.score(commerce, answersLoving("S")).matches.map { it.career.id }
        assertFalse("nurse" in ids)
        assertTrue("accountant" in ids)
    }

    @Test
    fun expensiveCareersAreFlaggedForLowBudget() {
        val match = engine.score(after10Low, answersLoving("R")).matches.first { it.career.id == "drone_operator" }
        assertTrue(Reason.BudgetStretch in match.reasons)
    }

    @Test
    fun unansweredTestStillScores() {
        val result = engine.score(after10Low, emptyMap())
        assertTrue(result.matches.isNotEmpty())
        assertTrue(result.matches.all { it.score in 0..99 })
    }
}
