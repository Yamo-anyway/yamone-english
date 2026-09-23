package com.yamone.english

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max

private data class Age1416AssessmentChunk(
    val id: Int,
    val text: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Age1416AssessmentScreen(
    isListening: Boolean,
    speak: (String) -> Unit,
    listen: ((String) -> Unit) -> Unit,
    onReviewResult: (Int, ReviewKind, Boolean) -> Unit,
    onFinish: (Age1416AssessmentSummary) -> Unit,
    onBack: () -> Unit
) {
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var listeningCorrect by rememberSaveable { mutableIntStateOf(0) }
    var speakingCorrect by rememberSaveable { mutableIntStateOf(0) }
    var orderCorrect by rememberSaveable { mutableIntStateOf(0) }
    var situationCorrect by rememberSaveable { mutableIntStateOf(0) }
    var finished by rememberSaveable { mutableStateOf(false) }

    val summary = Age1416AssessmentSummary(
        listeningCorrect = listeningCorrect,
        speakingCorrect = speakingCorrect,
        orderCorrect = orderCorrect,
        situationCorrect = situationCorrect
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("14~16세 과정 테스트") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("닫기") }
                }
            )
        }
    ) { padding ->
        if (finished) {
            Age1416AssessmentResult(
                modifier = Modifier.padding(padding),
                summary = summary,
                onSaveAndClose = { onFinish(summary) },
                onRetry = {
                    currentIndex = 0
                    listeningCorrect = 0
                    speakingCorrect = 0
                    orderCorrect = 0
                    situationCorrect = 0
                    finished = false
                }
            )
            return@Scaffold
        }

        val question = Age1416AssessmentCatalog.questions[currentIndex]
        val lesson = LessonCatalog.byId(question.lessonId) ?: return@Scaffold
        var answered by rememberSaveable(question.number) { mutableStateOf(false) }
        var success by rememberSaveable(question.number) { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            LinearProgressIndicator(
                progress = (currentIndex + 1).toFloat() / Age1416AssessmentCatalog.questions.size,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "문항 " + (currentIndex + 1) + " / " + Age1416AssessmentCatalog.questions.size,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(question.kind.label, fontSize = 28.sp, fontWeight = FontWeight.Bold)

            when (question.kind) {
                AssessmentKind.LISTENING -> Age1416ListeningQuestion(
                    lesson = lesson,
                    answered = answered,
                    speak = speak,
                    onAnswer = { correct ->
                        if (!answered) {
                            answered = true
                            success = correct
                            if (correct) listeningCorrect += 1
                            onReviewResult(lesson.id, ReviewKind.LISTENING, correct)
                        }
                    }
                )

                AssessmentKind.SPEAKING -> Age1416SpeakingQuestion(
                    lesson = lesson,
                    answered = answered,
                    isListening = isListening,
                    listen = listen,
                    onAnswer = { correct ->
                        if (!answered) {
                            answered = true
                            success = correct
                            if (correct) speakingCorrect += 1
                            onReviewResult(lesson.id, ReviewKind.SPEAKING, correct)
                        }
                    }
                )

                AssessmentKind.ORDER -> Age1416OrderQuestion(
                    lesson = lesson,
                    answered = answered,
                    onAnswer = { correct ->
                        if (!answered) {
                            answered = true
                            success = correct
                            if (correct) orderCorrect += 1
                            onReviewResult(lesson.id, ReviewKind.ORDER, correct)
                        }
                    }
                )

                AssessmentKind.SITUATION -> Age1416SituationQuestion(
                    lesson = lesson,
                    answered = answered,
                    isListening = isListening,
                    listen = listen,
                    onAnswer = { correct ->
                        if (!answered) {
                            answered = true
                            success = correct
                            if (correct) situationCorrect += 1
                            onReviewResult(lesson.id, ReviewKind.SPEAKING, correct)
                        }
                    }
                )
            }

            if (answered) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (success)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            if (success) "좋아요." else "이 표현은 자동 복습에 추가했어요.",
                            fontWeight = FontWeight.Bold
                        )
                        Text(lesson.target, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(lesson.meaning)
                        Text(
                            PronunciationQaCatalog.byLessonId(lesson.id).naturalKorean,
                            fontSize = 17.sp
                        )
                    }
                }

                Button(
                    onClick = {
                        if (currentIndex == Age1416AssessmentCatalog.questions.lastIndex) {
                            finished = true
                        } else {
                            currentIndex += 1
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (currentIndex == Age1416AssessmentCatalog.questions.lastIndex)
                            "결과 보기"
                        else
                            "다음 문제"
                    )
                }
            }
        }
    }
}

@Composable
private fun Age1416ListeningQuestion(
    lesson: Lesson,
    answered: Boolean,
    speak: (String) -> Unit,
    onAnswer: (Boolean) -> Unit
) {
    var playCount by rememberSaveable(lesson.id) { mutableIntStateOf(0) }
    val choices = remember(lesson.id) { age1416MeaningChoices(lesson) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("문장을 듣고 가장 가까운 뜻을 고르세요.", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text("핵심 주장뿐 아니라 조건·근거·뉘앙스의 차이까지 들어보세요.")

            Button(
                onClick = {
                    if (playCount < 2) {
                        playCount += 1
                        speak(lesson.target)
                    }
                },
                enabled = playCount < 2 && !answered,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.VolumeUp, contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text("듣기 " + playCount + " / 2")
            }

            choices.forEach { choice ->
                OutlinedButton(
                    onClick = { onAnswer(choice == lesson.meaning) },
                    enabled = !answered,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(choice)
                }
            }
        }
    }
}

@Composable
private fun Age1416SpeakingQuestion(
    lesson: Lesson,
    answered: Boolean,
    isListening: Boolean,
    listen: ((String) -> Unit) -> Unit,
    onAnswer: (Boolean) -> Unit
) {
    var recognized by rememberSaveable(lesson.id) { mutableStateOf("") }
    var score by rememberSaveable(lesson.id) { mutableIntStateOf(-1) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("한국어 의도를 보고 자연스럽게 영어로 말하세요.", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text(lesson.meaning, fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Button(
                onClick = {
                    listen { text ->
                        recognized = text
                        score = age1416SentenceMatch(text, lesson)
                        onAnswer(score >= 75)
                    }
                },
                enabled = !answered && !isListening,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Mic, contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text(if (isListening) "듣고 있어요..." else "영어로 말하기")
            }

            if (recognized.isNotBlank()) {
                Text("인식: " + recognized)
                Text("문장 인식 일치도 " + score + "%", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun Age1416OrderQuestion(
    lesson: Lesson,
    answered: Boolean,
    onAnswer: (Boolean) -> Unit
) {
    val chunks = remember(lesson.id) { age1416Chunks(lesson) }
    var selectedIds by rememberSaveable(lesson.id) { mutableStateOf(listOf<Int>()) }
    val selected = selectedIds.mapNotNull { id -> chunks.firstOrNull { it.id == id } }
    val available = chunks.filter { it.id !in selectedIds }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("한국어 어순으로 번역하지 말고 영어의 생각 순서대로 완성하세요.", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text(lesson.meaning, fontSize = 21.sp, fontWeight = FontWeight.Bold)

            Text(
                if (selected.isEmpty()) "영어 덩어리를 순서대로 누르세요."
                else selected.joinToString(" ") { it.text },
                fontSize = 20.sp
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(available, key = { it.id }) { chunk ->
                    FilterChip(
                        selected = false,
                        onClick = { selectedIds = selectedIds + chunk.id },
                        enabled = !answered,
                        label = { Text(chunk.text) }
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = {
                        if (selectedIds.isNotEmpty()) selectedIds = selectedIds.dropLast(1)
                    },
                    enabled = !answered && selectedIds.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("되돌리기")
                }

                Button(
                    onClick = {
                        val candidate = selected.joinToString(" ") { it.text }
                        onAnswer(age1416Normalize(candidate) == age1416Normalize(lesson.target))
                    },
                    enabled = !answered && selectedIds.size == chunks.size,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("확인")
                }
            }
        }
    }
}

@Composable
private fun Age1416SituationQuestion(
    lesson: Lesson,
    answered: Boolean,
    isListening: Boolean,
    listen: ((String) -> Unit) -> Unit,
    onAnswer: (Boolean) -> Unit
) {
    var recognized by rememberSaveable(lesson.id) { mutableStateOf("") }
    var score by rememberSaveable(lesson.id) { mutableIntStateOf(-1) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("상황을 보고 판단·근거·자기 입장을 영어로 말하세요.", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text(lesson.situation, color = MaterialTheme.colorScheme.primary)
            Text(lesson.ownPromptKo, fontSize = 21.sp, fontWeight = FontWeight.Bold)

            Button(
                onClick = {
                    listen { text ->
                        recognized = text
                        score = age1416SentenceMatch(text, lesson)
                        onAnswer(score >= 65)
                    }
                },
                enabled = !answered && !isListening,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Mic, contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text(if (isListening) "듣고 있어요..." else "내 생각 영어로 말하기")
            }

            if (recognized.isNotBlank()) {
                Text("인식: " + recognized)
                Text("문장 인식 일치도 " + score + "%", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun Age1416AssessmentResult(
    modifier: Modifier,
    summary: Age1416AssessmentSummary,
    onSaveAndClose: () -> Unit,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("14~16세 과정 테스트 결과", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Text(
            summary.totalCorrect.toString() + " / " + summary.totalQuestions,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(summary.overallLabel, fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Age1416ResultRow("듣기", summary.listeningCorrect, summary.label(summary.listeningCorrect))
        Age1416ResultRow("말하기", summary.speakingCorrect, summary.label(summary.speakingCorrect))
        Age1416ResultRow("어순", summary.orderCorrect, summary.label(summary.orderCorrect))
        Age1416ResultRow("상황·판단", summary.situationCorrect, summary.label(summary.situationCorrect))

        HorizontalDivider()
        Text("틀린 문항은 14~16세 자동 복습에 이미 반영되었습니다.")

        Button(onClick = onSaveAndClose, modifier = Modifier.fillMaxWidth()) {
            Text("결과 저장하고 홈으로")
        }
        OutlinedButton(onClick = onRetry, modifier = Modifier.fillMaxWidth()) {
            Text("다시 테스트")
        }
    }
}

@Composable
private fun Age1416ResultRow(
    label: String,
    correct: Int,
    result: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(label, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text(correct.toString() + " / 7")
            Text(result, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
    }
}

private fun age1416MeaningChoices(lesson: Lesson): List<String> {
    val lessons = CourseCatalog.lessons(CourseLevel.AGE_14_16)
    val index = lessons.indexOfFirst { it.id == lesson.id }.coerceAtLeast(0)
    val choices = mutableListOf(
        lesson.meaning,
        lessons[(index + 29) % lessons.size].meaning,
        lessons[(index + 63) % lessons.size].meaning
    ).distinct().toMutableList()

    var offset = 1
    while (choices.size < 3) {
        val value = lessons[(index + offset) % lessons.size].meaning
        if (value !in choices) choices += value
        offset += 1
    }

    val shift = lesson.courseLessonNumber % choices.size
    return choices.drop(shift) + choices.take(shift)
}

private fun age1416Chunks(lesson: Lesson): List<Age1416AssessmentChunk> {
    val words = lesson.target.split(" ").filter { it.isNotBlank() }
    val chunkSize = when {
        words.size >= 12 -> 3
        words.size >= 7 -> 2
        else -> 1
    }
    val original = words.chunked(chunkSize)
        .mapIndexed { index, part -> Age1416AssessmentChunk(index, part.joinToString(" ")) }

    if (original.size <= 1) return original

    val shift = ((lesson.courseLessonNumber * 5) % (original.size - 1)) + 1
    return original.drop(shift) + original.take(shift)
}

private fun age1416SentenceMatch(spoken: String, lesson: Lesson): Int {
    val candidates = listOf(lesson.target) + lesson.accepted
    return candidates.maxOfOrNull { target ->
        val a = age1416Normalize(spoken)
        val b = age1416Normalize(target)
        if (a.isBlank() || b.isBlank()) {
            0
        } else {
            val distance = age1416Levenshtein(a, b)
            ((1f - distance.toFloat() / max(a.length, b.length).coerceAtLeast(1)) * 100)
                .toInt()
                .coerceIn(0, 100)
        }
    } ?: 0
}

private fun age1416Normalize(text: String): String =
    text.lowercase()
        .replace(Regex("[^a-z0-9' ]"), "")
        .replace(Regex("\\s+"), " ")
        .trim()

private fun age1416Levenshtein(a: String, b: String): Int {
    if (a.isEmpty()) return b.length
    if (b.isEmpty()) return a.length

    val previous = IntArray(b.length + 1) { it }
    val current = IntArray(b.length + 1)

    for (i in a.indices) {
        current[0] = i + 1
        for (j in b.indices) {
            val cost = if (a[i] == b[j]) 0 else 1
            current[j + 1] = minOf(
                current[j] + 1,
                previous[j + 1] + 1,
                previous[j] + cost
            )
        }
        for (j in previous.indices) previous[j] = current[j]
    }

    return previous[b.length]
}
