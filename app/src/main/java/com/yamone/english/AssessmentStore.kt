package com.yamone.english

import android.content.Context

class AssessmentStore(context: Context) {
    private val prefs = context.getSharedPreferences(
        PersistenceContract.assessmentPrefs(CourseLevel.AGE_5_7),
        Context.MODE_PRIVATE
    )

    fun save(summary: AssessmentSummary) {
        prefs.edit()
            .putInt(PersistenceContract.ASSESSMENT_LISTENING, summary.listeningCorrect)
            .putInt(PersistenceContract.ASSESSMENT_SPEAKING, summary.speakingCorrect)
            .putInt(PersistenceContract.ASSESSMENT_ORDER, summary.orderCorrect)
            .putInt(PersistenceContract.ASSESSMENT_SITUATION, summary.situationCorrect)
            .putLong(PersistenceContract.ASSESSMENT_COMPLETED_AT, System.currentTimeMillis())
            .apply()
    }

    fun latest(): AssessmentSummary? {
        if (!prefs.contains(PersistenceContract.ASSESSMENT_COMPLETED_AT)) return null
        return AssessmentSummary(
            listeningCorrect = prefs.getInt(PersistenceContract.ASSESSMENT_LISTENING, 0),
            speakingCorrect = prefs.getInt(PersistenceContract.ASSESSMENT_SPEAKING, 0),
            orderCorrect = prefs.getInt(PersistenceContract.ASSESSMENT_ORDER, 0),
            situationCorrect = prefs.getInt(PersistenceContract.ASSESSMENT_SITUATION, 0)
        )
    }

    fun completedAt(): Long = prefs.getLong(PersistenceContract.ASSESSMENT_COMPLETED_AT, 0L)
}
