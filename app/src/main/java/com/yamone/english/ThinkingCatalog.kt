package com.yamone.english

data class ThinkingGuide(
    val lessonId: Int,
    val englishOrderKorean: String
)

object ThinkingCatalog {
    private val guides = listOf(
        ThinkingGuide(1, "안녕 → 나는 → 미나야"),
        ThinkingGuide(2, "어떻게 → 지내 → 너는?"),
        ThinkingGuide(3, "나는 → 좋아해 → 이것을"),
        ThinkingGuide(4, "나는 → 좋아하지 않아 → 그것을"),
        ThinkingGuide(5, "나는 → 배고파"),
        ThinkingGuide(6, "나는 → 목말라"),
        ThinkingGuide(7, "할 수 있을까 → 내가 가지는 것을 → 이것을?"),
        ThinkingGuide(8, "할 수 있을까 → 네가 돕는 것을 → 나를?"),
        ThinkingGuide(9, "기다려 → 잠깐"),
        ThinkingGuide(10, "말해줘 → 그것을 → 다시 → 부탁해"),
        ThinkingGuide(11, "나는 → 알지 못해"),
        ThinkingGuide(12, "나는 → 알았어 → 그것을"),
        ThinkingGuide(13, "어디로 → 가고 있어 → 너는?"),
        ThinkingGuide(14, "나는 → 가고 있어 → 집으로"),
        ThinkingGuide(15, "우리 → 하자 → 그것을 → 같이"),
        ThinkingGuide(16, "그것은 → 나의 → 차례야"),
        ThinkingGuide(17, "그것은 → 괜찮아"),
        ThinkingGuide(18, "나는 → 미안해"),
        ThinkingGuide(19, "고마워 → 너에게"),
        ThinkingGuide(20, "그것은 → 재미있었어"),
        ThinkingGuide(21, "나는 → 피곤해"),
        ThinkingGuide(22, "무엇을 → 하고 있어 → 너는?"),
        ThinkingGuide(23, "나는 → 준비됐어"),
        ThinkingGuide(24, "볼게 → 너를 → 나중에"),
        ThinkingGuide(25, "이 사람은 → 이야 → 나의 엄마"),
        ThinkingGuide(26, "저 사람은 → 이야 → 나의 아빠"),
        ThinkingGuide(27, "나는 → 가지고 있어 → 한 명의 남자 형제를"),
        ThinkingGuide(28, "그녀는 → 이야 → 나의 여자 형제"),
        ThinkingGuide(29, "어디에 있어 → 나의 장난감?"),
        ThinkingGuide(30, "그것은 → 있어 → 저쪽에"),
        ThinkingGuide(31, "이것은 → 이야 → 나의 것"),
        ThinkingGuide(32, "인가 → 이것이 → 너의 것?"),
        ThinkingGuide(33, "무엇이야 → 이것은?"),
        ThinkingGuide(34, "어느 것을 → 원해 → 너는?"),
        ThinkingGuide(35, "나는 → 원해 → 빨간 것을"),
        ThinkingGuide(36, "나는 → 필요해 → 연필 하나가"),
        ThinkingGuide(37, "할 수 있을까 → 내가 가는 것을 → 밖으로?"),
        ThinkingGuide(38, "할 수 있을까 → 내가 노는 것을 → 너와?"),
        ThinkingGuide(39, "나는 → 할 수 있어 → 그것을"),
        ThinkingGuide(40, "나는 → 할 수 없어 → 닿는 것을 → 그것에"),
        ThinkingGuide(41, "와 → 여기로 → 부탁해"),
        ThinkingGuide(42, "봐 → 이것을!"),
        ThinkingGuide(43, "되어라 → 조심스럽게"),
        ThinkingGuide(44, "나는 → 무서워"),
        ThinkingGuide(45, "나는 → 행복해"),
        ThinkingGuide(46, "나는 → 슬퍼"),
        ThinkingGuide(47, "나는 → 추워"),
        ThinkingGuide(48, "나는 → 더워"),
        ThinkingGuide(49, "나는 → 느끼지 않아 → 좋은 상태를"),
        ThinkingGuide(50, "비가 → 오고 있어")
    )

    private val allGuides =
        guides +
            ThinkingExpansionCatalog.guides +
            Age810ThinkingCatalog.guides +
            Age1113ThinkingCatalog.guides +
            Age1416ThinkingCatalog.guides +
            Age1416ThinkingExpansionCatalog.guides

    fun byLessonId(id: Int): ThinkingGuide =
        allGuides.firstOrNull { it.lessonId == id }
            ?: ThinkingGuide(id, LessonCatalog.byId(id)?.meaning.orEmpty())
}
