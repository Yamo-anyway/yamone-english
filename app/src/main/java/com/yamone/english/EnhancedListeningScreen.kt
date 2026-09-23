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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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

@Composable
fun EnhancedListenScreen(
    modifier: Modifier = Modifier,
    player: ListeningPlayer,
    speechRate: Float,
    onMessage: (String) -> Unit
) {
    var selectedLessonId by rememberSaveable { mutableIntStateOf(1) }
    var listenModeIndex by rememberSaveable { mutableIntStateOf(0) }
    var repeatCount by rememberSaveable { mutableIntStateOf(1) }
    var drillRate by rememberSaveable { mutableFloatStateOf(speechRate) }

    var isPlaying by remember { mutableStateOf(false) }
    var playingLabel by remember { mutableStateOf("") }
    var currentSegment by remember { mutableStateOf<ListeningSegment?>(null) }

    var hasListened by rememberSaveable(selectedLessonId) { mutableStateOf(false) }
    var showHint by rememberSaveable(selectedLessonId) { mutableStateOf(false) }
    var revealAnswer by rememberSaveable(selectedLessonId) { mutableStateOf(false) }
    var selectedMeaning by rememberSaveable(selectedLessonId) { mutableStateOf<String?>(null) }
    var showDialogue by rememberSaveable(selectedLessonId) { mutableStateOf(false) }

    val lesson = LessonCatalog.byId(selectedLessonId) ?: LessonCatalog.lessons.first()
    val dialogue = DialogueCatalog.byLessonId(lesson.id)
    val mode = ListenMode.entries[listenModeIndex]
    val meaningChoices = remember(lesson.id) { buildMeaningChoices(lesson) }

    fun startPlayback(label: String, segments: List<ListeningSegment>, rate: Float = drillRate) {
        isPlaying = true
        playingLabel = label
        currentSegment = null

        player.play(
            newSegments = segments,
            rate = rate,
            onSegmentChanged = { _, segment ->
                currentSegment = segment
            },
            onComplete = {
                isPlaying = false
                playingLabel = ""
                currentSegment = null
            },
            onError = { message ->
                isPlaying = false
                playingLabel = ""
                currentSegment = null
                onMessage(message)
            }
        )
    }

    fun playTarget() {
        hasListened = true
        val segment = ListeningSegment(
            lessonId = lesson.id,
            speaker = null,
            text = lesson.target,
            languageTag = "en-US",
            part = "집중 듣기"
        )
        startPlayback(
            label = "핵심 문장",
            segments = List(repeatCount) { segment }
        )
    }

    fun playDialogueOnly() {
        val segments = dialogue?.lines.orEmpty().map {
            ListeningSegment(
                lessonId = lesson.id,
                speaker = it.speaker,
                text = it.english,
                languageTag = "en-US",
                part = "대화"
            )
        }
        startPlayback("6턴 대화", segments)
    }

    DisposableEffect(Unit) {
        onDispose { player.stop() }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("듣기 훈련", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text("먼저 듣고, 나중에 글자를 확인합니다.")
        }

        item {
            Text("레슨", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(LessonCatalog.lessons, key = { it.id }) { item ->
                    FilterChip(
                        selected = selectedLessonId == item.id,
                        onClick = {
                            player.stop()
                            isPlaying = false
                            selectedLessonId = item.id
                        },
                        label = { Text(item.id.toString()) }
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        lesson.emoji + " " + lesson.id + ". " + lesson.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (!revealAnswer) {
                        Text(
                            if (hasListened) "무슨 뜻인지 생각해보세요." else "문장을 보지 말고 먼저 들어보세요.",
                            fontSize = 18.sp
                        )
                    }

                    if (showHint && !revealAnswer) {
                        Text("힌트", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                        Text(maskSentence(lesson.target), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }

                    if (revealAnswer) {
                        Text(lesson.target, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        Text(lesson.meaning, fontSize = 17.sp)
                        Text(lesson.soundEnglish, fontSize = 16.sp)
                        Text(SoundGuideCatalog.byLessonId(lesson.id).naturalKorean, fontSize = 18.sp)
                    }

                    Button(
                        onClick = { playTarget() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text(if (repeatCount == 1) "문장 듣기" else "문장 " + repeatCount + "회 듣기")
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { showHint = !showHint },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (showHint) "힌트 숨김" else "힌트")
                        }
                        OutlinedButton(
                            onClick = { revealAnswer = !revealAnswer },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (revealAnswer) "정답 숨김" else "정답 보기")
                        }
                    }
                }
            }
        }

        item {
            Text("반복", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(1, 2, 3).forEach { count ->
                    FilterChip(
                        selected = repeatCount == count,
                        onClick = { repeatCount = count },
                        label = { Text(count.toString() + "회") }
                    )
                }
            }
        }

        item {
            Text("속도", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    0.70f to "천천히",
                    0.85f to "학습",
                    1.00f to "자연"
                ).forEach { pair ->
                    FilterChip(
                        selected = drillRate == pair.first,
                        onClick = { drillRate = pair.first },
                        label = { Text(pair.second) }
                    )
                }
            }
        }

        if (hasListened) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        Text("무슨 뜻일까요?", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                        meaningChoices.forEach { choice ->
                            val chosen = selectedMeaning == choice
                            val correct = choice == lesson.meaning
                            OutlinedButton(
                                onClick = { selectedMeaning = choice },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    when {
                                        chosen && correct -> "✓ " + choice
                                        chosen && !correct -> "✕ " + choice
                                        else -> choice
                                    }
                                )
                            }
                        }

                        selectedMeaning?.let { answer ->
                            Text(
                                if (answer == lesson.meaning) "맞았어요." else "다시 들어보고 정답을 확인해보세요.",
                                color = if (answer == lesson.meaning)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("대화 통째로 듣기", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("6턴 대화를 글자 없이 먼저 들어보세요.")

                    Button(
                        onClick = { playDialogueOnly() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text("6턴 영어 대화 듣기")
                    }

                    OutlinedButton(
                        onClick = { showDialogue = !showDialogue },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (showDialogue) "대본 숨기기" else "대본 확인")
                    }

                    if (showDialogue) {
                        dialogue?.lines.orEmpty().forEach { line ->
                            Text(line.speaker + "  " + line.english, fontWeight = FontWeight.SemiBold)
                            Text("    " + line.korean, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        item {
            HorizontalDivider()
            Text("연속 듣기", fontSize = 23.sp, fontWeight = FontWeight.Bold)
            Text("학습하지 않고 계속 듣고 싶을 때 사용합니다.")
        }

        item {
            Text("재생 방식", fontWeight = FontWeight.Bold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(ListenMode.entries) { item ->
                    val index = ListenMode.entries.indexOf(item)
                    FilterChip(
                        selected = listenModeIndex == index,
                        onClick = { listenModeIndex = index },
                        label = { Text(item.label) }
                    )
                }
            }
        }

        item {
            Button(
                onClick = {
                    startPlayback(
                        label = "레슨 전체",
                        segments = ListeningPlaylistBuilder.lesson(lesson, mode),
                        rate = drillRate
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("이 레슨 전체 연속 듣기")
            }
        }

        item {
            Button(
                onClick = {
                    startPlayback(
                        label = "전체 과정",
                        segments = ListeningPlaylistBuilder.all(mode),
                        rate = drillRate
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(LessonCatalog.lessons.size.toString() + "개 레슨 전체 연속 듣기")
            }
        }

        if (isPlaying) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(playingLabel + " 재생 중", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        currentSegment?.let { segment ->
                            Text(
                                (segment.speaker?.let { it + " · " } ?: "") + segment.part,
                                fontSize = 13.sp
                            )
                            Text(segment.text, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                        }
                        OutlinedButton(
                            onClick = {
                                player.stop()
                                isPlaying = false
                                playingLabel = ""
                                currentSegment = null
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("정지")
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    player.stop()
                    isPlaying = false
                    selectedLessonId = if (selectedLessonId >= LessonCatalog.lessons.size) 1 else selectedLessonId + 1
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("다음 레슨 듣기")
            }
        }
    }
}

private fun buildMeaningChoices(lesson: Lesson): List<String> {
    val lessons = LessonCatalog.lessons
    val index = lessons.indexOfFirst { it.id == lesson.id }.coerceAtLeast(0)

    val candidates = mutableListOf(
        lesson.meaning,
        lessons[(index + 7) % lessons.size].meaning,
        lessons[(index + 19) % lessons.size].meaning
    ).distinct().toMutableList()

    var offset = 1
    while (candidates.size < 3) {
        val candidate = lessons[(index + offset) % lessons.size].meaning
        if (candidate !in candidates) candidates += candidate
        offset += 1
    }

    val shift = lesson.id % candidates.size
    return candidates.drop(shift) + candidates.take(shift)
}

private fun maskSentence(sentence: String): String {
    val parts = sentence.split(" ")
    return parts.mapIndexed { index, word ->
        if (index % 2 == 1 && word.length > 2) {
            "_".repeat(word.trimEnd('.', ',', '?', '!').length.coerceAtLeast(3)) +
                word.takeLastWhile { it == '.' || it == ',' || it == '?' || it == '!' }
        } else {
            word
        }
    }.joinToString(" ")
}
