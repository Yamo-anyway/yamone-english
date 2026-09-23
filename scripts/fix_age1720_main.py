from pathlib import Path

path = Path('app/src/main/java/com/yamone/english/MainActivity.kt')
text = path.read_text(encoding='utf-8')

selected_marker = '        when (selectedCourse) {'
selected_start = text.index(selected_marker)
return_marker = '\n        return\n    }'
return_pos = text.index(return_marker, selected_start)
selected_close = text.rfind('\n        }', selected_start, return_pos)
if selected_close < 0:
    raise RuntimeError('selectedCourse when close not found')
branch = '\n\n            CourseLevel.AGE_17_20 -> Unit'
if 'CourseLevel.AGE_17_20 -> Unit' not in text[selected_start:return_pos]:
    text = text[:selected_close] + branch + text[selected_close:]

course_marker = '        when (course) {'
course_start = text.index(course_marker)
lesson_marker = '\n\n        item {\n            Spacer(Modifier.height(4.dp))\n            Text("레슨 찾기"'
lesson_pos = text.index(lesson_marker, course_start)
course_close = text.rfind('\n        }', course_start, lesson_pos)
if course_close < 0:
    raise RuntimeError('course when close not found')
course_branch = '''

            CourseLevel.AGE_17_20 -> {
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
            }'''
if 'Text("17~20세 과정", fontWeight = FontWeight.Bold)' not in text[course_start:lesson_pos]:
    text = text[:course_close] + course_branch + text[course_close:]

path.write_text(text, encoding='utf-8')
Path('scripts/fix_age1720_main.py').unlink(missing_ok=True)
print('Added AGE_17_20 branches to assessment routing and TodayScreen.')
