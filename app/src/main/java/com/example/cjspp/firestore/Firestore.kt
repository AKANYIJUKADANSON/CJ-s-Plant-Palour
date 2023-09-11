package com.example.cjspp.firestore

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.example.cjspp.Signup
import com.example.cjspp.modules.User
import com.example.cjspp.newsapp.News
import com.example.cjspp.newsapp.NewsModel
import com.example.cjspp.utilities.Constants
import com.google.firebase.firestore.FirebaseFirestore

open class Firestore {
    private var mFirestore = FirebaseFirestore.getInstance()

    fun storeUserDetailsToFirestore(activity: Signup, user:User){
        mFirestore.collection(Constants.USERS_COLLECTION)
            .document().set(user)
            .addOnSuccessListener {
                activity.storeUserDataToFirestoreSuccess()
            }
    }

    fun downloadNewsListFromFireStore(activity: News){
        mFirestore.collection(Constants.NEWS_COLLECTION)
            .get().addOnSuccessListener { news ->
                val newsList:ArrayList<NewsModel> = ArrayList()

                for (i in news.documents){
                    val singleNews = i.toObject(NewsModel::class.java)
                    newsList.add(singleNews!!)
                }

                activity.getNewsListSuccess(newsList)
            }
    }
}