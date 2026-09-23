package com.yamone.english

data class Age1113AssessmentSummary(
    val listeningCorrect: Int,
    val speakingCorrect: Int,
    val orderCorrect: Int,
    val situationCorrect: Int
) {
    val totalCorrect: Int
        get() = listeningCorrect + speakingCorrect + orderCorrect + situationCorrect

    val totalQuestions: Int
        get() = 24

    fun label(correct: Int): String = when {
        correct >= 5 -> "잘함"
        correct >= 3 -> "조금 어려움"
        else -> "다시 연습 필요"
    }

    val overallLabel: String
        get() = when {
            totalCorrect >= 20 -> "잘함"
            totalCorrect >= 13 -> "조금 어려움"
            else -> "다시 연습 필요"
        }
}

object Age1113AssessmentCatalog {
    val questions = listOf(
        AssessmentQuestion(1, AssessmentKind.LISTENING, 304),
        AssessmentQuestion(2, AssessmentKind.LISTENING, 319),
        AssessmentQuestion(3, AssessmentKind.LISTENING, 337),
        AssessmentQuestion(4, AssessmentKind.LISTENING, 371),
        AssessmentQuestion(5, AssessmentKind.LISTENING, 386),
        AssessmentQuestion(6, AssessmentKind.LISTENING, 399),

        AssessmentQuestion(7, AssessmentKind.SPEAKING, 308),
        AssessmentQuestion(8, AssessmentKind.SPEAKING, 325),
        AssessmentQuestion(9, AssessmentKind.SPEAKING, 333),
        AssessmentQuestion(10, AssessmentKind.SPEAKING, 357),
        AssessmentQuestion(11, AssessmentKind.SPEAKING, 381),
        AssessmentQuestion(12, AssessmentKind.SPEAKING, 400),

        AssessmentQuestion(13, AssessmentKind.ORDER, 315),
        AssessmentQuestion(14, AssessmentKind.ORDER, 329),
        AssessmentQuestion(15, AssessmentKind.ORDER, 340),
        AssessmentQuestion(16, AssessmentKind.ORDER, 358),
        AssessmentQuestion(17, AssessmentKind.ORDER, 374),
        AssessmentQuestion(18, AssessmentKind.ORDER, 390),

        AssessmentQuestion(19, AssessmentKind.SITUATION, 306),
        AssessmentQuestion(20, AssessmentKind.SITUATION, 316),
        AssessmentQuestion(21, AssessmentKind.SITUATION, 327),
        AssessmentQuestion(22, AssessmentKind.SITUATION, 368),
        AssessmentQuestion(23, AssessmentKind.SITUATION, 378),
        AssessmentQuestion(24, AssessmentKind.SITUATION, 398)
    )
}
