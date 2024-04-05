
package com.example.collectivetrek.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.collectivetrek.R

class PhotoUploadFragment : Fragment() {
    private lateinit var image: ImageView
    private lateinit var btnBrowse: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.photo_upload, container, false)
        image = rootView.findViewById(R.id.imageView)
        btnBrowse = rootView.findViewById(R.id.button2)

        val galleryImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            image.setImageURI(uri)
        }

        btnBrowse.setOnClickListener {
            galleryImage.launch("image/*")
        }

        return rootView
    }
}