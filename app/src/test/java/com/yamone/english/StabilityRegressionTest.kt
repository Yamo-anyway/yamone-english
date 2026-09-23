package com.yamone.english

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class StabilityRegressionTest {
    @Test
    fun persistenceContractRemainsBackwardCompatible() {
        assertEquals("yamone_english", PersistenceContract.STUDY_PREFS)
        assertEquals("yamone_english_review", PersistenceContract.REVIEW_PREFS)
        assertEquals(
            setOf(
                "completed",
                "review",
                "thinking_completed",
                "expression_completed",
                "speech_rate",
                "show_korean",
                "show_sound_guide",
                "selected_course"
            ),
            PersistenceContract.studyKeys
        )

        assertEquals("yamone_english_assessment", PersistenceContract.assessmentPrefs(CourseLevel.AGE_5_7))
        assertEquals("yamone_english_assessment_8_10", PersistenceContract.assessmentPrefs(CourseLevel.AGE_8_10))
        assertEquals("yamone_english_assessment_11_13", PersistenceContract.assessmentPrefs(CourseLevel.AGE_11_13))
        assertEquals("yamone_english_assessment_14_16", PersistenceContract.assessmentPrefs(CourseLevel.AGE_14_16))
        assertEquals("yamone_english_assessment_17_20", PersistenceContract.assessmentPrefs(CourseLevel.AGE_17_20))

        assertEquals("listening", PersistenceContract.ASSESSMENT_LISTENING)
        assertEquals("speaking", PersistenceContract.ASSESSMENT_SPEAKING)
        assertEquals("order", PersistenceContract.ASSESSMENT_ORDER)
        assertEquals("situation", PersistenceContract.ASSESSMENT_SITUATION)
        assertEquals("completed_at", PersistenceContract.ASSESSMENT_COMPLETED_AT)
    }

    @Test
    fun courseProgressNeverCountsAnotherCourse() {
        CourseLevel.entries.forEach { course ->
            val lessons = CourseCatalog.lessons(course).sortedBy { it.courseLessonNumber }
            val foreignIds = LessonCatalog.lessons
                .filter { it.course != course }
                .take(25)
                .map { it.id }
                .toSet()

            val partialCompleted = lessons.take(99).map { it.id }.toSet() + foreignIds
            val progress = CourseProgressResolver.resolve(course, partialCompleted)

            assertEquals(course, progress.course)
            assertEquals(100, progress.totalLessons)
            assertEquals(99, progress.completedLessons)
            assertEquals(1, progress.remainingLessons)
            assertFalse(progress.isComplete)
            assertEquals(100, progress.nextLesson?.courseLessonNumber)
            assertEquals(course, progress.nextLesson?.course)

            val complete = CourseProgressResolver.resolve(
                course,
                lessons.map { it.id }.toSet() + foreignIds
            )
            assertEquals(100, complete.completedLessons)
            assertEquals(0, complete.remainingLessons)
            assertTrue(complete.isComplete)
            assertNull(complete.nextLesson)
        }
    }

    @Test
    fun courseFilteringReturnsOnlySelectedCourseIds() {
        val everyLessonId = LessonCatalog.lessons.map { it.id }.toSet()

        CourseLevel.entries.forEach { course ->
            val filtered = CourseProgressResolver.completedIds(course, everyLessonId)
            assertEquals(100, filtered.size)
            assertTrue(filtered.all { LessonCatalog.byId(it)?.course == course })
        }
    }

    @Test
    fun everyCourseHasSafeFirstLastAndSectionNavigation() {
        CourseLevel.entries.forEach { course ->
            val lessons = CourseCatalog.lessons(course).sortedBy { it.courseLessonNumber }
            val sections = CourseCatalog.sections(course)

            assertEquals(100, lessons.size)
            assertTrue(sections.isNotEmpty())
            assertEquals(1, lessons.first().courseLessonNumber)
            assertEquals(100, lessons.last().courseLessonNumber)

            lessons.forEach { lesson ->
                val sectionIndex = CourseCatalog.sectionIndexForLesson(course, lesson.id)
                assertTrue("${course.name} lesson ${lesson.id} section index", sectionIndex in sections.indices)
                assertTrue(lesson.id in sections[sectionIndex].internalIds)
            }
        }
    }

    @Test
    fun assessmentEntryDataMatchesEachCourse() {
        validateAssessment(CourseLevel.AGE_5_7, Age57AssessmentCatalog.questions, 16)
        validateAssessment(CourseLevel.AGE_8_10, Age810AssessmentCatalog.questions, 20)
        validateAssessment(CourseLevel.AGE_11_13, Age1113AssessmentCatalog.questions, 24)
        validateAssessment(CourseLevel.AGE_14_16, Age1416AssessmentCatalog.questions, 28)
        validateAssessment(CourseLevel.AGE_17_20, Age1720AssessmentCatalog.questions, 28)
    }

    private fun validateAssessment(
        course: CourseLevel,
        questions: List<AssessmentQuestion>,
        expectedCount: Int
    ) {
        assertEquals(expectedCount, questions.size)
        assertEquals((1..expectedCount).toList(), questions.map { it.number })
        assertEquals(expectedCount, questions.map { it.lessonId }.toSet().size)
        assertTrue(
            "$course assessment must use only its own lessons",
            questions.all { question -> LessonCatalog.byId(question.lessonId)?.course == course }
        )
    }
}
