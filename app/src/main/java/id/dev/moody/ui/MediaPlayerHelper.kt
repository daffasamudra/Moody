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
    var currentSongIndex = 0
    var isPlaying by mutableStateOf(false)
    var currentPosition by mutableStateOf(0)
    var duration by mutableStateOf(0)

    init {
        startUpdatingCurrentPosition()
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
        if (index >= songs.size) return
        val song = songs[index]
        val resId = song.filePath
        if (resId == 0) return

        if (mediaPlayer.isPlaying) {
            // If already playing, simply continue from where it is
            mediaPlayer.start()
            isPlaying = true
        } else {
            // If the media player has been reset or hasn't played anything, prepare it
            mediaPlayer.reset()
            mediaPlayer.setDataSource(context, android.net.Uri.parse("android.resource://${context.packageName}/$resId"))
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                duration = mediaPlayer.duration
                // Start from currentPosition if it was paused before
                mediaPlayer.seekTo(currentPosition)
                mediaPlayer.start()
                isPlaying = true
            }
        }
    }

    // Function to pause the current song
    fun pauseSong() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            isPlaying = false
        }
    }

    // Function to play or pause the current song
    fun playOrPauseSong() {
        if (isPlaying) {
            pauseSong()
        } else {
            playSong(currentSongIndex)
        }
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

    // Set completion listener to automatically play the next song
    fun setOnCompletionListener() {
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
