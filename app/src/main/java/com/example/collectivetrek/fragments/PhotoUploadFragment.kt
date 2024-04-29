package com.example.collectivetrek.fragments


import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.findViewTreeViewModelStoreOwner

import androidx.appcompat.app.AppCompatActivity

import com.example.collectivetrek.R


class PhotoUploadFragment : Fragment() {
    private var images: ArrayList<Uri?>? = null
    private lateinit var image: ImageView
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private lateinit var uploadButton: Button
    private lateinit var textView: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,

        ): View? {
        // Inflate the layout

        return inflater.inflate(R.layout.photo_upload, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        image = view.findViewById(R.id.imageView1)
        textView = view.findViewById(R.id.textView2)
        prevButton = view.findViewById(R.id.prevButton)
        nextButton = view.findViewById(R.id.nextButton)
        uploadButton = view.findViewById(R.id.uploadButton)

        val ids: Array<Int> = arrayOf(
            R.drawable.p,
            R.drawable.profile

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