package com.example.assignmentapp.di


import com.example.assignmentapp.home.util.HomeRepository
import com.example.assignmentapp.networkService.CommonApiService
import com.example.assignmentapp.networkService.OkHttpClientHelper
import com.example.assignmentapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesCommonApiService(): CommonApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(OkHttpClientHelper().getOkHttpClient())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CommonApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesHomeRepo(api:CommonApiService)=HomeRepository(api)


}