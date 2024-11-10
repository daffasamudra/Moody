package id.dev.moody.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class Song(
    val title: String,
    val artist: String,
    val mood: String,
    val filePath: Int,  // Menggunakan Int untuk filePath
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
