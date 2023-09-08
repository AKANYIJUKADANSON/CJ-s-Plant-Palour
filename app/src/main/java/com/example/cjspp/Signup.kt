package com.example.cjspp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.cjspp.databinding.ActivitySignupBinding
import com.example.cjspp.modules.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class Signup : AppCompatActivity() {
    lateinit var binding:ActivitySignupBinding
    lateinit var selectedGender:String
    private var mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)

        // Create account button
        binding.buttonRegister.setOnClickListener {
            createAccount()
        }
    }

    private fun validateUserData(
        firstName:String, lastName:String, placeOfResidence:String,
        phoneNumber:String, email:String, password:String, confirmPassword:String
    ) : Boolean{
        return when{
            TextUtils.isEmpty(firstName.trim { it <= ' ' })->{
                Toast.makeText(this, "First name is required", Toast.LENGTH_LONG).show()
                false
            }
            TextUtils.isEmpty(lastName.trim { it <= ' ' })->{
                Toast.makeText(this, "last name is required", Toast.LENGTH_LONG).show()
                false
            }
            TextUtils.isEmpty(placeOfResidence.trim { it <= ' ' })->{
                Toast.makeText(this, "place of residence is required", Toast.LENGTH_LONG).show()
                false
            }
            TextUtils.isEmpty(phoneNumber.trim { it <= ' ' })->{
                Toast.makeText(this, "Phone number is required", Toast.LENGTH_LONG).show()
                false
            }
            TextUtils.isEmpty(email.trim { it <= ' ' })->{
                Toast.makeText(this, "Email is required", Toast.LENGTH_LONG).show()
                false
            }
            TextUtils.isEmpty(password.trim { it <= ' ' })->{
                Toast.makeText(this, "Password is required", Toast.LENGTH_LONG).show()
                false
            }
            TextUtils.isEmpty(confirmPassword.trim { it <= ' ' })->{
                Toast.makeText(this, "First name is required", Toast.LENGTH_LONG).show()
                false
            }

            password.length < 8 ->{
                Toast.makeText(this, "Password must be eight characters and above",
                    Toast.LENGTH_LONG).show()
                false
            }

            password.trim { it <= ' ' } != confirmPassword.trim{it <= ' '} ->{
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show()
                false
            }

            selectedGender.isEmpty() ->{
                Toast.makeText(this, "Please select gender", Toast.LENGTH_LONG).show()
                false
            }
            else ->{
                mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            Toast.makeText(this, "Account created successfully", Toast.LENGTH_LONG).show()
                            val userId = mFirebaseAuth.currentUser!!.uid.toString()
                            // store user details
                            val user = User(userId,firstName, lastName, placeOfResidence
                            , phoneNumber, selectedGender, email)

                            TODO("store user details to user collection")
                        }
                    }
                return true
            }
        }

    }
    fun createAccount(){
        // List of gender values
        val genderList = arrayOf("Male", "Female")

        // Referencing the auto complete text view
        val autoCompleteGender = binding.autoCompleteGender

        // Create the adapter
        val adapter = ArrayAdapter(this, R.layout.custom_gender_list, genderList)

        // setting the adapter
        autoCompleteGender.setAdapter(adapter)

        // Get selected value
        autoCompleteGender.setOnItemClickListener { adapterView, view, i, l ->
            selectedGender = adapterView.getItemAtPosition(i).toString()
        }

        validateUserData(
            binding.edSignupFirstName.toString(), binding.edSignupLastName.toString(),
            binding.edSignupPlaceOfResidence.toString(), binding.edSignupPhoneNumber.toString(),
            binding.edSignInEmail.toString(), binding.edSignupPassword.toString(),
            binding.edSignupConfirmPassword.toString()
        )

    }
}