package id.dev.moody.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SongDao {
    @Insert
    suspend fun insertSong(song: Song)

    @Query("SELECT * FROM songs WHERE mood = :mood")
    suspend fun getSongsByMood(mood: String): List<Song>
}
