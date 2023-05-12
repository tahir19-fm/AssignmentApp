package com.example.assignmentapp.login.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.unit.Constraints
import androidx.fragment.app.activityViewModels
import com.example.assignmentapp.R
import com.example.assignmentapp.databinding.FragmentLoginBinding
import com.example.assignmentapp.home.ui.HomeActivity
import com.example.assignmentapp.login.utils.LoginViewModel
import com.example.assignmentapp.utils.Constants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.math.log


class LoginFragment : Fragment() {
private val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private val viewModel : LoginViewModel by activityViewModels()
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val reqCode: Int = 123
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setUpObservers()
        googleSignIn()
    }

    private fun setUpObservers() {
        viewModel.loading.observe(viewLifecycleOwner){
            if (it){
                binding.apply {
                    loginBtn.text=""
                    progressBar.visibility=View.VISIBLE
                    loginBtn.isEnabled=false
                }

            }else{
                binding.apply {
                    loginBtn.text="LOGIN"
                    progressBar.visibility=View.GONE
                    loginBtn.isEnabled=true
                }

            }
        }

    }

    private fun setupViews() {
        binding.signInGoogle.setOnClickListener {
            signInGoogle()
        }
        binding.loginBtn.setOnClickListener{
            val email=binding.eTemail.text.toString()
            val password=binding.eTpassword.text.toString()
            if (email.isBlank() || password.length<6){
                Toast.makeText(this.requireContext(),"please check your email or password > 6",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.signInWithEmailAndPassword(email=email,password=password, home = {
                startHomeActivity()
            }){
                Toast.makeText(this.requireContext(),it,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startHomeActivity() {
        val intent = Intent(this.requireActivity(), HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        requireActivity().finish()
    }


    private fun googleSignIn(){

        FirebaseApp.initializeApp(this.requireContext())

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Constants.WEB_ID)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this.requireActivity(), gso)
        firebaseAuth = FirebaseAuth.getInstance()


    }
    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, reqCode)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == reqCode) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }
    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                updateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this.requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
               startHomeActivity()
            }
        }
    }

}