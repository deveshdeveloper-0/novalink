package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val username: String,
    val handle: String,
    val avatarUrl: String,
    val bio: String,
    val followersCount: Int,
    val followingCount: Int,
    val postsCount: Int,
    val isCreatorVerified: Boolean = false,
    val moodPreference: String = "Inspired", // Mood: Inspired, Chill, Energetic, Cosmic
    val profileViews: Int = 1240,
    val contentEngagement: Float = 14.8f,
    val viralReach: Int = 4580
)

@Entity(tableName = "feed_posts")
data class FeedPost(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val userAvatarUrl: String,
    val imageUrl: String, // Public image URLs for mock rendering
    val caption: String,
    val hashtags: String, // space-separated or comma-separated
    val likesCount: Int,
    val commentsCount: Int,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val isReel: Boolean = false,
    val creatorVerified: Boolean = false,
    val moodCategory: String = "Inspired",
    val isModerationFlagged: Boolean = false
)

@Entity(tableName = "user_stories")
data class UserStory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val userAvatarUrl: String,
    val mediaUrl: String,
    val isViewed: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val expiresAt: Long = System.currentTimeMillis() + 24 * 60 * 60 * 1000 // 24-hour expiry
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // "me" or username
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isVoice: Boolean = false,
    val voiceDurationSec: Int = 0,
    val isRead: Boolean = false
)

@Entity(tableName = "post_comments")
data class PostComment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val postId: Long,
    val username: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
