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

    init {
        recognizer?.setRecognitionListener(this)
    }

    fun start(
        locale: String = "en-US",
        onResult: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val target = recognizer
        if (target == null) {
            onError("이 기기에서 음성 인식을 사용할 수 없습니다.")
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
        target.startListening(intent)
    }

    fun stop() = recognizer?.stopListening()
    fun destroy() = recognizer?.destroy()

    override fun onResults(results: Bundle?) {
        val text = results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.firstOrNull()
            .orEmpty()
        resultHandler?.invoke(text)
    }

    override fun onError(error: Int) {
        val message = when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "마이크 입력을 확인해 주세요."
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "마이크 권한이 필요합니다."
            SpeechRecognizer.ERROR_NETWORK,
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "음성 인식 네트워크 상태를 확인해 주세요."
            SpeechRecognizer.ERROR_NO_MATCH -> "잘 듣지 못했어요. 다시 말해보세요."
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "잠시 후 다시 말해보세요."
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "말소리가 들리지 않았어요."
            else -> "음성 인식 중 오류가 발생했습니다."
        }
        errorHandler?.invoke(message)
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
    private var tts: TextToSpeech? = null

    init {
        tts = TextToSpeech(context) { status ->
            ready = status == TextToSpeech.SUCCESS
            if (ready) {
                tts?.language = Locale.US
            }
        }
    }

    fun speak(text: String, rate: Float = 0.85f) {
        val engine = tts ?: return
        if (!ready) return
        engine.setSpeechRate(rate.coerceIn(0.5f, 1.2f))
        engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "yamone-english")
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
