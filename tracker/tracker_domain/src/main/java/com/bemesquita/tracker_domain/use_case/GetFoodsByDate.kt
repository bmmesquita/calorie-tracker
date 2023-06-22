package com.bemesquita.tracker_domain.use_case

import com.bemesquita.tracker_domain.model.TrackedFood
import com.bemesquita.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetFoodsByDate(
    private val repository: TrackerRepository
) {

    operator fun invoke(date: LocalDate): Flow<List<TrackedFood>> {
        return repository.getFoodsByDate(date)
    }
}