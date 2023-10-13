package com.example.cjspp

import android.app.Dialog
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.cjspp.databinding.ProgressDialogBinding

open class BaseActivity:AppCompatActivity() {
    lateinit var mProgressDialog: Dialog
    lateinit var progressBarBinding: ProgressDialogBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        progressBarBinding = DataBindingUtil.setContentView(this, R.layout.progress_dialog)
    }

    fun progressDialog(text: String) {
        mProgressDialog = Dialog(this)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        mProgressDialog.setContentView(R.layout.progress_dialog)


        progressBarBinding.tvProgressDialog.text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //Start the dialog and display it on screen.
        mProgressDialog.show()
    }
}