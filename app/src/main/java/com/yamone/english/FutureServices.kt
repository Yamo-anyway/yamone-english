package com.yamone.english

object FeatureFlags {
    const val AUTH_ENABLED = false
    const val ADS_ENABLED = false
    const val CONTENT_UPDATE_ENABLED = false
    const val ONLINE_AI_ENABLED = false
}

interface AuthGateway {
    suspend fun signIn(): Boolean
}

interface AdsGateway {
    fun showRewarded(onComplete: () -> Unit)
}

interface ContentUpdateGateway {
    suspend fun checkForContentUpdate(): Boolean
}

interface ConversationGateway {
    suspend fun reply(userText: String, context: String): String
}

object DisabledAuthGateway : AuthGateway {
    override suspend fun signIn() = false
}

object DisabledAdsGateway : AdsGateway {
    override fun showRewarded(onComplete: () -> Unit) = onComplete()
}

object DisabledContentUpdateGateway : ContentUpdateGateway {
    override suspend fun checkForContentUpdate() = false
}

object LocalConversationEngine {
    fun opening(topic: String): String = when (topic) {
        "오늘" -> "How was your day?"
        "음식" -> "What do you want to eat?"
        "취미" -> "What do you like to do for fun?"
        else -> "Hi! How are you today?"
    }

    fun reply(input: String): String {
        val text = input.lowercase()
        return when {
            text.isBlank() -> "Could you say that again?"
            "tired" in text -> "Sounds like a long day. What made you tired?"
            "hungry" in text || "eat" in text -> "What would you like to eat?"
            "coffee" in text || "water" in text || "drink" in text -> "Nice. Do you drink that often?"
            "like" in text || "love" in text -> "Nice. What do you like about it?"
            "home" in text -> "Sounds good. What will you do at home?"
            "today" in text -> "Tell me one more thing about your day."
            "good" in text || "fine" in text -> "Glad to hear that. What are you doing today?"
            else -> "Got it. Tell me a little more."
        }
    }
}
