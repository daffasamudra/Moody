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
            // Memeriksa apakah tabel `songs` kosong untuk mood "Bahagia" sebelum menambahkan data
            if (songDao.getSongsByMood("Bahagia").isEmpty()) {
                songDao.insertSongs(
                    listOf(
                        Song(
                            title = "Happy Song",
                            artist = "Artist A",
                            mood = "Bahagia",
                            filePath = R.raw.happy_song
                        ),
                        Song(
                            title = "Joyful Melody",
                            artist = "Artist B",
                            mood = "Bahagia",
                            filePath = R.raw.teeth // Pastikan file ini ada di res/raw
                        ),
                        Song(
                            title = "Uplifting Tune",
                            artist = "Artist C",
                            mood = "Bahagia",
                            filePath = R.raw.never // Pastikan file ini ada di res/raw
                        )
                    )
                )
            }

            // Memeriksa apakah tabel `songs` kosong untuk mood "Sedih" sebelum menambahkan data
            if (songDao.getSongsByMood("Sedih").isEmpty()) {
                songDao.insertSongs(
                    listOf(
                        Song(
                            title = "Sad Song",
                            artist = "Artist D",
                            mood = "Sedih",
                            filePath = R.raw.goodbye // Pastikan file ini ada di res/raw
                        ),
                        Song(
                            title = "Lonely Melody",
                            artist = "Artist E",
                            mood = "Sedih",
                            filePath = R.raw.atlantis // Pastikan file ini ada di res/raw
                        ),
                        Song(
                            title = "Melancholy Tune",
                            artist = "Artist F",
                            mood = "Sedih",
                            filePath = R.raw.glimpse // Pastikan file ini ada di res/raw
                        )
                    )
                )
            }

            // Memeriksa apakah tabel `songs` kosong untuk mood "Semangat" sebelum menambahkan data
            if (songDao.getSongsByMood("Semangat").isEmpty()) {
                songDao.insertSongs(
                    listOf(
                        Song(
                            title = "Energy Booster",
                            artist = "Artist G",
                            mood = "Semangat",
                            filePath = R.raw.best // Pastikan file ini ada di res/raw
                        ),
                        Song(
                            title = "High Spirits",
                            artist = "Artist H",
                            mood = "Semangat",
                            filePath = R.raw.best // Pastikan file ini ada di res/raw
                        ),
                        Song(
                            title = "Powerful Beat",
                            artist = "Artist I",
                            mood = "Semangat",
                            filePath = R.raw.best // Pastikan file ini ada di res/raw
                        )
                    )
                )
            }

            // Memeriksa apakah tabel `songs` kosong untuk mood "Stress" sebelum menambahkan data
            if (songDao.getSongsByMood("Stress").isEmpty()) {
                songDao.insertSongs(
                    listOf(
                        Song(
                            title = "Calm Waves",
                            artist = "Artist J",
                            mood = "Stress",
                            filePath = R.raw.best // Pastikan file ini ada di res/raw
                        ),
                        Song(
                            title = "Relaxing Breeze",
                            artist = "Artist K",
                            mood = "Stress",
                            filePath = R.raw.best // Pastikan file ini ada di res/raw
                        ),
                        Song(
                            title = "Mindful Moments",
                            artist = "Artist L",
                            mood = "Stress",
                            filePath = R.raw.best // Pastikan file ini ada di res/raw
                        )
                    )
                )
            }

            // Memeriksa apakah tabel `songs` kosong untuk mood "Santai" sebelum menambahkan data
            if (songDao.getSongsByMood("Santai").isEmpty()) {
                songDao.insertSongs(
                    listOf(
                        Song(
                            title = "Easy Going",
                            artist = "Artist M",
                            mood = "Santai",
                            filePath = R.raw.best // Pastikan file ini ada di res/raw
                        ),
                        Song(
                            title = "Tranquil Tune",
                            artist = "Artist N",
                            mood = "Santai",
                            filePath = R.raw.best // Pastikan file ini ada di res/raw
                        ),
                        Song(
                            title = "Peaceful Melody",
                            artist = "Artist O",
                            mood = "Santai",
                            filePath = R.raw.best // Pastikan file ini ada di res/raw
                        )
                    )
                )
            }
        }
    }
}

