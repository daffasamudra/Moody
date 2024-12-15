package id.dev.moody.ui

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("mood_data", Context.MODE_PRIVATE)

    // Membaca data mood dari SharedPreferences
    val moodData by remember {
        derivedStateOf {
            getMoodData(sharedPreferences)
        }
    }

    // Ambil nama bulan saat ini
    val currentMonth = remember {
        LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale("id"))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistik") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        containerColor = Color.White
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFE6F5D0)) // Latar belakang hijau memenuhi layar
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$currentMonth",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Grafik batang berada di tengah dengan jarak ke bawah
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp) // Menentukan tinggi canvas
                        .padding(bottom = 24.dp) // Menambah jarak bawah agar rapi
                ) {
                    val barWidth = size.width / (moodData.size * 2 + 1)
                    val maxBarHeight = size.height * 0.8f
                    val maxValue = (moodData.values.maxOrNull() ?: 1).toFloat()

                    val ySteps = 5
                    val stepValue = maxValue / ySteps
                    val yGap = maxBarHeight / ySteps

                    // Garis horizontal dan angka sumbu y
                    for (i in 0..ySteps) {
                        val y = size.height - (i * yGap)
                        drawLine(
                            color = Color.Gray,
                            start = androidx.compose.ui.geometry.Offset(0f, y),
                            end = androidx.compose.ui.geometry.Offset(size.width, y)
                        )

                        drawIntoCanvas { canvas ->
                            val paint = Paint().apply {
                                isAntiAlias = true
                                color = android.graphics.Color.BLACK
                                textSize = 28f
                            }
                            canvas.nativeCanvas.drawText(
                                (i * stepValue).toInt().toString(),
                                0f,
                                y - 10f, // Geser angka ke atas sedikit
                                paint
                            )
                        }
                    }

                    var xOffset = barWidth

                    // Menggambar batang grafik
                    moodData.forEach { (mood, value) ->
                        val barHeight = (value / maxValue) * maxBarHeight

                        // Menggambar batang
                        drawRect(
                            color = Color(0xFF4CAF50),
                            topLeft = androidx.compose.ui.geometry.Offset(xOffset, size.height - barHeight),
                            size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
                        )

                        // Menggambar label di bawah setiap batang dengan jarak tambahan
                        drawIntoCanvas { canvas ->
                            val paint = Paint().apply {
                                isAntiAlias = true
                                color = android.graphics.Color.BLACK
                                textSize = 32f
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                            val textX = xOffset + barWidth / 2
                            val textY = size.height + 50f // Tambahkan jarak ke bawah
                            canvas.nativeCanvas.drawText(mood, textX, textY, paint)
                        }

                        xOffset += barWidth * 2
                    }
                }
            }
        }
    }
}

// Fungsi untuk membaca data mood dari SharedPreferences
fun getMoodData(sharedPreferences: SharedPreferences): Map<String, Int> {
    val keys = listOf("Bahagia", "Sedih", "Stress", "Semangat", "Santai")
    return keys.associateWith { key ->
        sharedPreferences.getInt(key, 0)
    }
}
