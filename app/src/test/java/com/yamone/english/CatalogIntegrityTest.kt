package com.yamone.english

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogIntegrityTest {
    private val expectedIdRanges = mapOf(
        CourseLevel.AGE_5_7 to (1..100),
        CourseLevel.AGE_8_10 to (201..300),
        CourseLevel.AGE_11_13 to (301..400),
        CourseLevel.AGE_14_16 to (401..500)
    )

    @Test
    fun courseLessonIdsAndNumbersRemainStable() {
        val allIds = LessonCatalog.lessons.map { it.id }
        assertEquals("global lesson ids must be unique", allIds.size, allIds.toSet().size)
        assertEquals("four 100-lesson courses are expected", 400, allIds.size)

        expectedIdRanges.forEach { (course, idRange) ->
            val lessons = CourseCatalog.lessons(course).sortedBy { it.courseLessonNumber }
            assertEquals("${course.name} lesson count", 100, lessons.size)
            assertEquals("${course.name} course numbering", (1..100).toList(), lessons.map { it.courseLessonNumber })
            assertEquals("${course.name} persistent ids", idRange.toList(), lessons.map { it.id })

            lessons.forEach { lesson ->
                assertTrue("${lesson.id} target", lesson.target.isNotBlank())
                assertTrue("${lesson.id} meaning", lesson.meaning.isNotBlank())
                assertTrue("${lesson.id} situation", lesson.situation.isNotBlank())
                assertTrue("${lesson.id} own prompt", lesson.ownPromptKo.isNotBlank())
                assertTrue("${lesson.id} accepted examples", lesson.accepted.isNotEmpty())
                assertTrue("${lesson.id} accepted examples blank", lesson.accepted.all { it.isNotBlank() })
                assertTrue("${lesson.id} coach line", lesson.coachLine.isNotBlank())
            }
        }
    }

    @Test
    fun everyLessonHasOneSixTurnDialogue() {
        LessonCatalog.lessons.forEach { lesson ->
            val dialogue = DialogueCatalog.byLessonId(lesson.id)
            assertNotNull("missing dialogue for lesson ${lesson.id}", dialogue)
            assertEquals("lesson ${lesson.id} must have six dialogue turns", 6, dialogue!!.lines.size)
            dialogue.lines.forEachIndexed { index, line ->
                assertTrue("lesson ${lesson.id} dialogue speaker ${index + 1}", line.speaker.isNotBlank())
                assertTrue("lesson ${lesson.id} dialogue English ${index + 1}", line.english.isNotBlank())
                assertTrue("lesson ${lesson.id} dialogue Korean ${index + 1}", line.korean.isNotBlank())
            }
        }
    }

    @Test
    fun thinkingAndPronunciationGuidesCoverEveryLesson() {
        LessonCatalog.lessons.forEach { lesson ->
            val thinking = ThinkingCatalog.byLessonId(lesson.id)
            assertTrue("lesson ${lesson.id} thinking guide missing", thinking.englishOrderKorean.isNotBlank())
            assertTrue("lesson ${lesson.id} thinking guide must show English thought order", thinking.englishOrderKorean.contains("→"))

            val pronunciation = PronunciationQaCatalog.byLessonId(lesson.id)
            assertTrue("lesson ${lesson.id} rhythm English missing", pronunciation.rhythmEnglish.isNotBlank())
            assertTrue("lesson ${lesson.id} slow Korean missing", pronunciation.slowKorean.isNotBlank())
            assertTrue("lesson ${lesson.id} natural Korean missing", pronunciation.naturalKorean.isNotBlank())
            assertTrue("lesson ${lesson.id} rhythm missing", pronunciation.rhythm.isNotBlank())

            if (lesson.course == CourseLevel.AGE_14_16) {
                assertTrue("lesson ${lesson.id} stress marker missing", pronunciation.rhythmEnglish.contains("●"))
                assertTrue(
                    "lesson ${lesson.id} intonation marker missing",
                    pronunciation.rhythmEnglish.contains("↗") || pronunciation.rhythmEnglish.contains("↘")
                )
            }
        }
    }

    @Test
    fun sectionsCoverEveryCourseLessonExactlyOnce() {
        CourseLevel.entries.forEach { course ->
            val lessons = CourseCatalog.lessons(course)
            val lessonIds = lessons.map { it.id }.toSet()
            val sections = CourseCatalog.sections(course)

            lessons.forEach { lesson ->
                val matches = sections.count { lesson.id in it.internalIds }
                assertEquals("lesson ${lesson.id} must belong to exactly one section", 1, matches)
            }

            val covered = sections.flatMap { section ->
                section.internalIds.filter { it in lessonIds }
            }.toSet()
            assertEquals("${course.name} section coverage", lessonIds, covered)
        }
    }

    @Test
    fun age1416AssessmentIsBalancedAndUsesValidLessons() {
        val questions = Age1416AssessmentCatalog.questions
        assertEquals(28, questions.size)
        assertEquals((1..28).toList(), questions.map { it.number })
        assertEquals(28, questions.map { it.lessonId }.toSet().size)

        AssessmentKind.entries.forEach { kind ->
            assertEquals("$kind question count", 7, questions.count { it.kind == kind })
        }

        questions.forEach { question ->
            val lesson = LessonCatalog.byId(question.lessonId)
            assertNotNull("assessment lesson ${question.lessonId}", lesson)
            assertEquals(CourseLevel.AGE_14_16, lesson!!.course)
        }

        assertTrue("assessment must sample first half", questions.any { it.lessonId <= 450 })
        assertTrue("assessment must sample expansion half", questions.any { it.lessonId >= 451 })
    }
}
