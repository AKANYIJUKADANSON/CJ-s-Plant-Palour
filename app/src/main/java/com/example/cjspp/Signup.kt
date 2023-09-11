package com.example.cjspp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.example.cjspp.databinding.ActivitySignupBinding
import com.example.cjspp.firestore.Firestore
import com.example.cjspp.modules.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class Signup : AppCompatActivity() {
    lateinit var binding:ActivitySignupBinding
    lateinit var selectedGender:String
    private var mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)

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


        // Create account button
        binding.buttonRegister.setOnClickListener {
            createUser()
        }

        binding.llAlreadyHaveAccount.setOnClickListener {
            val i:Intent = Intent(this, Login::class.java)
            startActivity(i)
            finish()
        }
        // Action bar setup
        setUpActionBar()

        // making the progress dialog invisible
        binding.postProgressBar.visibility = View.INVISIBLE
    }

    private fun setUpActionBar(){
        val myToolBar = findViewById<Toolbar>(R.id.signup_toolbar)
        setSupportActionBar(myToolBar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        }

        myToolBar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateUserDetails(
        firstName:String, lastName:String, placeOfResidence:String,
        phoneNumber:String, email:String, password:String, confirmPassword:String
    ) : Boolean{
        return when{
//            TextUtils.isEmpty(firstName.trim { it <= ' ' })->{
//                Toast.makeText(this, "First name is required", Toast.LENGTH_LONG).show()
//                false
//            }
//            TextUtils.isEmpty(lastName.trim { it <= ' ' })->{
//                Toast.makeText(this, "last name is required", Toast.LENGTH_LONG).show()
//                false
//            }
//            TextUtils.isEmpty(placeOfResidence.trim { it <= ' ' })->{
//                Toast.makeText(this, "place of residence is required", Toast.LENGTH_LONG).show()
//                false
//            }
//            TextUtils.isEmpty(phoneNumber.trim { it <= ' ' })->{
//                Toast.makeText(this, "Phone number is required", Toast.LENGTH_LONG).show()
//                false
//            }

            TextUtils.isEmpty(email.trim { it <= ' ' })->{
                Toast.makeText(this, "Email is required", Toast.LENGTH_LONG).show()
                false
            }
            TextUtils.isEmpty(password.trim { it <= ' ' })->{
                Toast.makeText(this, "Password is required", Toast.LENGTH_LONG).show()
                false
            }

//            TextUtils.isEmpty(confirmPassword.trim { it <= ' ' })->{
//                Toast.makeText(this, "First name is required", Toast.LENGTH_LONG).show()
//                false
//            }
//
//            password.length < 8 ->{
//                Toast.makeText(this, "Password must be eight characters and above",
//                    Toast.LENGTH_LONG).show()
//                false
//            }
//
//            password.trim { it <= ' ' } != confirmPassword.trim{it <= ' '} ->{
//                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show()
//                false
//            }
//
//            selectedGender.isEmpty() ->{
//                Toast.makeText(this, "Please select gender", Toast.LENGTH_LONG).show()
//                false
//            }
            else ->{
                true
            }
        }

    }


    private fun createUser(){

        binding.postProgressBar.visibility = View.VISIBLE
        val email = binding.edSignInEmail.text.toString()
        val password = binding.edSignupPassword.text.toString()
        val firstName = binding.edSignupFirstName.text.toString()
        val lastName = binding.edSignupLastName.text.toString()
        val placeOfResidence = binding.edSignupPlaceOfResidence.text.toString()
        val phoneNumber = binding.edSignupPhoneNumber.text.toString()

        if (validateUserDetails(
                binding.edSignupFirstName.text.toString(), binding.edSignupLastName.text.toString(),
                binding.edSignupPlaceOfResidence.text.toString(), binding.edSignupPhoneNumber.text.toString(),
                binding.edSignInEmail.text.toString(), binding.edSignupPassword.text.toString(),
                binding.edSignupConfirmPassword.text.toString()
            )
        ){
            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
//                        Toast.makeText(this, "Account created successfully", Toast.LENGTH_LONG).show()
                        val userId = mFirebaseAuth.currentUser!!.uid.toString()
                        // store user details
                        val user = User(userId,firstName, lastName, placeOfResidence, phoneNumber, selectedGender, email)
                        Firestore().storeUserDetailsToFirestore(this, user)

                    }
                    else{
                        // making the progress dialog invisible
                        binding.postProgressBar.visibility = View.INVISIBLE

                        Toast.makeText(this,
                            task.exception?.message.toString()
                            , Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    fun storeUserDataToFirestoreSuccess() {
        // End the progress dialog
        binding.postProgressBar.visibility = View.INVISIBLE
        Toast.makeText(this, "Your account has been created successfully", Toast.LENGTH_LONG).show()
        val intent = Intent(this, Welcome::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

}