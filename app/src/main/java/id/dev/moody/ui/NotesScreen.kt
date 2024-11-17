package id.dev.moody.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment // Impor yang diperlukan
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import id.dev.novlityapp.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel,
    notesList: List<Pair<String, String>>,
    onBack: () -> Unit,
    onDeleteNote: (Int) -> Unit // Callback untuk menghapus catatan
) {
    val isDarkTheme by themeViewModel.isDarkTheme
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedNoteIndex by remember { mutableStateOf(-1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catatan Anda") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = if (isDarkTheme) Color.DarkGray else Color.White,
                    titleContentColor = if (isDarkTheme) Color.White else Color.Black
                )
            )

        },
        bottomBar = {
            BottomNavigationBar(navController = navController, themeViewModel = themeViewModel)
        },

        containerColor = if (isDarkTheme) Color.Black else Color.White
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (notesList.isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.note2),
                    contentDescription = "Background Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = if (isDarkTheme) 0.5f else 1f
                )
                Text(
                    text = "Belum ada catatan yang disimpan.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        color = if (isDarkTheme) Color.White else Color.Black
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                    itemsIndexed(notesList) { index, note ->
                        var isExpanded by remember { mutableStateOf(false) }

                        // Card untuk setiap catatan
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .combinedClickable(
                                    onClick = { isExpanded = !isExpanded }, // Expand/Collapse saat diklik
                                    onLongClick = {
                                        selectedNoteIndex = index
                                        showDeleteDialog = true
                                    }
                                )
                                .animateContentSize(), // Animasi saat expand/collapse
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isDarkTheme) Color.DarkGray else Color.White.copy(alpha = 0.9f)
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // Isi Catatan
                                Text(
                                    text = if (isExpanded) note.first else note.first.take(100) + "...",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDarkTheme) Color.White else Color.Black
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                // Keterangan Waktu
                                Text(
                                    text = note.second,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 14.sp,
                                        color = if (isDarkTheme) Color.LightGray else Color.Gray
                                    )
                                )

                                // Tombol "Lihat Selengkapnya" jika collapsed
                                if (!isExpanded) {
                                    Text(
                                        text = "Lihat Selengkapnya",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        ),
                                        modifier = Modifier.clickable { isExpanded = true }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Dialog konfirmasi penghapusan
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = {
                    Text(
                        "Hapus Catatan",
                        color = if (isDarkTheme) Color.White else Color.Black
                    )
                },
                text = {
                    Text(
                        "Apakah Anda yakin ingin menghapus catatan ini?",
                        color = if (isDarkTheme) Color.LightGray else Color.Gray
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        onDeleteNote(selectedNoteIndex)
                        showDeleteDialog = false
                    }) {
                        Text("Hapus", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Batal", color = MaterialTheme.colorScheme.primary)
                    }
                },
                containerColor = if (isDarkTheme) Color.DarkGray else Color.White
            )
        }
    }
}



