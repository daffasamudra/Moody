package id.dev.moody

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
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
import id.dev.moody.ui.LoginScreen
import id.dev.moody.ui.NotesScreen
import id.dev.moody.ui.RegisterScreen
import id.dev.moody.ui.SettingsScreen
import id.dev.moody.ui.ThemeViewModel
import id.dev.moody.ui.StatisticsScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkTheme by themeViewModel.isDarkTheme
            val navController = rememberNavController()
            val notesList = remember { mutableStateListOf<Pair<String, String>>() }

            MoodyTheme(darkTheme = isDarkTheme) {
                MainNavHost(
                    navController = navController,
                    themeViewModel = themeViewModel,
                    notesList = notesList
                )
            }
        }
    }

    @Composable
    fun MainNavHost(
        navController: NavHostController,
        themeViewModel: ThemeViewModel,
        notesList: MutableList<Pair<String, String>>
    ) {
        val context = LocalContext.current

        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                LoginScreen(navController = navController)
            }
            composable("register") {
                RegisterScreen(navController = navController)
            }

            // Mood Tracker
            composable("moodTracker") {
                MoodTrackerApp(
                    themeViewModel = themeViewModel,
                    onExploreSongsClick = { selectedMood ->
                        incrementMoodCount(context, selectedMood) // Menyimpan mood yang dipilih ke statistik
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

            // Settings Screen
            composable("settings") {
                SettingsScreen(
                    navController = navController,
                    themeViewModel = themeViewModel,
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo("moodTracker") { inclusive = true }
                        }
                    }
                )
            }

            // Statistik Screen
            composable("statistik") {
                StatisticsScreen(navController = navController)
            }

            // Notes Screen
            composable("notes") {
                NotesScreen(
                    navController = navController,
                    notesList = notesList,
                    onBack = { navController.popBackStack() },
                    onDeleteNote = { index -> notesList.removeAt(index) },
                    themeViewModel = themeViewModel
                )
            }

            // Song Recommendation Screens
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

    // Fungsi untuk menyimpan statistik mood ke SharedPreferences
    private fun incrementMoodCount(context: Context, mood: String) {
        val sharedPreferences = context.getSharedPreferences("mood_data", Context.MODE_PRIVATE)
        val currentCount = sharedPreferences.getInt(mood, 0)
        sharedPreferences.edit()
            .putInt(mood, currentCount + 1)
            .apply()
    }

    // Fungsi untuk mendapatkan lagu berdasarkan mood
    private fun getSongsForMood(mood: String): List<Song> {
        return when (mood) {
            "Bahagia" -> listOf(
                Song("Happy Song", "Artist A", "Bahagia", R.raw.happy_song, 180),
                Song("Joyful Melody", "Artist B", "Bahagia", R.raw.teeth, 240),
                Song("Uplifting Tune", "Artist C", "Bahagia", R.raw.never, 200)
            )
            "Sedih" -> listOf(
                Song("Sad Song", "Artist D", "Sedih", R.raw.goodbye, 210),
                Song("Lonely Melody", "Artist E", "Sedih", R.raw.atlantis, 230),
                Song("Melancholy Tune", "Artist F", "Sedih", R.raw.glimpse, 250)
            )
            "Semangat" -> listOf(
                Song("Energy Booster", "Artist G", "Semangat", R.raw.best, 300),
                Song("High Spirits", "Artist H", "Semangat", R.raw.best, 280),
                Song("Powerful Beat", "Artist I", "Semangat", R.raw.best, 260)
            )
            "Stress" -> listOf(
                Song("Calm Waves", "Artist J", "Stress", R.raw.best, 240),
                Song("Relaxing Breeze", "Artist K", "Stress", R.raw.best, 260),
                Song("Mindful Moments", "Artist L", "Stress", R.raw.best, 220)
            )
            "Santai" -> listOf(
                Song("Easy Going", "Artist M", "Santai", R.raw.best, 240),
                Song("Tranquil Tune", "Artist N", "Santai", R.raw.best, 230),
                Song("Peaceful Melody", "Artist O", "Santai", R.raw.best, 250)
            )
            else -> emptyList()
        }
    }

@Composable
    fun AnimatedBackground(modifier: Modifier = Modifier, isDarkTheme: Boolean) {
        // Create an infinite transition for the animation
        val infiniteTransition = rememberInfiniteTransition()

        // Define animated colors for the gradient based on theme
        val color1 by infiniteTransition.animateColor(
            initialValue = if (isDarkTheme) Color(0xFF5E35B1) else Color(0xFF85FFBD), // Dark Gray or Light Yellow
            targetValue = if (isDarkTheme) Color(0xFF1C1F26) else Color(0xFF7FD8FF), // Darker Gray or Soft Orange
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        val color2 by infiniteTransition.animateColor(
            initialValue = if (isDarkTheme) Color(0xFF0D1117) else Color(0xFF7FD8FF), // Darker Gray or Vibrant Yellow
            targetValue = if (isDarkTheme) Color(0xFF1C1F26) else Color(0xFFFFC3A0), // Deep Dark Gray or Light Yellow
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


    @Composable
    fun MoodTrackerApp(
        modifier: Modifier = Modifier,
        themeViewModel: ThemeViewModel,
        onExploreSongsClick: (String) -> Unit,
        onSaveNote: (String) -> Unit,
        onViewNotesClick: () -> Unit
    ) {
        val isDarkTheme by themeViewModel.isDarkTheme
        var selectedMood by remember { mutableStateOf("Bahagia") }
        var notes by remember { mutableStateOf("") }
        val moods = listOf(
            "Bahagia" to "😊",
            "Santai" to "😌",
            "Stress" to "😡",
            "Sedih" to "😢",
            "Semangat" to "😆"
        )

        // Ambil nama pengguna dari SharedPreferences
        val context = LocalContext.current
        val sharedPreferences =
            context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "Pengguna") ?: "Pengguna"

        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            // Use the animated background
            AnimatedBackground(isDarkTheme = isDarkTheme)

            // Content of the MoodTrackerApp
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Menambahkan pengguliran
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(1.dp))

                // MoodifyText Image
                Image(
                    painter = painterResource(id = R.drawable.moodifytext), // Use your resource ID here
                    contentDescription = "Moodify Text Logo",
                    modifier = Modifier
                        .height(100.dp) // Adjust height as needed
                        .align(Alignment.Start) // Align the image to the top-left corner
                        .padding(start = 9.dp) // Add padding from the edges if needed
                        .size(115.dp)
                )

                // Teks ucapan selamat datang
                Text(
                    text = "Selamat datang, $username!",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color.Black
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Bagaimana mood Anda hari ini?",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        color = if (isDarkTheme) Color.White else Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkTheme) Color.DarkGray else Color.White.copy(
                            alpha = 0.7f
                        )
                    )
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
                            color = if (isDarkTheme) Color.White else Color.Black
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = if (isDarkTheme) Color.White else Color(
                                0xFF000000
                            ),
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDarkTheme) Color.Gray else Color(
                            0xFF000000
                        )
                    )
                ) {
                    Text("Simpan Catatan", color = if (isDarkTheme) Color.Black else Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkTheme) Color.DarkGray else Color.White.copy(
                            alpha = 0.7f
                        )
                    )
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

                        moods.forEach { (mood, emoji) ->
                            val isSelected = selectedMood == mood

                            // Animasi untuk ukuran emoji
                            val animatedScale = animateFloatAsState(
                                targetValue = if (isSelected) 1.5f else 1.0f
                            )

                            // Animasi untuk warna teks
                            val animatedColor by animateColorAsState(
                                targetValue = if (isSelected) Color(0xFFF3B81A) else if (isDarkTheme) Color.White else Color.Black
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable { selectedMood = mood },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Emoji dengan animasi ukuran
                                Text(
                                    text = emoji,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp),
                                    modifier = Modifier
                                        .padding(end = 16.dp)
                                        .graphicsLayer(
                                            scaleX = animatedScale.value,
                                            scaleY = animatedScale.value
                                        )
                                )

                                // Mood Text dengan animasi warna
                                Text(
                                    text = mood,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                                    color = animatedColor
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDarkTheme) Color.Gray else Color(
                            0xFF000000
                        )
                    )
                ) {
                    Text("Eksplor Lagu", color = if (isDarkTheme) Color.Black else Color.White)
                }
            }
        }
    }
}





