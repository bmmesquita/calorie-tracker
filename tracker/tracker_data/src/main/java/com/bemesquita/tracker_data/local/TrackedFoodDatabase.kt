package com.bemesquita.tracker_data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bemesquita.tracker_data.local.entity.TrackedFoodEntity

@Database(
    version = 1,
    entities = [TrackedFoodEntity::class]
)
abstract class TrackedFoodDatabase: RoomDatabase() {

    abstract val dao: TrackerDao
}