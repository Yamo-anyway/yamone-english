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

@Composable
fun ExpressionTalkScreen(
    modifier: Modifier = Modifier,
    completedIds: Set<Int>,
    isListening: Boolean,
    speak: (String) -> Unit,
    listenKorean: ((String) -> Unit) -> Unit,
    listenEnglish: ((String) -> Unit) -> Unit,
    onComplete: (Int) -> Unit
) {
    var mode by rememberSaveable { mutableIntStateOf(0) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("대화 · 표현", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text("한국어 생각을 자연스러운 영어로 바꿔 말합니다.")
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = mode == 0,
                    onClick = { mode = 0 },
                    label = { Text("내 표현") }
                )
                FilterChip(
                    selected = mode == 1,
                    onClick = { mode = 1 },
                    label = { Text("자유 대화") }
                )
            }
        }

        if (mode == 0) {
            item {
                NaturalExpressionTrainingContent(
                    completedIds = completedIds,
                    isListening = isListening,
                    speak = speak,
                    listenKorean = listenKorean,
                    listenEnglish = listenEnglish,
                    onComplete = onComplete
                )
            }
        } else {
            item {
                OfflineFreeTalkContent(
                    isListening = isListening,
                    speak = speak,
                    listenEnglish = listenEnglish
                )
            }
        }
    }
}

@Composable
private fun NaturalExpressionTrainingContent(
    completedIds: Set<Int>,
    isListening: Boolean,
    speak: (String) -> Unit,
    listenKorean: ((String) -> Unit) -> Unit,
    listenEnglish: ((String) -> Unit) -> Unit,
    onComplete: (Int) -> Unit
) {
    var koreanSpeech by rememberSaveable { mutableStateOf("") }
    var selectedExpressionId by rememberSaveable { mutableIntStateOf(0) }
    var category by rememberSaveable { mutableStateOf("전체") }
    var spokenEnglish by rememberSaveable { mutableStateOf("") }
    var spokenScore by rememberSaveable { mutableIntStateOf(-1) }

    val matches = remember(koreanSpeech) {
        NaturalExpressionCatalog.bestMatches(koreanSpeech, 3)
    }
    val selectedExpression =
        NaturalExpressionCatalog.expressions.firstOrNull { it.id == selectedExpressionId }
            ?: matches.firstOrNull()?.first

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("1 · 한국어로 생각 말하기", fontWeight = FontWeight.Bold)
                Text("평소 말하듯 편하게 한국어로 말하세요.", fontSize = 17.sp)

                Button(
                    onClick = {
                        listenKorean { text ->
                            koreanSpeech = text
                            selectedExpressionId =
                                NaturalExpressionCatalog.bestMatches(text, 1)
                                    .firstOrNull()
                                    ?.first
                                    ?.id
                                    ?: 0
                            spokenEnglish = ""
                            spokenScore = -1
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Mic, contentDescription = null)
                    Spacer(Modifier.size(8.dp))
                    Text(if (isListening) "듣고 있어요..." else "한국어로 말하기")
                }

                if (koreanSpeech.isNotBlank()) {
                    Text("내가 말한 내용", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    Text(koreanSpeech, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        if (koreanSpeech.isNotBlank() && matches.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("가까운 표현", fontWeight = FontWeight.Bold)
                    matches.forEach { pair ->
                        val expression = pair.first
                        val selected = expression.id == selectedExpression?.id
                        FilterChip(
                            selected = selected,
                            onClick = {
                                selectedExpressionId = expression.id
                                spokenEnglish = ""
                                spokenScore = -1
                            },
                            label = {
                                Text(expression.korean)
                            }
                        )
                    }
                }
            }
        }

        selectedExpression?.let { expression ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    Text("2 · 자연스러운 영어", fontWeight = FontWeight.Bold)
                    Text(expression.naturalEnglish, fontSize = 24.sp, fontWeight = FontWeight.Bold)

                    Text("다른 자연스러운 표현", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                    Text(expression.alternative, fontSize = 17.sp)

                    Text("영문 리듬", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                    Text(expression.rhythmEnglish, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)

                    Text("실제 소리", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                    Text(expression.soundKorean, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                    Button(
                        onClick = { speak(expression.naturalEnglish) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text("자연스러운 영어 듣기")
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    Text("3 · 영어로 직접 말하기", fontWeight = FontWeight.Bold)
                    Text("영어 문장을 보고 익힌 뒤, 그대로 말해보세요.")

                    Button(
                        onClick = {
                            listenEnglish { text ->
                                spokenEnglish = text
                                spokenScore = expressionSentenceMatch(
                                    spoken = text,
                                    target = expression.naturalEnglish
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Mic, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text(if (isListening) "듣고 있어요..." else "영어로 말하기")
                    }

                    if (spokenEnglish.isNotBlank()) {
                        Text("내가 말한 영어", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                        Text(spokenEnglish, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                        Text(
                            "문장 인식 일치도 " + spokenScore + "%",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            onComplete(expression.id)
                            koreanSpeech = ""
                            selectedExpressionId = 0
                            spokenEnglish = ""
                            spokenScore = -1
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (expression.id in completedIds)
                                "다시 연습 완료"
                            else
                                "표현 익힘 완료"
                        )
                    }
                }
            }
        }

        HorizontalDivider()

        Text("표현 골라서 연습", fontSize = 21.sp, fontWeight = FontWeight.Bold)

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(NaturalExpressionCatalog.categories) { item ->
                FilterChip(
                    selected = category == item,
                    onClick = { category = item },
                    label = { Text(item) }
                )
            }
        }

        NaturalExpressionCatalog.byCategory(category)
            .forEach { expression ->
                Card(
                    onClick = {
                        selectedExpressionId = expression.id
                        koreanSpeech = expression.korean
                        spokenEnglish = ""
                        spokenScore = -1
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(
                            expression.korean,
                            fontWeight = FontWeight.Bold
                        )
                        Text(expression.naturalEnglish)
                        if (expression.id in completedIds) {
                            Text("✓ 익힘", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                        }
                    }
                }
            }

        Text(
            "현재는 앱 안의 표현과 가장 가까운 문장을 찾아주는 방식입니다. 온라인 AI를 연결하면 같은 화면에서 자유로운 한국어 문장까지 확장할 수 있습니다.",
            fontSize = 12.sp
        )
    }
}

@Composable
private fun OfflineFreeTalkContent(
    isListening: Boolean,
    speak: (String) -> Unit,
    listenEnglish: ((String) -> Unit) -> Unit
) {
    val topics = listOf("인사", "오늘", "음식", "취미")
    var topic by rememberSaveable { mutableStateOf(topics.first()) }
    var coach by rememberSaveable { mutableStateOf(LocalConversationEngine.opening(topic)) }
    var userText by rememberSaveable { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("자유 대화", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("현재 버전은 서버 없이 동작하는 짧은 회화 코치입니다.")

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(topics) { item ->
                FilterChip(
                    selected = topic == item,
                    onClick = {
                        topic = item
                        coach = LocalConversationEngine.opening(item)
                        userText = ""
                    },
                    label = { Text(item) }
                )
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("상대", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text(coach, fontSize = 18.sp)
            }
        }

        Button(
            onClick = { speak(coach) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.VolumeUp, contentDescription = null)
            Spacer(Modifier.size(8.dp))
            Text("상대 말 듣기")
        }

        Button(
            onClick = {
                listenEnglish { text ->
                    userText = text
                    coach = LocalConversationEngine.reply(text)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Mic, contentDescription = null)
            Spacer(Modifier.size(8.dp))
            Text(if (isListening) "듣고 있어요..." else "내 생각 영어로 말하기")
        }

        if (userText.isNotBlank()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("나", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Text(userText, fontSize = 18.sp)
                }
            }
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("다음 질문", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Text(coach, fontSize = 18.sp)
                }
            }
        }
    }
}

private fun expressionSentenceMatch(spoken: String, target: String): Int {
    val a = normalizeExpressionEnglish(spoken)
    val b = normalizeExpressionEnglish(target)
    if (a.isBlank() || b.isBlank()) return 0

    val distance = expressionLevenshtein(a, b)
    return ((1f - distance.toFloat() / max(a.length, b.length).coerceAtLeast(1)) * 100)
        .toInt()
        .coerceIn(0, 100)
}

private fun normalizeExpressionEnglish(text: String): String =
    text.lowercase()
        .replace(Regex("[^a-z0-9' ]"), "")
        .replace(Regex("\\s+"), " ")
        .trim()

private fun expressionLevenshtein(a: String, b: String): Int {
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
