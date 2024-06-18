package com.tapyou.myPointsGraph.modules

import android.content.Context
import com.tapyou.myPointsGraph.networking.ApiService
import com.tapyou.myPointsGraph.repositories.PointsRepository
import com.tapyou.myPointsGraph.repositories.PointsRepositoryImpl
import com.tapyou.myPointsGraph.ui.helpers.ChartSaver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        // Создание HttpLoggingInterceptor
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Создание OkHttpClient и добавление интерцептора
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://hr-challenge.dev.tapyou.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePointsRepository(apiService: ApiService): PointsRepository {
        return PointsRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideChartSaver(@ApplicationContext context: Context): ChartSaver {
        return ChartSaver(context)
    }
}
