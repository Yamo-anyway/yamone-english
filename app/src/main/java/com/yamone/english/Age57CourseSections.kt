package com.yamone.english

data class CourseSection(
    val id: Int,
    val title: String,
    val internalIds: IntRange
) {
    fun lessons(): List<Lesson> =
        LessonCatalog.lessons.filter { it.id in internalIds }
}

object Age57CourseSections {
    val sections = listOf(
        CourseSection(1, "1~20 · 기본 표현", 1..20),
        CourseSection(2, "21~40 · 상태·가족·요청", 21..40),
        CourseSection(3, "41~60 · 생활·감정", 41..60),
        CourseSection(4, "61~80 · 놀이·반응", 61..80),
        CourseSection(5, "81~100 · 시간·학교·식사", 81..100)
    )

    fun indexForLesson(lessonId: Int): Int =
        sections.indexOfFirst { lessonId in it.internalIds }.coerceAtLeast(0)
}
