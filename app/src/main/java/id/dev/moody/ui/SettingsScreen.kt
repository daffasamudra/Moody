package id.dev.moody.ui

import android.content.Context
import android.media.AudioManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, themeViewModel: ThemeViewModel) {
    val isDarkTheme by themeViewModel.isDarkTheme
    val context = LocalContext.current
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    // Volume state
    var volume by remember { mutableStateOf(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = if (isDarkTheme) Color.DarkGray else Color.White,
                    titleContentColor = if (isDarkTheme) Color.White else Color.Black
                )
            )
        },
        containerColor = if (isDarkTheme) Color.Black else Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Tema Section
            Text(
                text = "Tema",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color.Black
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Tema Gelap",
                    color = if (isDarkTheme) Color.White else Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { themeViewModel.toggleTheme() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF4CAF50),
                        uncheckedThumbColor = Color(0xFF4CAF50)
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Volume Control Section
            Text(
                text = "Volume",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color.Black
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = volume.toFloat(),
                onValueChange = { newVolume ->
                    volume = newVolume.toInt()
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
                },
                valueRange = 0f..audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat(),
                steps = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) - 1
            )
        }
    }
}
