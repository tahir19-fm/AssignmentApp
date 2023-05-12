package com.example.assignmentapp.login.data

data class RegisterationModel(val email: String,
                              val name: String,
                              val phoneNumber: String){
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf("email" to this.email,
            "name" to this.name,
            "phone_number" to this.phoneNumber)
    }

}