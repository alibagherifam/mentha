package dev.alibagherifam.mentha.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.alibagherifam.mentha.nutritionfacts.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CameraViewModel(private val repository: FoodRepository) : ViewModel() {
    val uiState = MutableStateFlow(CameraUiState())

    private suspend fun updateFood(foodLabel: String?) {
        val food = foodLabel?.let { repository.getFood(foodLabel) }
        uiState.update { it.copy(food = food) }
    }

    fun setImageRecognizer(recognizer: ImageAnalyzer) {
        viewModelScope.launch {
            recognizer.recognitionOutput.collect { foodLabel ->
                updateFood(foodLabel)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Provider(
        private val repository: FoodRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CameraViewModel(repository) as T
        }
    }
}
