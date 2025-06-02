package com.hfad.easyspeak.presentation.signup2

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel2 {
}

fun SignUp(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onResult: (Boolean) -> Unit
) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("MyLog", "createUserWithEmail:success")
                onResult(true)
            } else {
                Log.d("MyLog", "createUserWithEmail:Failed")
                onResult(false)
            }
        }
}
