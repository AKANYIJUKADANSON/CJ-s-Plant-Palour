package com.example.cjspp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.cjspp.databinding.ActivityLoginBinding
import com.example.cjspp.newsapp.AddNews
import com.example.cjspp.newsapp.News
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class Login : AppCompatActivity() {
    lateinit var binding:ActivityLoginBinding
    private var mFirebaseAuth:FirebaseAuth = FirebaseAuth.getInstance()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // Login button
        binding.buttonLogin.setOnClickListener {
            validateUserData()
        }

        // Text - have no account
        binding.llHaveNoAccount.setOnClickListener {
            val intentHaveNoAccount = Intent(this@Login, Signup::class.java)
            startActivity(intentHaveNoAccount)
//            finish()
        }

        // Forgot password
        binding.tvForgotPassword.setOnClickListener {
            val intentForgotPassword = Intent(this@Login, ForgotPassword::class.java)
            startActivity(intentForgotPassword)
        }

        // full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun validateUserData():Boolean {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()

        return when{
            TextUtils.isEmpty(email.trim { it <= ' ' }) -> {
                Toast.makeText(this, "Please enter email address", Toast.LENGTH_LONG).show()
                false
            }

            TextUtils.isEmpty(password.trim { it <= ' ' }) -> {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show()
                false
            }else ->{
                mFirebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show()

                            val intent = Intent(this, News::class.java)

                            // Clearing previous tasks
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        else{
                            Toast.makeText(
                                this@Login,
                                task.exception?.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                return true
            }
        }
    }
}