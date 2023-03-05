package com.example.image_classification_app

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import com.example.image_classification_app.databinding.ActivityMainBinding


    get() {}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private lateinit var tvOutput: TextView
    private val GALLERY_REQUEST_CODE=123


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        imageView=binding.imageView
        button=binding.binCaptureImage
        tvOutput=binding.textview
        val buttonLoad=binding.binLoadImage

        button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED)
                {
                 textPicturePreview.launch(null)
                }
            else
            {
             requestPermission.launch(android.Manifest.permission.CAMERA)
            }
        }
        
    }
    // Launch camera and take a picture
    private val takePicturePreview= registerForActivityResult(ActivityResultContracts.TakePicturePreview()){
        bitmap->if(bitmap!=null)
    {
     imageView.setImageBitmap(bitmap)
     outputGenerator(bitmap)

    }
    }
}