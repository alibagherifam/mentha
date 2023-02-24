package dev.alibagherifam.mentha.comoon

import androidx.annotation.StringRes
import dev.alibagherifam.mentha.R
import java.text.DecimalFormat

class FormatFloatValueUseCase(private val stringProvider: StringProvider) {
    operator fun invoke(value: Float, @StringRes unitStringRes: Int): String {
        val valueFormatted = DecimalFormat("0").apply {
            maximumFractionDigits = 3
        }.format(value)

        return stringProvider.getString(unitStringRes, valueFormatted)
    }
}

class FormatNutritionWeightUseCase(
    private val formatFloatValue: FormatFloatValueUseCase
) {
    operator fun invoke(weight: Float): String {
        val isMilligrams = (weight < 0.5)
        return formatFloatValue(
            value = weight * if (isMilligrams) 1000 else 1,
            unitStringRes = if (isMilligrams) {
                R.string.label_weight_in_milligram
            } else {
                R.string.label_weight_in_gram
            }
        )
    }
}

class FormatEnergyUseCase(
    private val formatFloatValue: FormatFloatValueUseCase
) {
    operator fun invoke(energy: Float): String = formatFloatValue(
        value = energy, unitStringRes = R.string.label_weight_in_gram
    )
}
