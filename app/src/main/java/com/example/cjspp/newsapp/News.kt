package com.example.cjspp.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cjspp.R
import com.example.cjspp.databinding.ActivityNewsBinding
import com.example.cjspp.firestore.Firestore

class News : AppCompatActivity() {
    lateinit var binding:ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news)
        setUpActionBar()

        binding.floatingActionButtonAddNews.setOnClickListener {
            val intent = Intent(this, AddNews::class.java)
            startActivity(intent)
        }
    }

    private fun setUpActionBar(){
        val myToolBar = findViewById<Toolbar>(R.id.news_list_toolbar)
        setSupportActionBar(myToolBar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        }

        myToolBar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onResume() {
        super.onResume()

        // download files from firestore
        Firestore().downloadNewsListFromFireStore(this)
    }

    fun getNewsListSuccess(downloadedNewsList:ArrayList<NewsModel>){
        if (downloadedNewsList.size > 0){
            val recyclerView = binding.rvNewsList
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(this, 1)

            val adapter = NewsAdapter(this)
            adapter.diff.submitList(downloadedNewsList)
            recyclerView.adapter = adapter
        }

    }
}