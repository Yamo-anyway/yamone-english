package com.yamone.english

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch
import kotlin.math.max

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF1B7F79),
                    secondary = Color(0xFFE66D8A),
                    primaryContainer = Color(0xFFD9F3EF),
                    secondaryContainer = Color(0xFFFFE2EA),
                    surface = Color(0xFFFFFBFD)
                )
            ) {
                YamoneEnglishApp()
            }
        }
    }
}

private enum class AppTab(val label: String) {
    TODAY("오늘"),
    LISTEN("듣기"),
    THINK("어순"),
    TALK("대화"),
    REVIEW("복습")
}

private class StudyStore(context: Context) {
    private val prefs = context.getSharedPreferences(PersistenceContract.STUDY_PREFS, Context.MODE_PRIVATE)

    private fun storedLessonIds(key: String): Set<Int> =
        StoredStateSanitizer.validLessonIds(
            prefs.getStringSet(key, emptySet()).orEmpty().mapNotNull { it.toIntOrNull() }.toSet()
        )

    fun completedIds(): Set<Int> = storedLessonIds(PersistenceContract.KEY_COMPLETED)

    fun reviewIds(): Set<Int> = storedLessonIds(PersistenceContract.KEY_REVIEW)

    fun markCompleted(id: Int) {
        if (LessonCatalog.byId(id) == null) return
        val next = completedIds().toMutableSet().apply { add(id) }
        prefs.edit()
            .putStringSet(PersistenceContract.KEY_COMPLETED, next.map { it.toString() }.toSet())
            .apply()
    }

    fun addReview(id: Int) {
        if (LessonCatalog.byId(id) == null) return
        val next = reviewIds().toMutableSet().apply { add(id) }
        prefs.edit()
            .putStringSet(PersistenceContract.KEY_REVIEW, next.map { it.toString() }.toSet())
            .apply()
    }

    fun clearReview(id: Int) {
        val next = reviewIds().toMutableSet().apply { remove(id) }
        prefs.edit()
            .putStringSet(PersistenceContract.KEY_REVIEW, next.map { it.toString() }.toSet())
            .apply()
    }

    fun thinkingCompletedIds(): Set<Int> =
        storedLessonIds(PersistenceContract.KEY_THINKING_COMPLETED)

    fun markThinkingCompleted(id: Int) {
        if (LessonCatalog.byId(id) == null) return
        val next = thinkingCompletedIds().toMutableSet().apply { add(id) }
        prefs.edit()
            .putStringSet(PersistenceContract.KEY_THINKING_COMPLETED, next.map { it.toString() }.toSet())
            .apply()
    }

    fun expressionCompletedIds(): Set<Int> =
        prefs.getStringSet(PersistenceContract.KEY_EXPRESSION_COMPLETED, emptySet()).orEmpty()
            .mapNotNull { it.toIntOrNull() }
            .toSet()

    fun markExpressionCompleted(id: Int) {
        if (NaturalExpressionCatalog.expressions.none { it.id == id }) return
        val next = expressionCompletedIds().toMutableSet().apply { add(id) }
        prefs.edit()
            .putStringSet(PersistenceContract.KEY_EXPRESSION_COMPLETED, next.map { it.toString() }.toSet())
            .apply()
    }

    fun speechRate(): Float = prefs.getFloat(PersistenceContract.KEY_SPEECH_RATE, 0.85f)
    fun setSpeechRate(value: Float) = prefs.edit().putFloat(PersistenceContract.KEY_SPEECH_RATE, value).apply()

    fun showKorean(): Boolean = prefs.getBoolean(PersistenceContract.KEY_SHOW_KOREAN, true)
    fun setShowKorean(value: Boolean) = prefs.edit().putBoolean(PersistenceContract.KEY_SHOW_KOREAN, value).apply()

    fun showSoundGuide(): Boolean = prefs.getBoolean(PersistenceContract.KEY_SHOW_SOUND_GUIDE, true)
    fun setShowSoundGuide(value: Boolean) = prefs.edit().putBoolean(PersistenceContract.KEY_SHOW_SOUND_GUIDE, value).apply()

    fun selectedCourse(): CourseLevel =
        runCatching {
            CourseLevel.valueOf(
                prefs.getString(PersistenceContract.KEY_SELECTED_COURSE, CourseLevel.AGE_5_7.name)
                    ?: CourseLevel.AGE_5_7.name
            )
        }.getOrDefault(CourseLevel.AGE_5_7)

    fun setSelectedCourse(value: CourseLevel) =
        prefs.edit().putString(PersistenceContract.KEY_SELECTED_COURSE, value.name).apply()
}

@Composable
private fun YamoneEnglishApp() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val store = remember { StudyStore(context) }
    val reviewStore = remember { UnifiedReviewStore(context) }
    val assessmentStore = remember { AssessmentStore(context) }
    val age810AssessmentStore = remember { Age810AssessmentStore(context) }
    val age1113AssessmentStore = remember { Age1113AssessmentStore(context) }
    val age1416AssessmentStore = remember { Age1416AssessmentStore(context) }
    val age1720AssessmentStore = remember { Age1720AssessmentStore(context) }
    val voice = remember { VoiceController(context) }
    val tts = remember { EnglishTts(context) }
    val listeningPlayer = remember { ListeningPlayer(context) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var tabIndex by rememberSaveable { mutableIntStateOf(AppTab.TODAY.ordinal) }
    var selectedCourse by rememberSaveable { mutableStateOf(store.selectedCourse()) }
    var selectedLesson by remember { mutableStateOf<Lesson?>(null) }
    var showAssessment by rememberSaveable { mutableStateOf(false) }
    var showSettings by rememberSaveable { mutableStateOf(false) }
    var latestAssessment by remember { mutableStateOf(assessmentStore.latest()) }
    var latestAge810Assessment by remember { mutableStateOf(age810AssessmentStore.latest()) }
    var latestAge1113Assessment by remember { mutableStateOf(age1113AssessmentStore.latest()) }
    var latestAge1416Assessment by remember { mutableStateOf(age1416AssessmentStore.latest()) }
    var latestAge1720Assessment by remember { mutableStateOf(age1720AssessmentStore.latest()) }
    var completed by remember { mutableStateOf(store.completedIds()) }
    var reviewIds by remember { mutableStateOf(store.reviewIds()) }
    var unifiedReviewItems by remember { mutableStateOf(reviewStore.items()) }
    var thinkingCompleted by remember { mutableStateOf(store.thinkingCompletedIds()) }
    var expressionCompleted by remember { mutableStateOf(store.expressionCompletedIds()) }
    var speechRate by remember { mutableFloatStateOf(store.speechRate()) }
    var showKorean by remember { mutableStateOf(store.showKorean()) }
    var showSoundGuide by remember { mutableStateOf(store.showSoundGuide()) }
    var isListening by remember { mutableStateOf(false) }
    var micGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    var pendingVoiceAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    val safeTabIndex = AppNavigationState.sanitizeTabIndex(tabIndex, AppTab.entries.size)

    LaunchedEffect(tabIndex, safeTabIndex) {
        if (tabIndex != safeTabIndex) tabIndex = safeTabIndex
    }

    BackHandler(
        enabled = showSettings || showAssessment || selectedLesson != null || safeTabIndex != AppTab.TODAY.ordinal
    ) {
        when {
            showSettings -> showSettings = false
            showAssessment -> showAssessment = false
            selectedLesson != null -> selectedLesson = null
            else -> tabIndex = AppTab.TODAY.ordinal
        }
    }

    val showMessage: (String) -> Unit = { message ->
        scope.launch { snackbarHostState.showSnackbar(message) }
    }

    val speakEnglish: (String) -> Unit = { text ->
        if (!tts.speak(text, speechRate)) {
            showMessage("영어 음성을 재생할 수 없습니다. 기기의 TTS 영어 음성 데이터를 확인해 주세요.")
        }
    }

    val micLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        micGranted = granted
        if (granted) pendingVoiceAction?.invoke()
        pendingVoiceAction = null
        if (!granted) {
            showMessage("말하기 연습에는 마이크 권한이 필요합니다.")
        }
    }

    val startListening: ((String) -> Unit) -> Unit = { onResult ->
        val action = {
            isListening = true
            voice.start(
                locale = "en-US",
                onResult = {
                    isListening = false
                    onResult(it)
                },
                onError = {
                    isListening = false
                    showMessage(it)
                }
            )
        }
        if (micGranted) {
            action()
        } else {
            pendingVoiceAction = action
            micLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    val startKoreanListening: ((String) -> Unit) -> Unit = { onResult ->
        val action = {
            isListening = true
            voice.start(
                locale = "ko-KR",
                onResult = {
                    isListening = false
                    onResult(it)
                },
                onError = {
                    isListening = false
                    showMessage(it)
                }
            )
        }
        if (micGranted) {
            action()
        } else {
            pendingVoiceAction = action
            micLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            voice.destroy()
            tts.shutdown()
            listeningPlayer.shutdown()
        }
    }

    if (showSettings) {
        SettingsScreen(
            speechRate = speechRate,
            showKorean = showKorean,
            showSoundGuide = showSoundGuide,
            onSpeechRateChange = {
                speechRate = it
                store.setSpeechRate(it)
            },
            onShowKoreanChange = {
                showKorean = it
                store.setShowKorean(it)
            },
            onShowSoundGuideChange = {
                showSoundGuide = it
                store.setShowSoundGuide(it)
            },
            onBack = { showSettings = false }
        )
        return
    }

    if (showAssessment) {
        val reviewCallback: (Int, ReviewKind, Boolean) -> Unit = { id, kind, success ->
            if (success) reviewStore.recordSuccess(id, kind)
            else reviewStore.recordError(id, kind)
            unifiedReviewItems = reviewStore.items()
        }

        when (selectedCourse) {
            CourseLevel.AGE_5_7 -> Age57AssessmentScreen(
                isListening = isListening,
                speak = speakEnglish,
                listen = startListening,
                onReviewResult = reviewCallback,
                onFinish = { summary ->
                    assessmentStore.save(summary)
                    latestAssessment = assessmentStore.latest()
                    showAssessment = false
                },
                onBack = { showAssessment = false }
            )

            CourseLevel.AGE_8_10 -> Age810AssessmentScreen(
                isListening = isListening,
                speak = speakEnglish,
                listen = startListening,
                onReviewResult = reviewCallback,
                onFinish = { summary ->
                    age810AssessmentStore.save(summary)
                    latestAge810Assessment = age810AssessmentStore.latest()
                    showAssessment = false
                },
                onBack = { showAssessment = false }
            )

            CourseLevel.AGE_11_13 -> Age1113AssessmentScreen(
                isListening = isListening,
                speak = speakEnglish,
                listen = startListening,
                onReviewResult = reviewCallback,
                onFinish = { summary ->
                    age1113AssessmentStore.save(summary)
                    latestAge1113Assessment = age1113AssessmentStore.latest()
                    showAssessment = false
                },
                onBack = { showAssessment = false }
            )

            CourseLevel.AGE_14_16 -> Age1416AssessmentScreen(
                isListening = isListening,
                speak = speakEnglish,
                listen = startListening,
                onReviewResult = reviewCallback,
                onFinish = { summary ->
                    age1416AssessmentStore.save(summary)
                    latestAge1416Assessment = age1416AssessmentStore.latest()
                    showAssessment = false
                },
                onBack = { showAssessment = false }
            )

            CourseLevel.AGE_17_20 -> Age1720AssessmentScreen(
                isListening = isListening,
                speak = speakEnglish,
                listen = startListening,
                onReviewResult = reviewCallback,
                onFinish = { summary ->
                    age1720AssessmentStore.save(summary)
                    latestAge1720Assessment = age1720AssessmentStore.latest()
                    showAssessment = false
                },
                onBack = { showAssessment = false }
            )
        }
        return
    }

    if (selectedLesson != null) {
        LessonScreen(
            lesson = selectedLesson!!,
            showKorean = showKorean,
            showSoundGuide = showSoundGuide,
            isListening = isListening,
            speak = speakEnglish,
            listen = startListening,
            onBack = { selectedLesson = null },
            onSpeakingResult = { id, success ->
                if (success) {
                    reviewStore.recordSuccess(id, ReviewKind.SPEAKING)
                } else {
                    reviewStore.recordError(id, ReviewKind.SPEAKING)
                    store.addReview(id)
                    reviewIds = store.reviewIds()
                }
                unifiedReviewItems = reviewStore.items()
            },
            onComplete = { id ->
                store.markCompleted(id)
                store.clearReview(id)
                completed = store.completedIds()
                reviewIds = store.reviewIds()
            },
            onOpenNext = { id ->
                val current = LessonCatalog.byId(id)
                selectedLesson = if (current == null) {
                    null
                } else {
                    CourseCatalog.lessons(current.course)
                        .sortedBy { it.courseLessonNumber }
                        .firstOrNull { it.courseLessonNumber > current.courseLessonNumber }
                }
            }
        )
        return
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar {
                AppTab.entries.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = safeTabIndex == index,
                        onClick = { tabIndex = index },
                        icon = {
                            Icon(
                                imageVector = when (tab) {
                                    AppTab.TODAY -> Icons.Default.Home
                                    AppTab.LISTEN -> Icons.Default.VolumeUp
                                    AppTab.THINK -> Icons.Default.SwapHoriz
                                    AppTab.TALK -> Icons.Default.Mic
                                    AppTab.REVIEW -> Icons.Default.Refresh
                                },
                                contentDescription = tab.label
                            )
                        },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        when (AppTab.entries[safeTabIndex]) {
            AppTab.TODAY -> TodayScreen(
                modifier = Modifier.padding(padding),
                course = selectedCourse,
                completed = completed,
                thinkingCompleted = thinkingCompleted,
                expressionCompletedCount = expressionCompleted.size,
                reviewDueCount = unifiedReviewItems.count {
                    it.isDue && LessonCatalog.byId(it.lessonId)?.course == selectedCourse
                },
                latestAssessment = latestAssessment,
                latestAge810Assessment = latestAge810Assessment,
                latestAge1113Assessment = latestAge1113Assessment,
                latestAge1416Assessment = latestAge1416Assessment,
                latestAge1720Assessment = latestAge1720Assessment,
                onCourseChange = { course ->
                    selectedCourse = course
                    store.setSelectedCourse(course)
                },
                onOpenLesson = { selectedLesson = it },
                onOpenAssessment = { showAssessment = true },
                onOpenListening = { tabIndex = AppTab.LISTEN.ordinal },
                onOpenThinking = { tabIndex = AppTab.THINK.ordinal },
                onOpenTalk = { tabIndex = AppTab.TALK.ordinal },
                onOpenReview = { tabIndex = AppTab.REVIEW.ordinal },
                onOpenSettings = { showSettings = true }
            )

            AppTab.LISTEN -> EnhancedListenScreen(
                modifier = Modifier.padding(padding),
                course = selectedCourse,
                player = listeningPlayer,
                speechRate = speechRate,
                onMessage = showMessage,
                onReviewResult = { id, kind, success ->
                    if (success) reviewStore.recordSuccess(id, kind)
                    else reviewStore.recordError(id, kind)
                    unifiedReviewItems = reviewStore.items()
                }
            )

            AppTab.THINK -> ThinkingTrainingScreen(
                modifier = Modifier.padding(padding),
                course = selectedCourse,
                completedIds = CourseProgressResolver.completedIds(selectedCourse, thinkingCompleted),
                isListening = isListening,
                speak = speakEnglish,
                listen = startListening,
                onComplete = { id ->
                    store.markThinkingCompleted(id)
                    thinkingCompleted = store.thinkingCompletedIds()
                },
                onOrderResult = { id, success ->
                    if (success) reviewStore.recordSuccess(id, ReviewKind.ORDER)
                    else reviewStore.recordError(id, ReviewKind.ORDER)
                    unifiedReviewItems = reviewStore.items()
                },
                onSpeakingResult = { id, success ->
                    if (success) reviewStore.recordSuccess(id, ReviewKind.SPEAKING)
                    else reviewStore.recordError(id, ReviewKind.SPEAKING)
                    unifiedReviewItems = reviewStore.items()
                }
            )

            AppTab.TALK -> ExpressionTalkScreen(
                modifier = Modifier.padding(padding),
                completedIds = expressionCompleted,
                isListening = isListening,
                speak = speakEnglish,
                listenKorean = startKoreanListening,
                listenEnglish = startListening,
                onComplete = { id ->
                    store.markExpressionCompleted(id)
                    expressionCompleted = store.expressionCompletedIds()
                }
            )

            AppTab.REVIEW -> UnifiedReviewScreen(
                modifier = Modifier.padding(padding),
                course = selectedCourse,
                reviewItems = unifiedReviewItems.filter {
                    LessonCatalog.byId(it.lessonId)?.course == selectedCourse
                },
                isListening = isListening,
                speak = speakEnglish,
                listen = startListening,
                onResult = { id, kind, success ->
                    if (success) reviewStore.recordSuccess(id, kind)
                    else reviewStore.recordError(id, kind)
                    unifiedReviewItems = reviewStore.items()
                }
            )
        }
    }
}

@Composable
private fun TodayScreen(
    modifier: Modifier = Modifier,
    course: CourseLevel,
    completed: Set<Int>,
    thinkingCompleted: Set<Int>,
    expressionCompletedCount: Int,
    reviewDueCount: Int,
    latestAssessment: AssessmentSummary?,
    latestAge810Assessment: Age810AssessmentSummary?,
    latestAge1113Assessment: Age1113AssessmentSummary?,
    latestAge1416Assessment: Age1416AssessmentSummary?,
    latestAge1720Assessment: Age1720AssessmentSummary?,
    onCourseChange: (CourseLevel) -> Unit,
    onOpenLesson: (Lesson) -> Unit,
    onOpenAssessment: () -> Unit,
    onOpenListening: () -> Unit,
    onOpenThinking: () -> Unit,
    onOpenTalk: () -> Unit,
    onOpenReview: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val courseLessons = CourseCatalog.lessons(course).sortedBy { it.courseLessonNumber }
    val courseSections = CourseCatalog.sections(course)

    if (courseLessons.isEmpty() || courseSections.isEmpty()) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Yamone English", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Text("선택한 과정 데이터를 불러올 수 없습니다.")
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(CourseLevel.entries) { item ->
                        FilterChip(
                            selected = course == item,
                            onClick = { onCourseChange(item) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
        return
    }

    val progress = CourseProgressResolver.resolve(course, completed)
    val completedInCourse = CourseProgressResolver.completedIds(course, completed)
    val thinkingCompletedInCourse = CourseProgressResolver.completedIds(course, thinkingCompleted)
    val focusLesson = progress.nextLesson ?: courseLessons.last()
    val initialSectionIndex = CourseCatalog.sectionIndexForLesson(course, focusLesson.id)
        .coerceIn(courseSections.indices)
    var sectionIndex by rememberSaveable(course.name) {
        mutableIntStateOf(initialSectionIndex)
    }
    if (sectionIndex !in courseSections.indices) sectionIndex = initialSectionIndex
    val selectedSection = courseSections[sectionIndex]
    val sectionLessons = selectedSection.lessons().filter { it.course == course }

    val assessmentTitle: String
    val assessmentSubtitle: String
    val assessmentResult: String?
    when (course) {
        CourseLevel.AGE_5_7 -> {
            assessmentTitle = "5~7세 과정 테스트"
            assessmentSubtitle = "듣기 · 말하기 · 어순 · 상황 대응 16문항"
            assessmentResult = latestAssessment?.let {
                "최근 결과 ${it.totalCorrect} / ${it.totalQuestions} · ${it.overallLabel}"
            }
        }

        CourseLevel.AGE_8_10 -> {
            assessmentTitle = "8~10세 과정 테스트"
            assessmentSubtitle = "듣기 · 말하기 · 어순 · 상황·추론 20문항"
            assessmentResult = latestAge810Assessment?.let {
                "최근 결과 ${it.totalCorrect} / ${it.totalQuestions} · ${it.overallLabel}"
            }
        }

        CourseLevel.AGE_11_13 -> {
            assessmentTitle = "11~13세 과정 테스트"
            assessmentSubtitle = "듣기 · 말하기 · 어순 · 상황·판단 24문항"
            assessmentResult = latestAge1113Assessment?.let {
                "최근 결과 ${it.totalCorrect} / ${it.totalQuestions} · ${it.overallLabel}"
            }
        }

        CourseLevel.AGE_14_16 -> {
            assessmentTitle = "14~16세 과정 테스트"
            assessmentSubtitle = "듣기 · 말하기 · 어순 · 상황·판단 28문항"
            assessmentResult = latestAge1416Assessment?.let {
                "최근 결과 ${it.totalCorrect} / ${it.totalQuestions} · ${it.overallLabel}"
            }
        }

        CourseLevel.AGE_17_20 -> {
            assessmentTitle = "17~20세 과정 테스트"
            assessmentSubtitle = "듣기 · 말하기 · 어순 · 상황·판단 28문항"
            assessmentResult = latestAge1720Assessment?.let {
                "최근 결과 ${it.totalCorrect} / ${it.totalQuestions} · ${it.overallLabel}"
            }
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Yamone English", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text(
                "들리면, 말할 수 있어요.",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(18.dp))
            Text("과정 선택", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(CourseLevel.entries) { item ->
                    FilterChip(
                        selected = course == item,
                        onClick = { onCourseChange(item) },
                        label = { Text(item.label) }
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                "영어권 ${course.label} 회화 · ${course.description}",
                fontWeight = FontWeight.Bold
            )
            Text("${progress.totalLessons}개 표현을 듣고, 이해하고, 직접 말합니다.")
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = progress.completedLessons.toFloat() / progress.totalLessons.coerceAtLeast(1).toFloat(),
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${progress.completedLessons} / ${progress.totalLessons} 완료")
                TextButton(onClick = onOpenSettings) {
                    Icon(Icons.Default.Settings, contentDescription = null)
                    Spacer(Modifier.size(4.dp))
                    Text("설정")
                }
            }
        }

        item {
            if (progress.isComplete) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    onClick = onOpenAssessment,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text("과정 학습 완료", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "${course.label} 100개 레슨을 모두 완료했어요.",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("이제 과정 종료 테스트로 듣기·말하기·어순·상황 대응을 확인해보세요.")
                        Spacer(Modifier.height(8.dp))
                        Text("과정 테스트 열기", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                progress.nextLesson?.let { next ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        onClick = { onOpenLesson(next) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(18.dp)) {
                            Text("이어서 학습", fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(6.dp))
                            Text(
                                "${next.emoji} ${next.courseLessonNumber}. ${next.title}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(next.target, fontSize = 18.sp)
                            Spacer(Modifier.height(10.dp))
                            Text("남은 레슨 ${progress.remainingLessons}개 · 듣기 → 이해 → 말하기 → 내 표현 → 대화")
                        }
                    }
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("오늘 할 일", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "레슨 ${progress.completedLessons}/${progress.totalLessons}" +
                            " · 어순 ${thinkingCompletedInCourse.size}/${progress.totalLessons}" +
                            " · 내 표현(공통) $expressionCompletedCount/${NaturalExpressionCatalog.expressions.size}",
                        fontSize = 13.sp
                    )

                    if (reviewDueCount > 0) {
                        Button(
                            onClick = onOpenReview,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("오늘 복습 ${reviewDueCount}개 먼저 하기")
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = onOpenListening,
                            modifier = Modifier.weight(1f)
                        ) { Text("듣기") }
                        OutlinedButton(
                            onClick = onOpenThinking,
                            modifier = Modifier.weight(1f)
                        ) { Text("어순") }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = onOpenTalk,
                            modifier = Modifier.weight(1f)
                        ) { Text("내 표현") }
                        OutlinedButton(
                            onClick = onOpenReview,
                            modifier = Modifier.weight(1f)
                        ) { Text("복습") }
                    }
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                onClick = onOpenAssessment,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(assessmentTitle, fontWeight = FontWeight.Bold)
                    Text(assessmentSubtitle)
                    Spacer(Modifier.height(8.dp))
                    if (assessmentResult != null) {
                        Text(
                            assessmentResult,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        if (!progress.isComplete) {
                            Spacer(Modifier.height(4.dp))
                            Text("기존 테스트 결과는 유지됩니다. 현재 레슨 진행과 별도로 다시 응시할 수 있어요.", fontSize = 12.sp)
                        }
                    } else if (progress.isComplete) {
                        Text(
                            "100개 레슨을 모두 마쳤어요. 지금 실력을 확인해보세요.",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text("레슨 ${progress.completedLessons}/100 · 100개 완료 후 응시를 권장합니다.")
                        Text("원하면 지금도 테스트를 미리 확인할 수 있어요.", fontSize = 12.sp)
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(4.dp))
            Text("레슨 찾기", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(courseSections, key = { it.id }) { section ->
                    val index = courseSections.indexOf(section)
                    FilterChip(
                        selected = sectionIndex == index,
                        onClick = { sectionIndex = index },
                        label = { Text(section.title) }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(selectedSection.title, fontWeight = FontWeight.Bold)
        }

        if (sectionLessons.isEmpty()) {
            item {
                Text("이 구간의 레슨을 불러올 수 없습니다.")
            }
        } else {
            items(sectionLessons, key = { it.id }) { lesson ->
                LessonCard(
                    lesson = lesson,
                    completed = lesson.id in completedInCourse,
                    onClick = { onOpenLesson(lesson) }
                )
            }
        }
    }
}

@Composable
private fun LessonCard(lesson: Lesson, completed: Boolean, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(lesson.emoji, fontSize = 28.sp)
            Column(Modifier.weight(1f)) {
                Text(
                    "${lesson.courseLessonNumber}. ${lesson.title}",
                    fontWeight = FontWeight.Bold
                )
                Text(lesson.target)
            }
            Text(if (completed) "완료" else "학습", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LessonScreen(
    lesson: Lesson,
    showKorean: Boolean,
    showSoundGuide: Boolean,
    isListening: Boolean,
    speak: (String) -> Unit,
    listen: ((String) -> Unit) -> Unit,
    onBack: () -> Unit,
    onSpeakingResult: (Int, Boolean) -> Unit,
    onComplete: (Int) -> Unit,
    onOpenNext: (Int) -> Unit
) {
    var stage by rememberSaveable(lesson.id) { mutableIntStateOf(0) }
    var reveal by rememberSaveable(lesson.id) { mutableStateOf(false) }
    var recognized by remember { mutableStateOf("") }
    var matchScore by remember { mutableIntStateOf(-1) }
    var coachReply by remember { mutableStateOf("") }
    val lessonScrollState = rememberScrollState()
    val hasNextLesson = CourseCatalog.lessons(lesson.course)
        .any { it.courseLessonNumber > lesson.courseLessonNumber }

    LaunchedEffect(stage, lesson.id) {
        lessonScrollState.scrollTo(0)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${lesson.emoji} ${lesson.title}") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("닫기") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
                .verticalScroll(lessonScrollState),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            LinearProgressIndicator(
                progress = (stage + 1) / 6f,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                when (stage) {
                    0 -> "1 · 먼저 듣기"
                    1 -> "2 · 의미와 소리"
                    2 -> "3 · 따라 말하기"
                    3 -> "4 · 내 말로 바꾸기"
                    4 -> "5 · 짧게 대화하기"
                    else -> "완료"
                },
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            when (stage) {
                0 -> {
                    BigGuide("문장을 보지 않고 먼저 들어보세요.", "소리 덩어리와 리듬에 집중합니다.")
                    Button(onClick = { speak(lesson.target) }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text("자연스러운 속도로 듣기")
                    }
                    OutlinedButton(
                        onClick = { reveal = !reveal },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (reveal) "문장 숨기기" else "문장 보기")
                    }
                    if (reveal) PhraseCard(lesson.target, if (showKorean) lesson.meaning else null)
                }

                1 -> {
                    PhraseCard(lesson.target, if (showKorean) lesson.meaning else null)
                    if (showSoundGuide) PronunciationGuideCard(lesson)
                    InfoCard("상황", lesson.situation)
                    Button(onClick = { speak(lesson.target) }, modifier = Modifier.fillMaxWidth()) {
                        Text("다시 듣기")
                    }
                }

                2 -> {
                    PhraseCard(lesson.target, if (showKorean) lesson.meaning else null)
                    if (showSoundGuide) PronunciationGuideCard(lesson)
                    BigGuide("이제 그대로 말해보세요.", "문장 전체를 한 호흡으로 말하는 데 집중합니다.")
                    Button(
                        onClick = {
                            listen { text ->
                                recognized = text
                                matchScore = sentenceMatch(text, listOf(lesson.target))
                                onSpeakingResult(lesson.id, matchScore >= 75)
                            }
                        },
                        enabled = !isListening,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Mic, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text(if (isListening) "듣고 있어요..." else "말하기")
                    }
                    if (recognized.isNotBlank()) {
                        InfoCard("들린 문장", recognized)
                        Text(
                            "문장 인식 일치도 $matchScore%",
                            fontWeight = FontWeight.Bold,
                            color = if (matchScore >= 70) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                        )
                        Text("※ 발음 점수가 아니라 음성 인식 결과와 목표 문장의 일치 정도입니다.", fontSize = 12.sp)
                    }
                }

                3 -> {
                    if (showKorean) InfoCard("이번에는 내 말로", lesson.ownPromptKo)
                    else BigGuide("같은 패턴으로 내 문장을 만들어 말해보세요.", null)
                    Button(
                        onClick = {
                            listen { text -> recognized = text }
                        },
                        enabled = !isListening,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Mic, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text(if (isListening) "듣고 있어요..." else "내 문장 말하기")
                    }
                    if (recognized.isNotBlank()) {
                        InfoCard("내가 말한 영어", recognized)
                        if (lesson.accepted.isNotEmpty()) {
                            InfoCard("자연스러운 예", lesson.accepted.joinToString("  /  "))
                            lesson.accepted.firstOrNull()?.let { example ->
                                OutlinedButton(
                                    onClick = { speak(example) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("예문 듣기")
                                }
                            }
                        }
                    }
                }

                4 -> {
                    BigGuide("외운 문장으로 끝내지 않고 대화를 이어갑니다.", null)
                    DialoguePreviewCard(lesson)
                    InfoCard("상대", lesson.coachLine)
                    Button(
                        onClick = { speak(lesson.coachLine) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text("상대 말 듣기")
                    }
                    Button(
                        onClick = {
                            listen { text ->
                                recognized = text
                                coachReply = LocalConversationEngine.reply(text)
                            }
                        },
                        enabled = !isListening,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Mic, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text(if (isListening) "듣고 있어요..." else "영어로 대답하기")
                    }
                    if (recognized.isNotBlank()) {
                        InfoCard("나", recognized)
                        if (coachReply.isNotBlank()) {
                            InfoCard("상대", coachReply)
                            OutlinedButton(
                                onClick = { speak(coachReply) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("대답 듣기")
                            }
                        }
                    }
                }

                else -> {
                    Spacer(Modifier.height(24.dp))
                    Text("잘했어요!", fontSize = 34.sp, fontWeight = FontWeight.Bold)
                    Text("이 표현은 완료 처리됐습니다. 나중에 다른 대화 속에서 다시 만나게 됩니다.")
                    Button(
                        onClick = {
                            if (hasNextLesson) onOpenNext(lesson.id)
                            else onBack()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (hasNextLesson) "다음 레슨" else "홈으로")
                    }
                    OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                        Text("홈으로")
                    }
                }
            }

            if (stage < 5) {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { if (stage > 0) stage-- else onBack() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("이전")
                    }
                    Button(
                        onClick = {
                            if (stage == 4) onComplete(lesson.id)
                            stage++
                            recognized = ""
                            matchScore = -1
                            coachReply = ""
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (stage == 4) "학습 완료" else "다음")
                    }
                }
            }
            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
private fun BigGuide(title: String, subtitle: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        if (subtitle != null) {
            Spacer(Modifier.height(6.dp))
            Text(subtitle, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun PhraseCard(english: String, korean: String?) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(english, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            if (korean != null) {
                Spacer(Modifier.height(6.dp))
                Text(korean, fontSize = 17.sp)
            }
        }
    }
}

@Composable
private fun PronunciationGuideCard(lesson: Lesson) {
    val guide = PronunciationQaCatalog.byLessonId(lesson.id)

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("소리 가이드", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)

            Text("영문 리듬", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
            Text(guide.rhythmEnglish, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)

            Text("천천히", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
            Text(guide.slowKorean, fontSize = 18.sp)

            Text("실제 소리", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
            Text(guide.naturalKorean, fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Text("강약 · 억양", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
            Text(guide.rhythm, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun InfoCard(label: String, text: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(label, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(text, fontSize = 17.sp)
        }
    }
}

@Composable
private fun DialoguePreviewCard(lesson: Lesson) {
    val dialogue = DialogueCatalog.byLessonId(lesson.id) ?: return
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Text("이 레슨의 실제 대화", fontWeight = FontWeight.Bold)
            dialogue.lines.forEach { line ->
                Text("${line.speaker}  ${line.english}", fontWeight = FontWeight.SemiBold)
                Text("    ${line.korean}", fontSize = 14.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    modifier: Modifier = Modifier,
    speechRate: Float,
    showKorean: Boolean,
    showSoundGuide: Boolean,
    onSpeechRateChange: (Float) -> Unit,
    onShowKoreanChange: (Boolean) -> Unit,
    onShowSoundGuideChange: (Boolean) -> Unit,
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("설정") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("닫기") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("설정", fontSize = 30.sp, fontWeight = FontWeight.Bold)

            Text("듣기 속도", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    0.7f to "천천히",
                    0.85f to "학습",
                    1.0f to "자연스럽게",
                    1.12f to "실제 빠름"
                ).forEach { pair ->
                    val rate = pair.first
                    val label = pair.second
                    FilterChip(
                        selected = speechRate == rate,
                        onClick = { onSpeechRateChange(rate) },
                        label = { Text(label) }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text("한국어 도움말", fontWeight = FontWeight.Bold)
                    Text("뜻과 한국어 미션을 함께 표시")
                }
                Switch(checked = showKorean, onCheckedChange = onShowKoreanChange)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text("한글 발음·리듬 가이드", fontWeight = FontWeight.Bold)
                    Text("강세, 붙여읽기, 높낮이와 한국어식 소리 표시")
                }
                Switch(checked = showSoundGuide, onCheckedChange = onShowSoundGuideChange)
            }

            HorizontalDivider()
            Text("추후 활성화 준비", fontWeight = FontWeight.Bold)
            FutureFeatureRow("로그인 / 기기 동기화", FeatureFlags.AUTH_ENABLED)
            FutureFeatureRow("광고 / 광고 제거", FeatureFlags.ADS_ENABLED)
            FutureFeatureRow("콘텐츠 업데이트", FeatureFlags.CONTENT_UPDATE_ENABLED)
            FutureFeatureRow("온라인 AI 회화", FeatureFlags.ONLINE_AI_ENABLED)

            HorizontalDivider()
            Text("Yamone English v${BuildConfig.VERSION_NAME}")
            Text("현재 콘텐츠와 학습 기록은 앱/기기 내부를 중심으로 사용합니다.", fontSize = 12.sp)
        }
    }
}

@Composable
private fun FutureFeatureRow(name: String, enabled: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name, modifier = Modifier.weight(1f))
        Text(if (enabled) "사용" else "준비됨 · 비활성", color = MaterialTheme.colorScheme.primary)
    }
}

private fun sentenceMatch(spoken: String, targets: List<String>): Int {
    if (spoken.isBlank()) return 0
    val normalizedSpoken = normalize(spoken)
    return targets.maxOfOrNull { target ->
        val normalizedTarget = normalize(target)
        if (normalizedTarget.isBlank()) return@maxOfOrNull 0
        val distance = levenshtein(normalizedSpoken, normalizedTarget)
        ((1f - distance.toFloat() / max(normalizedSpoken.length, normalizedTarget.length).coerceAtLeast(1)) * 100)
            .toInt()
            .coerceIn(0, 100)
    } ?: 0
}

private fun normalize(text: String): String =
    text.lowercase()
        .replace(Regex("[^a-z0-9 ]"), "")
        .replace(Regex("\\s+"), " ")
        .trim()

private fun levenshtein(a: String, b: String): Int {
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
