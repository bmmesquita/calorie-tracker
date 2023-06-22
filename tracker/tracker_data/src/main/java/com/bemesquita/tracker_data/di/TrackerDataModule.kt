package com.bemesquita.tracker_data.di

import android.app.Application
import androidx.room.Room
import com.bemesquita.tracker_data.local.TrackedFoodDatabase
import com.bemesquita.tracker_data.local.TrackerDao
import com.bemesquita.tracker_data.remote.OpenFoodApi
import com.bemesquita.tracker_data.repository.TrackerRepositoryImpl
import com.bemesquita.tracker_domain.repository.TrackerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrackerDataModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenFoodApi(client: OkHttpClient): OpenFoodApi {
        return Retrofit.Builder()
            .baseUrl(OpenFoodApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideTrackedFoodDatabase(application: Application): TrackedFoodDatabase {
        return Room.databaseBuilder(
            application,
            TrackedFoodDatabase::class.java,
            "tracked_food_db"
        ).build()
    }



    @Provides
    @Singleton
    fun providesTrackerRepository(
        api: OpenFoodApi,
        database: TrackedFoodDatabase
    ): TrackerRepository {
        return TrackerRepositoryImpl(api = api, dao = database.dao)
    }
}