package com.example.cjspp.newsapp


import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.cjspp.R
import com.example.cjspp.databinding.ActivityAddNewsBinding
import com.example.cjspp.utilities.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class AddNews : AppCompatActivity() {
    lateinit var binding:ActivityAddNewsBinding
    private var storageReference = FirebaseStorage.getInstance()
    private var mFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var auth:FirebaseAuth
    private lateinit var imageUri:Uri
//    var collection:CollectionReference = FirebaseFirestore.getInstance().collection(Constants.NEWS_COLLECTION)

    // Credentials
    var currentUserId: String = ""
    var currentUserName: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_news)

        auth = FirebaseAuth.getInstance()


        binding.apply {
            // get the current loged in user and their id
            currentUserId = auth.currentUser!!.uid
            currentUserName = auth.currentUser!!.displayName.toString()

            buttonAddNews.setOnClickListener {
                addNews()
            }

            buttonAddNewsImage.setOnClickListener {
                var localData : Intent = Intent(Intent.ACTION_GET_CONTENT)
                localData.setType("image/*")
                startActivityForResult(localData,Constants.NEWS_IMAGE_REQUEST_CODE)
            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addNews(){
        val title = binding.edNewsTitle.text.toString()
        val description = binding.edNewsDescription.text.toString()

        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(this.contentResolver.getType(imageUri))
        // adding time stamp will make unique name of the image in the database
        val filePath:StorageReference = storageReference.reference.child("News").child("daily_images" + Timestamp.now().seconds)
        filePath.putFile(imageUri)
            .addOnSuccessListener {

                // Get the image file/download url and store data
                filePath.downloadUrl.addOnSuccessListener {
                    val imageDownloadUrl = it

                    // time from firebase
                    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                    val dateCreated = LocalDateTime.now().format(formatter)

                    // store other data to firebase storage
                    val newsArticle = NewsModel(
                        imageDownloadUrl.toString(),title, description, dateCreated.toString(), currentUserName
                    )

                    mFirestore.collection(Constants.NEWS_COLLECTION).add(newsArticle)
                        .addOnSuccessListener {
                            Toast.makeText(this, "News article added  successfully", Toast.LENGTH_LONG).show()

                            val intent = Intent(this, News::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "News article not added", Toast.LENGTH_LONG).show()
                        }


                }
            }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Constants.NEWS_IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            if (data != null){
                // Getting actual image path and assign it to the imageUri variable
                imageUri = data.data!!

                // Displaying the image in the app
                binding.imageViewNews.setImageURI(imageUri)
            }
        }
    }
}