package com.example.aiassistant.di

import com.example.aiassistant.data.remote.api.N8nApiService
import com.example.aiassistant.data.repository.TaskExecutionRepositoryImpl
import com.example.aiassistant.domain.repository.TaskExecutionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * NetworkModule - Provides network-related dependencies
 * Sets up Retrofit, API services, and repositories for dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS) // Extended timeout for long-running AI operations
            .writeTimeout(60, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS) // Overall call timeout for complete operations
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(N8nApiService.BASE_URL) // Use the BASE_URL from N8nApiService
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideN8nApiService(retrofit: Retrofit): N8nApiService {
        return retrofit.create(N8nApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTaskExecutionRepository(
        n8nApiService: N8nApiService
    ): TaskExecutionRepository {
        return TaskExecutionRepositoryImpl(n8nApiService)
    }
}
