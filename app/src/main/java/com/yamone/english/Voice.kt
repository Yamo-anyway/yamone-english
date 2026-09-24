package com.yamone.english

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import java.util.Locale

class VoiceController(private val context: Context) : RecognitionListener {
    private val recognizer: SpeechRecognizer? =
        if (SpeechRecognizer.isRecognitionAvailable(context)) SpeechRecognizer.createSpeechRecognizer(context) else null

    private var resultHandler: ((String) -> Unit)? = null
    private var errorHandler: ((String) -> Unit)? = null
    private var active = false
    private var destroyed = false

    init {
        recognizer?.setRecognitionListener(this)
    }

    fun start(
        locale: String = "en-US",
        onResult: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        if (destroyed) {
            onError("음성 인식기를 다시 시작할 수 없습니다. 화면을 다시 열어주세요.")
            return
        }

        val target = recognizer
        if (target == null) {
            onError("이 기기에서 음성 인식을 사용할 수 없습니다.")
            return
        }

        if (active) {
            onError("이미 음성 인식을 실행 중입니다.")
            return
        }

        resultHandler = onResult
        errorHandler = onError

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, locale)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
        }

        runCatching {
            active = true
            target.startListening(intent)
        }.onFailure {
            active = false
            clearHandlers()
            onError("음성 인식을 시작할 수 없습니다. 잠시 후 다시 시도해 주세요.")
        }
    }

    fun stop() {
        if (!active || destroyed) return
        recognizer?.stopListening()
    }

    fun cancel() {
        if (!destroyed) recognizer?.cancel()
        active = false
        clearHandlers()
    }

    fun destroy() {
        if (destroyed) return
        destroyed = true
        recognizer?.cancel()
        recognizer?.destroy()
        active = false
        clearHandlers()
    }

    override fun onResults(results: Bundle?) {
        if (destroyed) return
        active = false
        val onResult = resultHandler
        val onError = errorHandler
        clearHandlers()

        val text = results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.firstOrNull()
            .orEmpty()
            .trim()

        if (text.isBlank()) {
            onError?.invoke("잘 듣지 못했어요. 다시 말해보세요.")
        } else {
            onResult?.invoke(text)
        }
    }

    override fun onError(error: Int) {
        if (destroyed) return
        active = false
        val callback = errorHandler
        clearHandlers()

        val message = when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "마이크 입력을 확인해 주세요."
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "마이크 권한이 필요합니다."
            SpeechRecognizer.ERROR_NETWORK,
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "음성 인식 네트워크 상태를 확인해 주세요."
            SpeechRecognizer.ERROR_NO_MATCH -> "잘 듣지 못했어요. 다시 말해보세요."
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "음성 인식이 사용 중입니다. 잠시 후 다시 말해보세요."
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "말소리가 들리지 않았어요."
            SpeechRecognizer.ERROR_CLIENT -> "음성 인식이 취소되었습니다."
            SpeechRecognizer.ERROR_SERVER,
            SpeechRecognizer.ERROR_SERVER_DISCONNECTED -> "음성 인식 서비스 연결을 확인해 주세요."
            else -> "음성 인식 중 오류가 발생했습니다."
        }
        callback?.invoke(message)
    }

    private fun clearHandlers() {
        resultHandler = null
        errorHandler = null
    }

    override fun onReadyForSpeech(params: Bundle?) = Unit
    override fun onBeginningOfSpeech() = Unit
    override fun onRmsChanged(rmsdB: Float) = Unit
    override fun onBufferReceived(buffer: ByteArray?) = Unit
    override fun onEndOfSpeech() = Unit
    override fun onPartialResults(partialResults: Bundle?) = Unit
    override fun onEvent(eventType: Int, params: Bundle?) = Unit
}

class EnglishTts(context: Context) {
    private var ready = false
    private var released = false
    private var tts: TextToSpeech? = null

    init {
        tts = TextToSpeech(context) { status ->
            if (released) return@TextToSpeech
            ready = status == TextToSpeech.SUCCESS
            if (ready) {
                val engine = tts
                val result = engine?.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    ready = false
                }
            }
        }
    }

    fun speak(text: String, rate: Float = 0.85f): Boolean {
        val engine = tts ?: return false
        if (released || !ready || text.isBlank()) return false
        engine.setSpeechRate(rate.coerceIn(0.5f, 1.2f))
        return engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "yamone-english") != TextToSpeech.ERROR
    }

    fun shutdown() {
        if (released) return
        released = true
        ready = false
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
