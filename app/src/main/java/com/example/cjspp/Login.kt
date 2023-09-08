package com.example.cjspp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.cjspp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    lateinit var binding:ActivityLoginBinding
    private var mFirebaseAuth:FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // Login button
        binding.buttonLogin.setOnClickListener {
            validateUserData()
        }

        // Text - have no account
        binding.tvHaveNoAccount.setOnClickListener {
            val intent = Intent(this@Login, Signup::class.java)
            startActivity(intent)
            finish()
        }
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

                            val intent = Intent(this, Welcome::class.java)

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