package com.yamone.english

data class Age1416AssessmentSummary(
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

object Age1416AssessmentCatalog {
    val questions = listOf(
        AssessmentQuestion(1, AssessmentKind.LISTENING, 404),
        AssessmentQuestion(2, AssessmentKind.LISTENING, 419),
        AssessmentQuestion(3, AssessmentKind.LISTENING, 437),
        AssessmentQuestion(4, AssessmentKind.LISTENING, 451),
        AssessmentQuestion(5, AssessmentKind.LISTENING, 466),
        AssessmentQuestion(6, AssessmentKind.LISTENING, 482),
        AssessmentQuestion(7, AssessmentKind.LISTENING, 497),

        AssessmentQuestion(8, AssessmentKind.SPEAKING, 408),
        AssessmentQuestion(9, AssessmentKind.SPEAKING, 425),
        AssessmentQuestion(10, AssessmentKind.SPEAKING, 443),
        AssessmentQuestion(11, AssessmentKind.SPEAKING, 457),
        AssessmentQuestion(12, AssessmentKind.SPEAKING, 473),
        AssessmentQuestion(13, AssessmentKind.SPEAKING, 489),
        AssessmentQuestion(14, AssessmentKind.SPEAKING, 500),

        AssessmentQuestion(15, AssessmentKind.ORDER, 415),
        AssessmentQuestion(16, AssessmentKind.ORDER, 429),
        AssessmentQuestion(17, AssessmentKind.ORDER, 440),
        AssessmentQuestion(18, AssessmentKind.ORDER, 458),
        AssessmentQuestion(19, AssessmentKind.ORDER, 474),
        AssessmentQuestion(20, AssessmentKind.ORDER, 490),
        AssessmentQuestion(21, AssessmentKind.ORDER, 495),

        AssessmentQuestion(22, AssessmentKind.SITUATION, 406),
        AssessmentQuestion(23, AssessmentKind.SITUATION, 416),
        AssessmentQuestion(24, AssessmentKind.SITUATION, 427),
        AssessmentQuestion(25, AssessmentKind.SITUATION, 468),
        AssessmentQuestion(26, AssessmentKind.SITUATION, 478),
        AssessmentQuestion(27, AssessmentKind.SITUATION, 488),
        AssessmentQuestion(28, AssessmentKind.SITUATION, 498)
    )
}
