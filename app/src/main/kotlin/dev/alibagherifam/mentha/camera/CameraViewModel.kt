package dev.alibagherifam.mentha.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.alibagherifam.mentha.imageclassifier.ImageClassifierHelper
import dev.alibagherifam.mentha.nutritionfacts.FoodRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.update

typealias RotatedBitmap = Pair<Bitmap, Int>

@OptIn(FlowPreview::class)
class CameraViewModel(
    private val imageClassifier: ImageClassifierHelper,
    private val repository: FoodRepository
) : ViewModel() {
    private val _classifyImageChannel = Channel<RotatedBitmap>(capacity = Channel.CONFLATED)
    val classifyImageChannel: SendChannel<RotatedBitmap> get() = _classifyImageChannel

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> get() = _uiState

    init {
        _classifyImageChannel.consumeAsFlow()
            .sample(periodMillis = 150L)
            .map { (bitmap, rotation) -> imageClassifier.classify(bitmap, rotation) }
            .distinctUntilChanged()
            .onEach { foodLabel -> updateFood(foodLabel) }
            .launchIn(viewModelScope)
    }

    private suspend fun updateFood(foodLabel: String?) {
        val food = foodLabel?.let { repository.getFood(foodLabel) }
        _uiState.update { it.copy(food = food) }
    }

    fun setFlashlightSupported(isSupported: Boolean) {
        _uiState.update {
            it.copy(isFlashlightSupported = isSupported)
        }
    }

    fun setFlashlightEnabled(isEnabled: Boolean) {
        _uiState.update {
            it.copy(isFlashlightEnabled = isEnabled)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Provider(
        private val imageClassifier: ImageClassifierHelper,
        private val repository: FoodRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CameraViewModel(imageClassifier, repository) as T
        }
    }
}
