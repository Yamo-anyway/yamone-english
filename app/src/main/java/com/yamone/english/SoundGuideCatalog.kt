package com.yamone.english

data class SoundGuideExtra(
    val slowKorean: String,
    val naturalKorean: String,
    val rhythm: String
)

object SoundGuideCatalog {
    private val guides = mapOf(
        1 to SoundGuideExtra("하이 / 아임 / 미나", "●하이 | 아임 ●미나↘", "강 | 약-강 ↘"),
        2 to SoundGuideExtra("하우 / 아 / 유", "●하워유↗", "강-약-약 ↗"),
        3 to SoundGuideExtra("아이 / 라익 / 디스", "아이 ●라익 디스↘", "약-강-약 ↘"),
        4 to SoundGuideExtra("아이 / 돈트 / 라익 / 잇", "아이 돈(트) ●라이킷↘", "약-약-강-약 ↘"),
        5 to SoundGuideExtra("아임 / 헝그리", "아임 ●헝그리↘", "약-강 ↘"),
        6 to SoundGuideExtra("아임 / 썰스티", "아임 ●썰스티↘", "약-강 ↘"),
        7 to SoundGuideExtra("캔 / 아이 / 해브 / 디스", "커나이 ●해브 디스↗", "약-약-강-약 ↗"),
        8 to SoundGuideExtra("캔 / 유 / 헬프 / 미", "컨유 ●헬프 미↗", "약-약-강-약 ↗"),
        9 to SoundGuideExtra("웨잇 / 어 / 세컨드", "웨이러 ●세컨(드)↘", "강-약-강 ↘"),
        10 to SoundGuideExtra("세이 / 댓 / 어겐 / 플리즈", "세이 대러●겐 | 플리즈↘", "강-약-강 | 약 ↘"),
        11 to SoundGuideExtra("아이 / 돈트 / 노우", "아이 돈 ●노우↘", "약-약-강 ↘"),
        12 to SoundGuideExtra("아이 / 갓 / 잇", "아이 ●가릿↘", "약-강-약 ↘"),
        13 to SoundGuideExtra("웨어 / 아 / 유 / 고잉", "●웨어러유 ●고잉↘", "강-약-약-강 ↘"),
        14 to SoundGuideExtra("아임 / 고잉 / 홈", "아임 ●고잉 ●홈↘", "약-강-강 ↘"),
        15 to SoundGuideExtra("렛츠 / 두 / 잇 / 터게더", "렛츠 두윗 터●게더↘", "강-강-약-강 ↘"),
        16 to SoundGuideExtra("잇츠 / 마이 / 턴", "잇츠 마이 ●턴↘", "약-약-강 ↘"),
        17 to SoundGuideExtra("잇츠 / 오케이", "이츠 오●케이↘", "약-강 ↘"),
        18 to SoundGuideExtra("아임 / 쏘리", "아임 ●쏘리↘", "약-강 ↘"),
        19 to SoundGuideExtra("땡크 / 유", "●땡큐↘", "강-약 ↘"),
        20 to SoundGuideExtra("댓 / 워즈 / 펀", "댓 워즈 ●펀↘", "약-약-강 ↘"),
        21 to SoundGuideExtra("아임 / 타이어드", "아임 ●타이어드↘", "약-강 ↘"),
        22 to SoundGuideExtra("왓 / 아 / 유 / 두잉", "●워러유 ●두잉↘", "강-약-약-강 ↘"),
        23 to SoundGuideExtra("아임 / 레디", "아임 ●레디↘", "약-강 ↘"),
        24 to SoundGuideExtra("씨 / 유 / 레이터", "씨유 ●레이러↘", "강-약-강 ↘")
    )

    fun byLessonId(id: Int): SoundGuideExtra =
        guides[id] ?: SoundGuideExtra("", "", "")
}
