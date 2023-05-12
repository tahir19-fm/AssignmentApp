package com.example.assignmentapp.login.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.assignmentapp.R
import com.example.assignmentapp.databinding.ActivityLoginBinding
import com.example.assignmentapp.login.utils.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel:LoginViewModel by viewModels()
    companion object{
        const val LOGIN_FRAGMENT=1
        const val REGISTER_FRAGMENT=2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViews()
        setupObservers()
    }


    private fun setupViews() {
        viewModel.setFragmentState(LOGIN_FRAGMENT)
        binding.registerFrameTv.setOnClickListener{
           viewModel.setFragmentState(REGISTER_FRAGMENT)
        }
        binding.loginFrameTv.setOnClickListener{
            viewModel.setFragmentState(LOGIN_FRAGMENT)
        }
    }

    private fun setupObservers() {
        viewModel.fragmentState.observe(this){
            when(it){
                LOGIN_FRAGMENT->{
                    beginLoginTransitions()
                }
                REGISTER_FRAGMENT->{
                    beginRegisterTransitions()
                }
            }
        }
    }


    private fun beginRegisterTransitions(){
        binding.apply {
            registerFrameTv.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.white))
            registerFrameTv.setBackgroundResource(R.drawable.round_solid_bottomstroke)
            loginFrameTv.setBackgroundResource(R.color.transparentColor)
            loginFrameTv.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.black))
            val fragmentManager = supportFragmentManager.beginTransaction()
            fragmentManager.replace(R.id.fragmentHolder,RegisterFragment()).commit()
        }
    }
    private fun beginLoginTransitions(){
        binding.apply {
            loginFrameTv.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.white))
            loginFrameTv.setBackgroundResource(R.drawable.round_solid_bottomstroke)
            registerFrameTv.setBackgroundResource(R.color.transparentColor)
            registerFrameTv.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.black))
            val fragmentManager = supportFragmentManager.beginTransaction()
            fragmentManager.replace(R.id.fragmentHolder,LoginFragment()).commit()
        }
    }


    override fun onBackPressed() {
        if(backPressOverride())
            return
        super.onBackPressed()
    }
    private fun backPressOverride() : Boolean {
        when(viewModel.fragmentState.value){
            REGISTER_FRAGMENT -> {
                viewModel.setFragmentState(LOGIN_FRAGMENT)
                return true
            }
        }
        return false
    }

}