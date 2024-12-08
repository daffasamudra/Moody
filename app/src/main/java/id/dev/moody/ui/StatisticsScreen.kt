package id.dev.moody.ui

import android.content.Context
import android.graphics.Paint
import android.content.SharedPreferences
import androidx.compose.foundation.Canvas
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Bagian Chart
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F5D0))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Grafik Mood",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Menggambar grafik batang menggunakan Canvas
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        val barWidth = size.width / (moodData.size * 2)
                        val maxBarHeight = size.height * 0.7f
                        val maxValue = (moodData.values.maxOrNull() ?: 1).toFloat()

                        var xOffset = barWidth

                        moodData.forEach { (mood, value) ->
                            val barHeight = (value / maxValue) * maxBarHeight

                            // Menggambar batang
                            drawRect(
                                color = Color(0xFF4CAF50),
                                topLeft = androidx.compose.ui.geometry.Offset(xOffset, size.height - barHeight),
                                size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
                            )

                            // Menggambar label di bawah setiap batang
                            drawIntoCanvas { canvas ->
                                val paint = Paint().apply {
                                    isAntiAlias = true
                                    color = android.graphics.Color.BLACK
                                    textSize = 32f
                                    textAlign = android.graphics.Paint.Align.CENTER
                                }
                                val textX = xOffset + barWidth / 2
                                val textY = size.height + 40f // Tambahkan jarak dengan menyesuaikan nilai ini
                                canvas.nativeCanvas.drawText(mood, textX, textY, paint)
                            }

                            xOffset += barWidth * 2
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bagian Rekap Mood
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Rekap Mood",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Rekap list mood
                    moodData.forEach { (mood, count) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = mood,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            )
                            Text(
                                text = count.toString(),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

fun getMoodData(sharedPreferences: SharedPreferences): Map<String, Int> {
    val keys = listOf("Bahagia", "Sedih", "Stress", "Semangat", "Santai")
    return keys.associateWith { key ->
        sharedPreferences.getInt(key, 0)
    }
}