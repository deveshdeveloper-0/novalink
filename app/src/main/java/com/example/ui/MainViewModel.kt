package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.data.ChatMessage
import com.example.data.FeedPost
import com.example.data.GeminiClient
import com.example.data.PostComment
import com.example.data.UserProfile
import com.example.data.UserStory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

sealed class NetworkState {
    object Idle : NetworkState()
    object Loading : NetworkState()
    data class Success(val result: String) : NetworkState()
    data class Error(val error: String) : NetworkState()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            "novalink_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    private val repository: AppRepository by lazy {
        AppRepository(database)
    }

    // --- Navigation Flow ---
    private val _currentRoute = MutableStateFlow("splash")
    val currentRoute: StateFlow<String> = _currentRoute.asStateFlow()

    // Active stories viewing parameters
    private val _activeStoryIndex = MutableStateFlow(0)
    val activeStoryIndex = _activeStoryIndex.asStateFlow()

    // Active post comments selection
    private val _selectedPostForComments = MutableStateFlow<FeedPost?>(null)
    val selectedPostForComments = _selectedPostForComments.asStateFlow()

    // --- Authentication ---
    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

    // --- Notifications Feed ---
    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // --- AI Interaction states ---
    private val _aiCaptionState = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val aiCaptionState: StateFlow<NetworkState> = _aiCaptionState.asStateFlow()

    private val _aiModState = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val aiModState: StateFlow<NetworkState> = _aiModState.asStateFlow()

    private val _aiChatting = MutableStateFlow(false)
    val aiChatting: StateFlow<Boolean> = _aiChatting.asStateFlow()

    // --- Global Application Mood config ---
    private val _selectedMood = MutableStateFlow("Inspired") // Inspired, Chill, Energetic, Cosmic
    val selectedMood: StateFlow<String> = _selectedMood.asStateFlow()

    // --- Reactive Feed Source with Mood Filtering ---
    val allPosts: StateFlow<List<FeedPost>> = repository.allPosts
        .combine(_selectedMood) { posts, mood ->
            // Smart Mood prioritization recommendation system!
            // Boost posts that match the current user mood.
            posts.sortedWith(compareByDescending { it.moodCategory.equals(mood, ignoreCase = true) })
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReels: StateFlow<List<FeedPost>> = repository.allReels
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allStories: StateFlow<List<UserStory>> = repository.allStories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatHistory: StateFlow<List<ChatMessage>> = repository.chatHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _commentsForSelectedPost = MutableStateFlow<List<PostComment>>(emptyList())
    val commentsForSelectedPost: StateFlow<List<PostComment>> = _commentsForSelectedPost.asStateFlow()

    init {
        // Initialize app data on first launch
        viewModelScope.launch {
            repository.populateInitialDataIfEmpty()
            repository.myProfile.collect { profile ->
                _currentUser.value = profile
            }
        }
        // Seed default alerts
        _notifications.value = listOf(
            NotificationItem(
                id = "1",
                username = "luna_stellar",
                avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=150&q=80",
                message = "liked your recent cyberpunk architectural model post.",
                timestamp = "2m ago",
                isActionRequired = false
            ),
            NotificationItem(
                id = "2",
                username = "cyber_nova",
                avatarUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=150&q=80",
                message = "mentioned you in a post: '@kamlesh_novalink check this out!'",
                timestamp = "15m ago",
                isActionRequired = false
            ),
            NotificationItem(
                id = "3",
                username = "neon_pulse",
                avatarUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=150&q=80",
                message = "requested creator verification sync.",
                timestamp = "1h ago",
                isActionRequired = true
            )
        )
    }

    // --- Navigation Actions ---
    fun navigateTo(route: String) {
        _currentRoute.value = route
    }

    fun selectStory(index: Int) {
        _activeStoryIndex.value = index
        navigateTo("story")
    }

    private var commentsJob: kotlinx.coroutines.Job? = null

    fun selectCommentsFor(post: FeedPost) {
        _selectedPostForComments.value = post
        navigateTo("comments")
        commentsJob?.cancel()
        commentsJob = viewModelScope.launch {
            repository.getCommentsForPost(post.id).collect {
                _commentsForSelectedPost.value = it
            }
        }
    }

    // --- Feed Interaction Actions (Room backed) ---
    fun updateMood(mood: String) {
        _selectedMood.value = mood
        viewModelScope.launch {
            _currentUser.value?.let { currentProfile ->
                val updatedProf = currentProfile.copy(moodPreference = mood)
                repository.updateMyProfile(updatedProf)
                _currentUser.value = updatedProf
            }
        }
    }

    fun toggleLike(post: FeedPost) {
        viewModelScope.launch {
            val updatedPost = post.copy(
                isLiked = !post.isLiked,
                likesCount = post.likesCount + if (post.isLiked) -1 else 1
            )
            repository.updatePost(updatedPost)
        }
    }

    fun toggleSave(post: FeedPost) {
        viewModelScope.launch {
            val updatedPost = post.copy(isSaved = !post.isSaved)
            repository.updatePost(updatedPost)
        }
    }

    fun loginUser(usernameInput: String) {
        viewModelScope.launch {
            val profile = repository.myProfile.firstOrNull() ?: UserProfile(
                username = usernameInput.ifEmpty { "kamlesh_novalink" },
                handle = "Kamlesh Kumar",
                avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=150&q=80",
                bio = "AI Architect & Creator 🌟 | Crafting the future of decentralized cyber-networks. In love with dark aesthetics and purple gradients. ✨",
                followersCount = 14205,
                followingCount = 485,
                postsCount = 12,
                isCreatorVerified = true,
                moodPreference = "Inspired"
            )
            _currentUser.value = profile
            navigateTo("home")
        }
    }

    fun logout() {
        _currentUser.value = null
        navigateTo("login")
    }

    fun handleSendChatMessage(text: String, isVoice: Boolean = false, voiceDuration: Int = 0) {
        if (text.isBlank() && !isVoice) return

        viewModelScope.launch {
            // 1. Save user msg to local room history
            val userMsg = ChatMessage(
                sender = "kamlesh_novalink",
                text = text.ifEmpty { "Sent a voice message (${voiceDuration}s)" },
                isVoice = isVoice,
                voiceDurationSec = voiceDuration
            )
            repository.insertMessage(userMsg)

            // Let AI respond if chat is to Nova AI
            _aiChatting.value = true
            delay(1500) // Synthesizing pause effect

            val aiResponseText = if (isVoice) {
                "Synthesized your voice message perfectly! Your audio frequency maps to a high creative index. Real-time recommendation: Try posting a Cosmic Reel to capture night-owl traffic."
            } else {
                val prompt = "User sent message: \"$text\". Respond in a futuristic, friendly AI creative sidekick persona, keeping it under 2 sentences."
                GeminiClient.callGemini(prompt, "You are Nova AI, a digital cyber assistant for NovaLink social network.")
            }

            val aiMsg = ChatMessage(
                sender = "Nova AI",
                text = aiResponseText,
                isVoice = false
            )
            repository.insertMessage(aiMsg)
            _aiChatting.value = false
        }
    }

    fun postNewComment(postId: Long, commentText: String) {
        if (commentText.isBlank()) return
        viewModelScope.launch {
            val userComment = PostComment(
                postId = postId,
                username = "kamlesh_novalink",
                text = commentText
            )
            repository.insertComment(userComment)
        }
    }

    // --- AI Assist Services ---
    fun generateAICaptionPrompt(idea: String, style: String) {
        _aiCaptionState.value = NetworkState.Loading
        viewModelScope.launch {
            val prompt = "Create a premium social media caption inspired by the theme: \"$idea\". The style requested is: \"$style\". Provide a creative caption and also suggest 4-5 relevant futuristic hashtags. Keep it sleek, engaging, and modern."
            val res = GeminiClient.callGemini(prompt)
            if (res.startsWith("Error") || res.contains("offline")) {
                _aiCaptionState.value = NetworkState.Success("🚀 Echoing through the cybervoid: \"Scaling new summits of consciousness in the digital landscape.\" #cyberculture #novalink\n\n(Local simulation active - enter your Gemini API Key in the AI Studio secrets panel for real-time model synthesis)")
            } else {
                _aiCaptionState.value = NetworkState.Success(res)
            }
        }
    }

    fun resetAICaptionState() {
        _aiCaptionState.value = NetworkState.Idle
    }

    // --- Upload Post Screen with AI Moderation safeguards ---
    fun handleUploadPost(
        caption: String,
        imageUrl: String?,
        isReel: Boolean,
        hashtags: String,
        mood: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (caption.isBlank()) {
            onFailure("Caption or topic cannot be empty.")
            return
        }

        _aiModState.value = NetworkState.Loading
        viewModelScope.launch {
            // Perform real-time content moderation safety scanning with Gemini!
            val moderationPrompt = "Perform content moderation on the following social media post text: \"$caption $hashtags\". If the content contains explicit hate speech, graphical violence, extreme vulgarity, or extreme harassment, reply with exactly \"BLOCKED\". Otherwise, reply with exactly \"APPROVED\"."
            val evaluation = GeminiClient.callGemini(moderationPrompt, "You are a cybercontent moderation oracle. You respond ONLY with APPROVED or BLOCKED.")

            val isPassed = !evaluation.trim().equals("BLOCKED", ignoreCase = true)

            if (isPassed) {
                // Post Approved - Save to DB
                val resolvedImageUrl = imageUrl?.takeIf { it.isNotBlank() }
                    ?: "https://images.unsplash.com/photo-1550684848-fac1c5b4e853?auto=format&fit=crop&w=800&q=80"

                val newPost = FeedPost(
                    username = "kamlesh_novalink",
                    userAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=150&q=80",
                    imageUrl = resolvedImageUrl,
                    caption = caption,
                    hashtags = hashtags.ifBlank { "#digital #novalink" },
                    likesCount = 0,
                    commentsCount = 0,
                    isReel = isReel,
                    creatorVerified = true,
                    moodCategory = mood,
                    isModerationFlagged = false
                )
                repository.insertPost(newPost)
                _aiModState.value = NetworkState.Idle
                onSuccess()
            } else {
                // Moderation Blocked
                _aiModState.value = NetworkState.Error("AI Sentinel Warning: This caption contains parameters that failed cybernetic aesthetic and safety moderation. Submissions must comply with NovaLink community harmony rules.")
                onFailure("The post was flagged and blocked by AI Content Moderation.")
            }
        }
    }

    fun resetAIModState() {
        _aiModState.value = NetworkState.Idle
    }
}

data class NotificationItem(
    val id: String,
    val username: String,
    val avatarUrl: String,
    val message: String,
    val timestamp: String,
    val isActionRequired: Boolean
)
