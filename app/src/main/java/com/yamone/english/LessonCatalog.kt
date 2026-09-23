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
    val coachLine: String
)

object LessonCatalog {
    val lessons = listOf(
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
        Lesson(19, "고마워", "💛", "감사할 때", "Thank‿you ↘", "고마워.", "●THANK‿you ↘", "●땡큐↘", "Thank you는 실제 회화에서 거의 한 덩어리처럼 이어집니다.", "도와줘서 고마워.", listOf("Thanks for helping me.", "Thank you for helping me."), "You're welcome. Happy to help."),
        Lesson(20, "재밌었어", "🎉", "경험을 말할 때", "That was fun.", "그거 재밌었어.", "That was ●FUN ↘", "댓 워즈 ●펀↘", "fun에 핵심 강세를 주면 감정이 더 자연스럽게 전달됩니다.", "오늘 정말 재밌었어.", listOf("Today was really fun.", "That was really fun."), "What was the most fun part?"),
        Lesson(21, "피곤해", "😴", "상태를 말할 때", "I'm tired.", "나 피곤해.", "I'm ●TIRED ↘", "아임 ●타이어드↘", "tired에 힘을 주고 I'm은 짧게 붙여 말해보세요.", "오늘 좀 피곤해.", listOf("I'm a little tired today.", "I'm tired today."), "Why are you tired?"),
        Lesson(22, "뭐 하고 있어?", "👀", "상대가 하는 일을 물을 때", "What are you doing?", "뭐 하고 있어?", "●WHAT‿are‿you ●DO-ing ↘", "●워러유 ●두잉↘", "What are you가 워러유처럼 빠르게 이어져 들릴 수 있어요.", "지금 뭐 하고 있어?", listOf("What are you doing now?", "What are you doing?"), "I'm just relaxing. What are you doing?"),
        Lesson(23, "나 준비됐어", "✅", "시작할 준비가 됐을 때", "I'm ready.", "나 준비됐어.", "I'm ●READ-y ↘", "아임 ●레디↘", "ready의 첫 음절을 분명하게 주고 끝은 자연스럽게 내려보세요.", "응, 이제 준비됐어.", listOf("I'm ready now.", "Yes, I'm ready."), "Great. Shall we start?"),
        Lesson(24, "또 보자", "👋", "헤어질 때", "See you later.", "나중에 봐.", "SEE‿you ●LA-ter ↘", "씨유 ●레이러↘", "See you는 붙여 말하고 미국식에서는 later의 t가 부드럽게 들릴 수 있어요.", "내일 또 보자.", listOf("See you tomorrow.", "I'll see you tomorrow."), "See you! Have a good day.")
    )

    fun byId(id: Int): Lesson? = lessons.firstOrNull { it.id == id }
}
