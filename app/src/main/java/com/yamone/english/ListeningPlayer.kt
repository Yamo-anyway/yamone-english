package com.yamone.english

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import java.util.Locale

enum class ListenMode(val label: String) {
    ENGLISH_ONLY("영어만"),
    ENGLISH_KOREAN("영어 → 해석"),
    ENGLISH_KOREAN_ENGLISH("영어 → 해석 → 영어")
}

enum class ListenVoiceMode(val label: String) {
    STANDARD("기본"),
    ALTERNATE("다른 목소리"),
    DIALOGUE_AB("A·B 교차")
}

data class ListeningSegment(
    val lessonId: Int,
    val speaker: String?,
    val text: String,
    val languageTag: String,
    val part: String
)

object ListeningPlaylistBuilder {
    fun lesson(lesson: Lesson, mode: ListenMode): List<ListeningSegment> {
        val result = mutableListOf<ListeningSegment>()

        appendPair(
            result = result,
            lessonId = lesson.id,
            speaker = null,
            english = lesson.target,
            korean = lesson.meaning,
            part = "핵심 표현",
            mode = mode
        )

        DialogueCatalog.byLessonId(lesson.id)?.lines.orEmpty().forEach { line ->
            appendPair(
                result = result,
                lessonId = lesson.id,
                speaker = line.speaker,
                english = line.english,
                korean = line.korean,
                part = "대화",
                mode = mode
            )
        }
        return result
    }

    fun all(mode: ListenMode): List<ListeningSegment> =
        LessonCatalog.lessons.flatMap { lesson(it, mode) }

    private fun appendPair(
        result: MutableList<ListeningSegment>,
        lessonId: Int,
        speaker: String?,
        english: String,
        korean: String,
        part: String,
        mode: ListenMode
    ) {
        val englishSegment = ListeningSegment(
            lessonId = lessonId,
            speaker = speaker,
            text = english,
            languageTag = "en-US",
            part = part
        )
        val koreanSegment = ListeningSegment(
            lessonId = lessonId,
            speaker = speaker,
            text = korean,
            languageTag = "ko-KR",
            part = "해석"
        )

        when (mode) {
            ListenMode.ENGLISH_ONLY -> result += englishSegment
            ListenMode.ENGLISH_KOREAN -> {
                result += englishSegment
                result += koreanSegment
            }
            ListenMode.ENGLISH_KOREAN_ENGLISH -> {
                result += englishSegment
                result += koreanSegment
                result += englishSegment
            }
        }
    }
}

class ListeningPlayer(context: Context) {
    private val mainHandler = Handler(Looper.getMainLooper())
    private var tts: TextToSpeech? = null
    private var ready = false
    private var segments: List<ListeningSegment> = emptyList()
    private var index = 0
    private var englishRate = 0.85f
    private var voiceMode = ListenVoiceMode.STANDARD
    private var token = 0L
    private var englishVoices: List<Voice> = emptyList()
    private var koreanVoices: List<Voice> = emptyList()
    private var onSegmentChanged: ((Int, ListeningSegment) -> Unit)? = null
    private var onComplete: (() -> Unit)? = null
    private var onError: ((String) -> Unit)? = null

    init {
        tts = TextToSpeech(context) { status ->
            ready = status == TextToSpeech.SUCCESS
            if (!ready) {
                mainHandler.post { onError?.invoke("음성 재생기를 시작할 수 없습니다.") }
                return@TextToSpeech
            }

            refreshVoices()

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) = Unit

                override fun onDone(utteranceId: String?) {
                    val expectedToken = utteranceId?.substringBefore("-")?.toLongOrNull() ?: return
                    if (expectedToken != token) return

                    val previousLesson = segments.getOrNull(index)?.lessonId
                    index += 1
                    val nextLesson = segments.getOrNull(index)?.lessonId
                    val pause = if (
                        previousLesson != null &&
                        nextLesson != null &&
                        previousLesson != nextLesson
                    ) 850L else 260L

                    mainHandler.postDelayed({ speakCurrent(expectedToken) }, pause)
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    mainHandler.post {
                        this@ListeningPlayer.onError?.invoke("연속 재생 중 음성 출력 오류가 발생했습니다.")
                        stop()
                    }
                }

                override fun onError(utteranceId: String?, errorCode: Int) {
                    onError(utteranceId)
                }
            })

            if (segments.isNotEmpty()) {
                mainHandler.post { speakCurrent(token) }
            }
        }
    }

    fun play(
        newSegments: List<ListeningSegment>,
        rate: Float,
        voiceMode: ListenVoiceMode = ListenVoiceMode.STANDARD,
        onSegmentChanged: (Int, ListeningSegment) -> Unit,
        onComplete: () -> Unit,
        onError: (String) -> Unit
    ) {
        stopInternal(clearCallbacks = false)
        token += 1
        segments = newSegments
        index = 0
        englishRate = rate
        this.voiceMode = voiceMode
        this.onSegmentChanged = onSegmentChanged
        this.onComplete = onComplete
        this.onError = onError

        if (segments.isEmpty()) {
            onComplete()
            return
        }

        if (ready) {
            refreshVoices()
            speakCurrent(token)
        }
    }

    fun stop() {
        stopInternal(clearCallbacks = true)
    }

    private fun stopInternal(clearCallbacks: Boolean) {
        token += 1
        tts?.stop()
        segments = emptyList()
        index = 0

        if (clearCallbacks) {
            onSegmentChanged = null
            onComplete = null
            onError = null
        }
    }

    private fun refreshVoices() {
        val voices = tts?.voices.orEmpty()
        englishVoices = voices
            .filter { it.locale.language.equals("en", ignoreCase = true) }
            .sortedWith(
                compareBy<Voice> { it.isNetworkConnectionRequired }
                    .thenByDescending { it.quality }
                    .thenBy { it.name }
            )

        koreanVoices = voices
            .filter { it.locale.language.equals("ko", ignoreCase = true) }
            .sortedWith(
                compareBy<Voice> { it.isNetworkConnectionRequired }
                    .thenByDescending { it.quality }
                    .thenBy { it.name }
            )
    }

    private fun speakCurrent(expectedToken: Long) {
        if (expectedToken != token) return

        val segment = segments.getOrNull(index)
        if (segment == null) {
            val completed = onComplete
            stopInternal(clearCallbacks = true)
            completed?.invoke()
            return
        }

        val engine = tts ?: return
        val locale = Locale.forLanguageTag(segment.languageTag)
        val languageResult = engine.setLanguage(locale)

        if (
            languageResult == TextToSpeech.LANG_MISSING_DATA ||
            languageResult == TextToSpeech.LANG_NOT_SUPPORTED
        ) {
            onError?.invoke(
                if (segment.languageTag.startsWith("ko")) "기기에 한국어 TTS 음성이 없습니다."
                else "기기에 영어 TTS 음성이 없습니다."
            )
            stop()
            return
        }

        if (segment.languageTag.startsWith("ko")) {
            koreanVoices.firstOrNull()?.let { engine.voice = it }
            engine.setPitch(1.0f)
            engine.setSpeechRate(0.95f)
        } else {
            configureEnglishVoice(engine, segment)
            engine.setSpeechRate(englishRate.coerceIn(0.5f, 1.25f))
        }

        mainHandler.post { onSegmentChanged?.invoke(index, segment) }

        engine.speak(
            segment.text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            expectedToken.toString() + "-" + index
        )
    }

    private fun configureEnglishVoice(engine: TextToSpeech, segment: ListeningSegment) {
        val primary = englishVoices.getOrNull(0)
        val alternate = englishVoices.getOrNull(1) ?: primary
        val third = englishVoices.getOrNull(2) ?: alternate

        when (voiceMode) {
            ListenVoiceMode.STANDARD -> {
                primary?.let { engine.voice = it }
                engine.setPitch(1.0f)
            }

            ListenVoiceMode.ALTERNATE -> {
                alternate?.let { engine.voice = it }
                engine.setPitch(if (englishVoices.size > 1) 1.0f else 1.08f)
            }

            ListenVoiceMode.DIALOGUE_AB -> {
                when (segment.speaker) {
                    "A" -> {
                        primary?.let { engine.voice = it }
                        engine.setPitch(0.96f)
                    }

                    "B" -> {
                        alternate?.let { engine.voice = it }
                        engine.setPitch(if (englishVoices.size > 1) 1.03f else 1.10f)
                    }

                    else -> {
                        third?.let { engine.voice = it }
                        engine.setPitch(1.0f)
                    }
                }
            }
        }
    }

    fun shutdown() {
        stop()
        tts?.shutdown()
        tts = null
    }
}
