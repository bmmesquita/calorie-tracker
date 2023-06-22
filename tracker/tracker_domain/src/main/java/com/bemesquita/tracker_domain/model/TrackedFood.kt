package com.bemesquita.tracker_domain.model

import java.time.LocalDate

data class TrackedFood(
    val name: String,
    val imageUrl: String?,
    val calories: Int,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    val mealType: MealType,
    val amount: Int,
    val date: LocalDate,
    val id: Int? = null
)
