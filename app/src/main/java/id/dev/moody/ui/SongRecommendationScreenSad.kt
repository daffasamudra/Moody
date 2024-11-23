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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import id.dev.moody.database.Song
import id.dev.novlityapp.R

// Impor BottomNavigationBar dari BottomNavigationBar.kt

@Composable
fun AnimatedBackgroundSad(modifier: Modifier = Modifier) {
    // Create an infinite transition for the animation
    val infiniteTransition = rememberInfiniteTransition()

    // Define animated colors for the gradient
    val color1 by infiniteTransition.animateColor(
        initialValue = Color(0xFF536DFE), // Soft Blue
        targetValue = Color(0xFF8E99F3), // Light Lavender
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val color2 by infiniteTransition.animateColor(
        initialValue = Color(0xFF8E99F3), // Light Lavender
        targetValue = Color(0xFF3949AB), // Deep Blue
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Apply the animated gradient as the background
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
fun SongRecommendationScreenSad(
    navController: NavController,
    themeViewModel: ThemeViewModel,
    onBack: () -> Unit,
    mediaPlayer: MediaPlayer = MediaPlayer(),
    songs: List<Song> = emptyList()
) {
    val context = LocalContext.current
    var currentSongIndex by remember { mutableStateOf(0) } // Index of the currently playing song
    var isPlaying by remember { mutableStateOf(false) }
    val isDarkTheme by themeViewModel.isDarkTheme

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    // Function to skip to the next song
    fun skipSong() {
        currentSongIndex = (currentSongIndex + 1) % songs.size // Loop back to the first song
        playOrPauseSongSad(context, mediaPlayer, songs[currentSongIndex]) { isPlaying = it }
    }

    // Function to go to the previous song
    fun previousSong() {
        currentSongIndex = if (currentSongIndex > 0) currentSongIndex - 1 else songs.size - 1
        playOrPauseSongSad(context, mediaPlayer, songs[currentSongIndex]) { isPlaying = it }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rekomendasi Lagu - Sedih") },
                navigationIcon = {
                    IconButton(onClick = {
                        mediaPlayer.stop()
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
        containerColor = Color.Transparent // Allow the animated background to be visible
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Animated Background for Sad Mood
            AnimatedBackgroundSad()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ftsedih),
                    contentDescription = "Gambar Mood Sedih",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Even the darkest night will end and the sun will rise.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = if (isDarkTheme) Color.White else Color.Black,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkTheme) Color.Gray else Color(0xFF7189FF)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.iconsad),
                            contentDescription = "Album Art Sedih",
                            modifier = Modifier.size(50.dp)
                        )
                        IconButton(onClick = { previousSong() }) {
                            Icon(
                                Icons.Rounded.SkipPrevious,
                                contentDescription = "Previous",
                                tint = if (isDarkTheme) Color.LightGray else Color.White
                            )
                        }
                        IconButton(onClick = { playOrPauseSongSad(context, mediaPlayer, songs[currentSongIndex]) { isPlaying = it } }) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = if (isDarkTheme) Color.LightGray else Color.White
                            )
                        }
                        IconButton(onClick = { skipSong() }) {
                            Icon(
                                Icons.Rounded.SkipNext,
                                contentDescription = "Next",
                                tint = if (isDarkTheme) Color.LightGray else Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(songs) { index, song ->
                        SongListItemSad(index + 1, song, if (isDarkTheme) Color.Gray else Color(0xFF7189FF)) {
                            currentSongIndex = index
                            playOrPauseSongSad(context, mediaPlayer, song) { isPlaying = it }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SongListItemSad(index: Int, song: Song, backgroundColor: Color, onPlayClick: () -> Unit) {
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

// Fungsi untuk memainkan atau menjeda lagu
fun playOrPauseSongSad(
    context: Context,
    mediaPlayer: MediaPlayer,
    song: Song?,
    onPlaybackChange: (Boolean) -> Unit
) {
    if (song == null) return

    val resId = song.filePath  // Menggunakan filePath sebagai Int (ID resource langsung)
    if (resId == 0) return

    if (mediaPlayer.isPlaying) {
        mediaPlayer.pause()
        onPlaybackChange(false)
    } else {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(context, android.net.Uri.parse("android.resource://${context.packageName}/$resId"))
        mediaPlayer.prepare()
        mediaPlayer.start()
        onPlaybackChange(true)
    }

    mediaPlayer.setOnCompletionListener {
        onPlaybackChange(false) // Reset icon to Play when song finishes
    }
}


