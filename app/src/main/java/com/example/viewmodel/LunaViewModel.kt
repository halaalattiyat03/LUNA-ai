package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MockData
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LunaUiState(
    val currentTab: BottomTab = BottomTab.HOME,
    val currentOverlay: AppOverlay = AppOverlay.SPLASH,
    val isDarkMode: Boolean = false,
    val isSystemTheme: Boolean = true,
    val accentColorHex: String = "#D946EF",
    val fontSizeScale: Float = 1.0f,
    val isAnimationsEnabled: Boolean = true,
    
    // User Profile
    val userName: String = "Sophia Vance",
    val userEmail: String = "sophia.vance@example.com",
    val isPremiumUser: Boolean = true,
    val selectedMood: String = "🌸",

    // Chat State
    val chatMessages: List<ChatMessage> = MockData.initialChatHistory,
    val isTyping: Boolean = false,
    val isVoiceModalOpen: Boolean = false,
    val activeCategoryFilter: String? = null,

    // Explore State
    val selectedCategory: ExploreCategory? = null,

    // Journal State
    val journalEntries: List<JournalEntry> = MockData.initialJournalEntries,
    val journalSearchQuery: String = "",

    // Habit State
    val habits: List<HabitItem> = MockData.initialHabits,

    // Saved & Notifications
    val savedResponses: List<SavedResponse> = MockData.initialSavedResponses,
    val notifications: List<AppNotification> = MockData.initialNotifications,
    
    // Search & Global
    val globalSearchQuery: String = "",
    val isOfflineMode: Boolean = false
)

class LunaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LunaUiState())
    val uiState: StateFlow<LunaUiState> = _uiState.asStateFlow()

    init {
        // Automatically hide splash after 1.8 seconds on startup
        viewModelScope.launch {
            delay(1800)
            if (_uiState.value.currentOverlay == AppOverlay.SPLASH) {
                _uiState.update { it.copy(currentOverlay = AppOverlay.NONE) }
            }
        }
    }

    fun setBottomTab(tab: BottomTab) {
        _uiState.update { it.copy(currentTab = tab, currentOverlay = AppOverlay.NONE) }
    }

    fun openOverlay(overlay: AppOverlay) {
        _uiState.update { it.copy(currentOverlay = overlay) }
    }

    fun closeOverlay() {
        _uiState.update { it.copy(currentOverlay = AppOverlay.NONE) }
    }

    fun toggleDarkMode(isDark: Boolean) {
        _uiState.update { it.copy(isDarkMode = isDark, isSystemTheme = false) }
    }

    fun setSelectedMood(moodEmoji: String) {
        _uiState.update { it.copy(selectedMood = moodEmoji) }
    }

    fun selectExploreCategory(category: ExploreCategory?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun toggleVoiceModal(isOpen: Boolean) {
        _uiState.update { it.copy(isVoiceModalOpen = isOpen) }
    }

    fun toggleOfflineMode() {
        val newOfflineState = !_uiState.value.isOfflineMode
        _uiState.update { 
            it.copy(
                isOfflineMode = newOfflineState,
                currentOverlay = if (newOfflineState) AppOverlay.OFFLINE else AppOverlay.NONE
            )
        }
    }

    fun sendMessage(userText: String, categoryName: String? = null) {
        if (userText.isBlank()) return

        val userMessage = ChatMessage(
            sender = MessageSender.USER,
            text = userText,
            timestamp = "Just now",
            category = categoryName
        )

        _uiState.update { state ->
            state.copy(
                chatMessages = state.chatMessages + userMessage,
                isTyping = true,
                currentTab = BottomTab.CHAT
            )
        }

        // Simulate AI streaming response
        viewModelScope.launch {
            delay(1200)
            val aiResponseText = generateSimulatedLunaResponse(userText, categoryName)
            val aiMessageId = java.util.UUID.randomUUID().toString()
            
            val aiMessage = ChatMessage(
                id = aiMessageId,
                sender = MessageSender.LUNA,
                text = aiResponseText,
                timestamp = "Just now",
                category = categoryName ?: "Luna AI",
                isStreaming = false
            )

            _uiState.update { state ->
                state.copy(
                    chatMessages = state.chatMessages + aiMessage,
                    isTyping = false
                )
            }
        }
    }

    fun reactToMessage(messageId: String, isLiked: Boolean) {
        _uiState.update { state ->
            val updated = state.chatMessages.map { msg ->
                if (msg.id == messageId) msg.copy(isLiked = isLiked) else msg
            }
            state.copy(chatMessages = updated)
        }
    }

    fun toggleHabit(habitId: String) {
        _uiState.update { state ->
            val updated = state.habits.map { habit ->
                if (habit.id == habitId) {
                    val newCount = if (habit.isCompleted) 0 else habit.targetCount
                    habit.copy(currentCount = newCount, isCompleted = !habit.isCompleted)
                } else habit
            }
            state.copy(habits = updated)
        }
    }

    fun addJournalEntry(title: String, content: String, moodEmoji: String, moodLabel: String, tags: List<String>) {
        val newEntry = JournalEntry(
            date = "Today, " + java.text.SimpleDateFormat("h:mm a", java.util.Locale.getDefault()).format(java.util.Date()),
            title = title,
            content = content,
            moodEmoji = moodEmoji,
            moodLabel = moodLabel,
            tags = tags
        )
        _uiState.update { state ->
            state.copy(journalEntries = listOf(newEntry) + state.journalEntries)
        }
    }

    fun saveResponseToFavorites(title: String, text: String, category: String) {
        val newSave = SavedResponse(
            title = title,
            text = text,
            category = category,
            savedDate = "Today"
        )
        _uiState.update { state ->
            state.copy(savedResponses = listOf(newSave) + state.savedResponses)
        }
    }

    fun setGlobalSearchQuery(query: String) {
        _uiState.update { it.copy(globalSearchQuery = query) }
    }

    fun markNotificationRead(id: String) {
        _uiState.update { state ->
            val updated = state.notifications.map {
                if (it.id == id) it.copy(isRead = true) else it
            }
            state.copy(notifications = updated)
        }
    }

    private fun generateSimulatedLunaResponse(prompt: String, category: String?): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("skincare") || lower.contains("glow") || lower.contains("face") ->
                "Here is your personalized **Glow Skincare Routine** 🌸:\n\n1. **Hydrating Cleanser:** Gently cleanse without stripping skin oils.\n2. **Toner Mist:** Spray organic rosewater for calming hydration.\n3. **Serum Layering:** Apply Niacinamide serum to damp skin.\n4. **Moisturizer & SPF:** Lock with lightweight peptide cream.\n\n*Tip: Always pat gently into skin rather than rubbing!*"

            lower.contains("pilates") || lower.contains("workout") || lower.contains("fitness") ->
                "Here is a **15-Minute Gentle Core & Posture Pilates Flow** 🧘‍♀️:\n\n• **Cat-Cow Stretching:** 10 slow breath cycles\n• **Bird-Dog Alignment:** 12 reps each side\n• **Glute Bridge Hold:** 3 sets of 30 seconds\n• **Child's Pose:** 2 minutes deep restorative breathing"

            lower.contains("recipe") || lower.contains("breakfast") || lower.contains("food") ->
                "Here is a **Glowing Skin Anti-Inflammatory Breakfast Bowl** 🫐:\n\n• 1 cup Greek yogurt or almond yogurt\n• Handful of fresh blueberries & chia seeds\n• 1 tbsp almond butter & pinch of cinnamon\n• Drizzle of organic raw honey\n\n*Provides 24g protein and antioxidant support for natural morning energy!*"

            lower.contains("career") || lower.contains("interview") || lower.contains("salary") ->
                "Here is your **Confident Communication Guide** 💼:\n\n1. Frame your experience around outcomes and value created.\n2. State your expected compensation range clearly based on research.\n3. Pause calmly after making your key points."

            else ->
                "Thank you for sharing that with me ✨ Here is a thoughtful approach tailored to your journey:\n\n1. Focus on what brings you clarity and peace in this moment.\n2. Take one small action step toward your priority.\n3. Remember to celebrate your daily progress.\n\n*How else can I assist you with this?*"
        }
    }
}
