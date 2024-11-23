package id.dev.moody.ui

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import id.dev.moody.database.Song
import id.dev.novlityapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnimatedBackgroundHappy(modifier: Modifier = Modifier, isDarkTheme: Boolean) {
    val infiniteTransition = rememberInfiniteTransition()

    val color1 by infiniteTransition.animateColor(
        initialValue = if (isDarkTheme) Color(0xFF5E35B1) else Color(0xFFF1B125),
        targetValue = if (isDarkTheme) Color(0xFF1C1F26) else Color(0xFFE5D73C),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val color2 by infiniteTransition.animateColor(
        initialValue = if (isDarkTheme) Color(0xFF0D1117) else Color(0xFFE5D73C),
        targetValue = if (isDarkTheme) Color(0xFF1C1F26) else Color(0xFFF1B125),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(color1, color2)
                )
            )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongRecommendationScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel,
    selectedMood: String,
    onBack: () -> Unit,
    songs: List<Song>
) {
    val context = LocalContext.current
    val isDarkTheme by themeViewModel.isDarkTheme
    val coroutineScope = rememberCoroutineScope()

    val mediaPlayerHelper = remember {
        MediaPlayerHelper(context, songs, coroutineScope)
    }

    DisposableEffect(Unit) {
        mediaPlayerHelper.setOnCompletionListener()

        onDispose {
            mediaPlayerHelper.release()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rekomendasi Lagu - $selectedMood") },
                navigationIcon = {
                    IconButton(onClick = {
                        mediaPlayerHelper.pauseSong()
                        onBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = if (isDarkTheme) Color.DarkGray else Color.White,
                    titleContentColor = if (isDarkTheme) Color.White else Color.Black
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, themeViewModel = themeViewModel)
        },
        containerColor = Color.Transparent
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AnimatedBackgroundHappy(isDarkTheme = isDarkTheme)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fotohappy),
                    contentDescription = "Mood Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkTheme) Color.Gray else Color(0xFFFFEB3B)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(onClick = { mediaPlayerHelper.previousSong() }) {
                                Icon(
                                    Icons.Rounded.SkipPrevious,
                                    contentDescription = "Previous",
                                    tint = if (isDarkTheme) Color.White else Color.Black
                                )
                            }
                            IconButton(onClick = {
                                mediaPlayerHelper.playOrPauseSong()
                            }) {
                                Icon(
                                    if (mediaPlayerHelper.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (mediaPlayerHelper.isPlaying) "Pause" else "Play",
                                    tint = if (isDarkTheme) Color.White else Color.Black
                                )
                            }
                            IconButton(onClick = { mediaPlayerHelper.skipSong() }) {
                                Icon(
                                    Icons.Rounded.SkipNext,
                                    contentDescription = "Next",
                                    tint = if (isDarkTheme) Color.White else Color.Black
                                )
                            }
                        }

                        // Slider for controlling the song position
                        Slider(
                            value = mediaPlayerHelper.currentPosition.toFloat(),
                            onValueChange = { newPosition ->
                                mediaPlayerHelper.seekTo(newPosition.toInt())
                            },
                            valueRange = 0f..mediaPlayerHelper.duration.toFloat(),
                            colors = SliderDefaults.colors(
                                thumbColor = if (isDarkTheme) Color.White else Color.Black,
                                activeTrackColor = if (isDarkTheme) Color.White else Color.Black
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Display current and total song time
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = formatTime(mediaPlayerHelper.currentPosition),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = formatTime(mediaPlayerHelper.duration),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(songs) { index, song ->
                        SongListItem(index + 1, song, if (isDarkTheme) Color.Gray else Color(0xFFFFEB3B)) {
                            mediaPlayerHelper.currentSongIndex = index
                            mediaPlayerHelper.playSong(index)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SongListItem(index: Int, song: Song, backgroundColor: Color, onPlayClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onPlayClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$index.",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                modifier = Modifier.padding(end = 16.dp)
            )
            Column {
                Text(
                    text = song.title,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "By ${song.artist}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                )
            }
        }
    }
}

fun formatTime(milliseconds: Int): String {
    val minutes = (milliseconds / 1000) / 60
    val seconds = (milliseconds / 1000) % 60
    return "%d:%02d".format(minutes, seconds)
}
