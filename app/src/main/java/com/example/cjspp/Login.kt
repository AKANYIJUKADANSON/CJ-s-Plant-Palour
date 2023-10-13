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
import androidx.lifecycle.ViewModelProvider
import com.example.cjspp.databinding.ActivityLoginBinding
import com.example.cjspp.mvvm.FirebaseViewModel
import com.example.cjspp.mvvm.FirebaseViewModelFactory
import com.example.cjspp.mvvm.FirebaseViewModelRepository
import com.example.cjspp.newsapp.AddNews
import com.example.cjspp.newsapp.News
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class Login : BaseActivity() {
    lateinit var binding:ActivityLoginBinding
    lateinit var firebaseViewModel:FirebaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // Initializing the view model
        val repository = FirebaseViewModelRepository()
        val factory = FirebaseViewModelFactory(repository)
        firebaseViewModel = ViewModelProvider(this, factory)[FirebaseViewModel::class.java]

        // Login button
        binding.buttonLogin.setOnClickListener {
            validateUserData()
        }

        // Text - have no account
        binding.llHaveNoAccount.setOnClickListener {
            val intentHaveNoAccount = Intent(this@Login, Signup::class.java)
            startActivity(intentHaveNoAccount)
            // finish()
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
                // View model login
                firebaseViewModel.userLogin(this, email, password)
                return true
            }
        }
    }

    fun loginSuccess() {
        // End the progress dialog
        mProgressDialog.dismiss()
        val intent = Intent(this, Welcome::class.java)

        // Clearing previous tasks
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}