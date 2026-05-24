package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class AppRepository(private val db: AppDatabase) {

    val myProfile: Flow<UserProfile?> = db.userProfileDao().getMyProfile()
    val allPosts: Flow<List<FeedPost>> = db.feedPostDao().getAllPosts()
    val allReels: Flow<List<FeedPost>> = db.feedPostDao().getAllReels()
    val chatHistory: Flow<List<ChatMessage>> = db.chatMessageDao().getChatHistory()
    val allStories: Flow<List<UserStory>> = db.userStoryDao().getAllStories()

    fun getCommentsForPost(postId: Long): Flow<List<PostComment>> =
        db.postCommentDao().getCommentsForPost(postId)

    fun getPostsByUsername(username: String): Flow<List<FeedPost>> =
        db.feedPostDao().getPostsByUsername(username)

    suspend fun insertPost(post: FeedPost) {
        db.feedPostDao().insertPost(post)
    }

    suspend fun updatePost(post: FeedPost) {
        db.feedPostDao().insertPost(post) // In Dao, insert uses OnConflictStrategy.REPLACE
    }

    suspend fun deletePost(postId: Long) {
        db.feedPostDao().deletePost(postId)
    }

    suspend fun insertStory(story: UserStory) {
        db.userStoryDao().insertStory(story)
    }

    suspend fun insertMessage(message: ChatMessage) {
        db.chatMessageDao().insertMessage(message)
    }

    suspend fun insertComment(comment: PostComment) {
        db.postCommentDao().insertComment(comment)
    }

    suspend fun updateMyProfile(profile: UserProfile) {
        db.userProfileDao().insertProfile(profile)
    }

    suspend fun populateInitialDataIfEmpty() {
        val currentPostsCount = db.feedPostDao().getPostsCount()
        if (currentPostsCount > 0) return

        // Populate User Profile
        val defaultProfile = UserProfile(
            username = "kamlesh_novalink",
            handle = "Kamlesh Kumar",
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=150&q=80",
            bio = "AI Architect & Creator 🌟 | Crafting the future of decentralized cyber-networks. In love with dark aesthetics and purple gradients. ✨",
            followersCount = 14205,
            followingCount = 485,
            postsCount = 12,
            isCreatorVerified = true,
            moodPreference = "Inspired"
        )
        db.userProfileDao().insertProfile(defaultProfile)

        // Seed Stories
        db.userStoryDao().insertStory(
            UserStory(
                username = "cyber_nova",
                userAvatarUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=150&q=80",
                mediaUrl = "https://images.unsplash.com/photo-1462331940025-496dfbfc7564?auto=format&fit=crop&w=600&q=80"
            )
        )
        db.userStoryDao().insertStory(
            UserStory(
                username = "neon_pulse",
                userAvatarUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=150&q=80",
                mediaUrl = "https://images.unsplash.com/photo-1542838132-92c53300491e?auto=format&fit=crop&w=600&q=80"
            )
        )
        db.userStoryDao().insertStory(
            UserStory(
                username = "luna_stellar",
                userAvatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=150&q=80",
                mediaUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=600&q=80"
            )
        )

        // Seed Chat history with AI Companion
        db.chatMessageDao().insertMessage(
            ChatMessage(
                sender = "Nova AI",
                text = "Welcome to NovaLink, Kamlesh! 🌌 I am your futuristic AI Creative Assistant. I can moderate post submissions, build awesome neon-style hashtags, and personalize your feed dynamically. Tell me, what's your vibe today?",
                isVoice = false
            )
        )
        db.chatMessageDao().insertMessage(
            ChatMessage(
                sender = "kamlesh_novalink",
                text = "Hey Nova! Excited to launch this next-gen platform.",
                isVoice = false
            )
        )
        db.chatMessageDao().insertMessage(
            ChatMessage(
                sender = "Nova AI",
                text = "Voice memo sent on trending hashtags",
                isVoice = true,
                voiceDurationSec = 34
            )
        )

        // Seed Posts (A mixture of static feeds and video reels mockup)
        db.feedPostDao().insertPost(
            FeedPost(
                username = "cyber_nova",
                userAvatarUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=150&q=80",
                imageUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=800&q=80",
                caption = "Stepping into the neon-drenched future of tech and style. The cyberpunk scene in Neo-Tokyo is absolutely unmatched.",
                hashtags = "#cyberpunk #neotokyo #aesthetic #futurism",
                likesCount = 2394,
                commentsCount = 42,
                creatorVerified = true,
                moodCategory = "Cosmic",
                isReel = false
            )
        )

        db.feedPostDao().insertPost(
            FeedPost(
                username = "luna_stellar",
                userAvatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=150&q=80",
                imageUrl = "https://images.unsplash.com/photo-1462331940025-496dfbfc7564?auto=format&fit=crop&w=800&q=80",
                caption = "The deep cosmic gas clouds reflecting light millions of lightyears away. Captured with my new smart tele-telescope. Trillions of worlds!",
                hashtags = "#cosmic #nebula #universe #astronomy #ai",
                likesCount = 4580,
                commentsCount = 180,
                creatorVerified = false,
                moodCategory = "Inspired",
                isReel = true // Reel video card
            )
        )

        db.feedPostDao().insertPost(
            FeedPost(
                username = "neon_pulse",
                userAvatarUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=150&q=80",
                imageUrl = "https://images.unsplash.com/photo-1542838132-92c53300491e?auto=format&fit=crop&w=800&q=80",
                caption = "Diving deep into the neural code matrices of security protocols. The system is breathing, guys...",
                hashtags = "#neuralcode #matrix #developer #cybersecurity",
                likesCount = 824,
                commentsCount = 28,
                creatorVerified = true,
                moodCategory = "Energetic",
                isReel = false
            )
        )

        db.feedPostDao().insertPost(
            FeedPost(
                username = "echo_breeze",
                userAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80",
                imageUrl = "https://images.unsplash.com/photo-1550684848-fac1c5b4e853?auto=format&fit=crop&w=800&q=80",
                caption = "Finding peace amidst the high-speed frequencies of the digital matrix. Unwinding in the neon shadows.",
                hashtags = "#chillvibes #ambient #meditation #futurelowfi",
                likesCount = 1438,
                commentsCount = 67,
                creatorVerified = false,
                moodCategory = "Chill",
                isReel = true // Reel video card
            )
        )

        // Seed initial comments
        db.postCommentDao().insertComment(
            PostComment(
                postId = 1,
                username = "luna_stellar",
                text = "Wow! Those neon reflections are beautiful 😍"
            )
        )
        db.postCommentDao().insertComment(
            PostComment(
                postId = 1,
                username = "neon_pulse",
                text = "Absolutely insane shot, cyber_nova!"
            )
        )
    }
}
