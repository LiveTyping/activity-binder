package com.livetyping.activitybinder

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.livetyping.images.ImagesBinder
import kotlinx.android.synthetic.main.activity_images.*


class ImagesExampleActivity : AppCompatActivity() {

    private lateinit var imagesBinder: ImagesBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)
        val binderExampleApplication = application as BinderExampleApplication
        imagesBinder = binderExampleApplication.imagesBinder

        gallery.setOnClickListener {
            imagesBinder.pickImageFromGallery { imageFile ->
                image.setImageURI(Uri.fromFile(imageFile))
            }
        }
        thumbnail_camera.setOnClickListener {
            imagesBinder.takeThumbnailFromCamera { bitmap ->
                image.setImageBitmap(bitmap)
            }
        }
        multiple_gallery.setOnClickListener {
            imagesBinder.multiplePickFromGallery {
                Toast.makeText(this, it.size.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        full_size_images.setOnClickListener {
            startActivity(Intent(this, FullSizeImagesActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        imagesBinder.attach(this)
    }

    override fun onStop() {
        super.onStop()
        imagesBinder.detach(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imagesBinder.onActivityResult(requestCode, resultCode, data, this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imagesBinder.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
