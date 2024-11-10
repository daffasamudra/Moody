package id.dev.moody.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.dev.moody.MainActivity
import id.dev.moody.ui.theme.MoodyTheme
import id.dev.novlityapp.R

class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoodyTheme {
                SplashScreen()
            }
        }

        // Handler untuk menunda navigasi ke MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000) // 3000 ms = 3 detik
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Gambar logo
            Image(
                painter = painterResource(id = R.drawable.moodlogo), // Pastikan resource logo Anda benar
                contentDescription = "App Logo",
                modifier = Modifier.size(200.dp)
            )

            // Mengurangi jarak antar elemen agar gambar teks berada lebih dekat ke logo
            Spacer(modifier = Modifier.height(8.dp))

            // Gambar teks di bawah logo dengan ukuran lebih kecil
            Image(
                painter = painterResource(id = R.drawable.moodifytext), // Pastikan resource gambar teks Anda benar
                contentDescription = "App Text",
                modifier = Modifier
                    .height(40.dp) // Sesuaikan tinggi teks agar lebih kecil dan menempel ke logo
                    .wrapContentWidth() // Lebar disesuaikan dengan konten
                    .size(200.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    MoodyTheme {
        SplashScreen()
    }
}
