package com.example.collectivetrek.fragments

import android.app.Activity
import android.net.Uri
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
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.collectivetrek.R


class PhotoUploadFragment : AppCompatActivity() {
    private var images: ArrayList<Uri?>? = null
    private lateinit var image: ImageView
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private lateinit var uploadButton: Button
    private lateinit var textView: TextView

    override fun onCreate(savedInsanceState: Bundle?) {
        super.onCreate(savedInsanceState)
        setContentView(R.layout.photo_upload)

        image = findViewById(R.id.imageView)
        prevButton = findViewById(R.id.prevButton)
        nextButton = findViewById(R.id.nextButton)
        uploadButton = findViewById(R.id.uploadButton)
        textView = findViewById(R.id.textView2)

        val ids: Array<Int> = arrayOf(
            R.drawable.p,
            R.drawable.theresa

        )

        var counter = 0

        val length = ids.size
        var currentImage = counter + 1

        image.setImageResource(ids[0])

        textView.text = "$currentImage / $length"


        uploadButton.setOnClickListener{

        }
        nextButton.setOnClickListener {
            counter++
            if(counter >= length) {
                counter = 0
                currentImage = 0
            }
            image.setImageResource(ids[counter])
            currentImage++
            textView.text = "$currentImage / $length"
        }
        prevButton.setOnClickListener {
            counter--
            if(counter < 0) {
                counter = length - 1
                currentImage = 2
            }
            image.setImageResource(ids[counter])
            currentImage--
            textView.text = "$currentImage / $length"
        }






        val galleryImage = registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback{
                image.setImageURI(it)
            })

        uploadButton.setOnClickListener{
            galleryImage.launch("image/*")
        }


    }
}





