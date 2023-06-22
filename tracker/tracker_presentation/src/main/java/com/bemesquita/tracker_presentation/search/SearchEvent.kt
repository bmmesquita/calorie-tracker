package com.bemesquita.tracker_presentation.search

import com.bemesquita.tracker_domain.model.MealType
import com.bemesquita.tracker_domain.model.TrackableFood
import java.time.LocalDate

sealed class SearchEvent {
    data class OnQueryChange(val query: String): SearchEvent()
    data class OnToggleTrackableFood(val food: TrackableFood): SearchEvent()
    data class OnAmountForFoodChange(val amount: String, val food: TrackableFood): SearchEvent()
    data class OnTrackFoodClick(
        val food: TrackableFood,
        val mealType: MealType,
        val date: LocalDate,
    ): SearchEvent()
    data class OnSearchFocusChange(val isFocused: Boolean): SearchEvent()
    object OnSearch: SearchEvent()

}