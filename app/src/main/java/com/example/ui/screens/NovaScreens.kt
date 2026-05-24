package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.ChatMessage
import com.example.data.FeedPost
import com.example.data.PostComment
import com.example.data.UserProfile
import com.example.data.UserStory
import com.example.ui.MainViewModel
import com.example.ui.NetworkState
import com.example.ui.NotificationItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// -------------------------------------------------------------
// Core Theme & Gradient Generators
// -------------------------------------------------------------

@Composable
fun getMoodColor(mood: String): Color {
    return when (mood) {
        "Inspired" -> Color(0xFFA855F7) // Velvet Purple
        "Chill" -> Color(0xFF3B82F6) // Neon Blue Sky
        "Energetic" -> Color(0xFFF43F5E) // Radiant Coral Rose
        "Cosmic" -> Color(0xFF22D3EE) // Deep Cyan Sky
        else -> Color(0xFFA855F7)
    }
}

@Composable
fun getMoodColorDark(mood: String): Color {
    return when (mood) {
        "Inspired" -> Color(0xFF581C87)
        "Chill" -> Color(0xFF1E3A8A)
        "Energetic" -> Color(0xFF881337)
        "Cosmic" -> Color(0xFF0891B2)
        else -> Color(0xFF581C87)
    }
}

@Composable
fun NeonGradientBackground(mood: String = "Inspired", content: @Composable BoxScope.() -> Unit) {
    val col1 = getMoodColor(mood)
    val col2 = getMoodColorDark(mood)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050505))
    ) {
        // Aesthetic deep futuristic neon ambient clouds
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(col1.copy(alpha = 0.14f), Color.Transparent),
                    center = Offset(size.width * 0.15f, size.height * 0.2f),
                    radius = size.width * 0.95f
                )
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(col2.copy(alpha = 0.16f), Color.Transparent),
                    center = Offset(size.width * 0.85f, size.height * 0.75f),
                    radius = size.width * 1.0f
                )
            )
        }

        Box(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {
            content()
        }
    }
}

@Composable
fun GlassyCard(
    modifier: Modifier = Modifier,
    mood: String = "Inspired",
    borderColorAlpha: Float = 0.09f,
    content: @Composable ColumnScope.() -> Unit
) {
    val mCol = getMoodColor(mood)
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF100C1B).copy(alpha = 0.72f)
        ),
        border = BorderStroke(1.dp, mCol.copy(alpha = borderColorAlpha))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            content = content
        )
    }
}

@Composable
fun VerificationBadge(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Default.Verified,
        contentDescription = "Verified Sync Creator",
        tint = Color(0xFF22D3EE),
        modifier = modifier.size(16.dp)
    )
}

// -------------------------------------------------------------
// SPLASH SCREEN
// -------------------------------------------------------------

@Composable
fun SplashScreen(viewModel: MainViewModel) {
    var startAnimation by remember { mutableStateOf(false) }
    val scale = animateFloatAsState(
        targetValue = if (startAnimation) 1.2f else 0.8f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
    )
    val opacity = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2200)
        // Auto-navigate to login
        viewModel.navigateTo("login")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF040208)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF8A2BE2).copy(alpha = 0.15f), Color.Transparent),
                    radius = size.width * 0.7f
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value,
                alpha = opacity.value
            )
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        Brush.linearGradient(listOf(Color(0xFFA855F7), Color(0xFF3B82F6))),
                        shape = RoundedCornerShape(28.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AllInclusive,
                    contentDescription = "NovaLink Space Engine",
                    tint = Color.White,
                    modifier = Modifier.size(54.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "NOVALINK",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 6.sp,
                fontFamily = FontFamily.SansSerif
            )
            Text(
                text = "The AI-Personalized Social Cosmos",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 12.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(48.dp))
            CircularProgressIndicator(
                color = Color(0xFFA855F7),
                strokeWidth = 3.dp,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// -------------------------------------------------------------
// LOGIN / SIGNUP SCREEN
// -------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginSignupScreen(viewModel: MainViewModel) {
    var username by remember { mutableStateOf("") }
    var inviteCode by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }

    NeonGradientBackground("Inspired") {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            GlassyCard(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 24.dp),
                mood = "Inspired",
                borderColorAlpha = 0.16f
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.AllInclusive,
                        contentDescription = "Logo",
                        tint = Color(0xFFA855F7),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Initialize Space Sync",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Join NovaLink digital multiverse workspace",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                    )

                    TextField(
                        value = username,
                        onValueChange = {
                            username = it
                            loginError = ""
                        },
                        label = { Text("Cyber Handle (@username)", color = Color.White.copy(alpha = 0.5f)) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF1E1B2E),
                            unfocusedContainerColor = Color(0xFF151221),
                            focusedIndicatorColor = Color(0xFFA855F7),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("username_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = inviteCode,
                        onValueChange = { inviteCode = it },
                        label = { Text("Creator Node Invite (Optional)", color = Color.White.copy(alpha = 0.5f)) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF1E1B2E),
                            unfocusedContainerColor = Color(0xFF151221),
                            focusedIndicatorColor = Color(0xFF3B82F6),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    if (loginError.isNotEmpty()) {
                        Text(
                            text = loginError,
                            color = Color(0xFFF43F5E),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = {
                            if (username.isBlank()) {
                                loginError = "Write down a futuristic username handle to sync!"
                            } else {
                                viewModel.loginUser(username.trim().removePrefix("@"))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFA855F7),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("login_button")
                    ) {
                        Text(
                            text = "SYNC CONSCIOUSNESS",
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "By entering, you sync with our AI Safety guidelines & protocol buffers.",
                        color = Color.White.copy(alpha = 0.35f),
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// CORE NAVIGATION HOST & SHELL COMPONENTS
// -------------------------------------------------------------

@Composable
fun NovaNavigationShell(viewModel: MainViewModel, content: @Composable (PaddingValues) -> Unit) {
    val currentRoute by viewModel.currentRoute.collectAsState()
    val mood by viewModel.selectedMood.collectAsState()
    val tint = getMoodColor(mood)

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            if (currentRoute != "splash" && currentRoute != "login" && currentRoute != "story") {
                val myProfile by viewModel.currentUser.collectAsState()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF000000))
                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .navigationBarsPadding()
                        .testTag("app_navigation_bar"),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 1. Home tab
                        IconButton(
                            onClick = { viewModel.navigateTo("home") },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                tint = if (currentRoute == "home") tint else Color.White.copy(alpha = 0.4f),
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        // 2. Explore tab
                        IconButton(
                            onClick = { viewModel.navigateTo("explore") },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Explore",
                                tint = if (currentRoute == "explore") tint else Color.White.copy(alpha = 0.4f),
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        // Spacer to budget space for the Center FAB in overlay
                        Spacer(modifier = Modifier.width(56.dp))

                        // 4. Reels tab
                        IconButton(
                            onClick = { viewModel.navigateTo("reels") },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VideoLibrary,
                                contentDescription = "Reels",
                                tint = if (currentRoute == "reels") tint else Color.White.copy(alpha = 0.4f),
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        // 5. User Profile tab
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clickable { viewModel.navigateTo("profile") },
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = myProfile?.avatarUrl ?: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=150&q=80",
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .border(
                                        BorderStroke(
                                            1.5.dp,
                                            if (currentRoute == "profile") tint else Color.White.copy(alpha = 0.3f)
                                        ),
                                        CircleShape
                                    ),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    // 3. Center FAB overlaid perfectly
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-18).dp)
                            .size(54.dp)
                            .graphicsLayer(rotationZ = 45f)
                            .background(
                                brush = Brush.linearGradient(listOf(Color(0xFFA855F7), Color(0xFF3B82F6))),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { viewModel.navigateTo("upload") },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Upload FAB",
                            tint = Color.White,
                            modifier = Modifier
                                .size(28.dp)
                                .graphicsLayer(rotationZ = -45f) // Rotated back upright
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}

// -------------------------------------------------------------
// USER STORY VISUAL WIDGETS
// -------------------------------------------------------------

@Composable
fun StoriesBar(stories: List<UserStory>, onStoryClick: (Int) -> Unit, myProfile: UserProfile?) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .testTag("stories_row"),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        // Current User upload indicator bubble
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { /* Simulate Adding a story */ }
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    AsyncImage(
                        model = myProfile?.avatarUrl ?: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=150&q=80",
                        contentDescription = "My Head Avatar",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(Color(0xFFA855F7), CircleShape)
                            .border(2.dp, Color(0xFF040208), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Story",
                            tint = Color.Black,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("Your Story", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
            }
        }

        items(stories.size) { i ->
            val story = stories[i]
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onStoryClick(i) }
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .background(
                            Brush.sweepGradient(listOf(Color(0xFFA855F7), Color(0xFF3B82F6), Color(0xFFA855F7))),
                            CircleShape
                        )
                        .padding(2.dp)
                ) {
                    AsyncImage(
                        model = story.userAvatarUrl,
                        contentDescription = "Story bubble of ${story.username}",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color.Black)
                            .border(2.dp, Color.Black, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "@${story.username}",
                    color = Color.White,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(68.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// -------------------------------------------------------------
// STORY VIEWER OVERLAY
// -------------------------------------------------------------

@Composable
fun StoryViewerScreen(viewModel: MainViewModel) {
    val stories by viewModel.allStories.collectAsState()
    val index by viewModel.activeStoryIndex.collectAsState()

    LaunchedEffect(stories) {
        if (stories.isEmpty()) {
            viewModel.navigateTo("home")
        }
    }

    if (stories.isEmpty()) {
        return
    }

    val story = stories.getOrNull(index) ?: stories.firstOrNull() ?: return
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(key1 = index) {
        progress = 0f
        while (progress < 1f) {
            delay(50)
            progress += 0.015f
        }
        // Advance story index when timer expires
        if (index < stories.size - 1) {
            viewModel.selectStory(index + 1)
        } else {
            viewModel.navigateTo("home")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // High fidelity story graphic
        AsyncImage(
            model = story.mediaUrl,
            contentDescription = "Active story view background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay layout gradients
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.6f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.5f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(16.dp)
        ) {
            // Top story progress bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                stories.forEachIndexed { i, _ ->
                    val curProgressVal = when {
                        i < index -> 1f
                        i > index -> 0f
                        else -> progress
                    }
                    LinearProgressIndicator(
                        progress = { curProgressVal },
                        modifier = Modifier
                            .weight(1f)
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                }
            }

            // Creator parameters
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = story.userAvatarUrl,
                    contentDescription = "Story bubble avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "@${story.username}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        VerificationBadge()
                    }
                    Text(
                        text = "Nova Node Stream",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp
                    )
                }

                IconButton(onClick = { viewModel.navigateTo("home") }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close story", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Swipe / Bottom navigational buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Interactive taps to control flow
                Text(
                    text = if (index > 0) "◀ Tap left for previous" else "",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    modifier = Modifier.clickable {
                        if (index > 0) viewModel.selectStory(index - 1)
                    }
                )

                Text(
                    text = "Aesthetic Digital Dream",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )

                Text(
                    text = if (index < stories.size - 1) "Tap right for next ▶" else "Dismiss",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    modifier = Modifier.clickable {
                        if (index < stories.size - 1) {
                            viewModel.selectStory(index + 1)
                        } else {
                            viewModel.navigateTo("home")
                        }
                    }
                )
            }
        }
    }
}

// -------------------------------------------------------------
// POST FEED SCREEN & CARD COMPONENT
// -------------------------------------------------------------

@Composable
fun FeedPostCard(
    post: FeedPost,
    mood: String,
    onLikeClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCommentClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val accent = getMoodColor(mood)

    GlassyCard(
        mood = mood,
        borderColorAlpha = 0.12f,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("post_card_${post.id}")
    ) {
        // Creator row header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            AsyncImage(
                model = post.userAvatarUrl,
                contentDescription = "Post user avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { onProfileClick() },
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onProfileClick() }
                ) {
                    Text(
                        text = "@${post.username}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    if (post.creatorVerified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        VerificationBadge()
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(accent, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Vibe: ${post.moodCategory}",
                        color = accent.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            IconButton(onClick = { /* Share menu */ }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Post actions", tint = Color.White.copy(alpha = 0.6f))
            }
        }

        // Beautiful glass rounded image block with overlay indicators
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black)
        ) {
            AsyncImage(
                model = post.imageUrl,
                contentDescription = "Post creative asset visualizer",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            if (post.isReel) {
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = "Video post",
                            tint = accent,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("REEL", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Direct Interaction Panel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onLikeClick, modifier = Modifier.testTag("like_button_${post.id}")) {
                Icon(
                    imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like button",
                    tint = if (post.isLiked) Color(0xFFFF3F62) else Color.White
                )
            }
            Text(
                text = "${post.likesCount}",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(end = 16.dp)
            )

            IconButton(onClick = onCommentClick, modifier = Modifier.testTag("comment_button_${post.id}")) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Comment button",
                    tint = Color.White
                )
            }
            Text(
                text = "${post.commentsCount}",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(end = 16.dp)
            )

            IconButton(onClick = { /* Simulate Share alert */ }) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "Share",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onSaveClick, modifier = Modifier.testTag("save_button_${post.id}")) {
                Icon(
                    imageVector = if (post.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Save post",
                    tint = if (post.isSaved) accent else Color.White
                )
            }
        }

        // Caption text
        Text(
            text = post.caption,
            color = Color.White,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 4.dp),
            lineHeight = 18.sp
        )

        // Custom Hashtags list
        if (post.hashtags.isNotEmpty()) {
            Text(
                text = post.hashtags,
                color = accent,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Composable
fun HomeFeedScreen(viewModel: MainViewModel) {
    val posts by viewModel.allPosts.collectAsState()
    val stories by viewModel.allStories.collectAsState()
    val curMood by viewModel.selectedMood.collectAsState()
    val myProfile by viewModel.currentUser.collectAsState()

    NeonGradientBackground(curMood) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Core Top Brand Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                Brush.linearGradient(listOf(Color(0xFFA855F7), Color(0xFF3B82F6))),
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AllInclusive,
                            contentDescription = "App Icon",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "NovaLink",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconButton(onClick = { viewModel.navigateTo("upload") }) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Upload Content",
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(onClick = { viewModel.navigateTo("chat") }) {
                        Box {
                            Icon(
                                imageVector = Icons.Default.Forum,
                                contentDescription = "Active Chat Sync",
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(24.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .background(Color(0xFFA855F7), CircleShape)
                                    .align(Alignment.TopEnd)
                            )
                        }
                    }

                    IconButton(onClick = { viewModel.navigateTo("notifications") }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Active Notifications",
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Account Profile Access Bubble
                    AsyncImage(
                        model = myProfile?.avatarUrl ?: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=150&q=80",
                        contentDescription = "Profile head",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, getMoodColor(curMood), CircleShape)
                            .clickable { viewModel.navigateTo("profile") },
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Mood-Based Smart Feed Switch Toolbar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            ) {
                val moods = listOf("Inspired", "Chill", "Energetic", "Cosmic")
                LazyRow(
                    modifier = Modifier.fillMaxWidth().testTag("mood_filters_row"),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(moods) { m ->
                        val selected = m == curMood
                        val moodAccent = getMoodColor(m)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (selected) moodAccent.copy(alpha = 0.22f) else Color(0xFF141125).copy(alpha = 0.6f)
                                )
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        if (selected) moodAccent else Color.White.copy(alpha = 0.08f)
                                    ),
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { viewModel.updateMood(m) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .background(moodAccent, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = m,
                                    color = if (selected) Color.White else Color.White.copy(alpha = 0.6f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Divider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(top = 4.dp))

            // Main Feed List View
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .testTag("home_feed_list"),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item {
                    StoriesBar(
                        stories = stories,
                        onStoryClick = { index -> viewModel.selectStory(index) },
                        myProfile = myProfile
                    )
                    Divider(color = Color.White.copy(alpha = 0.05f))
                }

                if (posts.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = getMoodColor(curMood))
                        }
                    }
                } else {
                    items(posts, key = { it.id }) { post ->
                        Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                            FeedPostCard(
                                post = post,
                                mood = curMood,
                                onLikeClick = { viewModel.toggleLike(post) },
                                onSaveClick = { viewModel.toggleSave(post) },
                                onCommentClick = { viewModel.selectCommentsFor(post) },
                                onProfileClick = { viewModel.navigateTo("profile") }
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// REELS SCREEN (VERTICAL VIDEO SCROLLER INTERACTION)
// -------------------------------------------------------------

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReelsScreen(viewModel: MainViewModel) {
    val reels by viewModel.allReels.collectAsState()
    val curMood by viewModel.selectedMood.collectAsState()
    val accent = getMoodColor(curMood)

    NeonGradientBackground(curMood) {
        if (reels.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = accent)
            }
        } else {
            // Full screen vertical page scrolling model representation
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("reels_scroller"),
                verticalArrangement = Arrangement.Top,
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(reels) { reel ->
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .background(Color.Black)
                    ) {
                        // High fidelity reels graphic visualizer
                        AsyncImage(
                            model = reel.imageUrl,
                            contentDescription = "Reel Background Video Frame representation",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Glass fade gradients overlays
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color.Black.copy(alpha = 0.4f),
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.75f)
                                        )
                                    )
                                )
                        )

                        // Simulated play/pause radar concentric circles
                        var isPlayingSim by remember { mutableStateOf(true) }
                        IconButton(
                            onClick = { isPlayingSim = !isPlayingSim },
                            modifier = Modifier
                                .size(72.dp)
                                .align(Alignment.Center)
                        ) {
                            Icon(
                                imageVector = if (isPlayingSim) Icons.Default.PlayArrow else Icons.Default.Pause,
                                contentDescription = "Simulated Playback Toggle",
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        // Right interaction rail
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 12.dp, bottom = 120.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                IconButton(
                                    onClick = { viewModel.toggleLike(reel) },
                                    modifier = Modifier
                                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                        .size(48.dp)
                                ) {
                                    Icon(
                                        imageVector = if (reel.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Like Reel",
                                        tint = if (reel.isLiked) Color.Red else Color.White
                                    )
                                }
                                Text("${reel.likesCount}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                IconButton(
                                    onClick = { viewModel.selectCommentsFor(reel) },
                                    modifier = Modifier
                                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                        .size(48.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.ChatBubbleOutline,
                                        contentDescription = "Thread comments feedback",
                                        tint = Color.White
                                    )
                                }
                                Text("${reel.commentsCount}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            IconButton(
                                onClick = { /* Share simulated link */ },
                                modifier = Modifier
                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                    .size(48.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Share, contentDescription = "Share link", tint = Color.White)
                            }

                            IconButton(
                                onClick = { viewModel.toggleSave(reel) },
                                modifier = Modifier
                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                    .size(48.dp)
                            ) {
                                Icon(
                                    imageVector = if (reel.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Save video catalog",
                                    tint = if (reel.isSaved) accent else Color.White
                                )
                            }
                        }

                        // Bottom caption information cards
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth(0.8f)
                                .padding(horizontal = 16.dp, vertical = 90.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = reel.userAvatarUrl,
                                    contentDescription = "Reel User Profile",
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "@${reel.username}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                VerificationBadge()
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = reel.caption,
                                color = Color.White,
                                fontSize = 13.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = reel.hashtags,
                                color = accent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// POST DETAILED COMMENTS SHEET
// -------------------------------------------------------------

@Composable
fun CommentsScreen(viewModel: MainViewModel) {
    val post by viewModel.selectedPostForComments.collectAsState()
    val comments by viewModel.commentsForSelectedPost.collectAsState()
    val mood by viewModel.selectedMood.collectAsState()
    var userCommentInput by remember { mutableStateOf("") }
    val accent = getMoodColor(mood)

    LaunchedEffect(post) {
        if (post == null) {
            viewModel.navigateTo("home")
        }
    }

    if (post == null) {
        return
    }

    NeonGradientBackground(mood) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateTo("home") }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Return home", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("Comments Threads", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            // Original Post caption card ref
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF141024)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = post!!.userAvatarUrl,
                        contentDescription = "post user avatar",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = post!!.caption,
                        color = Color.White,
                        fontSize = 12.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Comments lists
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (comments.isEmpty()) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Forum, contentDescription = "Prompt start conversation", tint = accent.copy(alpha = 0.4f), modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "No cybernetic feedback on this thread yet.",
                                color = Color.White.copy(alpha = 0.4f),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(comments) { comment ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF131024).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(accent, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = comment.username.take(2).uppercase(),
                                    color = Color.Black,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "@${comment.username}",
                                    color = accent,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = comment.text,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Chat-style user comment input bar at bottom
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .background(Color(0xFF0C091A))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = userCommentInput,
                        onValueChange = { userCommentInput = it },
                        placeholder = { Text("Synthesize feedback...", color = Color.White.copy(alpha = 0.4f)) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF1B182B),
                            unfocusedContainerColor = Color(0xFF131024),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedIndicatorColor = accent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("comment_input_box"),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (userCommentInput.isNotBlank()) {
                                viewModel.postNewComment(post!!.id, userCommentInput.trim())
                                userCommentInput = ""
                            }
                        },
                        modifier = Modifier
                            .background(accent, CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Submit comment", tint = Color.Black)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// CHAT SCREEN (REAL-TIME CONVERSATION WITH AI ASSISTANT)
// -------------------------------------------------------------

@Composable
fun ChatScreen(viewModel: MainViewModel) {
    val chatHistory by viewModel.chatHistory.collectAsState()
    val isAiChatting by viewModel.aiChatting.collectAsState()
    val curMood by viewModel.selectedMood.collectAsState()
    var messageInput by remember { mutableStateOf("") }
    val accent = getMoodColor(curMood)

    // Simulated voice recording state variables
    var isRecordingSim by remember { mutableStateOf(false) }
    var recordingTimer by remember { mutableStateOf(0) }

    LaunchedEffect(isRecordingSim) {
        if (isRecordingSim) {
            recordingTimer = 0
            while (isRecordingSim) {
                delay(1000)
                recordingTimer++
            }
        }
    }

    NeonGradientBackground(curMood) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Profile banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0E0B1B).copy(alpha = 0.7f))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Brush.sweepGradient(listOf(Color(0xFFFF007F), Color(0xFF00D2FF))),
                            CircleShape
                        )
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.AllInclusive, contentDescription = "Nova AI avatar Icon", tint = Color.White, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Nova AI Assistant", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF22D3EE).copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text("CORE MODEL", color = Color(0xFF22D3EE), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Text(
                        text = if (isAiChatting) "Nova AI is synthesizing..." else "Direct Mind Sync Active",
                        color = if (isAiChatting) accent else Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp
                    )
                }

                IconButton(onClick = { /* Reset logs clear */ }) {
                    Icon(imageVector = Icons.Default.DeleteSweep, contentDescription = "Clear Chat log", tint = Color.White.copy(alpha = 0.5f))
                }
            }

            // Message Scroll Area
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(chatHistory) { msg ->
                    val isMe = msg.sender == "kamlesh_novalink"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = if (isMe) 16.dp else 4.dp,
                                        bottomEnd = if (isMe) 4.dp else 16.dp
                                    )
                                )
                                .background(
                                    if (isMe) accent.copy(alpha = 0.25f) else Color(0xFF131024)
                                )
                                .border(
                                    1.dp,
                                    if (isMe) accent.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.05f),
                                    RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = if (isMe) 16.dp else 4.dp,
                                        bottomEnd = if (isMe) 4.dp else 16.dp
                                    )
                                )
                                .padding(12.dp)
                                .widthIn(max = 280.dp)
                        ) {
                            Column {
                                if (msg.isVoice) {
                                    // Custom visual layout representing an audio waveform visualizer
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play voice audio", tint = accent)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        // Draw multiple bars resembling waveform
                                        Row(
                                            modifier = Modifier.height(18.dp),
                                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            val bars = listOf(8, 14, 18, 10, 16, 12, 6, 14, 10)
                                            bars.forEach { h ->
                                                Box(
                                                    modifier = Modifier
                                                        .width(2.dp)
                                                        .height(h.dp)
                                                        .background(accent, RoundedCornerShape(1.dp))
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text("${msg.voiceDurationSec}s", color = Color.White, fontSize = 11.sp)
                                    }
                                } else {
                                    Text(text = msg.text, color = Color.White, fontSize = 13.sp, lineHeight = 18.sp)
                                }
                            }
                        }
                    }
                }

                if (isAiChatting) {
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF131024)),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                    CircularProgressIndicator(color = accent, strokeWidth = 2.dp, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Nova AI is synthesizing...", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Bottom bar holding text field or active voice simulation UI
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .background(Color(0xFF0F0C1D))
                    .padding(10.dp)
            ) {
                if (isRecordingSim) {
                    // Recording simulator strip (Neon pulse wave)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1B1228), RoundedCornerShape(24.dp))
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(Color.Red, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("RECORDING CONSCIOUSNESS AUDIO... ${recordingTimer}s", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Text(
                            "CANCEL",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { isRecordingSim = false }
                                .padding(4.dp)
                        )

                        Text(
                            "SEND",
                            color = accent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable {
                                    viewModel.handleSendChatMessage("", isVoice = true, voiceDuration = recordingTimer)
                                    isRecordingSim = false
                                }
                                .padding(4.dp)
                        )
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Voice message simulation button
                        IconButton(onClick = { isRecordingSim = true }) {
                            Icon(imageVector = Icons.Default.Mic, contentDescription = "Simulate voice message", tint = accent)
                        }

                        TextField(
                            value = messageInput,
                            onValueChange = { messageInput = it },
                            placeholder = { Text("Synchronize message with AI...", color = Color.White.copy(alpha = 0.4f)) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF1B172D),
                                unfocusedContainerColor = Color(0xFF131024),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedIndicatorColor = accent
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("chat_input_field"),
                            shape = RoundedCornerShape(20.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (messageInput.isNotBlank()) {
                                    viewModel.handleSendChatMessage(messageInput.trim())
                                    messageInput = ""
                                }
                            },
                            modifier = Modifier
                                .background(accent, CircleShape)
                                .size(40.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Send, contentDescription = "Send text message", tint = Color.Black)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// USER PROFILE SCREEN (WITH INTERACTIVE ANALYTICS CANVAS GRAPH)
// -------------------------------------------------------------

@Composable
fun UserProfileScreen(viewModel: MainViewModel) {
    val myProfile by viewModel.currentUser.collectAsState()
    val curMood by viewModel.selectedMood.collectAsState()
    val posts by viewModel.allPosts.collectAsState()
    val accent = getMoodColor(curMood)

    LaunchedEffect(myProfile) {
        if (myProfile == null) {
            viewModel.navigateTo("login")
        }
    }

    if (myProfile == null) {
        return
    }

    NeonGradientBackground(curMood) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header actions banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Nova Node: ${myProfile!!.username}",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    VerificationBadge()
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.navigateTo("settings") }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                    }
                    IconButton(onClick = { viewModel.logout() }) {
                        Icon(imageVector = Icons.Default.Logout, contentDescription = "Logout", tint = Color.White.copy(alpha = 0.6f))
                    }
                }
            }

            // Core Profile statistics Row
            GlassyCard(
                mood = curMood,
                borderColorAlpha = 0.14f,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = myProfile!!.avatarUrl,
                        contentDescription = "My Headshot pic",
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .border(2.dp, accent, CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileStatItem(label = "Followers", value = "14.2K")
                        ProfileStatItem(label = "Following", value = "485")
                        ProfileStatItem(label = "Grid Posts", value = "${posts.filter { it.username == myProfile!!.username }.size}")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bio editor block
                Text(
                    text = myProfile!!.handle,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = myProfile!!.bio,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Action Buttons inside Profile
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { viewModel.navigateTo("upload") },
                        colors = ButtonDefaults.buttonColors(containerColor = accent),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Add Post Stream", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { viewModel.navigateTo("settings") },
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Personalize Theme", color = Color.White, fontSize = 12.sp)
                    }
                }
            }

            // High Fidelity Custom Canvas Analytics dashboard
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Intelligence Insights Engine",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 10.dp)
            )

            GlassyCard(
                mood = curMood,
                borderColorAlpha = 0.12f,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
            ) {
                Text(
                    text = "Weekly Engagement index (Consensus Sync)",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )

                // High fidelity analytics graphic representation via Canvas
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(vertical = 12.dp)
                ) {
                    val points = listOf(0.15f, 0.42f, 0.35f, 0.78f, 0.54f, 0.92f, 0.75f)
                    val stepX = size.width / (points.size - 1)
                    val path = androidx.compose.ui.graphics.Path()

                    points.forEachIndexed { idx, value ->
                        val x = idx * stepX
                        val y = size.height - (value * size.height)
                        if (idx == 0) {
                            path.moveTo(x, y)
                        } else {
                            path.lineTo(x, y)
                        }
                    }

                    // Draw gradient stroke path line
                    drawPath(
                        path = path,
                        brush = Brush.linearGradient(listOf(accent, Color(0xFF38BDF8))),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 4.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )

                    // Draw filled glowing area under graph
                    val fillPath = androidx.compose.ui.graphics.Path().apply {
                        addPath(path)
                        lineTo(size.width, size.height)
                        lineTo(0f, size.height)
                        close()
                    }
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            listOf(accent.copy(alpha = 0.2f), Color.Transparent)
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                        Text(day, color = Color.White.copy(alpha = 0.4f), fontSize = 10.sp)
                    }
                }
            }

            // Grid items mapping user posts
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Cyber Node Visual Grid",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 10.dp)
            )

            val myPosts = posts.filter { it.username == myProfile!!.username }
            if (myPosts.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = Icons.Default.VideoLibrary, contentDescription = "Empty list", tint = accent.copy(alpha = 0.4f), modifier = Modifier.size(36.dp))
                    Text("Your cyberspace gallery is blank.", color = Color.White.copy(alpha = 0.4f), fontSize = 12.sp, modifier = Modifier.padding(top = 10.dp))
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(start = 14.dp, end = 14.dp, bottom = 80.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(myPosts) { post ->
                        AsyncImage(
                            model = post.imageUrl,
                            contentDescription = "grid item thumbnail",
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
    }
}

// -------------------------------------------------------------
// NOTIFICATIONS LIST SCREEN
// -------------------------------------------------------------

@Composable
fun NotificationsScreen(viewModel: MainViewModel) {
    val alerts by viewModel.notifications.collectAsState()
    val curMood by viewModel.selectedMood.collectAsState()
    val accent = getMoodColor(curMood)

    NeonGradientBackground(curMood) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateTo("home") }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("Sentinel Radar Notifications", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(alerts) { alert ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF131024).copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(14.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = alert.avatarUrl,
                            contentDescription = "Notification avatar",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "@${alert.username}",
                                color = accent,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = alert.message,
                                color = Color.White,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            Text(
                                text = alert.timestamp,
                                color = Color.White.copy(alpha = 0.4f),
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        if (alert.isActionRequired) {
                            Button(
                                onClick = { /* Approve creator */ },
                                colors = ButtonDefaults.buttonColors(containerColor = accent),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Text("SYNC APPROVED", color = Color.Black, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// SEARCH / EXPLORE TRENDING PAGE
// -------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(viewModel: MainViewModel) {
    val posts by viewModel.allPosts.collectAsState()
    val curMood by viewModel.selectedMood.collectAsState()
    val accent = getMoodColor(curMood)

    var searchQuery by remember { mutableStateOf("") }
    val categories = listOf("All", "NeonArt", "Astronuts", "Synthetics", "MetaDev", "LowfiMusic")
    var activeCategory by remember { mutableStateOf("All") }

    val filteredPosts = posts.filter { post ->
        val matchSearch = searchQuery.isBlank() ||
            post.username.contains(searchQuery, ignoreCase = true) ||
            post.caption.contains(searchQuery, ignoreCase = true) ||
            post.hashtags.contains(searchQuery, ignoreCase = true)

        val matchCategory = activeCategory == "All" ||
            post.hashtags.contains(activeCategory, ignoreCase = true) ||
            post.caption.contains(activeCategory, ignoreCase = true)

        matchSearch && matchCategory
    }

    NeonGradientBackground(curMood) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Live Search Input Box
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search users, futuristic aesthetics, hashtags...", color = Color.White.copy(alpha = 0.4f)) },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon", tint = accent) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF141124),
                    unfocusedContainerColor = Color(0xFF0F0C1E),
                    focusedIndicatorColor = accent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillOuterContainerWidth(0.92f)
                    .padding(vertical = 12.dp)
                    .align(Alignment.CenterHorizontally)
                    .testTag("explore_search_field")
            )

            // Category Horizontal Selection
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
            ) {
                items(categories) { category ->
                    val selected = category == activeCategory
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (selected) accent.copy(alpha = 0.25f) else Color(0xFF141024).copy(alpha = 0.7f))
                            .border(BorderStroke(1.dp, if (selected) accent else Color.White.copy(alpha = 0.05f)), RoundedCornerShape(16.dp))
                            .clickable { activeCategory = category }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "#$category",
                            color = if (selected) Color.White else Color.White.copy(alpha = 0.6f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Grid Layout for masonry feeling explore visuals
            if (filteredPosts.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No visual nodes detected in this quadrant.", color = Color.White.copy(alpha = 0.4f), fontSize = 12.sp)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(filteredPosts) { post ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.04f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clickable { viewModel.selectCommentsFor(post) }
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                AsyncImage(
                                    model = post.imageUrl,
                                    contentDescription = "Explore post media",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )

                                // Visual Neon overlays for trending feeling
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                                            )
                                        )
                                )

                                Row(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(imageVector = Icons.Default.Favorite, contentDescription = "Likes tag", tint = Color.Red, modifier = Modifier.size(10.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("${post.likesCount}", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("@${post.username}", color = accent, fontSize = 9.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Utility to ensure modifiers are robust
private fun Modifier.fillOuterContainerWidth(fraction: Float = 0.9f) : Modifier {
    return this.fillMaxWidth(fraction)
}

// -------------------------------------------------------------
// POST CREATION / UPLOAD SCREEN (WITH CAPTION ENGINE GENERATOR)
// -------------------------------------------------------------

@Composable
fun UploadPostScreen(viewModel: MainViewModel) {
    val aiCaptionState by viewModel.aiCaptionState.collectAsState()
    val aiModState by viewModel.aiModState.collectAsState()
    val curMood by viewModel.selectedMood.collectAsState()
    val accent = getMoodColor(curMood)

    var ideaInput by remember { mutableStateOf("") }
    var captionInput by remember { mutableStateOf("") }
    var hashtagsInput by remember { mutableStateOf("") }
    var imageUrlInput by remember { mutableStateOf("") }
    var isReelCheck by remember { mutableStateOf(false) }
    var uploadStatusMessage by remember { mutableStateOf("") }

    NeonGradientBackground(curMood) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateTo("home") }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Cancel", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("Synthesize New Stream Post", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            // Wizard input fields
            GlassyCard(
                mood = curMood,
                borderColorAlpha = 0.14f,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text("1. Visual Media Parameters", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = imageUrlInput,
                    onValueChange = { imageUrlInput = it },
                    placeholder = { Text("https://images.unsplash.com/your-neon-asset...", color = Color.White.copy(alpha = 0.4f)) },
                    label = { Text("Unsplash / Asset Media URL (Optional)", color = Color.White.copy(alpha = 0.5f)) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1B172D),
                        unfocusedContainerColor = Color(0xFF131024),
                        focusedIndicatorColor = accent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isReelCheck,
                        onCheckedChange = { isReelCheck = it },
                        colors = CheckboxDefaults.colors(checkedColor = accent)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Publish as an interactive Vertical Reel", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // AI Caption generation workshop card
            GlassyCard(
                mood = curMood,
                borderColorAlpha = 0.14f,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "AI tool", tint = Color(0xFF22D3EE), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("2. Nova Cap & Suggestion Forge", color = Color(0xFF22D3EE), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = ideaInput,
                    onValueChange = { ideaInput = it },
                    placeholder = { Text("Describe post visual theme e.g., lowfi cafe in purple light...", color = Color.White.copy(alpha = 0.4f)) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1B172D),
                        unfocusedContainerColor = Color(0xFF131024),
                        focusedIndicatorColor = accent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { viewModel.generateAICaptionPrompt(ideaInput, "Sleek and Futuristic") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B172D)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Futuristic Cap", color = Color.White, fontSize = 10.sp)
                    }

                    Button(
                        onClick = { viewModel.generateAICaptionPrompt(ideaInput, "Cyberpunk lowfi") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B172D)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cyber Cap", color = Color.White, fontSize = 10.sp)
                    }
                }

                when (val state = aiCaptionState) {
                    is NetworkState.Loading -> {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(color = accent, strokeWidth = 1.5.dp, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Assistant is writing captions...", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                        }
                    }
                    is NetworkState.Success -> {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Generated Draft:",
                            color = Color(0xFF22D3EE),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF0F0B22), RoundedCornerShape(8.dp))
                                .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), RoundedCornerShape(8.dp))
                                .clickable {
                                    // Copy text content into inputs
                                    captionInput = state.result
                                        .substringBefore("#")
                                        .trim()
                                    hashtagsInput = "#" + state.result
                                        .substringAfter("#")
                                        .trim()
                                    viewModel.resetAICaptionState()
                                }
                                .padding(10.dp)
                        ) {
                            Text(state.result, color = Color.White, fontSize = 11.sp, lineHeight = 16.sp)
                        }
                        Text(
                            "Tap the draft card to copy parameters safely to standard input.",
                            color = Color.White.copy(alpha = 0.4f),
                            fontSize = 9.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    else -> {}
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Text input configurations
            GlassyCard(
                mood = curMood,
                borderColorAlpha = 0.14f,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text("3. Core Metadata & Feed Category", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = captionInput,
                    onValueChange = { captionInput = it },
                    placeholder = { Text("Type coordinates and captions manually...", color = Color.White.copy(alpha = 0.4f)) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1B172D),
                        unfocusedContainerColor = Color(0xFF131024),
                        focusedIndicatorColor = accent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("post_caption_box")
                )

                Spacer(modifier = Modifier.height(12.dp))

                TextField(
                    value = hashtagsInput,
                    onValueChange = { hashtagsInput = it },
                    placeholder = { Text("#cyberpunk #lowfi #neotokyo #novalink...", color = Color.White.copy(alpha = 0.4f)) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1B172D),
                        unfocusedContainerColor = Color(0xFF131024),
                        focusedIndicatorColor = accent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            if (uploadStatusMessage.isNotEmpty()) {
                Text(
                    text = uploadStatusMessage,
                    color = accent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            when (val state = aiModState) {
                is NetworkState.Loading -> {
                    CircularProgressIndicator(
                        color = accent,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 12.dp)
                    )
                }
                is NetworkState.Error -> {
                    Text(
                        text = state.error,
                        color = Color(0xFFF43F5E),
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }
                else -> {}
            }

            // Publish submission controller button
            Button(
                onClick = {
                    uploadStatusMessage = ""
                    viewModel.handleUploadPost(
                        caption = captionInput,
                        imageUrl = imageUrlInput,
                        isReel = isReelCheck,
                        hashtags = hashtagsInput,
                        mood = curMood,
                        onSuccess = {
                            uploadStatusMessage = ""
                            viewModel.navigateTo("home")
                        },
                        onFailure = { err ->
                            uploadStatusMessage = err
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = accent),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillOuterContainerWidth(0.92f)
                    .height(50.dp)
                    .padding(vertical = 12.dp)
                    .align(Alignment.CenterHorizontally)
                    .testTag("submit_post_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.AllInclusive, contentDescription = "Publish", tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SYNCHRONIZE TO SPACE FEED", color = Color.Black, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// SETTINGS PAGE
// -------------------------------------------------------------

@Composable
fun SettingsPageScreen(viewModel: MainViewModel) {
    val curMood by viewModel.selectedMood.collectAsState()
    val myProfile by viewModel.currentUser.collectAsState()
    val accent = getMoodColor(curMood)

    NeonGradientBackground(curMood) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateTo("profile") }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Return profile", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("Cyber Workspace Controls", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131024).copy(alpha = 0.5f)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("User Synced Node Configuration", color = accent, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Active Profile Handle", color = Color.White, fontSize = 12.sp)
                        Text("@${myProfile?.username ?: "resolving..."}", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Personalized Color space Hues", color = Color.White, fontSize = 12.sp)
                        Text(curMood, color = accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131024).copy(alpha = 0.5f)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("AI Integration Parameters Status", color = Color(0xFF22D3EE), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "NovaLink coordinates with Google Gemini models inside the workspace utilizing cloud secrets parameters. To modify the AI capabilities or supply different custom keys, navigate to your AI Studio panel and edit the GEMINI_API_KEY secret.",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Current Platform Level: Prototyping",
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Security Warning: I have included your API keys in the generated APK file for this prototype. Please be aware that Android APKs can be easily decompiled, and these keys can be extracted by anyone who has access to the file. Do not share this APK file publicly or with unauthorized individuals to prevent potential misuse.",
                        color = Color(0xFFF43F5E).copy(alpha = 0.82f),
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}
