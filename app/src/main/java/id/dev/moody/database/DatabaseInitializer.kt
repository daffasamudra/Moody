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
                            filePath = R.raw.happy_song,
                            duration = 180 // 3 menit
                        ),
                        Song(
                            title = "Joyful Melody",
                            artist = "Artist B",
                            mood = "Bahagia",
                            filePath = R.raw.teeth,
                            duration = 240 // 4 menit
                        ),
                        Song(
                            title = "Uplifting Tune",
                            artist = "Artist C",
                            mood = "Bahagia",
                            filePath = R.raw.never,
                            duration = 200 // 3 menit 20 detik
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
                            filePath = R.raw.goodbye,
                            duration = 210 // 3 menit 30 detik
                        ),
                        Song(
                            title = "Lonely Melody",
                            artist = "Artist E",
                            mood = "Sedih",
                            filePath = R.raw.atlantis,
                            duration = 230 // 3 menit 50 detik
                        ),
                        Song(
                            title = "Melancholy Tune",
                            artist = "Artist F",
                            mood = "Sedih",
                            filePath = R.raw.glimpse,
                            duration = 250 // 4 menit 10 detik
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
                            filePath = R.raw.best,
                            duration = 300 // 5 menit
                        ),
                        Song(
                            title = "High Spirits",
                            artist = "Artist H",
                            mood = "Semangat",
                            filePath = R.raw.best,
                            duration = 280 // 4 menit 40 detik
                        ),
                        Song(
                            title = "Powerful Beat",
                            artist = "Artist I",
                            mood = "Semangat",
                            filePath = R.raw.best,
                            duration = 260 // 4 menit 20 detik
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
                            filePath = R.raw.best,
                            duration = 240 // 4 menit
                        ),
                        Song(
                            title = "Relaxing Breeze",
                            artist = "Artist K",
                            mood = "Stress",
                            filePath = R.raw.best,
                            duration = 260 // 4 menit 20 detik
                        ),
                        Song(
                            title = "Mindful Moments",
                            artist = "Artist L",
                            mood = "Stress",
                            filePath = R.raw.best,
                            duration = 220 // 3 menit 40 detik
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
                            filePath = R.raw.best,
                            duration = 240 // 4 menit
                        ),
                        Song(
                            title = "Tranquil Tune",
                            artist = "Artist N",
                            mood = "Santai",
                            filePath = R.raw.best,
                            duration = 230 // 3 menit 50 detik
                        ),
                        Song(
                            title = "Peaceful Melody",
                            artist = "Artist O",
                            mood = "Santai",
                            filePath = R.raw.best,
                            duration = 250 // 4 menit 10 detik
                        )
                    )
                )
            }
        }
    }
}


