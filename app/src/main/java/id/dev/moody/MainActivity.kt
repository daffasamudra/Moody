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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.dev.moody.ui.theme.MoodyTheme
import id.dev.novlityapp.R

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoodyTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Mood Tracker", fontWeight = FontWeight.Bold) },
                            actions = {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Mood Icon",
                                    tint = Color.Gray
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6200EE))
                        )
                    }
                ) { innerPadding ->
                    MoodTrackerApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MoodTrackerApp(modifier: Modifier = Modifier) {
    var selectedMood by remember { mutableStateOf("Bahagia") }
    var notes by remember { mutableStateOf("") }
    var savedNotes by remember { mutableStateOf("") }
    val moods = listOf("Bahagia", "Sedih", "Bersemangat", "Santai", "Stres")

    // Menggunakan Box untuk menumpuk Image sebagai latar belakang
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Image untuk latar belakang
        Image(
            painter = painterResource(id = R.drawable.retro),
            contentDescription = "Background Image",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.50f
        )

        // Konten aplikasi di dalam Column
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
                elevation = CardDefaults.cardElevation(4.dp),
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
                    maxLines = 5,
                    singleLine = false,
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

            // Card untuk RadioButton dan judul
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f))
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    // Teks judul di dalam Card
                    Text(
                        "Bagaimana Perasaanmu Hari Ini?",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9C27B0) // Warna teks putih
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Daftar RadioButton
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
                                    .border( // Border pada RadioButton
                                        width = 1.dp,
                                        color = Color.Gray.copy(alpha = 0.6f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
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
                                color = if (mood == selectedMood) Color(0xFF9C27B0) else Color(0xFF9C27B0)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Simpan mood dan catatan, lalu tampilkan lagu */ },
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

@Preview(showBackground = true)
@Composable
fun MoodTrackerAppPreview() {
    MoodyTheme {
        MoodTrackerApp()
    }
}
