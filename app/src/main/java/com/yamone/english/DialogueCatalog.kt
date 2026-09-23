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
    private val baseDialogues = listOf(
        LessonDialogue(1, listOf(
            DialogueLine("A", "Hi! What's your name?", "안녕! 이름이 뭐야?"),
            DialogueLine("B", "Hi, I'm Mina.", "안녕, 나는 미나야."),
            DialogueLine("A", "I'm Joon.", "나는 준이야."),
            DialogueLine("B", "Nice to meet you.", "만나서 반가워."),
            DialogueLine("A", "Nice to meet you, too.", "나도 만나서 반가워."),
            DialogueLine("B", "Let's play together.", "같이 놀자.")
        )),
        LessonDialogue(2, listOf(
            DialogueLine("A", "Hi! How are you?", "안녕! 잘 지냈어?"),
            DialogueLine("B", "I'm good. How are you?", "나 잘 지내. 너는?"),
            DialogueLine("A", "I'm good, too.", "나도 잘 지내."),
            DialogueLine("B", "What are you doing?", "뭐 하고 있어?"),
            DialogueLine("A", "I'm just playing.", "그냥 놀고 있어."),
            DialogueLine("B", "Can I play, too?", "나도 같이 놀아도 돼?")
        )),
        LessonDialogue(3, listOf(
            DialogueLine("A", "Do you like this?", "이거 좋아해?"),
            DialogueLine("B", "Yes. I like this.", "응. 나 이거 좋아해."),
            DialogueLine("A", "Why do you like it?", "왜 좋아해?"),
            DialogueLine("B", "It's funny.", "재밌어서."),
            DialogueLine("A", "I like it, too.", "나도 좋아해."),
            DialogueLine("B", "Let's use it together.", "같이 쓰자.")
        )),
        LessonDialogue(4, listOf(
            DialogueLine("A", "Do you want this?", "이거 갖고 싶어?"),
            DialogueLine("B", "No. I don't like it.", "아니. 나 그거 안 좋아해."),
            DialogueLine("A", "Why not?", "왜 싫어?"),
            DialogueLine("B", "It's too spicy.", "너무 매워."),
            DialogueLine("A", "What do you like?", "그럼 뭐 좋아해?"),
            DialogueLine("B", "I like this one.", "나는 이게 좋아.")
        )),
        LessonDialogue(5, listOf(
            DialogueLine("A", "Are you hungry?", "배고파?"),
            DialogueLine("B", "Yes. I'm hungry.", "응. 나 배고파."),
            DialogueLine("A", "Do you want a snack?", "간식 먹을래?"),
            DialogueLine("B", "Yes, please.", "응, 줘."),
            DialogueLine("A", "Do you want an apple?", "사과 먹을래?"),
            DialogueLine("B", "Yes. I like apples.", "응. 나 사과 좋아해.")
        )),
        LessonDialogue(6, listOf(
            DialogueLine("A", "Are you thirsty?", "목말라?"),
            DialogueLine("B", "Yes. I'm thirsty.", "응. 나 목말라."),
            DialogueLine("A", "Do you want some water?", "물 좀 마실래?"),
            DialogueLine("B", "Yes, please.", "응, 줘."),
            DialogueLine("A", "Here you go.", "자, 여기 있어."),
            DialogueLine("B", "Thank you.", "고마워.")
        )),
        LessonDialogue(7, listOf(
            DialogueLine("A", "What do you want?", "뭐 갖고 싶어?"),
            DialogueLine("B", "Can I have this?", "이거 가져도 돼?"),
            DialogueLine("A", "Which one?", "어느 거?"),
            DialogueLine("B", "The blue one.", "파란 거."),
            DialogueLine("A", "Sure. Here you go.", "그럼. 여기 있어."),
            DialogueLine("B", "Thank you.", "고마워.")
        )),
        LessonDialogue(8, listOf(
            DialogueLine("A", "What's wrong?", "무슨 일이야?"),
            DialogueLine("B", "Can you help me?", "나 좀 도와줄래?"),
            DialogueLine("A", "Sure. What do you need?", "그럼. 뭐가 필요해?"),
            DialogueLine("B", "I can't open this.", "이걸 못 열겠어."),
            DialogueLine("A", "Let me try.", "내가 해볼게."),
            DialogueLine("B", "Thanks!", "고마워!")
        )),
        LessonDialogue(9, listOf(
            DialogueLine("A", "Come on. Let's go.", "자, 가자."),
            DialogueLine("B", "Wait a second.", "잠깐만."),
            DialogueLine("A", "What are you doing?", "뭐 하고 있어?"),
            DialogueLine("B", "I'm putting on my shoes.", "신발 신고 있어."),
            DialogueLine("A", "Are you ready now?", "이제 준비됐어?"),
            DialogueLine("B", "Okay. I'm ready.", "응. 준비됐어.")
        )),
        LessonDialogue(10, listOf(
            DialogueLine("A", "Let's meet after lunch.", "점심 먹고 만나자."),
            DialogueLine("B", "Say that again, please.", "다시 말해줘."),
            DialogueLine("A", "Let's meet after lunch.", "점심 먹고 만나자."),
            DialogueLine("B", "After lunch?", "점심 먹고?"),
            DialogueLine("A", "Yes. Right here.", "응. 바로 여기서."),
            DialogueLine("B", "Okay. I got it.", "응. 알겠어.")
        )),
        LessonDialogue(11, listOf(
            DialogueLine("A", "Where is my bag?", "내 가방 어디 있어?"),
            DialogueLine("B", "I don't know.", "나 모르겠어."),
            DialogueLine("A", "Did you see it?", "내 가방 봤어?"),
            DialogueLine("B", "No, I didn't.", "아니, 못 봤어."),
            DialogueLine("A", "Is it over there?", "저쪽에 있나?"),
            DialogueLine("B", "Maybe. Let's look.", "그럴지도 몰라. 찾아보자.")
        )),
        LessonDialogue(12, listOf(
            DialogueLine("A", "Press this button first.", "먼저 이 버튼을 눌러."),
            DialogueLine("B", "Okay. I got it.", "응. 알겠어."),
            DialogueLine("A", "Then press this one.", "그다음 이걸 눌러."),
            DialogueLine("B", "This one?", "이거?"),
            DialogueLine("A", "Yes. That's right.", "응. 맞아."),
            DialogueLine("B", "Got it.", "알겠어.")
        )),
        LessonDialogue(13, listOf(
            DialogueLine("A", "Where are you going?", "어디 가?"),
            DialogueLine("B", "I'm going outside.", "나 밖에 나가."),
            DialogueLine("A", "Why?", "왜?"),
            DialogueLine("B", "I want to play.", "놀고 싶어."),
            DialogueLine("A", "Can I come with you?", "나도 같이 가도 돼?"),
            DialogueLine("B", "Sure. Let's go.", "그럼. 같이 가자.")
        )),
        LessonDialogue(14, listOf(
            DialogueLine("A", "Where are you going?", "어디 가?"),
            DialogueLine("B", "I'm going home.", "나 집에 갈 거야."),
            DialogueLine("A", "Already?", "벌써?"),
            DialogueLine("B", "Yeah. I'm tired.", "응. 나 피곤해."),
            DialogueLine("A", "Okay. See you tomorrow.", "그래. 내일 보자."),
            DialogueLine("B", "See you.", "또 봐.")
        )),
        LessonDialogue(15, listOf(
            DialogueLine("A", "Do you want to play?", "같이 놀래?"),
            DialogueLine("B", "Yes. Let's do it together.", "응. 같이 하자."),
            DialogueLine("A", "What should we play?", "뭐 하고 놀까?"),
            DialogueLine("B", "Let's build this.", "이거 만들자."),
            DialogueLine("A", "Okay. You start.", "좋아. 네가 먼저 해."),
            DialogueLine("B", "Sounds good.", "좋아.")
        )),
        LessonDialogue(16, listOf(
            DialogueLine("A", "Whose turn is it?", "누구 차례야?"),
            DialogueLine("B", "It's my turn.", "내 차례야."),
            DialogueLine("A", "Okay. Go ahead.", "그래. 해."),
            DialogueLine("B", "Here I go.", "나 한다."),
            DialogueLine("A", "Good job!", "잘했어!"),
            DialogueLine("B", "Now it's your turn.", "이제 네 차례야.")
        )),
        LessonDialogue(17, listOf(
            DialogueLine("A", "Oops. I'm sorry.", "앗. 미안해."),
            DialogueLine("B", "It's okay.", "괜찮아."),
            DialogueLine("A", "Are you sure?", "정말 괜찮아?"),
            DialogueLine("B", "Yes. Don't worry.", "응. 걱정하지 마."),
            DialogueLine("A", "Thanks.", "고마워."),
            DialogueLine("B", "No problem.", "괜찮아.")
        )),
        LessonDialogue(18, listOf(
            DialogueLine("A", "You're late.", "너 늦었어."),
            DialogueLine("B", "I'm sorry.", "미안해."),
            DialogueLine("A", "What happened?", "무슨 일이 있었어?"),
            DialogueLine("B", "I missed the bus.", "버스를 놓쳤어."),
            DialogueLine("A", "Are you okay?", "괜찮아?"),
            DialogueLine("B", "Yeah. I'm okay now.", "응. 이제 괜찮아.")
        )),
        LessonDialogue(19, listOf(
            DialogueLine("A", "Here you go.", "자, 여기 있어."),
            DialogueLine("B", "Thank you.", "고마워."),
            DialogueLine("A", "You're welcome.", "천만에."),
            DialogueLine("B", "That really helped.", "정말 도움이 됐어."),
            DialogueLine("A", "Do you need anything else?", "또 필요한 거 있어?"),
            DialogueLine("B", "No, I'm good.", "아니, 괜찮아.")
        )),
        LessonDialogue(20, listOf(
            DialogueLine("A", "Did you have fun?", "재밌었어?"),
            DialogueLine("B", "Yes. That was fun.", "응. 재밌었어."),
            DialogueLine("A", "What did you like?", "뭐가 좋았어?"),
            DialogueLine("B", "I liked the game.", "게임이 좋았어."),
            DialogueLine("A", "Do you want to play again?", "또 하고 싶어?"),
            DialogueLine("B", "Yes! Let's do it again.", "응! 또 하자.")
        )),
        LessonDialogue(21, listOf(
            DialogueLine("A", "You look tired.", "너 피곤해 보여."),
            DialogueLine("B", "Yeah. I'm tired.", "응. 나 피곤해."),
            DialogueLine("A", "Did you sleep well?", "잘 잤어?"),
            DialogueLine("B", "Not really.", "별로 못 잤어."),
            DialogueLine("A", "Do you want to rest?", "좀 쉴래?"),
            DialogueLine("B", "Yes. Just for a minute.", "응. 잠깐만.")
        )),
        LessonDialogue(22, listOf(
            DialogueLine("A", "What are you doing?", "뭐 하고 있어?"),
            DialogueLine("B", "I'm drawing.", "나 그림 그리고 있어."),
            DialogueLine("A", "What are you drawing?", "뭐 그리고 있어?"),
            DialogueLine("B", "A big dog.", "큰 개를 그리고 있어."),
            DialogueLine("A", "Can I see it?", "나도 봐도 돼?"),
            DialogueLine("B", "Sure. Look!", "그럼. 봐!")
        )),
        LessonDialogue(23, listOf(
            DialogueLine("A", "Are you ready?", "준비됐어?"),
            DialogueLine("B", "Yes. I'm ready.", "응. 준비됐어."),
            DialogueLine("A", "Do you have everything?", "다 챙겼어?"),
            DialogueLine("B", "Yes, I do.", "응, 다 챙겼어."),
            DialogueLine("A", "Okay. Let's start.", "좋아. 시작하자."),
            DialogueLine("B", "Let's go!", "가자!")
        )),
        LessonDialogue(24, listOf(
            DialogueLine("A", "I have to go now.", "나 이제 가야 해."),
            DialogueLine("B", "Okay. See you later.", "그래. 나중에 봐."),
            DialogueLine("A", "See you tomorrow.", "내일 보자."),
            DialogueLine("B", "Okay. Same time?", "그래. 같은 시간에?"),
            DialogueLine("A", "Yes. See you then.", "응. 그때 보자."),
            DialogueLine("B", "Bye! Have a good day.", "안녕! 좋은 하루 보내.")
        )),
        LessonDialogue(25, listOf(
            DialogueLine("A", "Who's this?", "이분은 누구야?"),
            DialogueLine("B", "This is my mom.", "우리 엄마야."),
            DialogueLine("A", "Hi!", "안녕하세요!"),
            DialogueLine("B", "Mom, this is Joon.", "엄마, 얘는 준이야."),
            DialogueLine("A", "Nice to meet you.", "만나서 반가워요."),
            DialogueLine("B", "Nice to meet you, too.", "나도 만나서 반가워.")
        )),
        LessonDialogue(26, listOf(
            DialogueLine("A", "Who's that?", "저분은 누구야?"),
            DialogueLine("B", "That's my dad.", "우리 아빠야."),
            DialogueLine("A", "What's he doing?", "뭐 하고 계셔?"),
            DialogueLine("B", "He's making dinner.", "저녁 만들고 계셔."),
            DialogueLine("A", "That smells good.", "맛있는 냄새 난다."),
            DialogueLine("B", "Yeah. He's a good cook.", "응. 요리 잘하셔.")
        )),
        LessonDialogue(27, listOf(
            DialogueLine("A", "Do you have a brother?", "남자 형제 있어?"),
            DialogueLine("B", "Yes. I have a brother.", "응. 남자 형제가 있어."),
            DialogueLine("A", "Is he older?", "형이야?"),
            DialogueLine("B", "No. He's younger.", "아니. 동생이야."),
            DialogueLine("A", "Do you play together?", "같이 놀아?"),
            DialogueLine("B", "Yes. Every day.", "응. 매일.")
        )),
        LessonDialogue(28, listOf(
            DialogueLine("A", "Who's she?", "저 애는 누구야?"),
            DialogueLine("B", "She's my sister.", "내 여자 형제야."),
            DialogueLine("A", "What's her name?", "이름이 뭐야?"),
            DialogueLine("B", "Her name is Mina.", "미나야."),
            DialogueLine("A", "How old is she?", "몇 살이야?"),
            DialogueLine("B", "She's six.", "여섯 살이야.")
        )),
        LessonDialogue(29, listOf(
            DialogueLine("A", "What are you looking for?", "뭐 찾고 있어?"),
            DialogueLine("B", "Where's my toy?", "내 장난감 어디 있어?"),
            DialogueLine("A", "Which toy?", "어떤 장난감?"),
            DialogueLine("B", "My little car.", "내 작은 자동차."),
            DialogueLine("A", "Is it under the chair?", "의자 밑에 있나?"),
            DialogueLine("B", "Oh, there it is!", "아, 저기 있다!")
        )),
        LessonDialogue(30, listOf(
            DialogueLine("A", "Where's my book?", "내 책 어디 있어?"),
            DialogueLine("B", "It's over there.", "저기 있어."),
            DialogueLine("A", "On the table?", "탁자 위에?"),
            DialogueLine("B", "Yes, right there.", "응, 바로 저기."),
            DialogueLine("A", "I see it.", "보인다."),
            DialogueLine("B", "Go get it.", "가서 가져와.")
        )),
        LessonDialogue(31, listOf(
            DialogueLine("A", "Whose bag is this?", "이 가방 누구 거야?"),
            DialogueLine("B", "This is mine.", "이건 내 거야."),
            DialogueLine("A", "Are you sure?", "확실해?"),
            DialogueLine("B", "Yes. My name is on it.", "응. 내 이름이 써 있어."),
            DialogueLine("A", "Oh, okay.", "아, 그래."),
            DialogueLine("B", "Thanks.", "고마워.")
        )),
        LessonDialogue(32, listOf(
            DialogueLine("A", "Is this yours?", "이거 네 거야?"),
            DialogueLine("B", "No, it's not.", "아니, 내 거 아니야."),
            DialogueLine("A", "Whose is it?", "그럼 누구 거지?"),
            DialogueLine("B", "Maybe it's Mina's.", "미나 거일지도 몰라."),
            DialogueLine("A", "Let's ask her.", "물어보자."),
            DialogueLine("B", "Okay.", "좋아.")
        )),
        LessonDialogue(33, listOf(
            DialogueLine("A", "What is this?", "이게 뭐야?"),
            DialogueLine("B", "It's a puzzle.", "퍼즐이야."),
            DialogueLine("A", "How do you play?", "어떻게 해?"),
            DialogueLine("B", "You put these together.", "이걸 서로 맞추는 거야."),
            DialogueLine("A", "Can I try?", "나도 해봐도 돼?"),
            DialogueLine("B", "Sure.", "그럼.")
        )),
        LessonDialogue(34, listOf(
            DialogueLine("A", "Which one do you want?", "어느 거 원해?"),
            DialogueLine("B", "I don't know.", "잘 모르겠어."),
            DialogueLine("A", "The red one or the blue one?", "빨간 거 아니면 파란 거?"),
            DialogueLine("B", "The blue one.", "파란 거."),
            DialogueLine("A", "This one?", "이거?"),
            DialogueLine("B", "Yes, that one.", "응, 그거.")
        )),
        LessonDialogue(35, listOf(
            DialogueLine("A", "Which color do you want?", "어느 색 원해?"),
            DialogueLine("B", "I want the red one.", "빨간 거 원해."),
            DialogueLine("A", "This one?", "이거?"),
            DialogueLine("B", "Yes, please.", "응, 그거 줘."),
            DialogueLine("A", "Here you go.", "여기 있어."),
            DialogueLine("B", "Thanks!", "고마워!")
        )),
        LessonDialogue(36, listOf(
            DialogueLine("A", "What do you need?", "뭐가 필요해?"),
            DialogueLine("B", "I need a pencil.", "연필이 필요해."),
            DialogueLine("A", "Do you need paper, too?", "종이도 필요해?"),
            DialogueLine("B", "Yes, please.", "응, 줘."),
            DialogueLine("A", "Here you go.", "여기 있어."),
            DialogueLine("B", "Thank you.", "고마워.")
        )),
        LessonDialogue(37, listOf(
            DialogueLine("A", "What do you want to do?", "뭐 하고 싶어?"),
            DialogueLine("B", "Can I go outside?", "밖에 나가도 돼?"),
            DialogueLine("A", "Did you finish your snack?", "간식 다 먹었어?"),
            DialogueLine("B", "Yes, I did.", "응, 다 먹었어."),
            DialogueLine("A", "Okay. Put on your shoes.", "좋아. 신발 신어."),
            DialogueLine("B", "Okay!", "응!")
        )),
        LessonDialogue(38, listOf(
            DialogueLine("A", "We're playing tag.", "우리 술래잡기하고 있어."),
            DialogueLine("B", "Can I play with you?", "나도 같이 놀아도 돼?"),
            DialogueLine("A", "Sure!", "그럼!"),
            DialogueLine("B", "Who's it?", "누가 술래야?"),
            DialogueLine("A", "I am.", "나야."),
            DialogueLine("B", "Okay. Catch me!", "좋아. 나 잡아봐!")
        )),
        LessonDialogue(39, listOf(
            DialogueLine("A", "Do you need help?", "도와줄까?"),
            DialogueLine("B", "No. I can do it.", "아니. 내가 할 수 있어."),
            DialogueLine("A", "Are you sure?", "확실해?"),
            DialogueLine("B", "Yes. Watch me.", "응. 나 봐."),
            DialogueLine("A", "You did it!", "해냈네!"),
            DialogueLine("B", "I told you!", "내가 할 수 있다고 했잖아!")
        )),
        LessonDialogue(40, listOf(
            DialogueLine("A", "Can you get that box?", "저 상자 가져올 수 있어?"),
            DialogueLine("B", "I can't reach it.", "손이 안 닿아."),
            DialogueLine("A", "Do you want help?", "도와줄까?"),
            DialogueLine("B", "Yes, please.", "응, 부탁해."),
            DialogueLine("A", "Here you go.", "여기 있어."),
            DialogueLine("B", "Thanks.", "고마워.")
        )),
        LessonDialogue(41, listOf(
            DialogueLine("A", "Come here, please.", "여기로 와줘."),
            DialogueLine("B", "Why?", "왜?"),
            DialogueLine("A", "I want to show you something.", "보여줄 게 있어."),
            DialogueLine("B", "What is it?", "뭔데?"),
            DialogueLine("A", "Look at this.", "이거 봐."),
            DialogueLine("B", "Wow!", "와!")
        )),
        LessonDialogue(42, listOf(
            DialogueLine("A", "Look at this!", "이거 봐!"),
            DialogueLine("B", "What is it?", "뭔데?"),
            DialogueLine("A", "I made it.", "내가 만들었어."),
            DialogueLine("B", "You made that?", "네가 그거 만들었어?"),
            DialogueLine("A", "Yeah. Do you like it?", "응. 마음에 들어?"),
            DialogueLine("B", "I love it!", "정말 좋아!")
        )),
        LessonDialogue(43, listOf(
            DialogueLine("A", "I'm going up here.", "나 여기 올라갈 거야."),
            DialogueLine("B", "Be careful.", "조심해."),
            DialogueLine("A", "I will.", "그럴게."),
            DialogueLine("B", "Hold on tight.", "꽉 잡아."),
            DialogueLine("A", "Okay.", "응."),
            DialogueLine("B", "Good job.", "잘했어.")
        )),
        LessonDialogue(44, listOf(
            DialogueLine("A", "What's wrong?", "왜 그래?"),
            DialogueLine("B", "I'm scared.", "무서워."),
            DialogueLine("A", "Of what?", "뭐가?"),
            DialogueLine("B", "That big dog.", "저 큰 개가."),
            DialogueLine("A", "It's okay. I'm here.", "괜찮아. 내가 있잖아."),
            DialogueLine("B", "Okay.", "응.")
        )),
        LessonDialogue(45, listOf(
            DialogueLine("A", "You look happy.", "기분 좋아 보여."),
            DialogueLine("B", "I'm happy.", "나 기분 좋아."),
            DialogueLine("A", "Why?", "왜?"),
            DialogueLine("B", "It's my birthday.", "오늘 내 생일이야."),
            DialogueLine("A", "Happy birthday!", "생일 축하해!"),
            DialogueLine("B", "Thank you!", "고마워!")
        )),
        LessonDialogue(46, listOf(
            DialogueLine("A", "You look sad.", "속상해 보여."),
            DialogueLine("B", "I'm sad.", "나 속상해."),
            DialogueLine("A", "What happened?", "무슨 일이야?"),
            DialogueLine("B", "I lost my toy.", "장난감을 잃어버렸어."),
            DialogueLine("A", "Let's look for it.", "같이 찾아보자."),
            DialogueLine("B", "Okay.", "응.")
        )),
        LessonDialogue(47, listOf(
            DialogueLine("A", "Are you okay?", "괜찮아?"),
            DialogueLine("B", "I'm cold.", "나 추워."),
            DialogueLine("A", "Do you want your jacket?", "재킷 입을래?"),
            DialogueLine("B", "Yes, please.", "응."),
            DialogueLine("A", "Here it is.", "여기 있어."),
            DialogueLine("B", "Much better.", "훨씬 낫다.")
        )),
        LessonDialogue(48, listOf(
            DialogueLine("A", "Why is the window open?", "창문 왜 열려 있어?"),
            DialogueLine("B", "I'm hot.", "나 더워."),
            DialogueLine("A", "Do you want some water?", "물 마실래?"),
            DialogueLine("B", "Yes, please.", "응."),
            DialogueLine("A", "Here you go.", "여기 있어."),
            DialogueLine("B", "Thanks.", "고마워.")
        )),
        LessonDialogue(49, listOf(
            DialogueLine("A", "You don't look well.", "안 좋아 보여."),
            DialogueLine("B", "I don't feel good.", "몸이 안 좋아."),
            DialogueLine("A", "What hurts?", "어디가 아파?"),
            DialogueLine("B", "My stomach hurts.", "배가 아파."),
            DialogueLine("A", "Do you want to lie down?", "누울래?"),
            DialogueLine("B", "Yes, please.", "응.")
        )),
        LessonDialogue(50, listOf(
            DialogueLine("A", "Can we go outside?", "밖에 나가도 돼?"),
            DialogueLine("B", "No. It's raining.", "안 돼. 비가 와."),
            DialogueLine("A", "Is it raining hard?", "비 많이 와?"),
            DialogueLine("B", "A little.", "조금."),
            DialogueLine("A", "Let's play inside.", "안에서 놀자."),
            DialogueLine("B", "Okay!", "좋아!")
        ))
    )

    val dialogues: List<LessonDialogue> = baseDialogues + DialogueExpansionCatalog.dialogues

    fun byLessonId(id: Int): LessonDialogue? = dialogues.firstOrNull { it.lessonId == id }
}
