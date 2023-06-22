package com.bemesquita.tracker_data.repository

import com.bemesquita.tracker_data.local.TrackerDao
import com.bemesquita.tracker_data.mapper.toTrackableFood
import com.bemesquita.tracker_data.mapper.toTrackedFood
import com.bemesquita.tracker_data.mapper.toTrackedFoodEntity
import com.bemesquita.tracker_data.remote.OpenFoodApi
import com.bemesquita.tracker_domain.model.TrackableFood
import com.bemesquita.tracker_domain.model.TrackedFood
import com.bemesquita.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TrackerRepositoryImpl(
    private val dao: TrackerDao,
    private val api: OpenFoodApi
): TrackerRepository {

    override suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>> {
        return try {
            val searchDto = api.searchFood(query = query, page = page, pageSize = pageSize)

            return Result.success(
                searchDto.products
                    .filter {product ->
                        val calculatedCalories =
                            product.nutriments.carbohydrates100g * 4f +
                            product.nutriments.proteins100g * 4f +
                            product.nutriments.fat100g * 9f

                        val lowerBound = calculatedCalories * 0.99f
                        val upperBound = calculatedCalories * 1.01f

                        product.nutriments.energyKcal100g in (lowerBound..upperBound)
                    }
                    .mapNotNull { it.toTrackableFood() }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun insertTrackedFood(food: TrackedFood) {
        dao.insertTrackedFood(food.toTrackedFoodEntity())
    }

    override suspend fun deleteTrackedFood(food: TrackedFood) {
        dao.deleteTrackedFood(food.toTrackedFoodEntity())
    }

    override fun getFoodsByDate(localDate: LocalDate): Flow<List<TrackedFood>> {
        return dao.getFoodsForDate(
            day = localDate.dayOfMonth,
            month = localDate.monthValue,
            year = localDate.year
        ).map { entities ->
            entities.map { it.toTrackedFood() }
        }
    }
}