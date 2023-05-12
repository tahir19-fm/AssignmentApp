package com.example.assignmentapp.home.util

import com.example.assignmentapp.home.data.NewsDAO
import com.example.assignmentapp.networkService.ApiResult
import com.example.assignmentapp.networkService.CommonApiService
import com.example.assignmentapp.networkService.networkCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiService: CommonApiService) {
    suspend fun getData(): ApiResult<NewsDAO> {
        return networkCall ( apiService.getData() )
    }
}