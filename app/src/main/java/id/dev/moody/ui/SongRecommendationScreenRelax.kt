package id.dev.moody.ui

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.Image
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongRecommendationScreenRelax(
    navController: NavController,
    themeViewModel: ThemeViewModel,
    onBack: () -> Unit,
    mediaPlayer: MediaPlayer = MediaPlayer(),
    songs: List<Song> = emptyList()
) {
    val context = LocalContext.current
    var currentSongIndex by remember { mutableStateOf(0) } // Menyimpan indeks lagu yang sedang diputar
    var isPlaying by remember { mutableStateOf(false) }
    val isDarkTheme by themeViewModel.isDarkTheme

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    // Fungsi untuk melanjutkan ke lagu berikutnya (skip)
    fun skipSong() {
        currentSongIndex = (currentSongIndex + 2) % songs.size // Jika lagu terakhir, kembali ke lagu pertama
        PlayOrPauseSongRelax(context, mediaPlayer, songs[currentSongIndex]) { isPlaying = it }
    }

    // Fungsi untuk kembali ke lagu sebelumnya (previous)
    fun previousSong() {
        currentSongIndex = if (currentSongIndex > 0) currentSongIndex - 1 else songs.size - 3 // Jika lagu pertama, kembali ke lagu terakhir
        PlayOrPauseSongRelax(context, mediaPlayer, songs[currentSongIndex]) { isPlaying = it }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rekomendasi Lagu - Santai") },
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
        containerColor = if (isDarkTheme) Color.Black else Color.White
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bgrsantai),
                contentDescription = "Background Santai",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = if (isDarkTheme) 0.5f else 1f
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ftsantai), // Gambar mood santai
                    contentDescription = "Gambar Mood Santai",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Take a deep breath, and let the music soothe your soul.",
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
                        containerColor = if (isDarkTheme) Color.Gray else Color(0xFF20AD2C)
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
                            painter = painterResource(id = R.drawable.iconsantai), // Placeholder untuk album art santai
                            contentDescription = "Album Art Santai",
                            modifier = Modifier.size(50.dp)
                        )
                        IconButton(onClick = { previousSong() }) {
                            Icon(
                                Icons.Rounded.SkipPrevious,
                                contentDescription = "Previous",
                                tint = if (isDarkTheme) Color.LightGray else Color.White
                            )
                        }
                        IconButton(onClick = { PlayOrPauseSongRelax(context, mediaPlayer, songs[currentSongIndex]) { isPlaying = it } }) {
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
                        SongListItemRelax(index + 1, song, if (isDarkTheme) Color.Gray else Color(0xFF20AD2C)) {
                            currentSongIndex = index
                            PlayOrPauseSongRelax(context, mediaPlayer, song) { isPlaying = it }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SongListItemRelax(index: Int, song: Song, backgroundColor: Color, onPlayClick: () -> Unit) {
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

fun PlayOrPauseSongRelax(
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
