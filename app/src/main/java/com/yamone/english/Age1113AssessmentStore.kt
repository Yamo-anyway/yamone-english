package com.yamone.english

import android.content.Context

class Age1113AssessmentStore(context: Context) {
    private val course = CourseLevel.AGE_11_13
    private val prefs = context.getSharedPreferences(
        PersistenceContract.assessmentPrefs(course),
        Context.MODE_PRIVATE
    )

    fun save(summary: Age1113AssessmentSummary) {
        prefs.edit()
            .putInt(PersistenceContract.ASSESSMENT_LISTENING, AssessmentStateSanitizer.score(course, summary.listeningCorrect))
            .putInt(PersistenceContract.ASSESSMENT_SPEAKING, AssessmentStateSanitizer.score(course, summary.speakingCorrect))
            .putInt(PersistenceContract.ASSESSMENT_ORDER, AssessmentStateSanitizer.score(course, summary.orderCorrect))
            .putInt(PersistenceContract.ASSESSMENT_SITUATION, AssessmentStateSanitizer.score(course, summary.situationCorrect))
            .putLong(PersistenceContract.ASSESSMENT_COMPLETED_AT, System.currentTimeMillis())
            .apply()
    }

    fun latest(): Age1113AssessmentSummary? {
        val completedAt = prefs.getLong(PersistenceContract.ASSESSMENT_COMPLETED_AT, 0L)
        if (!AssessmentStateSanitizer.hasValidCompletion(completedAt)) return null
        return Age1113AssessmentSummary(
            listeningCorrect = AssessmentStateSanitizer.score(course, prefs.getInt(PersistenceContract.ASSESSMENT_LISTENING, 0)),
            speakingCorrect = AssessmentStateSanitizer.score(course, prefs.getInt(PersistenceContract.ASSESSMENT_SPEAKING, 0)),
            orderCorrect = AssessmentStateSanitizer.score(course, prefs.getInt(PersistenceContract.ASSESSMENT_ORDER, 0)),
            situationCorrect = AssessmentStateSanitizer.score(course, prefs.getInt(PersistenceContract.ASSESSMENT_SITUATION, 0))
        )
    }
}
