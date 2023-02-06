package com.alibagherifam.mentha.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alibagherifam.mentha.nutritionfacts.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class CameraViewModel(private val repository: FoodRepository) : ViewModel() {
    val uiState = MutableStateFlow(CameraUiState())

    suspend fun updateFood(foodLabel: String?) {
        val food = foodLabel?.let { repository.getFood(foodLabel) }
        uiState.update { it.copy(food = food) }
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
