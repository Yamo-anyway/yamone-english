package com.yamone.english

data class Age1720AssessmentSummary(
    val listeningCorrect: Int,
    val speakingCorrect: Int,
    val orderCorrect: Int,
    val situationCorrect: Int
) {
    val totalCorrect: Int
        get() = listeningCorrect + speakingCorrect + orderCorrect + situationCorrect

    val totalQuestions: Int
        get() = 28

    fun label(correct: Int): String = when {
        correct >= 6 -> "잘함"
        correct >= 4 -> "조금 어려움"
        else -> "다시 연습 필요"
    }

    val overallLabel: String
        get() = when {
            totalCorrect >= 24 -> "잘함"
            totalCorrect >= 16 -> "조금 어려움"
            else -> "다시 연습 필요"
        }
}

object Age1720AssessmentCatalog {
    val questions = listOf(
        AssessmentQuestion(1, AssessmentKind.LISTENING, 502),
        AssessmentQuestion(2, AssessmentKind.LISTENING, 516),
        AssessmentQuestion(3, AssessmentKind.LISTENING, 531),
        AssessmentQuestion(4, AssessmentKind.LISTENING, 546),
        AssessmentQuestion(5, AssessmentKind.LISTENING, 561),
        AssessmentQuestion(6, AssessmentKind.LISTENING, 576),
        AssessmentQuestion(7, AssessmentKind.LISTENING, 592),

        AssessmentQuestion(8, AssessmentKind.SPEAKING, 507),
        AssessmentQuestion(9, AssessmentKind.SPEAKING, 523),
        AssessmentQuestion(10, AssessmentKind.SPEAKING, 538),
        AssessmentQuestion(11, AssessmentKind.SPEAKING, 553),
        AssessmentQuestion(12, AssessmentKind.SPEAKING, 568),
        AssessmentQuestion(13, AssessmentKind.SPEAKING, 584),
        AssessmentQuestion(14, AssessmentKind.SPEAKING, 600),

        AssessmentQuestion(15, AssessmentKind.ORDER, 511),
        AssessmentQuestion(16, AssessmentKind.ORDER, 527),
        AssessmentQuestion(17, AssessmentKind.ORDER, 542),
        AssessmentQuestion(18, AssessmentKind.ORDER, 557),
        AssessmentQuestion(19, AssessmentKind.ORDER, 572),
        AssessmentQuestion(20, AssessmentKind.ORDER, 588),
        AssessmentQuestion(21, AssessmentKind.ORDER, 596),

        AssessmentQuestion(22, AssessmentKind.SITUATION, 505),
        AssessmentQuestion(23, AssessmentKind.SITUATION, 520),
        AssessmentQuestion(24, AssessmentKind.SITUATION, 535),
        AssessmentQuestion(25, AssessmentKind.SITUATION, 550),
        AssessmentQuestion(26, AssessmentKind.SITUATION, 565),
        AssessmentQuestion(27, AssessmentKind.SITUATION, 580),
        AssessmentQuestion(28, AssessmentKind.SITUATION, 595)
    )
}
