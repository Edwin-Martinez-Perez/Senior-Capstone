package com.example.collectivetrek.fragments

import android.app.Activity
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.collectivetrek.R


class PhotoUploadFragment : AppCompatActivity() {
    private lateinit var image: ImageView
    private lateinit var btnBrowse: Button

    override fun onCreate(savedInsanceState: Bundle?){
        super.onCreate(savedInsanceState)
        setContentView(R.layout.photo_upload)

        image = findViewById(R.id.imageView)
        btnBrowse = findViewById(R.id.button2)

        val galleryImage = registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback{
                image.setImageURI(it)
            })

        btnBrowse.setOnClickListener{
            galleryImage.launch("image/*")
        }


    }
}