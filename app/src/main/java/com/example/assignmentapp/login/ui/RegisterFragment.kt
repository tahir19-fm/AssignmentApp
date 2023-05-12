package com.example.assignmentapp.login.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignmentapp.R
import com.example.assignmentapp.databinding.FragmentRegisterBinding
import com.example.assignmentapp.home.ui.HomeActivity
import com.example.assignmentapp.login.data.RegisterationModel
import com.example.assignmentapp.login.utils.LoginViewModel


class RegisterFragment : Fragment() {
private val binding by lazy { FragmentRegisterBinding.inflate(layoutInflater) }
    private val viewModel : LoginViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(this.requireContext())
        setupObservers()
    }

    private fun setupViews(context:Context) {
        binding.apply {
            countryCodePicker.registerCarrierNumberEditText(tvCreateNumber)
            registerBtn.setOnClickListener{
                val email=tvCreateEmail.text.toString()
                val password=tvCreatePassWord.text.toString()
                val name=tvEnterName.text.toString()
                val phoneNumber=countryCodePicker.fullNumberWithPlus
                if (email.isEmpty() || name.isEmpty() || password.length<6 ){
                    Toast.makeText(context,"Please check all fields",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (!agreeCheckBox.isActivated){
                    Toast.makeText(context,"Please accept terms and conditions",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel.createUserWithEmailAndPassword(RegisterationModel(email = email, name = name,phoneNumber=phoneNumber), password = password, home = {
                    startHomeActivity()
                }){
                    Toast.makeText(context,it,Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    private fun startHomeActivity() {
        val intent = Intent(this.requireActivity(), HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        requireActivity().finish()
    }
    private fun setupObservers() {
        viewModel.loading.observe(viewLifecycleOwner){
            if (it){
                binding.apply {
                    registerBtn.text=""
                    progressBar.visibility=View.VISIBLE
                    registerBtn.isEnabled=false
                }

            }else{
                binding.apply {
                    registerBtn.text="REGISTER"
                    progressBar.visibility=View.GONE
                    registerBtn.isEnabled=true
                }

            }
        }

    }



}