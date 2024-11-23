package id.dev.moody.ui

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import id.dev.moody.database.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MediaPlayerHelper(
    private val context: Context,
    private val songs: List<Song>,
    private val coroutineScope: CoroutineScope
) {
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    var currentSongIndex by mutableStateOf(-1) // Initially set to -1 to indicate no song selected
    var isPlaying by mutableStateOf(false)
    var currentPosition by mutableStateOf(0)
    var duration by mutableStateOf(0)

    init {
        startUpdatingCurrentPosition()
        setOnCompletionListener()  // Set the completion listener here
    }

    // Function to update the current position continuously
    private fun startUpdatingCurrentPosition() {
        coroutineScope.launch {
            while (true) {
                if (isPlaying) {
                    currentPosition = mediaPlayer.currentPosition
                }
                delay(500L)
            }
        }
    }

    // Function to play a song by index
    fun playSong(index: Int) {
        if (index < 0 || index >= songs.size) return
        val song = songs[index]
        val resId = song.filePath
        if (resId == 0) return

        // Reset the media player and prepare the new song
        mediaPlayer.reset()
        mediaPlayer.setDataSource(context, android.net.Uri.parse("android.resource://${context.packageName}/$resId"))
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            duration = mediaPlayer.duration
            mediaPlayer.start()
            isPlaying = true
        }
    }

    // Function to pause the current song
    fun pauseSong() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            currentPosition = mediaPlayer.currentPosition
            isPlaying = false
        }
    }

    // Function to play or pause the current song
    fun playOrPauseSong(): Boolean {
        if (!hasSongSelected()) {
            // Return false indicating no song is selected, caller will handle the warning
            return false
        }

        if (isPlaying) {
            pauseSong()
        } else {
            mediaPlayer.apply {
                if (!isPlaying) {
                    start()
                }
            }
            isPlaying = !isPlaying
        }
        return true // Return true indicating song play/pause operation was successful
    }

    // Function to skip to the next song
    fun skipSong() {
        currentSongIndex = (currentSongIndex + 1) % songs.size
        currentPosition = 0 // Reset the position when skipping
        playSong(currentSongIndex)
    }

    // Function to go to the previous song
    fun previousSong() {
        currentSongIndex = if (currentSongIndex > 0) currentSongIndex - 1 else songs.size - 1
        currentPosition = 0 // Reset the position when going to previous song
        playSong(currentSongIndex)
    }

    // Function to check if a song has been selected
    fun hasSongSelected(): Boolean {
        return currentSongIndex >= 0 && currentSongIndex < songs.size
    }

    // Set completion listener to automatically play the next song
    private fun setOnCompletionListener() {
        mediaPlayer.setOnCompletionListener {
            skipSong()
        }
    }

    // Function to seek to a new position
    fun seekTo(newPosition: Int) {
        if (newPosition <= duration) {
            mediaPlayer.seekTo(newPosition)
            currentPosition = newPosition
        }
    }

    // Function to release the media player
    fun release() {
        mediaPlayer.release()
    }
}
