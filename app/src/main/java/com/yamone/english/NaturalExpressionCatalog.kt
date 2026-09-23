package com.yamone.english

data class NaturalExpression(
    val id: Int,
    val category: String,
    val korean: String,
    val keywords: List<String>,
    val naturalEnglish: String,
    val alternative: String,
    val rhythmEnglish: String,
    val soundKorean: String
)

object NaturalExpressionCatalog {
    val expressions = listOf(
        NaturalExpression(1, "오늘", "오늘 일이 너무 많아서 피곤해.", listOf("오늘", "일", "많", "피곤"), "I had a lot to do today, so I'm tired.", "It was a long day. I'm tired.", "I had a LOT to DO today | so I'm TIRED ↘", "아이 해더 랏 터 두 터데이 | 쏘 아임 ●타이어드↘"),
        NaturalExpression(2, "기분", "지금 아무것도 하기 싫어.", listOf("아무것도", "하기 싫", "지금", "귀찮"), "I don't feel like doing anything right now.", "I just don't feel like doing anything.", "I don't FEEL like doing ANYthing | right NOW ↘", "아이 돈 필라익 두잉 애니띵 | 라잇 ●나우↘"),
        NaturalExpression(3, "오늘", "집에 가서 그냥 쉬고 싶어.", listOf("집", "쉬고 싶", "피곤", "가서"), "I just want to go home and rest.", "I just want to go home and relax.", "I just WANT to go HOME and REST ↘", "아이 저스트 원너 고 홈 앤 ●레스트↘"),
        NaturalExpression(4, "음식", "배는 안 고픈데 뭔가 먹고 싶어.", listOf("배", "안 고프", "먹고 싶", "뭔가"), "I'm not hungry, but I feel like eating something.", "I'm not really hungry, but I want something to eat.", "I'm not HUNgry | but I FEEL like EATing something ↘", "아임 낫 헝그리 | 벗 아이 필라익 ●이팅 썸띵↘"),
        NaturalExpression(5, "느낌", "생각보다 괜찮았어.", listOf("생각보다", "괜찮", "의외"), "It was better than I expected.", "It was actually pretty good.", "It was BETter than I exPECted ↘", "잇 워즈 베러 댄 아이 익●스펙티드↘"),
        NaturalExpression(6, "느낌", "별로 기대 안 했는데 재미있었어.", listOf("기대", "재미", "별로", "안 했"), "I didn't expect much, but it was fun.", "I wasn't expecting much, but I had fun.", "I didn't exPECT much | but it was FUN ↘", "아이 디든 익스펙트 머치 | 벗 잇 워즈 ●펀↘"),
        NaturalExpression(7, "대화", "나중에 다시 얘기하자.", listOf("나중", "다시", "얘기", "말하"), "Let's talk about it later.", "We can talk about it later.", "Let's TALK about it LAter ↘", "렛츠 토커바웃잇 ●레이러↘"),
        NaturalExpression(8, "의견", "지금은 잘 모르겠어.", listOf("지금", "모르겠", "잘 모르", "확실"), "I'm not sure right now.", "I don't really know yet.", "I'm not SURE | right NOW ↘", "아임 낫 슈어 | 라잇 ●나우↘"),
        NaturalExpression(9, "의견", "조금 생각해 볼게.", listOf("생각", "해볼게", "조금", "고민"), "Let me think about it.", "I'll think about it.", "Let me THINK about it ↘", "렛미 ●띵커바웃잇↘"),
        NaturalExpression(10, "의견", "그게 더 나을 것 같아.", listOf("더 나을", "그게", "좋을 것", "같아"), "I think that would be better.", "That might be better.", "I THINK that would be BETter ↘", "아이 띵크 댓 우드 비 ●베러↘"),
        NaturalExpression(11, "약속", "괜찮으면 같이 가자.", listOf("괜찮으면", "같이", "가자", "함께"), "If you're okay with it, let's go together.", "If that works for you, let's go together.", "If you're oKAY with it | let's go toGETHer ↘", "이프 유어 오케이 윗잇 | 렛츠 고 터●게더↘"),
        NaturalExpression(12, "위로", "너무 신경 쓰지 마.", listOf("신경", "쓰지 마", "걱정", "너무"), "Don't worry about it too much.", "Try not to worry about it.", "Don't WORry about it too MUCH ↘", "돈 워리 어바우릿 투 ●머치↘"),
        NaturalExpression(13, "사과", "일부러 그런 건 아니야.", listOf("일부러", "아니", "그런 건", "의도"), "I didn't mean to do that.", "I didn't do it on purpose.", "I didn't MEAN to do THAT ↘", "아이 디든 민 터 두 ●댓↘"),
        NaturalExpression(14, "대화", "무슨 말인지 알겠어.", listOf("무슨 말", "알겠", "이해", "뜻"), "I know what you mean.", "I get what you're saying.", "I KNOW what you MEAN ↘", "아이 노우 왓츄 ●민↘"),
        NaturalExpression(15, "의견", "나도 그렇게 생각해.", listOf("나도", "그렇게", "생각", "동의"), "I think so too.", "I agree.", "I THINK so TOO ↘", "아이 띵크 쏘 ●투↘"),
        NaturalExpression(16, "의견", "꼭 그런 건 아니야.", listOf("꼭", "그런 건 아니", "반드시", "아니야"), "Not necessarily.", "That's not always true.", "Not NECessarily ↘", "낫 네서●세럴리↘"),
        NaturalExpression(17, "반응", "그럴 수도 있지.", listOf("그럴 수도", "있지", "가능", "그럴 수"), "That could happen.", "Yeah, that's possible.", "That could HAPpen ↘", "댓 쿠드 ●해픈↘"),
        NaturalExpression(18, "질문", "그냥 궁금해서 물어봤어.", listOf("궁금", "물어", "그냥", "질문"), "I was just curious.", "I was just wondering.", "I was just CURious ↘", "아이 워즈 저스트 ●큐리어스↘"),
        NaturalExpression(19, "선택", "아직 결정 안 했어.", listOf("아직", "결정", "안 했", "못 정"), "I haven't decided yet.", "I still haven't decided.", "I HAVEn't deCIDed YET ↘", "아이 해븐 디사이디드 ●옛↘"),
        NaturalExpression(20, "약속", "시간 되면 연락해.", listOf("시간", "되면", "연락", "전화"), "Let me know when you're free.", "Text me when you have time.", "Let me KNOW | when you're FREE ↘", "렛미 노우 | 웬 유어 ●프리↘"),
        NaturalExpression(21, "약속", "조금 늦을 것 같아.", listOf("늦", "조금", "것 같", "도착"), "I think I'm going to be a little late.", "I might be a little late.", "I THINK I'm gonna be a little LATE ↘", "아이 띵크 아임 거너 비 어 리를 ●레잇↘"),
        NaturalExpression(22, "이동", "지금 가는 중이야.", listOf("가는 중", "가고 있", "지금", "이동"), "I'm on my way.", "I'm heading there now.", "I'm on my WAY ↘", "아임 온 마이 ●웨이↘"),
        NaturalExpression(23, "이동", "거의 다 왔어.", listOf("거의", "다 왔", "도착", "근처"), "I'm almost there.", "I'm nearly there.", "I'm ALmost THERE ↘", "아임 올모스트 ●데어↘"),
        NaturalExpression(24, "오늘", "오늘은 그냥 집에 있을래.", listOf("오늘", "집", "있을래", "안 나가"), "I think I'll just stay home today.", "I'm just going to stay home today.", "I think I'll just stay HOME today ↘", "아이 띵크 아일 저스트 스테이 ●홈 터데이↘"),
        NaturalExpression(25, "시간", "생각보다 시간이 오래 걸렸어.", listOf("시간", "오래", "생각보다", "걸렸"), "It took longer than I expected.", "It took more time than I thought.", "It took LONGer than I exPECted ↘", "잇 툭 롱거 댄 아이 익●스펙티드↘"),
        NaturalExpression(26, "기분", "오늘 기분이 좀 이상해.", listOf("기분", "이상", "오늘", "좀"), "I feel a little off today.", "I don't feel quite like myself today.", "I feel a little OFF today ↘", "아이 필 어 리를 ●오프 터데이↘"),
        NaturalExpression(27, "선택", "아무거나 괜찮아.", listOf("아무거나", "괜찮", "상관없", "뭐든"), "Anything is fine with me.", "I'm okay with anything.", "ANYthing is FINE with me ↘", "애니띵 이즈 파인 윗 ●미↘"),
        NaturalExpression(28, "선택", "난 둘 다 좋아.", listOf("둘 다", "좋아", "둘", "상관"), "I like both.", "Either one is fine with me.", "I like BOTH ↘", "아이 라익 ●보우쓰↘"),
        NaturalExpression(29, "의견", "그건 좀 애매해.", listOf("애매", "그건", "잘 모르", "미묘"), "I'm not sure about that.", "That's a little hard to say.", "I'm not SURE about THAT ↘", "아임 낫 슈어 어바웃 ●댓↘"),
        NaturalExpression(30, "선택", "난 상관없어.", listOf("상관없", "아무거나", "괜찮", "난"), "I don't mind.", "Either way is fine with me.", "I don't MIND ↘", "아이 돈 ●마인드↘"),
        NaturalExpression(31, "대화", "내 말은 그게 아니야.", listOf("내 말", "그게 아니", "뜻", "아니야"), "That's not what I mean.", "That's not what I was trying to say.", "That's NOT what I MEAN ↘", "댓츠 낫 왓 아이 ●민↘"),
        NaturalExpression(32, "반응", "진짜 그렇게 생각해?", listOf("진짜", "생각해", "정말", "그렇게"), "Do you really think so?", "You really think so?", "Do you REALly THINK so ↗", "두유 리얼리 ●띵크 쏘↗"),
        NaturalExpression(33, "대화", "내가 잘못 이해했나 봐.", listOf("잘못", "이해", "했나 봐", "착각"), "I think I misunderstood.", "Maybe I got it wrong.", "I THINK I misunderSTOOD ↘", "아이 띵크 아이 미스언더●스투드↘"),
        NaturalExpression(34, "약속", "가능하면 조금 일찍 와.", listOf("가능하면", "일찍", "와", "조금"), "Come a little early if you can.", "If you can, come a little early.", "Come a little EARly | if you CAN ↘", "컴 어 리를 얼리 | 이프 유 ●캔↘"),
        NaturalExpression(35, "오늘", "오늘은 좀 일찍 자야겠어.", listOf("오늘", "일찍", "자야", "잠"), "I should go to bed a little early tonight.", "I think I'll go to bed early tonight.", "I should go to BED a little EARly tonight ↘", "아이 슈드 고 터 베드 어 리를 ●얼리 터나잇↘"),
        NaturalExpression(36, "기분", "왜 그런지 모르겠는데 기분이 좋아.", listOf("왜", "모르겠", "기분이 좋아", "좋아"), "I don't know why, but I'm in a good mood.", "For some reason, I'm in a good mood.", "I don't know WHY | but I'm in a good MOOD ↘", "아이 돈 노우 와이 | 벗 아임 인 어 굿 ●무드↘")
    )

    val categories: List<String> =
        listOf("전체") + expressions.map { it.category }.distinct()

    fun byCategory(category: String): List<NaturalExpression> =
        if (category == "전체") expressions else expressions.filter { it.category == category }

    fun bestMatches(koreanSpeech: String, limit: Int = 3): List<Pair<NaturalExpression, Double>> {
        if (koreanSpeech.isBlank()) return emptyList()
        return expressions
            .map { it to matchScore(koreanSpeech, it) }
            .sortedByDescending { it.second }
            .take(limit)
    }

    private fun matchScore(input: String, expression: NaturalExpression): Double {
        val a = normalizeKorean(input)
        val b = normalizeKorean(expression.korean)

        if (a.isBlank() || b.isBlank()) return 0.0
        if (a == b) return 1.0

        val keywordScore = expression.keywords.sumOf { keyword ->
            val normalized = normalizeKorean(keyword)
            if (normalized.isNotBlank() && a.contains(normalized)) {
                0.14 + (normalized.length.coerceAtMost(6) * 0.015)
            } else {
                0.0
            }
        }.coerceAtMost(0.72)

        val bigramScore = diceCoefficient(a, b) * 0.55
        return (keywordScore + bigramScore).coerceAtMost(1.0)
    }

    private fun normalizeKorean(text: String): String =
        text.lowercase()
            .replace(Regex("[^가-힣a-z0-9]"), "")
            .trim()

    private fun diceCoefficient(a: String, b: String): Double {
        if (a.length < 2 || b.length < 2) return if (a == b) 1.0 else 0.0

        fun bigrams(value: String): MutableList<String> =
            (0 until value.length - 1).map { value.substring(it, it + 2) }.toMutableList()

        val left = bigrams(a)
        val right = bigrams(b)
        var matches = 0

        val remaining = right.toMutableList()
        left.forEach { gram ->
            val index = remaining.indexOf(gram)
            if (index >= 0) {
                matches += 1
                remaining.removeAt(index)
            }
        }

        return (2.0 * matches) / (left.size + right.size).coerceAtLeast(1)
    }
}
