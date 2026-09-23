package com.yamone.english

enum class AssessmentKind(val label: String) {
    LISTENING("듣기"),
    SPEAKING("말하기"),
    ORDER("어순"),
    SITUATION("상황 대응")
}

data class AssessmentQuestion(
    val number: Int,
    val kind: AssessmentKind,
    val lessonId: Int
)

data class AssessmentSummary(
    val listeningCorrect: Int,
    val speakingCorrect: Int,
    val orderCorrect: Int,
    val situationCorrect: Int
) {
    val totalCorrect: Int
        get() = listeningCorrect + speakingCorrect + orderCorrect + situationCorrect

    val totalQuestions: Int
        get() = 16

    fun label(correct: Int): String = when {
        correct >= 4 -> "잘함"
        correct >= 2 -> "조금 어려움"
        else -> "다시 연습 필요"
    }

    val overallLabel: String
        get() = when {
            totalCorrect >= 14 -> "잘함"
            totalCorrect >= 9 -> "조금 어려움"
            else -> "다시 연습 필요"
        }
}

object Age57AssessmentCatalog {
    val questions = listOf(
        AssessmentQuestion(1, AssessmentKind.LISTENING, 2),
        AssessmentQuestion(2, AssessmentKind.LISTENING, 37),
        AssessmentQuestion(3, AssessmentKind.LISTENING, 82),
        AssessmentQuestion(4, AssessmentKind.LISTENING, 94),

        AssessmentQuestion(5, AssessmentKind.SPEAKING, 21),
        AssessmentQuestion(6, AssessmentKind.SPEAKING, 65),
        AssessmentQuestion(7, AssessmentKind.SPEAKING, 75),
        AssessmentQuestion(8, AssessmentKind.SPEAKING, 97),

        AssessmentQuestion(9, AssessmentKind.ORDER, 15),
        AssessmentQuestion(10, AssessmentKind.ORDER, 36),
        AssessmentQuestion(11, AssessmentKind.ORDER, 73),
        AssessmentQuestion(12, AssessmentKind.ORDER, 91),

        AssessmentQuestion(13, AssessmentKind.SITUATION, 8),
        AssessmentQuestion(14, AssessmentKind.SITUATION, 43),
        AssessmentQuestion(15, AssessmentKind.SITUATION, 68),
        AssessmentQuestion(16, AssessmentKind.SITUATION, 100)
    )
}
