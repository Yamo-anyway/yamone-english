from pathlib import Path


def replace_once(path: Path, old: str, new: str, label: str) -> bool:
    text = path.read_text(encoding="utf-8")
    if new in text:
        print(f"{label}: already applied")
        return False
    if old not in text:
        raise SystemExit(f"{label}: expected source snippet not found")
    path.write_text(text.replace(old, new, 1), encoding="utf-8")
    print(f"{label}: applied")
    return True


main = Path("app/src/main/java/com/yamone/english/MainActivity.kt")
expansion = Path("app/src/main/java/com/yamone/english/Age1720ExpansionCatalog.kt")

replace_once(
    main,
    '    val age1416AssessmentStore = remember { Age1416AssessmentStore(context) }\n',
    '    val age1416AssessmentStore = remember { Age1416AssessmentStore(context) }\n'
    '    val age1720AssessmentStore = remember { Age1720AssessmentStore(context) }\n',
    "age17-20 assessment store",
)

replace_once(
    main,
    '    var latestAge1416Assessment by remember { mutableStateOf(age1416AssessmentStore.latest()) }\n',
    '    var latestAge1416Assessment by remember { mutableStateOf(age1416AssessmentStore.latest()) }\n'
    '    var latestAge1720Assessment by remember { mutableStateOf(age1720AssessmentStore.latest()) }\n',
    "age17-20 latest assessment state",
)

replace_once(
    main,
    '            CourseLevel.AGE_17_20 -> Unit\n',
    '''            CourseLevel.AGE_17_20 -> Age1720AssessmentScreen(
                isListening = isListening,
                speak = { tts.speak(it, speechRate) },
                listen = startListening,
                onReviewResult = reviewCallback,
                onFinish = { summary ->
                    age1720AssessmentStore.save(summary)
                    latestAge1720Assessment = age1720AssessmentStore.latest()
                    showAssessment = false
                },
                onBack = { showAssessment = false }
            )
''',
    "age17-20 assessment routing",
)

replace_once(
    main,
    '                latestAge1416Assessment = latestAge1416Assessment,\n',
    '                latestAge1416Assessment = latestAge1416Assessment,\n'
    '                latestAge1720Assessment = latestAge1720Assessment,\n',
    "TodayScreen age17-20 result argument",
)

replace_once(
    main,
    '    latestAge1416Assessment: Age1416AssessmentSummary?,\n',
    '    latestAge1416Assessment: Age1416AssessmentSummary?,\n'
    '    latestAge1720Assessment: Age1720AssessmentSummary?,\n',
    "TodayScreen age17-20 result parameter",
)

old_card = '''            CourseLevel.AGE_17_20 -> {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(18.dp)) {
                            Text("17~20세 과정", fontWeight = FontWeight.Bold)
                            Text("현재 1~50 레슨 · 학교·대학·직장 초입·관계·독립·의견·디지털·AI")
                            Spacer(Modifier.height(8.dp))
                            Text("과정 테스트는 100개 레슨 완성 후 열립니다.")
                        }
                    }
                }
            }
'''
new_card = '''            CourseLevel.AGE_17_20 -> {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                        onClick = onOpenAssessment,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(18.dp)) {
                            Text("17~20세 과정 테스트", fontWeight = FontWeight.Bold)
                            Text("듣기 · 말하기 · 어순 · 상황·판단 28문항")
                            Spacer(Modifier.height(8.dp))
                            if (latestAge1720Assessment == null) {
                                Text("100개 레슨을 마친 뒤 실력을 확인해보세요.")
                            } else {
                                Text(
                                    "최근 결과 " + latestAge1720Assessment.totalCorrect + " / " +
                                        latestAge1720Assessment.totalQuestions + " · " +
                                        latestAge1720Assessment.overallLabel,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
'''
replace_once(main, old_card, new_card, "age17-20 Today assessment card")

replace_once(
    main,
    '        Text("Yamone English v0.5.1")\n',
    '        Text("Yamone English v0.7.1")\n',
    "settings version label",
)

text = expansion.read_text(encoding="utf-8")
nul_count = text.count("\x00")
if nul_count:
    text = text.replace("\x00", "")
    print(f"removed {nul_count} NUL control character(s)")
else:
    print("no NUL control characters present")

pronunciation_replacements = [
    (
        'slowKorean = "천천히: ${seed.target}",',
        'slowKorean = "의미 덩어리로 천천히: ${seed.target}",',
        "age17-20 slow pronunciation helper",
    ),
    (
        'naturalKorean = "자연스럽게 연결해서: ${seed.target}",',
        'naturalKorean = "●=강세 · ‿=연결 · 실제 리듬: ${seed.rhythmEnglish}",',
        "age17-20 natural pronunciation helper",
    ),
    (
        'rhythm = "핵심어 강세 · 기능어 약화 · 연결 발음 · 문장 끝 $intonation"',
        'rhythm = "핵심어는 강하게 · 기능어는 약하게 · 붙여 읽기 · 문장 끝 $intonation"',
        "age17-20 rhythm helper",
    ),
]
for old, new, label in pronunciation_replacements:
    if new in text:
        print(f"{label}: already applied")
    elif old in text:
        text = text.replace(old, new, 1)
        print(f"{label}: applied")
    else:
        raise SystemExit(f"{label}: expected source snippet not found")
expansion.write_text(text, encoding="utf-8")

bad_files = []
for path in Path("app/src/main/java").rglob("*.kt"):
    data = path.read_bytes()
    if b"\x00" in data:
        bad_files.append(str(path))
if bad_files:
    raise SystemExit("NUL control characters remain in: " + ", ".join(bad_files))

print("v0.7.1 source patch complete")
