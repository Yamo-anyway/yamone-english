package com.yamone.english

object PersistenceContract {
    const val STUDY_PREFS = "yamone_english"
    const val REVIEW_PREFS = "yamone_english_review"

    const val KEY_COMPLETED = "completed"
    const val KEY_REVIEW = "review"
    const val KEY_THINKING_COMPLETED = "thinking_completed"
    const val KEY_EXPRESSION_COMPLETED = "expression_completed"
    const val KEY_SPEECH_RATE = "speech_rate"
    const val KEY_SHOW_KOREAN = "show_korean"
    const val KEY_SHOW_SOUND_GUIDE = "show_sound_guide"
    const val KEY_SELECTED_COURSE = "selected_course"

    const val ASSESSMENT_LISTENING = "listening"
    const val ASSESSMENT_SPEAKING = "speaking"
    const val ASSESSMENT_ORDER = "order"
    const val ASSESSMENT_SITUATION = "situation"
    const val ASSESSMENT_COMPLETED_AT = "completed_at"

    val studyKeys: Set<String> = setOf(
        KEY_COMPLETED,
        KEY_REVIEW,
        KEY_THINKING_COMPLETED,
        KEY_EXPRESSION_COMPLETED,
        KEY_SPEECH_RATE,
        KEY_SHOW_KOREAN,
        KEY_SHOW_SOUND_GUIDE,
        KEY_SELECTED_COURSE
    )

    fun assessmentPrefs(course: CourseLevel): String = when (course) {
        CourseLevel.AGE_5_7 -> "yamone_english_assessment"
        CourseLevel.AGE_8_10 -> "yamone_english_assessment_8_10"
        CourseLevel.AGE_11_13 -> "yamone_english_assessment_11_13"
        CourseLevel.AGE_14_16 -> "yamone_english_assessment_14_16"
        CourseLevel.AGE_17_20 -> "yamone_english_assessment_17_20"
    }
}

data class CourseProgress(
    val course: CourseLevel,
    val totalLessons: Int,
    val completedLessons: Int,
    val nextLesson: Lesson?
) {
    val isComplete: Boolean
        get() = totalLessons > 0 && completedLessons >= totalLessons

    val remainingLessons: Int
        get() = (totalLessons - completedLessons).coerceAtLeast(0)
}

object CourseProgressResolver {
    fun resolve(course: CourseLevel, completedIds: Set<Int>): CourseProgress {
        val lessons = CourseCatalog.lessons(course).sortedBy { it.courseLessonNumber }
        val courseIds = lessons.mapTo(linkedSetOf()) { it.id }
        val completedInCourse = completedIds.intersect(courseIds)
        val next = lessons.firstOrNull { it.id !in completedInCourse }

        return CourseProgress(
            course = course,
            totalLessons = lessons.size,
            completedLessons = completedInCourse.size,
            nextLesson = next
        )
    }

    fun completedIds(course: CourseLevel, completedIds: Set<Int>): Set<Int> {
        val courseIds = CourseCatalog.lessons(course).mapTo(hashSetOf()) { it.id }
        return completedIds.intersect(courseIds)
    }
}
