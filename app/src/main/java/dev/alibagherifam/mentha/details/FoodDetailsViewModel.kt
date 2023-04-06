package dev.alibagherifam.mentha.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.alibagherifam.mentha.R
import dev.alibagherifam.mentha.comoon.FormatEnergyUseCase
import dev.alibagherifam.mentha.comoon.FormatNutritionWeightUseCase
import dev.alibagherifam.mentha.comoon.StringProvider
import dev.alibagherifam.mentha.nutritionfacts.FoodRepository
import dev.alibagherifam.mentha.nutritionfacts.model.FoodEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FoodDetailsViewModel(
    foodId: String,
    repository: FoodRepository,
    private val formatEnergy: FormatEnergyUseCase,
    private val formatNutritionWeight: FormatNutritionWeightUseCase,
    private val stringProvider: StringProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow<FoodEntity?>(null)
    val uiState: StateFlow<FoodEntity?> get() = _uiState

    init {
        viewModelScope.launch {
            _uiState.value = repository.getFood(foodId)
        }
    }

    fun prepareShareNutritionFactsMessage(): String {
        val food = requireNotNull(uiState.value)
        val nutritionFacts = food.nutritionFacts

        val foodEnergy = formatEnergy(nutritionFacts.energy)
        val foodCarbohydrate = formatNutritionWeight(nutritionFacts.carbohydrate)

        return """
            "${food.name}"
            ${stringProvider.getString(R.string.label_energy)}: $foodEnergy
            ${stringProvider.getString(R.string.label_carbohydrate)}: $foodCarbohydrate
            
            ${getDownloadAppMessage()}
        """.trimIndent()
    }

    private fun getDownloadAppMessage(): String {
        val downloadAppUrl = stringProvider.getString(R.string.download_app_url)
        return stringProvider.getString(R.string.message_download_app, downloadAppUrl)
    }

    @Suppress("UNCHECKED_CAST")
    class Provider(
        private val foodId: String,
        private val repository: FoodRepository,
        private val formatEnergy: FormatEnergyUseCase,
        private val formatNutritionWeight: FormatNutritionWeightUseCase,
        private val stringProvider: StringProvider
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FoodDetailsViewModel(
                foodId,
                repository,
                formatEnergy,
                formatNutritionWeight,
                stringProvider
            ) as T
        }
    }
}
