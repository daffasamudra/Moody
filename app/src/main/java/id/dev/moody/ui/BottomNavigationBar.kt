package id.dev.moody.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import id.dev.novlityapp.R

@Composable
fun BottomNavigationBar(navController: NavController) { // Tambahkan NavController sebagai parameter
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Black
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
                    popUpTo("moodTracker") { inclusive = true } // Menghapus semua layar sebelumnya.
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
                    popUpTo("notes") { inclusive = true } // Menghapus semua layar sebelumnya.
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
                // Navigasi ke Pengaturan
            }
        )
    }
}
