package id.dev.moody

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import id.dev.moody.ui.SongRecommendationScreen
import id.dev.moody.ui.SongRecommendationScreenSad
import id.dev.moody.ui.theme.MoodyTheme
import id.dev.novlityapp.R
import id.dev.moody.database.Song
import id.dev.moody.ui.SongRecommendationScreenRelax
import id.dev.moody.ui.SongRecommendationScreenSpirit
import id.dev.moody.ui.SongRecommendationScreenStress
import id.dev.moody.ui.BottomNavigationBar
import id.dev.moody.ui.NotesScreen
import id.dev.moody.ui.SettingsScreen
import id.dev.moody.ui.ThemeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkTheme by themeViewModel.isDarkTheme

            MoodyTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                val notesList = remember { mutableStateListOf<Pair<String, String>>() }

                NavHost(navController = navController, startDestination = "moodTracker") {
                    composable("moodTracker") {
                        MoodTrackerApp(
                            themeViewModel = themeViewModel,
                            onExploreSongsClick = { selectedMood ->
                                when (selectedMood) {
                                    "Bahagia" -> navController.navigate("songRecommendationBahagia")
                                    "Sedih" -> navController.navigate("songRecommendationSad")
                                    "Santai" -> navController.navigate("songRecommendationSantai")
                                    "Semangat" -> navController.navigate("songRecommendationSemangat")
                                    "Stress" -> navController.navigate("songRecommendationStress")
                                }
                            },
                            onSaveNote = { note ->
                                val currentTime = SimpleDateFormat(
                                    "dd/MM/yyyy HH:mm:ss",
                                    Locale.getDefault()
                                ).format(Date())
                                notesList.add(note to currentTime)
                            },
                            onViewNotesClick = { navController.navigate("notes") }
                        )
                    }
                    composable("settings") {
                        SettingsScreen(navController = navController, themeViewModel = themeViewModel)
                    }
                    composable("notes") {
                        NotesScreen(
                            navController = navController,
                            notesList = notesList,
                            onBack = { navController.popBackStack() },
                            onDeleteNote = { index ->
                                notesList.removeAt(index)
                            },
                            themeViewModel = themeViewModel
                        )
                    }
                    composable("songRecommendationBahagia") {
                        val songs = getSongsForMood("Bahagia")
                        SongRecommendationScreen(
                            navController = navController,
                            selectedMood = "Bahagia",
                            songs = songs,
                            onBack = { navController.popBackStack() },
                            themeViewModel = themeViewModel
                        )
                    }
                    composable("songRecommendationSantai") {
                        val songs = getSongsForMood("Santai")
                        SongRecommendationScreenRelax(
                            navController = navController,
                            songs = songs,
                            onBack = { navController.popBackStack() },
                            themeViewModel = themeViewModel
                        )
                    }
                    composable("songRecommendationSemangat") {
                        val songs = getSongsForMood("Semangat")
                        SongRecommendationScreenSpirit(
                            navController = navController,
                            songs = songs,
                            onBack = { navController.popBackStack() },
                            themeViewModel = themeViewModel
                        )
                    }
                    composable("songRecommendationStress") {
                        val songs = getSongsForMood("Stress")
                        SongRecommendationScreenStress(
                            navController = navController,
                            songs = songs,
                            onBack = { navController.popBackStack() },
                            themeViewModel = themeViewModel
                        )
                    }
                    composable("songRecommendationSad") {
                        val songs = getSongsForMood("Sedih")
                        SongRecommendationScreenSad(
                            navController = navController,
                            songs = songs,
                            onBack = { navController.popBackStack() },
                            themeViewModel = themeViewModel
                        )
                    }
                }
            }
        }
    }

    private fun getSongsForMood(mood: String): List<Song> {
        return when (mood) {
            "Bahagia" -> listOf(
                Song("Happy Song", "Artist A", "Bahagia", R.raw.happy_song),
                Song("Joyful Melody", "Artist B", "Bahagia", R.raw.teeth),
                Song("Uplifting Tune", "Artist C", "Bahagia", R.raw.never)
            )
            "Sedih" -> listOf(
                Song("Sad Song", "Artist D", "Sedih", R.raw.goodbye),
                Song("Lonely Melody", "Artist E", "Sedih", R.raw.atlantis),
                Song("Melancholy Tune", "Artist F", "Sedih", R.raw.glimpse)
            )
            "Semangat" -> listOf(
                Song("Energy Booster", "Artist G", "Semangat", R.raw.best),
                Song("High Spirits", "Artist H", "Semangat", R.raw.best),
                Song("Powerful Beat", "Artist I", "Semangat", R.raw.best)
            )
            "Stress" -> listOf(
                Song("Calm Waves", "Artist J", "Stress", R.raw.best),
                Song("Relaxing Breeze", "Artist K", "Stress", R.raw.best),
                Song("Mindful Moments", "Artist L", "Stress", R.raw.best)
            )
            "Santai" -> listOf(
                Song("Easy Going", "Artist M", "Santai", R.raw.best),
                Song("Tranquil Tune", "Artist N", "Santai", R.raw.best),
                Song("Peaceful Melody", "Artist O", "Santai", R.raw.best)
            )
            else -> emptyList()
        }
    }
}



@Composable
fun MoodTrackerApp(
    modifier: Modifier = Modifier,
    themeViewModel: ThemeViewModel, // Tambahkan parameter ini
    onExploreSongsClick: (String) -> Unit,
    onSaveNote: (String) -> Unit,
    onViewNotesClick: () -> Unit
) {
    val isDarkTheme by themeViewModel.isDarkTheme // Observasi tema dari ViewModel
    var selectedMood by remember { mutableStateOf("Bahagia") }
    var notes by remember { mutableStateOf("") }
    val moods = listOf("Bahagia", "Sedih", "Semangat", "Santai", "Stress")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDarkTheme) Color.Black else Color.White) // Terapkan tema gelap/terang
    ) {
        Image(
            painter = painterResource(id = R.drawable.inputbgr),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = if (isDarkTheme) 0.50f else 0.80f // Sesuaikan transparansi berdasarkan tema
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(100.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color.DarkGray else Color.White.copy(alpha = 0.7f))
            ) {
                @OptIn(ExperimentalMaterial3Api::class)
                TextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Catatan Perasaan Anda") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(8.dp),
                    textStyle = LocalTextStyle.current.copy(
                        color = if (isDarkTheme) Color.White else Color.Black // Sesuaikan warna teks
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = if (isDarkTheme) Color.White else Color(0xFF000000),
                        unfocusedIndicatorColor = if (isDarkTheme) Color.Gray else Color.LightGray
                    )
                )

            }

            Button(
                onClick = {
                    if (notes.isNotBlank()) {
                        onSaveNote.invoke(notes)
                        notes = "" // Reset input setelah menyimpan catatan
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isDarkTheme) Color.Gray else Color(0xFF000000))
            ) {
                Text("Simpan Catatan", color = if (isDarkTheme) Color.Black else Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color.DarkGray else Color.White.copy(alpha = 0.7f))
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        "Bagaimana Perasaanmu Hari Ini?",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color.White else Color(0xFF000000)
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    moods.forEach { mood ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .border(1.dp, if (isDarkTheme) Color.White else Color.Gray.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                                    .background(
                                        color = if (mood == selectedMood) (if (isDarkTheme) Color.White else Color(0xFF000000)) else Color.Transparent,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .clickable { selectedMood = mood }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = mood,
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                                color = if (isDarkTheme) Color.White else Color(0xFF000000)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onExploreSongsClick(selectedMood) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isDarkTheme) Color.Gray else Color(0xFF000000))
            ) {
                Text("Eksplor Lagu", color = if (isDarkTheme) Color.Black else Color.White)
            }
        }
    }
}


