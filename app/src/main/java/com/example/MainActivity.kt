package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ui.MainViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                NovaNavigationShell(viewModel = viewModel) { _ ->
                    val currentRoute by viewModel.currentRoute.collectAsState()

                    when (currentRoute) {
                        "splash" -> SplashScreen(viewModel = viewModel)
                        "login" -> LoginSignupScreen(viewModel = viewModel)
                        "home" -> HomeFeedScreen(viewModel = viewModel)
                        "reels" -> ReelsScreen(viewModel = viewModel)
                        "story" -> StoryViewerScreen(viewModel = viewModel)
                        "comments" -> CommentsScreen(viewModel = viewModel)
                        "chat" -> ChatScreen(viewModel = viewModel)
                        "profile" -> UserProfileScreen(viewModel = viewModel)
                        "notifications" -> NotificationsScreen(viewModel = viewModel)
                        "explore" -> ExploreScreen(viewModel = viewModel)
                        "upload" -> UploadPostScreen(viewModel = viewModel)
                        "settings" -> SettingsPageScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
