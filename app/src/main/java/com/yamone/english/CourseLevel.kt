package com.yamone.english

enum class CourseLevel(
    val label: String,
    val description: String
) {
    AGE_5_7(
        label = "5~7세",
        description = "기본 일상 표현과 짧은 대화"
    ),
    AGE_8_10(
        label = "8~10세",
        description = "이유·경험·계획·비교를 말하는 대화"
    )
}

object CourseCatalog {
    fun lessons(course: CourseLevel): List<Lesson> =
        LessonCatalog.lessons.filter { it.course == course }

    fun displayNumber(lesson: Lesson): Int = lesson.courseLessonNumber

    fun sections(course: CourseLevel): List<CourseSection> = when (course) {
        CourseLevel.AGE_5_7 -> Age57CourseSections.sections
        CourseLevel.AGE_8_10 -> Age810CourseSections.sections
    }

    fun sectionIndexForLesson(course: CourseLevel, lessonId: Int): Int =
        sections(course).indexOfFirst { lessonId in it.internalIds }
            .coerceAtLeast(0)
}
