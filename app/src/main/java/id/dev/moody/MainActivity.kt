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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import id.dev.moody.ui.*
import id.dev.moody.database.Song
import id.dev.moody.ui.theme.MoodyTheme
import id.dev.novlityapp.R
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast


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
                        incrementMoodCount(context, selectedMood)
                        when (selectedMood) {
                            "Bahagia" -> navController.navigate("songRecommendationBahagia")
                            "Sedih" -> navController.navigate("songRecommendationSad")
                            "Santai" -> navController.navigate("songRecommendationSantai")
                            "Semangat" -> navController.navigate("songRecommendationSemangat")
                            "Stress" -> navController.navigate("songRecommendationStress")
                        }
                    },
                    onSaveNote = { note, selectedMood ->
                        notesList.add(note to selectedMood) // Menambahkan catatan dan mood
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

    private fun incrementMoodCount(context: Context, mood: String) {
        val sharedPreferences = context.getSharedPreferences("mood_data", Context.MODE_PRIVATE)
        val currentCount = sharedPreferences.getInt(mood, 0)
        sharedPreferences.edit()
            .putInt(mood, currentCount + 1)
            .apply()
    }

    private fun getSongsForMood(mood: String): List<Song> {
        return when (mood) {
            "Bahagia" -> listOf(
                Song("Shivers", "Ed Sheeran", "Bahagia", R.raw.shivers, 180),
                Song("Dont Start Now", "Dua Lipa", "Bahagia", R.raw.dontstartnow, 240),
                Song("Closer", "The Chainsmokers, Halsey", "Bahagia", R.raw.closewr, 200),
                Song("As It Was", "Harry Styles", "Bahagia", R.raw.asitwas, 200),
                Song("Good 4u", "Olivia Rodrigo", "Bahagia", R.raw.good4u, 200),
                Song("Bad Habit", "Steve Lacy", "Bahagia", R.raw.badhabit, 200),
                Song("About Damn Time", "Lizzo", "Bahagia", R.raw.aboutdamntime, 200),
                Song("Sunroof", "Nicky Youre, dazy", "Bahagia", R.raw.sunroof, 200),
                Song("Peasches", "Justin Bieber, feat. Daniel Caesar Giveon", "Bahagia", R.raw.peaches, 200),
                Song("Levitating", "Dua Lipa", "Bahagia", R.raw.levilating, 200)
            )

            "Sedih" -> listOf(
                Song("Better Together", "Jack Johnson", "Sedih", R.raw.bettertogtr, 210),
                Song("Easy On Me", "Adele", "Sedih", R.raw.easyonme, 230),
                Song("Banana Pancakes", "Jack Johnson", "Sedih", R.raw.bananapancakes, 250),
                Song("GoodBye My Lover", "James Blunt", "Sedih", R.raw.goodbyemylover, 250),
                Song("The Night We Met", "Lord Huron", "Sedih", R.raw.thenightwemet, 250),
                Song("I Will Always Love You", "Whitney Houston", "Sedih", R.raw.iwillalways, 250),
                Song("Before U Go", "Lewis Capaldi", "Sedih", R.raw.beforeyougo, 250),
                Song("Someone Like U", "Adele", "Sedih", R.raw.someonelikeu, 250),
                Song("When I Was Ur Man", "Bruno Mars", "Sedih", R.raw.wheniwasurman, 250),
                Song("Happier", "Olivia Rodrigo", "Sedih", R.raw.happier, 250)
            )

            "Semangat" -> listOf(
                Song("Titanium", "David Guetta, Sia", "Semangat", R.raw.titanium, 300),
                Song("Rise Up", "Andra Day", "Semangat", R.raw.riseup, 280),
                Song("Uptown Funk", "Mark Ronson", "Semangat", R.raw.uptownfunk, 260),
                Song("Happy(?)", "Pharrell Williams", "Semangat", R.raw.happy, 260),
                Song("Shake It Off", "Taylor Swift", "Semangat", R.raw.shakeitoff, 260),
                Song("Dont Stop Believin", "Journey", "Semangat", R.raw.dontstopbelievin, 260),
                Song("Fight Song", "Rachel Platten", "Semangat", R.raw.fightsong, 260),
                Song("Stronger", "Kanye West", "Semangat", R.raw.stronger, 260),
                Song("Cant Stop The Feeling", "Justin Timberlake", "Semangat", R.raw.cantstopthefeelin, 260),
                Song("Eye Of The Tiger", "Survivor", "Semangat", R.raw.eyeofthetiger, 260)
            )

            "Stress" -> listOf(
                Song("Stressed Out", "Tiko", "Stress", R.raw.stressedout, 240),
                Song("River Flows in U", "Yiruma", "Stress", R.raw.riverflows, 260),
                Song("Sunset Lover", "Petit Biscuit", "Stress", R.raw.sunsetlover, 220),
                Song("Goodbye", "Air Supply", "Stress", R.raw.goodbye, 220),
                Song("Riptide", "Vance Joy", "Stress", R.raw.riptide, 220),
                Song("The Lazy Song", "Bruno Mars", "Stress", R.raw.thelazysong, 220),
                Song("Faded", "Alan Walker", "Stress", R.raw.faded, 220),
                Song("Daylight", "Maroon 5", "Stress", R.raw.daylight, 220),
                Song("Fix U", "Coldplay", "Stress", R.raw.fixyou, 220),
                Song("Put It All on Me", "Ed Sheeran feat. Ella Mai", "Stress", R.raw.putitall, 220)
            )

            "Santai" -> listOf(
                Song("Weightless", "Marconi Union", "Santai", R.raw.weightless, 250),
                Song("Cold Little Heart", "Michael Kiwanuka", "Santai", R.raw.coldlittle, 250),
                Song("Stay", "Rihanna, Mikky Ekko", "Santai", R.raw.stay, 250),
                Song("Adore U", "Harry Styles", "Santai", R.raw.adoreu, 250),
                Song("Breathe Me", "Sia", "Santai", R.raw.breatheme, 250),
                Song("The Scientist", "Coldplay", "Santai", R.raw.thescientist, 250),
                Song("Let Her Go", "Passenger", "Santai", R.raw.lethergo, 250),
                Song("Lost In Japan", "Shawn Mendes", "Santai", R.raw.lostinjapan, 250),
                Song("Good Days", "SZA", "Santai", R.raw.gooddays, 250),
                Song("Breezelocks", "alt-J", "Santai", R.raw.breezeblocks, 250)

            )

            else -> emptyList()
        }
    }

    @Composable
    fun MoodTrackerApp(
        modifier: Modifier = Modifier,
        themeViewModel: ThemeViewModel,
        onExploreSongsClick: (String) -> Unit,
        onSaveNote: (String, String) -> Unit, // Menyimpan catatan beserta mood
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

        val context = LocalContext.current
        val sharedPreferences =
            context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "Pengguna") ?: "Pengguna"

        Box(
            modifier = modifier.fillMaxSize()
        ) {
            AnimatedBackground(isDarkTheme = isDarkTheme)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Menambahkan pengguliran
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(1.dp))

                // MoodifyText Image
                Image(
                    painter = painterResource(id = R.drawable.moodifytext),
                    contentDescription = "Moodify Text Logo",
                    modifier = Modifier
                        .height(100.dp)
                        .align(Alignment.Start)
                        .padding(start = 9.dp)
                        .size(115.dp)
                )

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
                        containerColor = if (isDarkTheme) Color.DarkGray else Color.White.copy(alpha = 0.7f)
                    )
                ) {
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

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkTheme) Color.DarkGray else Color.White.copy(alpha = 0.7f)
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
                            val animatedScale = animateFloatAsState(
                                targetValue = if (isSelected) 1.5f else 1.0f
                            )

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
                    onClick = {
                        if (notes.isNotBlank()) {
                            // Simpan catatan dan lanjutkan eksplorasi lagu
                            onSaveNote(notes, selectedMood) // Menyimpan catatan dengan mood
                            onExploreSongsClick(selectedMood) // Mengeksplorasi lagu berdasarkan mood
                            notes = "" // Reset input setelah aksi
                        } else {
                            // Tampilkan notifikasi jika catatan kosong
                            Toast.makeText(context, "Silahkan Tuliskan Perasaan Anda Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDarkTheme) Color.Gray else Color(0xFF000000)
                    )
                ) {
                    Text(
                        "Simpan dan Eksplor Lagu",
                        color = if (isDarkTheme) Color.Black else Color.White
                    )
                }
            }
        }
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