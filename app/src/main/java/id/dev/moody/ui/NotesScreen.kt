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
    notesList: List<Pair<String, String>>,
    onBack: () -> Unit,
    onDeleteNote: (Int) -> Unit // Callback untuk menghapus catatan
) {
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
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController) // Teruskan NavController ke BottomNavigationBar
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (notesList.isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.note2),
                    contentDescription = "Background Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Belum ada catatan yang disimpan.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        color = Color.Black
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
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // Isi Catatan
                                Text(
                                    text = if (isExpanded) note.first else note.first.take(100) + "...",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                // Keterangan Waktu
                                Text(
                                    text = note.second,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 14.sp,
                                        color = Color.Gray
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
                title = { Text("Hapus Catatan") },
                text = { Text("Apakah Anda yakin ingin menghapus catatan ini?") },
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
                        Text("Batal")
                    }
                }
            )
        }
    }
}


