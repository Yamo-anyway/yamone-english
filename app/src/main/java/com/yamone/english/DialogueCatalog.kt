package com.yamone.english

data class DialogueLine(
    val speaker: String,
    val english: String,
    val korean: String
)

data class LessonDialogue(
    val lessonId: Int,
    val lines: List<DialogueLine>
)

object DialogueCatalog {
    val dialogues = listOf(
        LessonDialogue(1, listOf(
            DialogueLine("A", "Hi! What's your name?", "안녕! 이름이 뭐야?"),
            DialogueLine("B", "Hi, I'm Mina.", "안녕, 나는 미나야."),
            DialogueLine("A", "Nice to meet you.", "만나서 반가워."),
            DialogueLine("B", "Nice to meet you, too.", "나도 만나서 반가워.")
        )),
        LessonDialogue(2, listOf(
            DialogueLine("A", "Hi! How are you?", "안녕! 잘 지냈어?"),
            DialogueLine("B", "I'm good. How are you?", "나 잘 지내. 너는?"),
            DialogueLine("A", "I'm good, too.", "나도 잘 지내."),
            DialogueLine("B", "That's good.", "잘됐다.")
        )),
        LessonDialogue(3, listOf(
            DialogueLine("A", "Do you like this?", "이거 좋아해?"),
            DialogueLine("B", "Yes. I like this.", "응. 나 이거 좋아해."),
            DialogueLine("A", "Me, too.", "나도."),
            DialogueLine("B", "It's really nice.", "이거 정말 좋아.")
        )),
        LessonDialogue(4, listOf(
            DialogueLine("A", "Do you want this?", "이거 갖고 싶어?"),
            DialogueLine("B", "No. I don't like it.", "아니. 나 그거 안 좋아해."),
            DialogueLine("A", "What do you like?", "그럼 뭐 좋아해?"),
            DialogueLine("B", "I like this one.", "나는 이게 좋아.")
        )),
        LessonDialogue(5, listOf(
            DialogueLine("A", "Are you hungry?", "배고파?"),
            DialogueLine("B", "Yes. I'm hungry.", "응. 나 배고파."),
            DialogueLine("A", "Do you want a snack?", "간식 먹을래?"),
            DialogueLine("B", "Yes, please.", "응, 줘.")
        )),
        LessonDialogue(6, listOf(
            DialogueLine("A", "Are you thirsty?", "목말라?"),
            DialogueLine("B", "Yes. I'm thirsty.", "응. 나 목말라."),
            DialogueLine("A", "Do you want some water?", "물 좀 마실래?"),
            DialogueLine("B", "Yes, please.", "응, 줘.")
        )),
        LessonDialogue(7, listOf(
            DialogueLine("A", "What do you want?", "뭐 갖고 싶어?"),
            DialogueLine("B", "Can I have this?", "이거 가져도 돼?"),
            DialogueLine("A", "Sure.", "그럼."),
            DialogueLine("B", "Thank you.", "고마워.")
        )),
        LessonDialogue(8, listOf(
            DialogueLine("A", "What's wrong?", "무슨 일이야?"),
            DialogueLine("B", "Can you help me?", "나 좀 도와줄래?"),
            DialogueLine("A", "Sure. What do you need?", "그럼. 뭐가 필요해?"),
            DialogueLine("B", "Help me with this, please.", "이거 좀 도와줘.")
        )),
        LessonDialogue(9, listOf(
            DialogueLine("A", "Come on. Let's go.", "자, 가자."),
            DialogueLine("B", "Wait a second.", "잠깐만."),
            DialogueLine("A", "Are you ready?", "준비됐어?"),
            DialogueLine("B", "Okay. I'm ready.", "응. 준비됐어.")
        )),
        LessonDialogue(10, listOf(
            DialogueLine("A", "Let's meet after lunch.", "점심 먹고 만나자."),
            DialogueLine("B", "Say that again, please.", "다시 말해줘."),
            DialogueLine("A", "Let's meet after lunch.", "점심 먹고 만나자."),
            DialogueLine("B", "Okay. I got it.", "응. 알겠어.")
        )),
        LessonDialogue(11, listOf(
            DialogueLine("A", "Where is my bag?", "내 가방 어디 있어?"),
            DialogueLine("B", "I don't know.", "나 모르겠어."),
            DialogueLine("A", "Is it over there?", "저쪽에 있나?"),
            DialogueLine("B", "Maybe.", "그럴지도 몰라.")
        )),
        LessonDialogue(12, listOf(
            DialogueLine("A", "Press this button first.", "먼저 이 버튼을 눌러."),
            DialogueLine("B", "Okay. I got it.", "응. 알겠어."),
            DialogueLine("A", "Then press this one.", "그다음 이걸 눌러."),
            DialogueLine("B", "Got it.", "알겠어.")
        )),
        LessonDialogue(13, listOf(
            DialogueLine("A", "Where are you going?", "어디 가?"),
            DialogueLine("B", "I'm going outside.", "나 밖에 나가."),
            DialogueLine("A", "Can I come with you?", "나도 같이 가도 돼?"),
            DialogueLine("B", "Sure.", "그럼.")
        )),
        LessonDialogue(14, listOf(
            DialogueLine("A", "What are you doing now?", "지금 뭐 할 거야?"),
            DialogueLine("B", "I'm going home.", "나 집에 갈 거야."),
            DialogueLine("A", "Okay. See you tomorrow.", "그래. 내일 보자."),
            DialogueLine("B", "See you.", "또 봐.")
        )),
        LessonDialogue(15, listOf(
            DialogueLine("A", "Do you want to play?", "같이 놀래?"),
            DialogueLine("B", "Yes. Let's do it together.", "응. 같이 하자."),
            DialogueLine("A", "Okay. You go first.", "좋아. 네가 먼저 해."),
            DialogueLine("B", "Sounds good.", "좋아.")
        )),
        LessonDialogue(16, listOf(
            DialogueLine("A", "Whose turn is it?", "누구 차례야?"),
            DialogueLine("B", "It's my turn.", "내 차례야."),
            DialogueLine("A", "Okay. Go ahead.", "그래. 해."),
            DialogueLine("B", "Here I go.", "나 한다.")
        )),
        LessonDialogue(17, listOf(
            DialogueLine("A", "I'm sorry.", "미안해."),
            DialogueLine("B", "It's okay.", "괜찮아."),
            DialogueLine("A", "Are you sure?", "정말 괜찮아?"),
            DialogueLine("B", "Yes. Don't worry.", "응. 걱정하지 마.")
        )),
        LessonDialogue(18, listOf(
            DialogueLine("A", "You're late.", "너 늦었어."),
            DialogueLine("B", "I'm sorry.", "미안해."),
            DialogueLine("A", "What happened?", "무슨 일이 있었어?"),
            DialogueLine("B", "I missed the bus.", "버스를 놓쳤어.")
        )),
        LessonDialogue(19, listOf(
            DialogueLine("A", "Here you go.", "자, 여기 있어."),
            DialogueLine("B", "Thank you.", "고마워."),
            DialogueLine("A", "You're welcome.", "천만에."),
            DialogueLine("B", "That helped a lot.", "정말 도움이 됐어.")
        )),
        LessonDialogue(20, listOf(
            DialogueLine("A", "Did you have fun?", "재밌었어?"),
            DialogueLine("B", "Yes. That was fun.", "응. 재밌었어."),
            DialogueLine("A", "What did you like?", "뭐가 좋았어?"),
            DialogueLine("B", "I liked the game.", "게임이 좋았어.")
        )),
        LessonDialogue(21, listOf(
            DialogueLine("A", "You look tired.", "너 피곤해 보여."),
            DialogueLine("B", "Yeah. I'm tired.", "응. 나 피곤해."),
            DialogueLine("A", "Do you want to rest?", "좀 쉴래?"),
            DialogueLine("B", "Yes. Just for a minute.", "응. 잠깐만.")
        )),
        LessonDialogue(22, listOf(
            DialogueLine("A", "What are you doing?", "뭐 하고 있어?"),
            DialogueLine("B", "I'm drawing.", "나 그림 그리고 있어."),
            DialogueLine("A", "What are you drawing?", "뭐 그리고 있어?"),
            DialogueLine("B", "A big dog.", "큰 개를 그리고 있어.")
        )),
        LessonDialogue(23, listOf(
            DialogueLine("A", "Are you ready?", "준비됐어?"),
            DialogueLine("B", "Yes. I'm ready.", "응. 준비됐어."),
            DialogueLine("A", "Okay. Let's start.", "좋아. 시작하자."),
            DialogueLine("B", "Let's go!", "가자!")
        )),
        LessonDialogue(24, listOf(
            DialogueLine("A", "I have to go now.", "나 이제 가야 해."),
            DialogueLine("B", "Okay. See you later.", "그래. 나중에 봐."),
            DialogueLine("A", "See you tomorrow.", "내일 보자."),
            DialogueLine("B", "Bye! Have a good day.", "안녕! 좋은 하루 보내.")
        ))
    )

    fun byLessonId(id: Int): LessonDialogue? = dialogues.firstOrNull { it.lessonId == id }
}
