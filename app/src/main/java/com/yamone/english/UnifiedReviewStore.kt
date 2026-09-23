package com.yamone.english

import android.content.Context

enum class ReviewKind(val label: String) {
    LISTENING("듣기"),
    SPEAKING("말하기"),
    ORDER("어순")
}

data class UnifiedReviewItem(
    val lessonId: Int,
    val counts: Map<ReviewKind, Int>,
    val nextDueAt: Long
) {
    val totalErrors: Int get() = counts.values.sum()
    val kinds: List<ReviewKind> get() = counts.filterValues { it > 0 }.keys.toList()
    val isDue: Boolean get() = nextDueAt <= System.currentTimeMillis()
}

class UnifiedReviewStore(context: Context) {
    private val prefs = context.getSharedPreferences("yamone_english_review", Context.MODE_PRIVATE)

    fun recordError(lessonId: Int, kind: ReviewKind) {
        val key = countKey(lessonId, kind)
        val next = prefs.getInt(key, 0) + 1
        prefs.edit()
            .putInt(key, next.coerceAtMost(9))
            .putLong(dueKey(lessonId, kind), System.currentTimeMillis())
            .apply()
    }

    fun recordSuccess(lessonId: Int, kind: ReviewKind) {
        val key = countKey(lessonId, kind)
        val current = prefs.getInt(key, 0)
        if (current <= 0) return

        if (current == 1) {
            prefs.edit()
                .remove(key)
                .remove(dueKey(lessonId, kind))
                .apply()
        } else {
            prefs.edit()
                .putInt(key, current - 1)
                .putLong(
                    dueKey(lessonId, kind),
                    System.currentTimeMillis() + 24L * 60L * 60L * 1000L
                )
                .apply()
        }
    }

    fun items(): List<UnifiedReviewItem> {
        val now = System.currentTimeMillis()

        return LessonCatalog.lessons.mapNotNull { lesson ->
            val counts = ReviewKind.entries.associateWith { kind ->
                prefs.getInt(countKey(lesson.id, kind), 0)
            }.filterValues { it > 0 }

            if (counts.isEmpty()) {
                null
            } else {
                val due = counts.keys.minOf { kind ->
                    prefs.getLong(dueKey(lesson.id, kind), now)
                }
                UnifiedReviewItem(
                    lessonId = lesson.id,
                    counts = counts,
                    nextDueAt = due
                )
            }
        }.sortedWith(
            compareByDescending<UnifiedReviewItem> { it.isDue }
                .thenByDescending { it.totalErrors }
                .thenBy { it.nextDueAt }
                .thenBy { it.lessonId }
        )
    }

    fun dueItems(): List<UnifiedReviewItem> = items().filter { it.isDue }

    fun activeLessonIds(): Set<Int> = items().map { it.lessonId }.toSet()

    fun clearLesson(lessonId: Int) {
        val editor = prefs.edit()
        ReviewKind.entries.forEach { kind ->
            editor.remove(countKey(lessonId, kind))
            editor.remove(dueKey(lessonId, kind))
        }
        editor.apply()
    }

    private fun countKey(lessonId: Int, kind: ReviewKind): String =
        "count_" + lessonId + "_" + kind.name

    private fun dueKey(lessonId: Int, kind: ReviewKind): String =
        "due_" + lessonId + "_" + kind.name
}
