package com.alibagherifam.mentha.nutritionfacts

import com.alibagherifam.mentha.nutritionfacts.model.FoodEntity
import com.alibagherifam.mentha.nutritionfacts.model.NutritionFact

fun getSampleFood() = FoodEntity(
    id = "orange",
    name = "پرتقال",
    summary = "پرتقال منبع خوبی از ویتامین C و پتاسیم است که هر دو می‌توانند به کاهش فشار خون کمک کنند. ویتامین C موجود در پرتقال یک آنتی اکسیدان قوی است که در حفظ جوانی پوست بسیار مهم بوده و در ترمیم سلولی و بهبود زخم نقش دارد. پرتقال سرشار از فیبر است؛ به طوری که یک پرتقال متوسط، 11 درصد نیاز روزانه‌ی بدن را تامین می‌کند.",
    image = "nutrition-facts/src/main/assets/image/img_orange.png",
    nutritionFact = NutritionFact(
        weight = 140,
        measure = "عدد",
        energy = 65.8f,
        water = 121f,
        protein = 1.27f,
        carbohydrate = 16.5f,
        fat = 0.21f,
        dietaryFiber = 2.8f,
        sugar = 12f,
        sodium = 12.6f
    )
)
