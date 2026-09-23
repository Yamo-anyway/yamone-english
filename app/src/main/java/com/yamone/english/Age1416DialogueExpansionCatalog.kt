package com.yamone.english

object Age1416DialogueExpansionCatalog {
    private fun dialogue(
        lesson: Lesson,
        opening: Triple<String, String, String>,
        followUp: Triple<String, String, String>,
        response: Triple<String, String, String>,
        close: Triple<String, String, String>
    ): LessonDialogue {
        val support = Age1416LessonExpansionCatalog.supportLine(lesson.id)
            ?: (lesson.accepted.firstOrNull().orEmpty() to lesson.ownPromptKo)
        return LessonDialogue(
            lesson.id,
            listOf(
                DialogueLine(opening.first, opening.second, opening.third),
                DialogueLine("B", lesson.target, lesson.meaning),
                DialogueLine(followUp.first, followUp.second, followUp.third),
                DialogueLine("B", support.first, support.second),
                DialogueLine(response.first, response.second, response.third),
                DialogueLine(close.first, close.second, close.third)
            )
        )
    }

    val dialogues: List<LessonDialogue> = Age1416LessonExpansionCatalog.lessons.map { lesson ->
        when (lesson.courseLessonNumber) {
            in 51..60 -> dialogue(
                lesson,
                Triple("A", "What do you think about this argument?", "이 주장 어떻게 생각해?"),
                Triple("A", "What makes that important here?", "여기서는 그게 왜 중요해?"),
                Triple("A", "That makes the reasoning clearer.", "그러면 논리가 더 분명해지네."),
                Triple("B", "Exactly. I want the conclusion to match the evidence.", "맞아. 결론이 근거에 맞았으면 해.")
            )
            in 61..70 -> dialogue(
                lesson,
                Triple("A", "How should we handle this as a team?", "이걸 팀으로 어떻게 풀면 좋을까?"),
                Triple("A", "What would make the plan work better?", "계획이 더 잘 되려면 뭐가 필요할까?"),
                Triple("A", "That sounds practical.", "그거 실용적인데."),
                Triple("B", "Good. Let's make the next step clear.", "좋아. 다음 단계를 분명히 하자.")
            )
            in 71..80 -> dialogue(
                lesson,
                Triple("A", "Can we trust this online?", "온라인에서 이걸 믿어도 될까?"),
                Triple("A", "What would you check before trusting it?", "믿기 전에 뭘 확인할래?"),
                Triple("A", "That would help us avoid a quick mistake.", "그러면 성급한 판단을 피하는 데 도움이 되겠다."),
                Triple("B", "Right. I'd rather verify it than just assume it's true.", "맞아. 그냥 사실이라고 생각하기보다 확인하고 싶어.")
            )
            in 81..90 -> dialogue(
                lesson,
                Triple("A", "How are you thinking about your next step?", "다음 단계를 어떻게 생각하고 있어?"),
                Triple("A", "What would make that realistic for you?", "너한테 현실적으로 만들려면 뭐가 필요해?"),
                Triple("A", "That sounds easier to keep doing.", "그러면 계속하기 더 쉬워 보이네."),
                Triple("B", "That's the goal. I want a plan I can actually maintain.", "그게 목표야. 실제로 유지할 수 있는 계획을 원해.")
            )
            in 91..100 -> dialogue(
                lesson,
                Triple("A", "How should we think about this fairly?", "이걸 공정하게 생각하려면 어떻게 해야 할까?"),
                Triple("A", "What perspective could we be missing?", "우리가 놓치고 있는 관점이 있을까?"),
                Triple("A", "That gives us more to consider.", "생각해볼 게 더 생기네."),
                Triple("B", "Exactly. I want to be clear without ignoring other people.", "맞아. 다른 사람을 무시하지 않으면서 내 생각도 분명히 하고 싶어.")
            )
            else -> error("Unexpected 14~16 expansion lesson: ${lesson.id}")
        }
    }
}
