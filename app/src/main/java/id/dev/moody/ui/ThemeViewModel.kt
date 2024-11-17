package id.dev.moody.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ThemeViewModel : ViewModel() {
    // State untuk tema gelap
    var isDarkTheme = mutableStateOf(false)
        private set

    // Fungsi untuk toggle tema
    fun toggleTheme() {
        viewModelScope.launch {
            isDarkTheme.value = !isDarkTheme.value
        }
    }
}
