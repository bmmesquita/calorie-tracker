package com.bemesquita.tracker_domain.di

import com.bemesquita.core.domain.preferences.Preferences
import com.bemesquita.tracker_domain.repository.TrackerRepository
import com.bemesquita.tracker_domain.use_case.CalculateMealNutrients
import com.bemesquita.tracker_domain.use_case.DeleteTrackedFood
import com.bemesquita.tracker_domain.use_case.GetFoodsByDate
import com.bemesquita.tracker_domain.use_case.SearchFood
import com.bemesquita.tracker_domain.use_case.TrackFood
import com.bemesquita.tracker_domain.use_case.TrackerUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object TrackerDomainModule {

    @ViewModelScoped
    @Provides
    fun provideTrackerUseCases(
        repository: TrackerRepository,
        preferences: Preferences
    ): TrackerUseCases {
        return TrackerUseCases(
            trackFood = TrackFood(repository),
            searchFood = SearchFood(repository),
            getFoodsByDate = GetFoodsByDate(repository),
            deleteTrackedFood = DeleteTrackedFood(repository),
            calculateMealNutrients = CalculateMealNutrients(preferences)
        )
    }
}