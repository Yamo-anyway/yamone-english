package com.yamone.english

import android.content.Context

class Age1416AssessmentStore(context: Context) {
    private val prefs =
        context.getSharedPreferences("yamone_english_assessment_14_16", Context.MODE_PRIVATE)

    fun save(summary: Age1416AssessmentSummary) {
        prefs.edit()
            .putInt("listening", summary.listeningCorrect)
            .putInt("speaking", summary.speakingCorrect)
            .putInt("order", summary.orderCorrect)
            .putInt("situation", summary.situationCorrect)
            .putLong("completed_at", System.currentTimeMillis())
            .apply()
    }

    fun latest(): Age1416AssessmentSummary? {
        if (!prefs.contains("completed_at")) return null
        return Age1416AssessmentSummary(
            listeningCorrect = prefs.getInt("listening", 0),
            speakingCorrect = prefs.getInt("speaking", 0),
            orderCorrect = prefs.getInt("order", 0),
            situationCorrect = prefs.getInt("situation", 0)
        )
    }
}
