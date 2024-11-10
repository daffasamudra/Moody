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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import id.dev.moody.ui.SongRecommendationScreen
import id.dev.moody.ui.theme.MoodyTheme
import id.dev.novlityapp.R
import id.dev.moody.database.Song

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoodyTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "moodTracker") {
                    composable("moodTracker") {
                        MoodTrackerApp(
                            onExploreSongsClick = { selectedMood ->
                                navController.navigate("songRecommendation/$selectedMood")
                            }
                        )
                    }
                    composable("songRecommendation/{mood}") { backStackEntry ->
                        val mood = backStackEntry.arguments?.getString("mood") ?: "Bahagia"
                        val songs = getSongsForMood(mood)
                        SongRecommendationScreen(
                            selectedMood = mood,
                            songs = songs,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }

    // Fungsi untuk mendapatkan daftar lagu berdasarkan mood
    private fun getSongsForMood(mood: String): List<Song> {
        return when (mood) {
            "Bahagia" -> listOf(
                Song("Happy Song", "Artist A", "Bahagia", R.raw.happy_song)
            )
            else -> emptyList()
        }
    }
}

@Composable
fun MoodTrackerApp(modifier: Modifier = Modifier, onExploreSongsClick: (String) -> Unit) {
    var selectedMood by remember { mutableStateOf("Bahagia") }
    var notes by remember { mutableStateOf("") }
    var savedNotes by remember { mutableStateOf("") }
    val moods = listOf("Bahagia", "Sedih", "Bersemangat", "Santai", "Stress")

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.retro),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.50f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f))
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
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFF9C27B0),
                        unfocusedIndicatorColor = Color.LightGray
                    )
                )
            }

            Button(
                onClick = { savedNotes = notes },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
            ) {
                Text("Simpan Catatan", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (savedNotes.isNotEmpty()) {
                Text(
                    text = "Catatan Tersimpan: $savedNotes",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp),
                    color = Color.DarkGray
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f))
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        "Bagaimana Perasaanmu Hari Ini?",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9C27B0)
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
                                    .border(1.dp, Color.Gray.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                                    .background(
                                        color = if (mood == selectedMood) Color(0xFF9C27B0) else Color.Transparent,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .clickable { selectedMood = mood }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = mood,
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                                color = Color(0xFF9C27B0)
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
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
            ) {
                Text("Eksplor Lagu", color = Color.White)
            }
        }
    }
}