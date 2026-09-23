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
        24 to SoundGuideExtra("씨 / 유 / 레이터", "씨유 ●레이러↘", "강-약-강 ↘"),
        25 to SoundGuideExtra("디스 / 이즈 / 마이 / 맘", "디스이즈 마이 ●맘↘", "강-약-약-강 ↘"),
        26 to SoundGuideExtra("댓츠 / 마이 / 대드", "댓츠 마이 ●대드↘", "강-약-강 ↘"),
        27 to SoundGuideExtra("아이 / 해브 / 어 / 브러더", "아이 해버 ●브러더↘", "약-강-약-강 ↘"),
        28 to SoundGuideExtra("쉬즈 / 마이 / 시스터", "쉬즈 마이 ●시스터↘", "강-약-강 ↘"),
        29 to SoundGuideExtra("웨어즈 / 마이 / 토이", "웨어즈 마이 ●토이↘", "강-약-강 ↘"),
        30 to SoundGuideExtra("잇츠 / 오버 / 데어", "잇츠 오버 ●데어↘", "약-약-강 ↘"),
        31 to SoundGuideExtra("디스 / 이즈 / 마인", "디스이즈 ●마인↘", "강-약-강 ↘"),
        32 to SoundGuideExtra("이즈 / 디스 / 요얼즈", "이즈디스 ●요얼즈↗", "약-약-강 ↗"),
        33 to SoundGuideExtra("왓 / 이즈 / 디스", "워디즈 ●디스↘", "강-약-강 ↘"),
        34 to SoundGuideExtra("위치 / 원 / 두 / 유 / 원트", "위치원 두유 ●원트↘", "강-약-약-약-강 ↘"),
        35 to SoundGuideExtra("아이 / 원트 / 더 / 레드 / 원", "아이 원트 더 ●레드원↘", "약-강-약-강-약 ↘"),
        36 to SoundGuideExtra("아이 / 니드 / 어 / 펜슬", "아이 니더 ●펜슬↘", "약-강-약-강 ↘"),
        37 to SoundGuideExtra("캔 / 아이 / 고 / 아웃사이드", "커나이 고 아웃●사이드↗", "약-약-약-강 ↗"),
        38 to SoundGuideExtra("캔 / 아이 / 플레이 / 윗 / 유", "커나이 플레이윗 ●유↗", "약-약-강-약-강 ↗"),
        39 to SoundGuideExtra("아이 / 캔 / 두 / 잇", "아이 컨 ●두윗↘", "약-약-강-약 ↘"),
        40 to SoundGuideExtra("아이 / 캔트 / 리치 / 잇", "아이 캔트 ●리칫↘", "약-강-강-약 ↘"),
        41 to SoundGuideExtra("컴 / 히어 / 플리즈", "컴 ●히어 | 플리즈↘", "강-강 | 약 ↘"),
        42 to SoundGuideExtra("룩 / 앳 / 디스", "루커트 ●디스↘", "강-약-강 ↘"),
        43 to SoundGuideExtra("비 / 케어풀", "비 ●케어풀↘", "약-강 ↘"),
        44 to SoundGuideExtra("아임 / 스케어드", "아임 ●스케어드↘", "약-강 ↘"),
        45 to SoundGuideExtra("아임 / 해피", "아임 ●해피↘", "약-강 ↘"),
        46 to SoundGuideExtra("아임 / 새드", "아임 ●새드↘", "약-강 ↘"),
        47 to SoundGuideExtra("아임 / 코울드", "아임 ●코울드↘", "약-강 ↘"),
        48 to SoundGuideExtra("아임 / 핫", "아임 ●핫↘", "약-강 ↘"),
        49 to SoundGuideExtra("아이 / 돈트 / 필 / 굿", "아이 돈 필 ●굿↘", "약-약-약-강 ↘"),
        50 to SoundGuideExtra("잇츠 / 레이닝", "잇츠 ●레이닝↘", "약-강 ↘")
    )

    fun byLessonId(id: Int): SoundGuideExtra =
        guides[id] ?: SoundGuideExtra("", "", "")
}
