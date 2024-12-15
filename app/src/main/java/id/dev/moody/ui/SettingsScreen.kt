package id.dev.moody.ui

import android.content.Context
import android.media.AudioManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import id.dev.novlityapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel,
    onLogout: () -> Unit
) {
    val isDarkTheme by themeViewModel.isDarkTheme
    val context = LocalContext.current
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val sharedPreferences = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE)

    // State untuk volume
    var volume by remember { mutableStateOf(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showEditNameDialog by remember { mutableStateOf(false) }
    var showEditAvatarDialog by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf(sharedPreferences.getString("username", "Pengguna") ?: "Pengguna") }
    var avatarResource by remember { mutableStateOf(sharedPreferences.getInt("avatar", R.drawable.profile_placeholder)) }

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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Gambar profil pengguna
            Image(
                painter = painterResource(id = avatarResource), // Gambar profil pengguna
                contentDescription = "User Profile",
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
                    .clickable { showEditAvatarDialog = true }
            )

            // Nama pengguna dengan ikon edit
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = username,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color.Black
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Name",
                    tint = if (isDarkTheme) Color.White else Color.Gray,
                    modifier = Modifier.clickable { showEditNameDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Tema Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color.DarkGray else Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_theme),
                            contentDescription = "Icon Theme",
                            tint = if (isDarkTheme) Color.White else Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
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
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Volume Control Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color.DarkGray else Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_volume),
                            contentDescription = "Icon Volume",
                            tint = if (isDarkTheme) Color.White else Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Volume",
                            color = if (isDarkTheme) Color.White else Color.Black,
                            modifier = Modifier.weight(1f)
                        )
                    }
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

            Spacer(modifier = Modifier.height(16.dp))

            // Statistik Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("statistik") }, // Navigasi ke halaman Statistik
                colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color.DarkGray else Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_statistics),
                        contentDescription = "Icon Statistik",
                        tint = if (isDarkTheme) Color.White else Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Statistik",
                        color = if (isDarkTheme) Color.White else Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logout Button
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color.DarkGray else Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { showLogoutDialog = true }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_logout),
                        contentDescription = "Icon Logout",
                        tint = if (isDarkTheme) Color.White else Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Keluar",
                        color = if (isDarkTheme) Color.White else Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Dialog Konfirmasi Logout
            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    title = { Text("Logout") },
                    text = { Text("Apakah Anda yakin ingin logout?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                with(sharedPreferences.edit()) {
                                    putBoolean("is_logged_in", false)
                                    apply()
                                }
                                showLogoutDialog = false
                                onLogout()
                            }
                        ) {
                            Text("Ya")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showLogoutDialog = false }) {
                            Text("Batal")
                        }
                    }
                )
            }

            // Dialog Edit Nama
            if (showEditNameDialog) {
                var newName by remember { mutableStateOf(username) }
                AlertDialog(
                    onDismissRequest = { showEditNameDialog = false },
                    title = { Text("Edit Nama") },
                    text = {
                        TextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text("Nama Pengguna") }
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                username = newName
                                with(sharedPreferences.edit()) {
                                    putString("username", newName)
                                    apply()
                                }
                                showEditNameDialog = false
                            }
                        ) {
                            Text("Simpan")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showEditNameDialog = false }) {
                            Text("Batal")
                        }
                    }
                )
            }

            // Dialog Edit Avatar
            if (showEditAvatarDialog) {
                AlertDialog(
                    onDismissRequest = { showEditAvatarDialog = false },
                    title = { Text("Pilih Avatar") },
                    text = {
                        Column {
                            val avatarOptions = listOf(
                                R.drawable.avatar_1,
                                R.drawable.avatar_2,
                                R.drawable.avatar_3,
                                R.drawable.avatar_4,
                                R.drawable.avatar_5,
                                R.drawable.avatar_6
                            )
                            for (i in avatarOptions.indices step 2) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    for (j in 0..1) {
                                        if (i + j < avatarOptions.size) {
                                            Image(
                                                painter = painterResource(id = avatarOptions[i + j]),
                                                contentDescription = "Avatar Option",
                                                modifier = Modifier
                                                    .size(64.dp)
                                                    .clickable {
                                                        avatarResource = avatarOptions[i + j]
                                                        with(sharedPreferences.edit()) {
                                                            putInt("avatar", avatarOptions[i + j])
                                                            apply()
                                                        }
                                                        showEditAvatarDialog = false
                                                    }
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = { showEditAvatarDialog = false }) {
                            Text("Tutup")
                        }
                    }
                )
            }
        }
    }
}