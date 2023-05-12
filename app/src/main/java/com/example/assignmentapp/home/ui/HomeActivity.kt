package com.example.assignmentapp.home.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignmentapp.databinding.ActivityHomeBinding
import com.example.assignmentapp.home.data.RvModalClass
import com.example.assignmentapp.home.util.HomeViewModel
import com.example.assignmentapp.login.ui.LoginActivity
import com.example.assignmentapp.networkService.ApiResult
import com.example.socialx.uiFrags.HomeScreenAdapter
import com.example.socialx.uiFrags.WrapContentLinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val viewModel:HomeViewModel by viewModels()
    private val parentAdapter by lazy {
       HomeScreenAdapter(list)
    }
    var refinedList= emptyList<RvModalClass>()
    private lateinit var list:MutableList<RvModalClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViews()
        setUpObservers()
    }


    private fun setupViews() {
        viewModel.fetchNewsData()
        list=ArrayList()
        binding.apply {
            progresbar.visibility= View.GONE
            rvHome.layoutManager=WrapContentLinearLayoutManager(this@HomeActivity)
            rvHome.adapter=parentAdapter
        }

        binding.tvEditSearchText.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {

                return true
            }


            override fun onQueryTextChange(text: String): Boolean {
              val nl=  viewModel.newsList.value?.filter { it.publishedAt.contains(other = text) } as MutableList<RvModalClass>

                if (text.isEmpty()){
                    viewModel.newsList.value?.let { parentAdapter.updateData(it) }
                }else{
                    if (nl.isNotEmpty()){
                        parentAdapter.updateData(nl)
                    }

                }
                return true
            }
        })
        binding.signOut.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startLoginActivity()
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    private fun setUpObservers() {
        viewModel.newsData.observe(this){
            when(it){
                ApiResult.Loading->{
                    binding.progresbar.visibility= View.VISIBLE
                }
               is ApiResult.Success->{
                   binding.progresbar.visibility= View.GONE
                   val data=it.data
                   list= ArrayList()
                   if (data != null) {
                       for (item in data.articles){
                           item.description?.let { it1 -> item.urlToImage?.let { it2 -> RvModalClass(publishedAt = "${
                               item.publishedAt?.split("T")?.get(0)
                           } by ${item.source?.name}", heading = item.title.toString(), description = it1, url = it2) } }
                               ?.let { it2 -> list.add(it2) }
                       }
                       refinedList=list
                       parentAdapter.updateData(list)
                       viewModel.setData(list as ArrayList<RvModalClass>)
                   }
               }
               is ApiResult.Error->{
                   binding.progresbar.visibility= View.GONE
                   Toast.makeText(this@HomeActivity,it.message,Toast.LENGTH_SHORT).show()
               }
            }
        }
    }
}