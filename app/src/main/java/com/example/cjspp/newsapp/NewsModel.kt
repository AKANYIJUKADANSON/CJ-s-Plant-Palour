package com.example.cjspp.newsapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsModel(
    val image:String = "",
    val title:String = "",
    val description:String = "",
    val dateCreated:String = "",
    val createdBy:String = ""
):Parcelable
