package com.example.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles LIMIT 1")
    fun getMyProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profiles WHERE username = :username")
    suspend fun getProfileByName(username: String): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile)

    @Update
    suspend fun updateProfile(profile: UserProfile)
}

@Dao
interface FeedPostDao {
    @Query("SELECT * FROM feed_posts WHERE isModerationFlagged = 0 ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<FeedPost>>

    @Query("SELECT * FROM feed_posts WHERE isReel = 1 AND isModerationFlagged = 0 ORDER BY timestamp DESC")
    fun getAllReels(): Flow<List<FeedPost>>

    @Query("SELECT * FROM feed_posts WHERE username = :username AND isModerationFlagged = 0 ORDER BY timestamp DESC")
    fun getPostsByUsername(username: String): Flow<List<FeedPost>>

    @Query("SELECT COUNT(*) FROM feed_posts")
    suspend fun getPostsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: FeedPost)

    @Update
    suspend fun updatePost(post: FeedPost)

    @Query("DELETE FROM feed_posts WHERE id = :postId")
    suspend fun deletePost(postId: Long)
}

@Dao
interface UserStoryDao {
    @Query("SELECT * FROM user_stories ORDER BY timestamp DESC")
    fun getAllStories(): Flow<List<UserStory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: UserStory)

    @Query("DELETE FROM user_stories WHERE id = :storyId")
    suspend fun deleteStory(storyId: Long)
}

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getChatHistory(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChat()
}

@Dao
interface PostCommentDao {
    @Query("SELECT * FROM post_comments WHERE postId = :postId ORDER BY timestamp ASC")
    fun getCommentsForPost(postId: Long): Flow<List<PostComment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: PostComment)
}

@Database(
    entities = [UserProfile::class, FeedPost::class, UserStory::class, ChatMessage::class, PostComment::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun feedPostDao(): FeedPostDao
    abstract fun userStoryDao(): UserStoryDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun postCommentDao(): PostCommentDao
}

// Room database needs actual entity classes reference. Under Models.kt, we declared PostComment.
// Let's make sure the reference in entities is exact. Let's make PostComment be mapped properly in Database.kt
