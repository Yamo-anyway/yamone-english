package com.yamone.english

data class Age810AssessmentSummary(
    val listeningCorrect: Int,
    val speakingCorrect: Int,
    val orderCorrect: Int,
    val situationCorrect: Int
) {
    val totalCorrect: Int
        get() = listeningCorrect + speakingCorrect + orderCorrect + situationCorrect

    val totalQuestions: Int
        get() = 20

    fun label(correct: Int): String = when {
        correct >= 5 -> "잘함"
        correct >= 3 -> "조금 어려움"
        else -> "다시 연습 필요"
    }

    val overallLabel: String
        get() = when {
            totalCorrect >= 17 -> "잘함"
            totalCorrect >= 11 -> "조금 어려움"
            else -> "다시 연습 필요"
        }
}

object Age810AssessmentCatalog {
    val questions = listOf(
        AssessmentQuestion(1, AssessmentKind.LISTENING, 203),
        AssessmentQuestion(2, AssessmentKind.LISTENING, 218),
        AssessmentQuestion(3, AssessmentKind.LISTENING, 239),
        AssessmentQuestion(4, AssessmentKind.LISTENING, 272),
        AssessmentQuestion(5, AssessmentKind.LISTENING, 293),

        AssessmentQuestion(6, AssessmentKind.SPEAKING, 221),
        AssessmentQuestion(7, AssessmentKind.SPEAKING, 229),
        AssessmentQuestion(8, AssessmentKind.SPEAKING, 253),
        AssessmentQuestion(9, AssessmentKind.SPEAKING, 268),
        AssessmentQuestion(10, AssessmentKind.SPEAKING, 299),

        AssessmentQuestion(11, AssessmentKind.ORDER, 214),
        AssessmentQuestion(12, AssessmentKind.ORDER, 227),
        AssessmentQuestion(13, AssessmentKind.ORDER, 263),
        AssessmentQuestion(14, AssessmentKind.ORDER, 282),
        AssessmentQuestion(15, AssessmentKind.ORDER, 297),

        AssessmentQuestion(16, AssessmentKind.SITUATION, 224),
        AssessmentQuestion(17, AssessmentKind.SITUATION, 233),
        AssessmentQuestion(18, AssessmentKind.SITUATION, 274),
        AssessmentQuestion(19, AssessmentKind.SITUATION, 287),
        AssessmentQuestion(20, AssessmentKind.SITUATION, 296)
    )
}
