package com.yamone.english

data class Lesson(
    val id: Int,
    val title: String,
    val emoji: String,
    val situation: String,
    val target: String,
    val meaning: String,
    val soundEnglish: String,
    val soundKorean: String,
    val connectedNote: String,
    val ownPromptKo: String,
    val accepted: List<String>,
    val coachLine: String,
    val course: CourseLevel = CourseLevel.AGE_5_7,
    val courseLessonNumber: Int = id
)

object LessonCatalog {
    private val baseLessons = listOf(
        Lesson(1, "안녕, 나는...", "👋", "처음 만났을 때", "Hi, I'm Mina.", "안녕, 나는 미나야.", "●HI | I'm ●MI-na ↘", "●하이 | 아임 ●미나↘", "쉼표에서 아주 짧게 쉬고, Hi와 이름에 힘을 주세요.", "안녕, 나는 야모야.", listOf("Hi, I'm Yamo.", "Hello, I'm Yamo."), "Hi! What's your name?"),
        Lesson(2, "잘 지냈어?", "🙂", "친구를 만났을 때", "How are you?", "어떻게 지내?", "●HOW‿are‿you ↗", "●하워유↗", "How are you?를 세 단어로 끊지 말고 한 덩어리처럼 들어보세요.", "나 괜찮아.", listOf("I'm good.", "I'm fine."), "How are you today?"),
        Lesson(3, "나 좋아해", "❤️", "좋아하는 것을 말할 때", "I like this.", "나 이거 좋아해.", "I ●LIKE this ↘", "아이 ●라익 디스↘", "like에 힘을 주고 뒤 표현은 조금 가볍게 이어보세요.", "나는 커피를 좋아해.", listOf("I like coffee.", "I love coffee."), "What do you like?"),
        Lesson(4, "나 이거 싫어", "🙅", "싫어하는 것을 말할 때", "I don't like it.", "나 그거 안 좋아해.", "I don't ●LIKE‿it ↘", "아이 돈(트) ●라이킷↘", "like와 it이 붙으면서 라이킷처럼 이어져 들릴 수 있어요.", "나는 매운 음식을 안 좋아해.", listOf("I don't like spicy food."), "Is there anything you don't like?"),
        Lesson(5, "나 배고파", "🍎", "배가 고플 때", "I'm hungry.", "나 배고파.", "I'm ●HUN-gry ↘", "아임 ●헝그리↘", "hungry의 첫 부분에 힘을 주고 문장 끝은 자연스럽게 내려보세요.", "나 지금 배고파.", listOf("I'm hungry now.", "I'm really hungry."), "Are you hungry?"),
        Lesson(6, "나 목말라", "🥤", "목이 마를 때", "I'm thirsty.", "나 목말라.", "I'm ●THIRS-ty ↘", "아임 ●썰스티↘", "th 소리는 한글과 정확히 같지 않으므로 소리를 먼저 듣고 한글은 보조로만 보세요.", "물 좀 마시고 싶어.", listOf("I want some water.", "I'd like some water."), "What do you want to drink?"),
        Lesson(7, "이거 주세요", "🤲", "무언가를 부탁할 때", "Can I have this?", "이거 가져도 돼? / 이거 주세요.", "Can‿I ●HAVE this ↗", "커나이 ●해브 디스↗", "Can I가 약해지면서 커나이처럼 빠르게 연결될 수 있어요.", "물 좀 주세요.", listOf("Can I have some water?", "Can I get some water?"), "What would you like?"),
        Lesson(8, "도와줘", "🆘", "도움이 필요할 때", "Can you help me?", "나 좀 도와줄래?", "Can‿you ●HELP me ↗", "컨유 ●헬프 미↗", "Can you를 한 덩어리처럼 시작하고 help에 힘을 주세요.", "이거 좀 도와줄래?", listOf("Can you help me with this?", "Can you help me?"), "Sure. What do you need help with?"),
        Lesson(9, "잠깐만", "⏳", "상대에게 기다려 달라고 할 때", "Wait a second.", "잠깐만.", "WAIT‿a ●SEC-ond ↘", "웨이러 ●세컨(드)↘", "Wait a가 빠르게 이어지면 웨이러처럼 들릴 수 있어요.", "잠깐만 기다려줘.", listOf("Wait a second.", "Give me a second."), "Okay, I'll wait."),
        Lesson(10, "다시 말해줘", "🔁", "못 알아들었을 때", "Say that again, please.", "다시 말해줘.", "SAY that‿a●GAIN | please ↘", "세이 대러●겐 | 플리즈↘", "that again은 실제 대화에서 서로 붙어 들릴 수 있어요.", "조금 천천히 다시 말해줘.", listOf("Say that again, please.", "Can you say that again slowly?"), "Of course. I'll say it again."),
        Lesson(11, "모르겠어", "🤔", "이해하지 못했을 때", "I don't know.", "나 모르겠어.", "I don't ●KNOW ↘", "아이 돈 ●노우↘", "know에 핵심 강세를 주고 앞부분은 가볍게 말해보세요.", "잘 모르겠어.", listOf("I don't know.", "I'm not sure."), "That's okay. Want to think about it?"),
        Lesson(12, "알겠어", "👌", "상대 말을 이해했을 때", "I got it.", "알겠어.", "I ●GOT‿it ↘", "아이 ●가릿↘", "미국식 회화에서는 got it의 t가 부드럽게 이어져 가릿처럼 들릴 수 있어요.", "응, 이제 알겠어.", listOf("I got it.", "Okay, I understand."), "Great. Does it make sense now?"),
        Lesson(13, "어디 가?", "🚶", "상대의 이동을 물을 때", "Where are you going?", "어디 가?", "●WHERE‿are‿you ●GO-ing ↘", "●웨어러유 ●고잉↘", "Where are you가 하나의 덩어리처럼 빠르게 연결됩니다.", "너 지금 어디 가?", listOf("Where are you going?", "Where are you going now?"), "I'm going outside. How about you?"),
        Lesson(14, "집에 갈 거야", "🏠", "계획을 말할 때", "I'm going home.", "나 집에 가.", "I'm ●GO-ing ●HOME ↘", "아임 ●고잉 ●홈↘", "going home을 끊지 않고 home까지 한 흐름으로 말해보세요.", "나 이제 집에 갈 거야.", listOf("I'm going home now.", "I'm going home."), "When are you going home?"),
        Lesson(15, "같이 하자", "🤝", "친구에게 제안할 때", "Let's do it together.", "같이 하자.", "Let's DO‿it to●GE-ther ↘", "렛츠 두윗 터●게더↘", "do it은 두윗처럼 연결하고 together의 가운데에 힘을 주세요.", "우리 같이 먹자.", listOf("Let's eat together.", "Let's have lunch together."), "Sure. What should we do together?"),
        Lesson(16, "내 차례야", "🎲", "순서를 말할 때", "It's my turn.", "내 차례야.", "It's my ●TURN ↘", "잇츠 마이 ●턴↘", "It's my는 가볍게 이어가고 turn에 핵심 강세를 주세요.", "이제 내 차례야.", listOf("It's my turn now.", "It's my turn."), "Okay, your turn!"),
        Lesson(17, "괜찮아", "🌿", "상대를 안심시킬 때", "It's okay.", "괜찮아.", "It's o●KAY ↘", "이츠 오●케이↘", "okay의 뒤 음절에 힘을 주고 안심시키는 느낌으로 부드럽게 내려보세요.", "괜찮아, 걱정하지 마.", listOf("It's okay. Don't worry.", "Don't worry. It's okay."), "Thanks. Are you sure?"),
        Lesson(18, "미안해", "🙏", "실수했을 때", "I'm sorry.", "미안해.", "I'm ●SOR-ry ↘", "아임 ●쏘리↘", "sorry의 첫 음절에 힘을 주고 전체는 부드럽게 말해보세요.", "늦어서 미안해.", listOf("I'm sorry I'm late.", "Sorry I'm late."), "It's okay. What happened?"),
        Lesson(19, "고마워", "💛", "감사할 때", "Thank you.", "고마워.", "●THANK‿you ↘", "●땡큐↘", "Thank you는 실제 회화에서 거의 한 덩어리처럼 이어집니다.", "도와줘서 고마워.", listOf("Thanks for helping me.", "Thank you for helping me."), "You're welcome. Happy to help."),
        Lesson(20, "재밌었어", "🎉", "경험을 말할 때", "That was fun.", "그거 재밌었어.", "That was ●FUN ↘", "댓 워즈 ●펀↘", "fun에 핵심 강세를 주면 감정이 더 자연스럽게 전달됩니다.", "오늘 정말 재밌었어.", listOf("Today was really fun.", "That was really fun."), "What was the most fun part?"),
        Lesson(21, "피곤해", "😴", "상태를 말할 때", "I'm tired.", "나 피곤해.", "I'm ●TIRED ↘", "아임 ●타이어드↘", "tired에 힘을 주고 I'm은 짧게 붙여 말해보세요.", "오늘 좀 피곤해.", listOf("I'm a little tired today.", "I'm tired today."), "Why are you tired?"),
        Lesson(22, "뭐 하고 있어?", "👀", "상대가 하는 일을 물을 때", "What are you doing?", "뭐 하고 있어?", "●WHAT‿are‿you ●DO-ing ↘", "●워러유 ●두잉↘", "What are you가 워러유처럼 빠르게 이어져 들릴 수 있어요.", "지금 뭐 하고 있어?", listOf("What are you doing now?", "What are you doing?"), "I'm just relaxing. What are you doing?"),
        Lesson(23, "나 준비됐어", "✅", "시작할 준비가 됐을 때", "I'm ready.", "나 준비됐어.", "I'm ●READ-y ↘", "아임 ●레디↘", "ready의 첫 음절을 분명하게 주고 끝은 자연스럽게 내려보세요.", "응, 이제 준비됐어.", listOf("I'm ready now.", "Yes, I'm ready."), "Great. Shall we start?"),
        Lesson(24, "또 보자", "👋", "헤어질 때", "See you later.", "나중에 봐.", "SEE‿you ●LA-ter ↘", "씨유 ●레이러↘", "See you는 붙여 말하고 미국식에서는 later의 t가 부드럽게 들릴 수 있어요.", "내일 또 보자.", listOf("See you tomorrow.", "I'll see you tomorrow."), "See you! Have a good day."),
        Lesson(25, "우리 엄마야", "👩", "가족을 소개할 때", "This is my mom.", "우리 엄마야.", "THIS is my ●MOM ↘", "디스 이즈 마이 ●맘↘", "my mom을 한 덩어리로 이어보세요.", "우리 엄마야.", listOf("This is my mom.", "She's my mom."), "Who's this?"),
        Lesson(26, "우리 아빠야", "👨", "가족을 소개할 때", "That's my dad.", "저분은 우리 아빠야.", "THAT'S my ●DAD ↘", "댓츠 마이 ●대드↘", "That's my를 가볍게 이어보세요.", "저분은 우리 아빠야.", listOf("That's my dad.", "He's my dad."), "Who's that?"),
        Lesson(27, "남동생이 있어", "👦", "가족 이야기를 할 때", "I have a brother.", "나는 남자 형제가 있어.", "I HAVE a ●BRO-ther ↘", "아이 해버 ●브러더↘", "have a가 자연스럽게 이어집니다.", "나는 누나가 있어.", listOf("I have a sister.", "I've got a sister."), "Do you have a brother or sister?"),
        Lesson(28, "내 여동생이야", "👧", "가족을 소개할 때", "She's my sister.", "그 애는 내 여자 형제야.", "SHE'S my ●SIS-ter ↘", "쉬즈 마이 ●시스터↘", "She's my를 한 흐름으로 말해보세요.", "그 애는 내 누나야.", listOf("She's my sister."), "How old is your sister?"),
        Lesson(29, "내 장난감 어디 있어?", "🧸", "물건을 찾을 때", "Where's my toy?", "내 장난감 어디 있어?", "WHERE'S my ●TOY ↘", "웨어즈 마이 ●토이↘", "Where's를 짧게 시작해보세요.", "내 가방 어디 있어?", listOf("Where's my bag?", "Where is my bag?"), "What are you looking for?"),
        Lesson(30, "저기 있어", "👉", "위치를 알려줄 때", "It's over there.", "저기 있어.", "It's O-ver ●THERE ↘", "잇츠 오버 ●데어↘", "there에 힘을 주세요.", "여기 있어.", listOf("It's right here.", "It's over there."), "Where is it?"),
        Lesson(31, "이건 내 거야", "🎒", "내 물건임을 말할 때", "This is mine.", "이건 내 거야.", "THIS is ●MINE ↘", "디스 이즈 ●마인↘", "mine에 핵심 강세를 주세요.", "그건 내 거야.", listOf("That's mine.", "This is mine."), "Whose is this?"),
        Lesson(32, "이거 네 거야?", "❓", "물건 주인을 물을 때", "Is this yours?", "이거 네 거야?", "Is this ●YOURS ↗", "이즈 디스 ●요얼즈↗", "yours를 올려서 물어보세요.", "이거 네 가방이야?", listOf("Is this your bag?", "Is this yours?"), "Is this yours?"),
        Lesson(33, "이게 뭐야?", "🔎", "모르는 물건을 물을 때", "What is this?", "이게 뭐야?", "WHAT is ●THIS ↘", "워디즈 ●디스↘", "What is가 빠르게 이어질 수 있어요.", "저게 뭐야?", listOf("What's that?", "What is that?"), "Do you know what this is?"),
        Lesson(34, "어느 거 원해?", "🎨", "여러 개 중 선택할 때", "Which one do you want?", "어느 거 원해?", "WHICH one do you ●WANT ↘", "위치 원 두유 ●원트↘", "which one을 붙여 시작해보세요.", "어느 색 원해?", listOf("Which color do you want?", "Which one do you want?"), "Which one do you want?"),
        Lesson(35, "빨간 거 원해", "🔴", "선택을 말할 때", "I want the red one.", "나는 빨간 거 원해.", "I WANT the ●RED one ↘", "아이 원트 더 ●레드원↘", "red one을 이어서 말해보세요.", "나는 파란 거 원해.", listOf("I want the blue one.", "I want this one."), "Which color do you want?"),
        Lesson(36, "연필이 필요해", "✏️", "필요한 것을 말할 때", "I need a pencil.", "연필이 필요해.", "I NEED a ●PEN-cil ↘", "아이 니더 ●펜슬↘", "need a가 이어집니다.", "물이 필요해.", listOf("I need some water.", "I need a pencil."), "What do you need?"),
        Lesson(37, "밖에 나가도 돼?", "🌳", "허락을 구할 때", "Can I go outside?", "밖에 나가도 돼?", "Can‿I go out●SIDE ↗", "커나이 고 아웃●사이드↗", "Can I를 가볍게 이어보세요.", "화장실 가도 돼?", listOf("Can I go to the bathroom?", "Can I go outside?"), "Where do you want to go?"),
        Lesson(38, "같이 놀아도 돼?", "⚽", "놀이에 끼고 싶을 때", "Can I play with you?", "나도 같이 놀아도 돼?", "Can‿I PLAY with ●YOU ↗", "커나이 플레이 윗 ●유↗", "play with you를 한 흐름으로 말해보세요.", "나도 같이 해도 돼?", listOf("Can I join you?", "Can I play with you?"), "Sure! What do you want to play?"),
        Lesson(39, "내가 할 수 있어", "💪", "자신감을 말할 때", "I can do it.", "내가 할 수 있어.", "I can ●DO‿it ↘", "아이 컨 ●두윗↘", "do it이 두윗처럼 이어집니다.", "나 혼자 할 수 있어.", listOf("I can do it myself.", "I can do it."), "Can you do it?"),
        Lesson(40, "손이 안 닿아", "🙌", "할 수 없는 것을 말할 때", "I can't reach it.", "손이 안 닿아.", "I can't ●REACH‿it ↘", "아이 캔트 ●리칫↘", "reach it을 이어보세요.", "이거 못 열겠어.", listOf("I can't open it.", "I can't reach it."), "Do you need help?"),
        Lesson(41, "여기로 와", "👋", "상대를 부를 때", "Come here, please.", "여기로 와줘.", "COME ●HERE | please ↘", "컴 ●히어 | 플리즈↘", "come here를 자연스럽게 붙여보세요.", "잠깐 여기로 와.", listOf("Come here for a second.", "Come here, please."), "What is it?"),
        Lesson(42, "이거 봐!", "✨", "무언가를 보여줄 때", "Look at this!", "이거 봐!", "LOOK‿at ●THIS ↘", "루커트 ●디스↘", "look at이 루커트처럼 이어질 수 있어요.", "저거 봐!", listOf("Look at that!", "Look at this!"), "Wow! What is it?"),
        Lesson(43, "조심해", "⚠️", "위험을 알려줄 때", "Be careful.", "조심해.", "Be ●CARE-ful ↘", "비 ●케어풀↘", "care에 힘을 주세요.", "거기 조심해.", listOf("Be careful there.", "Be careful."), "Okay. I will."),
        Lesson(44, "무서워", "😨", "두려움을 말할 때", "I'm scared.", "나 무서워.", "I'm ●SCARED ↘", "아임 ●스케어드↘", "scared에 힘을 주세요.", "나 조금 무서워.", listOf("I'm a little scared.", "I'm scared."), "What are you scared of?"),
        Lesson(45, "기분 좋아", "😊", "기분을 말할 때", "I'm happy.", "나 행복해.", "I'm ●HAP-py ↘", "아임 ●해피↘", "happy의 첫 음절에 힘을 주세요.", "오늘 기분 좋아.", listOf("I'm happy today.", "I feel happy."), "Why are you happy?"),
        Lesson(46, "속상해", "😢", "슬픈 기분을 말할 때", "I'm sad.", "나 속상해.", "I'm ●SAD ↘", "아임 ●새드↘", "sad를 짧고 분명하게 말해보세요.", "조금 속상해.", listOf("I'm a little sad.", "I'm sad."), "What happened?"),
        Lesson(47, "추워", "🥶", "추위를 말할 때", "I'm cold.", "나 추워.", "I'm ●COLD ↘", "아임 ●코울드↘", "cold에 힘을 주세요.", "밖이 너무 추워.", listOf("It's really cold outside.", "I'm cold."), "Do you want your jacket?"),
        Lesson(48, "더워", "🥵", "더위를 말할 때", "I'm hot.", "나 더워.", "I'm ●HOT ↘", "아임 ●핫↘", "hot을 짧게 강하게 말해보세요.", "여기 너무 더워.", listOf("It's too hot in here.", "I'm hot."), "Do you want some water?"),
        Lesson(49, "몸이 안 좋아", "🤒", "아플 때", "I don't feel good.", "몸이 안 좋아.", "I don't feel ●GOOD ↘", "아이 돈 필 ●굿↘", "feel good을 한 덩어리로 말해보세요.", "배가 아파.", listOf("My stomach hurts.", "I don't feel good."), "What's wrong?"),
        Lesson(50, "비가 와", "🌧️", "날씨를 말할 때", "It's raining.", "비가 와.", "It's ●RAIN-ing ↘", "잇츠 ●레이닝↘", "raining의 첫 음절에 힘을 주세요.", "밖에 비가 와.", listOf("It's raining outside.", "It's raining."), "Is it raining outside?")
    )

    val lessons: List<Lesson> =
        baseLessons +
            LessonExpansionCatalog.lessons +
            Age810LessonCatalog.lessons +
            Age1113LessonCatalog.lessons +
            Age1416LessonCatalog.lessons +
            Age1416LessonExpansionCatalog.lessons +
            Age1720LessonCatalog.lessons

    fun byId(id: Int): Lesson? = lessons.firstOrNull { it.id == id }
}
