package com.example.cjspp.mvvm

import android.widget.Toast
import com.example.cjspp.Login
import com.example.cjspp.Signup
import com.example.cjspp.modules.User
import com.google.firebase.auth.FirebaseAuth

class FirebaseViewModelRepository {
    private val mFirebaseAuth = FirebaseAuth.getInstance()

    // Auth | Login
    fun userLogin(activity: Login, userEmail:String, userPassword:String){
        // start the progress dialog
        activity.progressDialog("Authenticating...")
        mFirebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    activity.loginSuccess()
                }else{
                    Toast.makeText(
                        activity,
                        it.exception?.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}