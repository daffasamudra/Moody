package id.dev.moody.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class Song(
    val title: String,
    val artist: String,
    val mood: String,
    val filePath: Int,  // ID file di res/raw
    val duration: Int,  // Durasi dalam detik
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

