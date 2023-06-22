package com.bemesquita.tracker_presentation.tracker_overview.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import com.bemesquita.core_ui.CarbsColor
import com.bemesquita.core_ui.FatColor
import com.bemesquita.core_ui.ProteinColor

@Composable
fun NutrientsBar(
    carbs: Int,
    protein: Int,
    fat: Int,
    calories: Int,
    calorieGoal: Int,
    modifier: Modifier = Modifier
) {

    val backgroundColor = MaterialTheme.colors.background
    val caloriesExceededColor = MaterialTheme.colors.error

    val carbsWidthRatio = remember { Animatable(0f) }
    val proteinWidthRatio = remember { Animatable(0f) }
    val fatWidthRatio = remember { Animatable(0f) }
    
    LaunchedEffect(key1 = carbs) {
        carbsWidthRatio.animateTo(
            targetValue = ((carbs * 4f) / calorieGoal)
        )
    }

    LaunchedEffect(key1 = protein) {
        proteinWidthRatio.animateTo(
            targetValue = ((protein * 4f / calorieGoal))
        )
    }

    LaunchedEffect(key1 = fat) {
        fatWidthRatio.animateTo(
            targetValue = ((fat * 9f / calorieGoal))
        )
    }

    Canvas(modifier = modifier) {
        if (calories <= calorieGoal) {
            val carbsWidth = carbsWidthRatio.value * size.width
            val proteinWidth = proteinWidthRatio.value * size.width
            val fatWidth = fatWidthRatio.value * size.width

            // Draw nutrients Bar
            drawRoundRect(
                color = backgroundColor,
                size = size,
                cornerRadius = CornerRadius(100f)
            )

            // Draw fat bar on top of base bar
            drawRoundRect(
                color = FatColor,
                size = Size(
                    width = carbsWidth + proteinWidth + fatWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(100f)
            )

            // Draw protein bar on top of base bar
            drawRoundRect(
                color = ProteinColor,
                size = Size(
                    width = carbsWidth + proteinWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(100f)
            )

            // Draw carb bar on top of base bar
            drawRoundRect(
                color = CarbsColor,
                size = Size(
                    width = carbsWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(100f)
            )

        } else {
            // Draw nutrients bar all red
            drawRoundRect(
                color = caloriesExceededColor,
                size = size,
                cornerRadius = CornerRadius(100f)
            )
        }
    }
}