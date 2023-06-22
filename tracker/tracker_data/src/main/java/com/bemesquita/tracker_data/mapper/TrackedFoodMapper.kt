package com.bemesquita.tracker_data.mapper

import com.bemesquita.tracker_data.local.entity.TrackedFoodEntity
import com.bemesquita.tracker_domain.model.MealType
import com.bemesquita.tracker_domain.model.TrackedFood
import java.time.LocalDate

fun TrackedFoodEntity.toTrackedFood(): TrackedFood {
    return TrackedFood(
        id = id,
        name = name,
        imageUrl = imageUrl,
        carbs = carbs,
        protein = protein,
        fat = fat,
        mealType = MealType.fromString(type),
        amount = amount,
        date = LocalDate.of(year, month, dayOfMonth),
        calories = calories,
    )
}

fun TrackedFood.toTrackedFoodEntity(): TrackedFoodEntity {
    return TrackedFoodEntity(
        id = id,
        name = name,
        imageUrl = imageUrl,
        carbs = carbs,
        protein = protein,
        fat = fat,
        type = mealType.name,
        amount = amount,
        dayOfMonth = date.dayOfMonth,
        month = date.monthValue,
        year = date.year,
        calories = calories,
    )
}