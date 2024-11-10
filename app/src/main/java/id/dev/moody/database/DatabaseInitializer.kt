package id.dev.moody.database

import android.content.Context
import id.dev.novlityapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseInitializer {
    fun initialize(context: Context) {
        val songDao = SongDatabase.getDatabase(context).songDao()

        CoroutineScope(Dispatchers.IO).launch {
            // Memeriksa apakah tabel `songs` kosong sebelum menambahkan data
            if (songDao.getSongsByMood("Bahagia").isEmpty()) {
                // Menambahkan beberapa lagu contoh ke tabel `songs` dengan filePath dalam format Int
                songDao.insertSong(
                    Song(
                        title = "Happy Song",
                        artist = "Artist A",
                        mood = "Bahagia",
                        filePath = R.raw.happy_song
                    )
                )
            }
        }
    }
}
