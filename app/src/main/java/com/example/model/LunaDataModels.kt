package com.example.model

import java.util.UUID

enum class BottomTab {
    HOME, CHAT, EXPLORE, JOURNAL, PROFILE
}

enum class AppOverlay {
    NONE, SPLASH, ONBOARDING, WELCOME, SETTINGS, SUBSCRIPTION, NOTIFICATIONS, SEARCH, HISTORY, FAVORITES, OFFLINE, NOT_FOUND, ABOUT
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: String,
    val category: String? = null,
    val codeSnippet: String? = null,
    val isLiked: Boolean? = null,
    val reactionEmoji: String? = null,
    val isStreaming: Boolean = false
)

enum class MessageSender {
    USER, LUNA, SYSTEM
}

data class JournalEntry(
    val id: String = UUID.randomUUID().toString(),
    val date: String,
    val title: String,
    val content: String,
    val moodEmoji: String,
    val moodLabel: String,
    val tags: List<String>,
    val photoRes: Int? = null
)

data class HabitItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val category: String,
    val currentCount: Int,
    val targetCount: Int,
    val unit: String,
    val streakDays: Int,
    val isCompleted: Boolean = false,
    val iconName: String
)

data class ExploreCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconName: String,
    val description: String,
    val suggestedPrompts: List<String>,
    val featuredTip: String,
    val badge: String? = null
)

data class SavedResponse(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val text: String,
    val category: String,
    val savedDate: String
)

data class AppNotification(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val timeAgo: String,
    val isRead: Boolean = false,
    val type: NotificationType
)

enum class NotificationType {
    WELLNESS, HABIT, AI_TIP, SYSTEM
}
