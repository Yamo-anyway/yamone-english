package com.yamone.english

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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

private data class AssessmentChunk(
    val id: Int,
    val text: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Age57AssessmentScreen(
    isListening: Boolean,
    speak: (String) -> Unit,
    listen: ((String) -> Unit) -> Unit,
    onReviewResult: (Int, ReviewKind, Boolean) -> Unit,
    onFinish: (AssessmentSummary) -> Unit,
    onBack: () -> Unit
) {
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var listeningCorrect by rememberSaveable { mutableIntStateOf(0) }
    var speakingCorrect by rememberSaveable { mutableIntStateOf(0) }
    var orderCorrect by rememberSaveable { mutableIntStateOf(0) }
    var situationCorrect by rememberSaveable { mutableIntStateOf(0) }
    var finished by rememberSaveable { mutableStateOf(false) }

    val summary = AssessmentSummary(
        listeningCorrect = listeningCorrect,
        speakingCorrect = speakingCorrect,
        orderCorrect = orderCorrect,
        situationCorrect = situationCorrect
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("5~7세 과정 테스트") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("닫기") }
                }
            )
        }
    ) { padding ->
        if (finished) {
            AssessmentResultContent(
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

        val question = Age57AssessmentCatalog.questions[currentIndex]
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
                progress = (currentIndex + 1).toFloat() / Age57AssessmentCatalog.questions.size,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "문항 " + (currentIndex + 1) + " / " + Age57AssessmentCatalog.questions.size,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(question.kind.label, fontSize = 28.sp, fontWeight = FontWeight.Bold)

            when (question.kind) {
                AssessmentKind.LISTENING -> AssessmentListeningQuestion(
                    lesson = lesson,
                    answered = answered,
                    onAnswer = { isCorrect ->
                        if (!answered) {
                            answered = true
                            success = isCorrect
                            if (isCorrect) listeningCorrect += 1
                            onReviewResult(lesson.id, ReviewKind.LISTENING, isCorrect)
                        }
                    },
                    speak = speak
                )

                AssessmentKind.SPEAKING -> AssessmentSpeakingQuestion(
                    lesson = lesson,
                    answered = answered,
                    isListening = isListening,
                    listen = listen,
                    onAnswer = { isCorrect ->
                        if (!answered) {
                            answered = true
                            success = isCorrect
                            if (isCorrect) speakingCorrect += 1
                            onReviewResult(lesson.id, ReviewKind.SPEAKING, isCorrect)
                        }
                    }
                )

                AssessmentKind.ORDER -> AssessmentOrderQuestion(
                    lesson = lesson,
                    answered = answered,
                    onAnswer = { isCorrect ->
                        if (!answered) {
                            answered = true
                            success = isCorrect
                            if (isCorrect) orderCorrect += 1
                            onReviewResult(lesson.id, ReviewKind.ORDER, isCorrect)
                        }
                    }
                )

                AssessmentKind.SITUATION -> AssessmentSituationQuestion(
                    lesson = lesson,
                    answered = answered,
                    isListening = isListening,
                    listen = listen,
                    onAnswer = { isCorrect ->
                        if (!answered) {
                            answered = true
                            success = isCorrect
                            if (isCorrect) situationCorrect += 1
                            onReviewResult(lesson.id, ReviewKind.SPEAKING, isCorrect)
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
                            if (success) "좋아요." else "이 문장은 복습에 추가했어요.",
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
                        if (currentIndex == Age57AssessmentCatalog.questions.lastIndex) {
                            finished = true
                        } else {
                            currentIndex += 1
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (currentIndex == Age57AssessmentCatalog.questions.lastIndex)
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
private fun AssessmentListeningQuestion(
    lesson: Lesson,
    answered: Boolean,
    onAnswer: (Boolean) -> Unit,
    speak: (String) -> Unit
) {
    var playCount by rememberSaveable(lesson.id) { mutableIntStateOf(0) }
    val choices = remember(lesson.id) { assessmentMeaningChoices(lesson) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("영어 문장을 듣고 뜻을 고르세요.", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text("문장은 최대 2번 들을 수 있어요.")

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
                    enabled = !answered && !isListening,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(choice)
                }
            }
        }
    }
}

@Composable
private fun AssessmentSpeakingQuestion(
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
            Text("한국어를 보고 영어로 말하세요.", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text(lesson.meaning, fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Button(
                onClick = {
                    listen { text ->
                        recognized = text
                        score = assessmentSentenceMatch(text, lesson)
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
                Text("일치도 " + score + "%", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AssessmentOrderQuestion(
    lesson: Lesson,
    answered: Boolean,
    onAnswer: (Boolean) -> Unit
) {
    val chunks = remember(lesson.id) { assessmentChunks(lesson) }
    var selectedIds by rememberSaveable(lesson.id) { mutableStateOf(listOf<Int>()) }
    val selected = selectedIds.mapNotNull { id -> chunks.firstOrNull { it.id == id } }
    val available = chunks.filter { it.id !in selectedIds }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("영어 어순을 완성하세요.", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text(lesson.meaning, fontSize = 21.sp, fontWeight = FontWeight.Bold)
            Text(
                if (selected.isEmpty()) "아래 단어를 순서대로 누르세요."
                else selected.joinToString(" ") { it.text },
                fontSize = 20.sp
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(available, key = { it.id }) { chunk ->
                    FilterChip(
                        selected = false,
                        onClick = { selectedIds = selectedIds + chunk.id },
                        enabled = !answered && !isListening,
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
                        onAnswer(assessmentNormalize(candidate) == assessmentNormalize(lesson.target))
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
private fun AssessmentSituationQuestion(
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
            Text("상황만 보고 영어로 말하세요.", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text(lesson.situation, color = MaterialTheme.colorScheme.primary)
            Text(lesson.ownPromptKo, fontSize = 21.sp, fontWeight = FontWeight.Bold)

            Button(
                onClick = {
                    listen { text ->
                        recognized = text
                        score = assessmentSentenceMatch(text, lesson)
                        onAnswer(score >= 70)
                    }
                },
                enabled = !answered && !isListening,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Mic, contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text(if (isListening) "듣고 있어요..." else "상황에 맞게 말하기")
            }

            if (recognized.isNotBlank()) {
                Text("인식: " + recognized)
                Text("일치도 " + score + "%", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AssessmentResultContent(
    modifier: Modifier,
    summary: AssessmentSummary,
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
        Text("과정 테스트 결과", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Text(
            summary.totalCorrect.toString() + " / " + summary.totalQuestions,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(summary.overallLabel, fontSize = 22.sp, fontWeight = FontWeight.Bold)

        AssessmentResultRow("듣기", summary.listeningCorrect, summary.label(summary.listeningCorrect))
        AssessmentResultRow("말하기", summary.speakingCorrect, summary.label(summary.speakingCorrect))
        AssessmentResultRow("어순", summary.orderCorrect, summary.label(summary.orderCorrect))
        AssessmentResultRow("상황 대응", summary.situationCorrect, summary.label(summary.situationCorrect))

        HorizontalDivider()
        Text(
            "틀린 문항은 이미 자동 복습에 반영되었습니다. 복습 탭에서 우선순위대로 다시 연습할 수 있어요."
        )

        Button(onClick = onSaveAndClose, modifier = Modifier.fillMaxWidth()) {
            Text("결과 저장하고 홈으로")
        }
        OutlinedButton(onClick = onRetry, modifier = Modifier.fillMaxWidth()) {
            Text("다시 테스트")
        }
    }
}

@Composable
private fun AssessmentResultRow(
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
            Text(correct.toString() + " / 4")
            Text(result, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
    }
}

private fun assessmentMeaningChoices(lesson: Lesson): List<String> {
    val lessons = LessonCatalog.lessons
    val index = lessons.indexOfFirst { it.id == lesson.id }.coerceAtLeast(0)
    val list = mutableListOf(
        lesson.meaning,
        lessons[(index + 29) % lessons.size].meaning,
        lessons[(index + 61) % lessons.size].meaning
    ).distinct().toMutableList()

    var offset = 1
    while (list.size < 3) {
        val value = lessons[(index + offset) % lessons.size].meaning
        if (value !in list) list += value
        offset += 1
    }

    val shift = lesson.id % list.size
    return list.drop(shift) + list.take(shift)
}

private fun assessmentChunks(lesson: Lesson): List<AssessmentChunk> {
    val original = lesson.target.split(" ")
        .filter { it.isNotBlank() }
        .mapIndexed { index, text -> AssessmentChunk(index, text) }

    if (original.size <= 1) return original

    val shift = ((lesson.id * 5) % (original.size - 1)) + 1
    return original.drop(shift) + original.take(shift)
}

private fun assessmentSentenceMatch(spoken: String, lesson: Lesson): Int {
    val candidates = listOf(lesson.target) + lesson.accepted
    return candidates.maxOfOrNull { target ->
        val a = assessmentNormalize(spoken)
        val b = assessmentNormalize(target)
        if (a.isBlank() || b.isBlank()) {
            0
        } else {
            val distance = assessmentLevenshtein(a, b)
            ((1f - distance.toFloat() / max(a.length, b.length).coerceAtLeast(1)) * 100)
                .toInt()
                .coerceIn(0, 100)
        }
    } ?: 0
}

private fun assessmentNormalize(text: String): String =
    text.lowercase()
        .replace(Regex("[^a-z0-9' ]"), "")
        .replace(Regex("\\s+"), " ")
        .trim()

private fun assessmentLevenshtein(a: String, b: String): Int {
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
