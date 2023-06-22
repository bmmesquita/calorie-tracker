package com.bemesquita.tracker_presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun NutrientsInfo(
    amount: Int,
    unit: String,
    modifier: Modifier = Modifier,
    amountTextSize: TextUnit = 20.sp,
    amountColor: Color = MaterialTheme.colors.onBackground,
    unitTextSize: TextUnit = 14.sp,
    unitColor: Color = MaterialTheme.colors.onBackground,
    name: String,
    nameTextStyle: TextStyle = MaterialTheme.typography.body1
) {
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UnitDisplay(
            unit = unit,
            unitTextSize = unitTextSize,
            unitColor = unitColor,
            amount = amount,
            amountTextSize = amountTextSize,
            amountColor = amountColor,
        )
        Text(
            text = name,
            color = MaterialTheme.colors.onBackground,
            style = nameTextStyle,
            fontWeight = FontWeight.Light
        )
    }

}