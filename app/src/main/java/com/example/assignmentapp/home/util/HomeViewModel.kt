package com.example.assignmentapp.home.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignmentapp.home.data.Articles
import com.example.assignmentapp.home.data.NewsDAO
import com.example.assignmentapp.home.data.RvModalClass
import com.example.assignmentapp.networkService.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository):ViewModel() {

    private val _newsData = MutableLiveData<ApiResult<NewsDAO>>()
    val newsData: MutableLiveData<ApiResult<NewsDAO>>
        get() = _newsData

     fun fetchNewsData() {
        _newsData.value = ApiResult.Loading
        viewModelScope.launch {
            _newsData.value=repository.getData()
        }
    }

    private val _newsList = MutableLiveData<ArrayList<RvModalClass>>()
    val newsList: MutableLiveData<ArrayList<RvModalClass>>
        get() = _newsList
    fun setData(list: ArrayList<RvModalClass>) {
        _newsList.value=list
    }

}