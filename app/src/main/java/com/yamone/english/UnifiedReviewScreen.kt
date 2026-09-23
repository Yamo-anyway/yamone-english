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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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

private data class ReviewChunk(
    val id: Int,
    val text: String
)

@Composable
fun UnifiedReviewScreen(
    modifier: Modifier = Modifier,
    reviewItems: List<UnifiedReviewItem>,
    isListening: Boolean,
    speak: (String) -> Unit,
    listen: ((String) -> Unit) -> Unit,
    onResult: (Int, ReviewKind, Boolean) -> Unit
) {
    val dueItems = reviewItems.filter { it.isDue }
    val laterItems = reviewItems.filterNot { it.isDue }

    var selectedLessonId by rememberSaveable(reviewItems) {
        mutableIntStateOf(dueItems.firstOrNull()?.lessonId ?: reviewItems.firstOrNull()?.lessonId ?: 0)
    }
    val selectedItem = reviewItems.firstOrNull { it.lessonId == selectedLessonId }
    val lesson = LessonCatalog.byId(selectedLessonId)

    var selectedKindName by rememberSaveable(selectedLessonId) {
        mutableStateOf(selectedItem?.kinds?.firstOrNull()?.name ?: ReviewKind.LISTENING.name)
    }
    val selectedKind = runCatching { ReviewKind.valueOf(selectedKindName) }
        .getOrDefault(ReviewKind.LISTENING)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("자동 복습", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text("틀린 횟수가 많고 지금 복습할 문장부터 먼저 보여줍니다.")
            Spacer(Modifier.height(8.dp))
            Text(
                "지금 " + dueItems.size + "개 · 나중 " + laterItems.size + "개",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        if (reviewItems.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(24.dp)) {
                        Text("🌱", fontSize = 34.sp)
                        Text("지금 복습할 문장이 없어요.", fontWeight = FontWeight.Bold)
                        Text("듣기·말하기·어순에서 어려운 문장이 생기면 자동으로 여기에 모입니다.")
                    }
                }
            }
        } else {
            item {
                Text("복습 문장", fontSize = 21.sp, fontWeight = FontWeight.Bold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(reviewItems, key = { it.lessonId }) { item ->
                        val lessonItem = LessonCatalog.byId(item.lessonId)
                        FilterChip(
                            selected = item.lessonId == selectedLessonId,
                            onClick = {
                                selectedLessonId = item.lessonId
                                selectedKindName = item.kinds.firstOrNull()?.name
                                    ?: ReviewKind.LISTENING.name
                            },
                            label = {
                                Text(
                                    item.lessonId.toString() +
                                        if (item.isDue) " · 지금" else " · 예정"
                                )
                            }
                        )
                    }
                }
            }

            if (selectedItem != null && lesson != null) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                lesson.emoji + " " + lesson.id + ". " + lesson.title,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(lesson.target, fontSize = 20.sp)
                            Text(lesson.meaning)
                            Text(
                                "누적 약점 " + selectedItem.totalErrors + "회",
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                item {
                    Text("복습 종류", fontWeight = FontWeight.Bold)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(selectedItem.kinds) { kind ->
                            val count = selectedItem.counts[kind] ?: 0
                            FilterChip(
                                selected = selectedKind == kind,
                                onClick = { selectedKindName = kind.name },
                                label = { Text(kind.label + " " + count) }
                            )
                        }
                    }
                }

                when (selectedKind) {
                    ReviewKind.LISTENING -> {
                        item {
                            ListeningReviewCard(
                                lesson = lesson,
                                speak = speak,
                                onResult = { success ->
                                    onResult(lesson.id, ReviewKind.LISTENING, success)
                                }
                            )
                        }
                    }

                    ReviewKind.SPEAKING -> {
                        item {
                            SpeakingReviewCard(
                                lesson = lesson,
                                isListening = isListening,
                                speak = speak,
                                listen = listen,
                                onResult = { success ->
                                    onResult(lesson.id, ReviewKind.SPEAKING, success)
                                }
                            )
                        }
                    }

                    ReviewKind.ORDER -> {
                        item {
                            OrderReviewCard(
                                lesson = lesson,
                                onResult = { success ->
                                    onResult(lesson.id, ReviewKind.ORDER, success)
                                }
                            )
                        }
                    }
                }
            }

            if (laterItems.isNotEmpty()) {
                item {
                    HorizontalDivider()
                    Text("다음 복습 예정", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("여러 번 틀린 문장은 한 번 맞힌 뒤에도 다음 날 다시 나옵니다.")
                }
            }
        }
    }
}

@Composable
private fun ListeningReviewCard(
    lesson: Lesson,
    speak: (String) -> Unit,
    onResult: (Boolean) -> Unit
) {
    var heard by rememberSaveable(lesson.id) { mutableStateOf(false) }
    var answer by rememberSaveable(lesson.id) { mutableStateOf<String?>(null) }
    val choices = remember(lesson.id) { reviewMeaningChoices(lesson) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("듣기 복습", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("글자를 보지 말고 먼저 들어보세요.")

            Button(
                onClick = {
                    heard = true
                    answer = null
                    speak(lesson.target)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.VolumeUp, contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text("문장 듣기")
            }

            if (heard) {
                choices.forEach { choice ->
                    OutlinedButton(
                        onClick = {
                            answer = choice
                            onResult(choice == lesson.meaning)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            when {
                                answer == choice && choice == lesson.meaning -> "✓ " + choice
                                answer == choice -> "✕ " + choice
                                else -> choice
                            }
                        )
                    }
                }
            }

            if (answer == lesson.meaning) {
                Text("맞았어요. 듣기 약점이 한 단계 줄었습니다.", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SpeakingReviewCard(
    lesson: Lesson,
    isListening: Boolean,
    speak: (String) -> Unit,
    listen: ((String) -> Unit) -> Unit,
    onResult: (Boolean) -> Unit
) {
    var spoken by rememberSaveable(lesson.id) { mutableStateOf("") }
    var score by rememberSaveable(lesson.id) { mutableIntStateOf(-1) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("말하기 복습", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(lesson.meaning, fontSize = 18.sp)

            OutlinedButton(
                onClick = { speak(lesson.target) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("정답 한번 듣기")
            }

            Button(
                onClick = {
                    listen { text ->
                        spoken = text
                        score = unifiedSentenceMatch(text, lesson.target)
                        onResult(score >= 75)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Mic, contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text(if (isListening) "듣고 있어요..." else "영어로 말하기")
            }

            if (spoken.isNotBlank()) {
                Text("인식: " + spoken)
                Text(
                    "문장 인식 일치도 " + score + "%",
                    fontWeight = FontWeight.Bold,
                    color = if (score >= 75)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun OrderReviewCard(
    lesson: Lesson,
    onResult: (Boolean) -> Unit
) {
    val chunks = remember(lesson.id) { reviewChunks(lesson) }
    var selectedIds by rememberSaveable(lesson.id) { mutableStateOf(listOf<Int>()) }
    var checked by rememberSaveable(lesson.id) { mutableStateOf(false) }
    var correct by rememberSaveable(lesson.id) { mutableStateOf(false) }

    val selected = selectedIds.mapNotNull { id -> chunks.firstOrNull { it.id == id } }
    val available = chunks.filter { it.id !in selectedIds }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("어순 복습", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(lesson.meaning, fontSize = 18.sp)

            Text(
                if (selected.isEmpty()) "아래 영어 덩어리를 순서대로 누르세요."
                else selected.joinToString(" ") { it.text },
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(available, key = { it.id }) { chunk ->
                    FilterChip(
                        selected = false,
                        onClick = {
                            selectedIds = selectedIds + chunk.id
                            checked = false
                        },
                        label = { Text(chunk.text) }
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = {
                        if (selectedIds.isNotEmpty()) selectedIds = selectedIds.dropLast(1)
                        checked = false
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("되돌리기")
                }

                Button(
                    onClick = {
                        checked = true
                        correct = unifiedNormalize(
                            selected.joinToString(" ") { it.text }
                        ) == unifiedNormalize(lesson.target)
                        onResult(correct)
                    },
                    enabled = selectedIds.size == chunks.size,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("확인")
                }
            }

            if (checked) {
                Text(
                    if (correct) "맞았어요. 어순 약점이 한 단계 줄었습니다."
                    else "순서를 다시 만들어보세요.",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun reviewMeaningChoices(lesson: Lesson): List<String> {
    val lessons = LessonCatalog.lessons
    val index = lessons.indexOfFirst { it.id == lesson.id }.coerceAtLeast(0)
    val choices = listOf(
        lesson.meaning,
        lessons[(index + 17) % lessons.size].meaning,
        lessons[(index + 43) % lessons.size].meaning
    ).distinct().toMutableList()

    var offset = 1
    while (choices.size < 3) {
        val value = lessons[(index + offset) % lessons.size].meaning
        if (value !in choices) choices += value
        offset += 1
    }

    val shift = lesson.id % choices.size
    return choices.drop(shift) + choices.take(shift)
}

private fun reviewChunks(lesson: Lesson): List<ReviewChunk> {
    val original = lesson.target.split(" ")
        .filter { it.isNotBlank() }
        .mapIndexed { index, text -> ReviewChunk(index, text) }

    if (original.size <= 1) return original

    val shift = ((lesson.id * 3) % (original.size - 1)) + 1
    return original.drop(shift) + original.take(shift)
}

private fun unifiedSentenceMatch(spoken: String, target: String): Int {
    val a = unifiedNormalize(spoken)
    val b = unifiedNormalize(target)
    if (a.isBlank() || b.isBlank()) return 0

    val distance = unifiedLevenshtein(a, b)
    return ((1f - distance.toFloat() / max(a.length, b.length).coerceAtLeast(1)) * 100)
        .toInt()
        .coerceIn(0, 100)
}

private fun unifiedNormalize(text: String): String =
    text.lowercase()
        .replace(Regex("[^a-z0-9' ]"), "")
        .replace(Regex("\\s+"), " ")
        .trim()

private fun unifiedLevenshtein(a: String, b: String): Int {
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
