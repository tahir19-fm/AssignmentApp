package com.example.assignmentapp.login.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignmentapp.login.data.RegisterationModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() :ViewModel() {
    private val _fragmentState = MutableLiveData<Int>()
    val fragmentState : LiveData<Int>
        get() = _fragmentState

    fun setFragmentState(state:Int){
        _fragmentState.value=state
    }

    private val auth: FirebaseAuth = Firebase.auth
    private val _loading=MutableLiveData(false)
    val loading:LiveData<Boolean> =_loading

    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit,error:(String)->Unit)
            = viewModelScope.launch {
        _loading.value = true
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _loading.value = false
                        Log.d("FireBSe", "signInWithEmailAndPassword: Yayayay! ${task.result}")
                        home()
                    }
                }
                .addOnFailureListener {
                    _loading.value = false
                    Log.d("FireBSe", "signInWithEmailAndPassword: $it")
                    it.localizedMessage?.let { it1 -> error(it1) }
                }

        } catch (ex: Exception) {
            _loading.value = false
            ex.localizedMessage?.let { error(it) }
            Log.d("FireBSe", "signInWithEmailAndPassword: ${ex.message}")
        }
    }


    fun createUserWithEmailAndPassword(
        user: RegisterationModel,
        password: String,
        home: () -> Unit,
        error: (String) -> Unit) {
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(user.email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        createUser(user)
                        home()
                    }else {
                        error(task.result)

                    }
                    _loading.value = false


                }
                .addOnFailureListener{
                    _loading.value=false
                    it.localizedMessage?.let { it1 -> error(it1) }
                }
        }


    }

    private fun createUser(user: RegisterationModel) {
        val userId = auth.currentUser?.uid

        val userData = RegisterationModel(email = user.email, name = user.name, phoneNumber = user.phoneNumber).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .document(userId.toString())
            .set(userData)

    }

    }