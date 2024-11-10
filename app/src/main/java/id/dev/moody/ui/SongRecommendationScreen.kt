package id.dev.moody.ui

import android.content.Context
import android.media.MediaPlayer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.dev.moody.database.Song
import id.dev.novlityapp.R
import kotlinx.coroutines.launch

@Composable
fun SongRecommendationScreen(
    selectedMood: String,
    onBack: () -> Unit,
    mediaPlayer: MediaPlayer = MediaPlayer(),
    songs: List<Song> = emptyList()
) {
    val context = LocalContext.current
    var currentSong by remember { mutableStateOf<Song?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    // Bersihkan MediaPlayer saat keluar dari layar
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rekomendasi Lagu - $selectedMood") },
                navigationIcon = {
                    IconButton(onClick = {
                        mediaPlayer.stop() // Hentikan pemutaran saat kembali
                        onBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.LightGray)
                .padding(16.dp)
        ) {
            // Gambar mood di bagian atas
            Image(
                painter = painterResource(id = R.drawable.retro),
                contentDescription = "Gambar Mood",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color.Gray)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Teks "Quotes" di bawah gambar
            Text(
                text = "Quotes",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Pemutar musik sederhana
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.retro),
                        contentDescription = "Album Art",
                        modifier = Modifier.size(50.dp)
                    )
                    IconButton(onClick = { /* Placeholder untuk lagu sebelumnya */ }) {
                        Icon(Icons.Rounded.SkipPrevious, contentDescription = "Previous")
                    }
                    IconButton(onClick = { playOrPauseSong(context, mediaPlayer, currentSong) { isPlaying = it } }) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play"
                        )
                    }
                    IconButton(onClick = { /* Placeholder untuk lagu berikutnya */ }) {
                        Icon(Icons.Rounded.SkipNext, contentDescription = "Next")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Daftar lagu di bawahnya
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(songs) { index, song ->
                    SongListItem(index + 1, song) {
                        currentSong = song
                        playOrPauseSong(context, mediaPlayer, song) { isPlaying = it }
                    }
                }
            }
        }
    }
}

@Composable
fun SongListItem(index: Int, song: Song, onPlayClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onPlayClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$index.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(end = 16.dp)
            )
            Column {
                Text(text = song.title, fontWeight = FontWeight.Bold)
                Text(text = "By ${song.artist}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

fun playOrPauseSong(
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

@Preview(showBackground = true)
@Composable
fun SongRecommendationScreenPreview() {
    val dummySongs = listOf(
        Song(title = "Happy Song", artist = "Artist A", mood = "Bahagia", filePath = R.raw.happy_song)
    )

    SongRecommendationScreen(selectedMood = "Bahagia", onBack = {}, songs = dummySongs)
}
