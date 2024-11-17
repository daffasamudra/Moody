package id.dev.moody.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import id.dev.moody.ui.ThemeViewModel
import id.dev.novlityapp.R

@Composable
fun BottomNavigationBar(
    navController: NavController,
    themeViewModel: ThemeViewModel // Ambil ViewModel sebagai parameter
) {
    val isDarkTheme by themeViewModel.isDarkTheme // Observasi tema

    NavigationBar(
        containerColor = if (isDarkTheme) Color.DarkGray else Color.White, // Warna latar sesuai tema
        contentColor = if (isDarkTheme) Color.White else Color.Black // Warna konten sesuai tema
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.iconhome),
                    contentDescription = "Home",
                    modifier = Modifier.size(28.dp)
                )
            },
            label = { Text("Beranda") },
            selected = false,
            onClick = {
                navController.navigate("moodTracker") {
                    popUpTo("moodTracker") { inclusive = true } // Menghapus semua layar sebelumnya
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.icondoc),
                    contentDescription = "Catatan",
                    modifier = Modifier.size(28.dp)
                )
            },
            label = { Text("Catatan") },
            selected = false,
            onClick = {
                navController.navigate("notes") {
                    popUpTo("notes") { inclusive = true } // Menghapus semua layar sebelumnya
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.iconsetting),
                    contentDescription = "Pengaturan",
                    modifier = Modifier.size(28.dp)
                )
            },
            label = { Text("Pengaturan") },
            selected = false,
            onClick = {
                navController.navigate("settings") {
                    popUpTo("settings") { inclusive = true } // Menghapus semua layar sebelumnya
                }
            }
        )
    }
}
