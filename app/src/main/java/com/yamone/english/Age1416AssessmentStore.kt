package com.yamone.english

import android.content.Context

class Age1416AssessmentStore(context: Context) {
    private val prefs = context.getSharedPreferences(
        PersistenceContract.assessmentPrefs(CourseLevel.AGE_14_16),
        Context.MODE_PRIVATE
    )

    fun save(summary: Age1416AssessmentSummary) {
        prefs.edit()
            .putInt(PersistenceContract.ASSESSMENT_LISTENING, summary.listeningCorrect)
            .putInt(PersistenceContract.ASSESSMENT_SPEAKING, summary.speakingCorrect)
            .putInt(PersistenceContract.ASSESSMENT_ORDER, summary.orderCorrect)
            .putInt(PersistenceContract.ASSESSMENT_SITUATION, summary.situationCorrect)
            .putLong(PersistenceContract.ASSESSMENT_COMPLETED_AT, System.currentTimeMillis())
            .apply()
    }

    fun latest(): Age1416AssessmentSummary? {
        if (!prefs.contains(PersistenceContract.ASSESSMENT_COMPLETED_AT)) return null
        return Age1416AssessmentSummary(
            listeningCorrect = prefs.getInt(PersistenceContract.ASSESSMENT_LISTENING, 0),
            speakingCorrect = prefs.getInt(PersistenceContract.ASSESSMENT_SPEAKING, 0),
            orderCorrect = prefs.getInt(PersistenceContract.ASSESSMENT_ORDER, 0),
            situationCorrect = prefs.getInt(PersistenceContract.ASSESSMENT_SITUATION, 0)
        )
    }
}
