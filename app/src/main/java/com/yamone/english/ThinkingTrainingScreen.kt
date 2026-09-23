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
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max

private data class ThinkingChunk(
    val id: Int,
    val text: String
)

@Composable
fun ThinkingTrainingScreen(
    modifier: Modifier = Modifier,
    completedIds: Set<Int>,
    isListening: Boolean,
    speak: (String) -> Unit,
    listen: ((String) -> Unit) -> Unit,
    onComplete: (Int) -> Unit,
    onOrderResult: (Int, Boolean) -> Unit,
    onSpeakingResult: (Int, Boolean) -> Unit
) {
    var selectedLessonId by rememberSaveable { mutableIntStateOf(1) }
    var sectionIndex by rememberSaveable { mutableIntStateOf(0) }

    val lesson = LessonCatalog.byId(selectedLessonId) ?: LessonCatalog.lessons.first()
    val selectedSection = Age57CourseSections.sections[sectionIndex]
    val sectionLessons = selectedSection.lessons()
    val guide = ThinkingCatalog.byLessonId(lesson.id)

    var showOrderHint by rememberSaveable(selectedLessonId) { mutableStateOf(false) }
    var selectedChunkIds by rememberSaveable(selectedLessonId) { mutableStateOf(listOf<Int>()) }
    var checked by rememberSaveable(selectedLessonId) { mutableStateOf(false) }
    var arrangementCorrect by rememberSaveable(selectedLessonId) { mutableStateOf(false) }
    var spokenText by rememberSaveable(selectedLessonId) { mutableStateOf("") }
    var spokenScore by rememberSaveable(selectedLessonId) { mutableIntStateOf(-1) }

    val chunks = remember(lesson.id) { shuffledThinkingChunks(lesson) }
    val selectedChunks = selectedChunkIds.mapNotNull { id -> chunks.firstOrNull { it.id == id } }
    val availableChunks = chunks.filter { it.id !in selectedChunkIds }

    fun resetPractice() {
        selectedChunkIds = emptyList()
        checked = false
        arrangementCorrect = false
        spokenText = ""
        spokenScore = -1
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("영어식 생각", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text("한국어를 먼저 영어 어순으로 바꾸고, 영어를 바로 꺼내는 연습입니다.")
            Spacer(Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = completedIds.size.toFloat() / LessonCatalog.lessons.size.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                completedIds.size.toString() + " / " + LessonCatalog.lessons.size + " 완료",
                fontSize = 13.sp
            )
        }

        item {
            Text("레슨", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(Age57CourseSections.sections, key = { it.id }) { section ->
                    val index = Age57CourseSections.sections.indexOf(section)
                    FilterChip(
                        selected = sectionIndex == index,
                        onClick = {
                            sectionIndex = index
                            selectedLessonId = section.range.first
                        },
                        label = { Text(section.title) }
                    )
                }
            }
            Spacer(Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(sectionLessons, key = { it.id }) { item ->
                    FilterChip(
                        selected = selectedLessonId == item.id,
                        onClick = {
                            selectedLessonId = item.id
                        },
                        label = {
                            Text(
                                if (item.id in completedIds) item.id.toString() + " ✓"
                                else item.id.toString()
                            )
                        }
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        lesson.emoji + " " + lesson.id + ". " + lesson.title,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text("한국어", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    Text(lesson.meaning, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)

                    OutlinedButton(
                        onClick = { showOrderHint = !showOrderHint },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (showOrderHint) "영어식 순서 숨기기" else "영어식 순서 보기")
                    }

                    if (showOrderHint) {
                        Text("영어식 생각 순서", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                        Text(guide.englishOrderKorean, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Text("영어 덩어리를 순서대로 눌러보세요.", fontSize = 19.sp, fontWeight = FontWeight.Bold)

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("내가 만든 문장", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)

                    if (selectedChunks.isEmpty()) {
                        Text("아래 단어를 눌러 문장을 만드세요.", fontSize = 17.sp)
                    } else {
                        Text(
                            selectedChunks.joinToString(" ") { it.text },
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (selectedChunks.isNotEmpty()) {
                        OutlinedButton(
                            onClick = {
                                selectedChunkIds = selectedChunkIds.dropLast(1)
                                checked = false
                                arrangementCorrect = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("마지막 단어 되돌리기")
                        }
                    }
                }
            }
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(availableChunks, key = { it.id }) { chunk ->
                    FilterChip(
                        selected = false,
                        onClick = {
                            selectedChunkIds = selectedChunkIds + chunk.id
                            checked = false
                            arrangementCorrect = false
                        },
                        label = { Text(chunk.text) }
                    )
                }
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { resetPractice() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("초기화")
                }

                Button(
                    onClick = {
                        checked = true
                        arrangementCorrect = normalizeThinking(
                            selectedChunks.joinToString(" ") { it.text }
                        ) == normalizeThinking(lesson.target)
                        onOrderResult(lesson.id, arrangementCorrect)
                    },
                    enabled = selectedChunkIds.size == chunks.size,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("순서 확인")
                }
            }
        }

        if (checked) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (arrangementCorrect)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            if (arrangementCorrect) "좋아요. 영어 순서가 맞아요." else "순서를 다시 생각해보세요.",
                            fontWeight = FontWeight.Bold
                        )

                        if (arrangementCorrect) {
                            Text(lesson.target, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Text(
                                PronunciationQaCatalog.byLessonId(lesson.id).naturalKorean,
                                fontSize = 17.sp
                            )
                            Button(
                                onClick = { speak(lesson.target) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.VolumeUp, contentDescription = null)
                                Spacer(Modifier.size(8.dp))
                                Text("완성 문장 듣기")
                            }
                        } else {
                            OutlinedButton(
                                onClick = { showOrderHint = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("영어식 생각 순서 보기")
                            }
                        }
                    }
                }
            }
        }

        if (arrangementCorrect) {
            item {
                HorizontalDivider()
                Text("이제 보지 않고 말해보세요.", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                Button(
                    onClick = {
                        listen { text ->
                            spokenText = text
                            spokenScore = thinkingSentenceMatch(text, lesson.target)
                            onSpeakingResult(lesson.id, spokenScore >= 75)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Mic, contentDescription = null)
                    Spacer(Modifier.size(8.dp))
                    Text(if (isListening) "듣고 있어요..." else "영어로 말하기")
                }

                if (spokenText.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Text("내가 말한 문장", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            Text(spokenText, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                            Text("문장 인식 일치도 " + spokenScore + "%", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        onComplete(lesson.id)
                        selectedLessonId =
                            if (lesson.id >= LessonCatalog.lessons.size) 1 else lesson.id + 1
                        sectionIndex = Age57CourseSections.indexForLesson(selectedLessonId)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (lesson.id in completedIds) "다음 문장"
                        else "완료하고 다음 문장"
                    )
                }
            }
        }

        item {
            Spacer(Modifier.height(12.dp))
            Text(
                "목표: 한국어 → 번역이 아니라, 한국어 단계에서부터 영어 어순으로 생각한 뒤 바로 영어로 말하기",
                fontSize = 13.sp
            )
        }
    }
}

private fun shuffledThinkingChunks(lesson: Lesson): List<ThinkingChunk> {
    val original = lesson.target
        .split(" ")
        .filter { it.isNotBlank() }
        .mapIndexed { index, text -> ThinkingChunk(index, text) }

    if (original.size <= 1) return original

    val shift = (lesson.id % (original.size - 1)) + 1
    return original.drop(shift) + original.take(shift)
}

private fun normalizeThinking(text: String): String =
    text.lowercase()
        .replace(Regex("[^a-z0-9' ]"), "")
        .replace(Regex("\\s+"), " ")
        .trim()

private fun thinkingSentenceMatch(spoken: String, target: String): Int {
    val a = normalizeThinking(spoken)
    val b = normalizeThinking(target)
    if (a.isBlank() || b.isBlank()) return 0

    val distance = thinkingLevenshtein(a, b)
    return ((1f - distance.toFloat() / max(a.length, b.length).coerceAtLeast(1)) * 100)
        .toInt()
        .coerceIn(0, 100)
}

private fun thinkingLevenshtein(a: String, b: String): Int {
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
