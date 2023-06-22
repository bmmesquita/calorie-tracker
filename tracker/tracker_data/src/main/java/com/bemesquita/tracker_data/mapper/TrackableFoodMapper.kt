package com.bemesquita.tracker_data.mapper

import com.bemesquita.tracker_data.remote.dto.ProductDto
import com.bemesquita.tracker_domain.model.TrackableFood
import kotlin.math.roundToInt

fun ProductDto.toTrackableFood(): TrackableFood? {

    val carbsPer100g = nutriments.carbohydrates100g.toInt()
    val proteinPer100g = nutriments.proteins100g.toInt()
    val fatPer100g = nutriments.fat100g.toInt()
    val caloriesPer100g = nutriments.energyKcal100g.roundToInt()

    return TrackableFood(
        name = productName ?: return null,
        imageUrl = imageFrontThumbUrl,
        carbsPer100g = carbsPer100g,
        proteinPer100g = proteinPer100g,
        fatPer100g = fatPer100g,
        caloriesPer100g = caloriesPer100g
    )
}