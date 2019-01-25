package com.livetyping.activitybinder

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.livetyping.images.ImagesBinder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_images.*
import java.io.File
import java.io.IOException


class ImagesExampleActivity : AppCompatActivity() {

    private lateinit var imagesBinder: ImagesBinder
    private lateinit var mCurrentPhotoPath: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)
        imagesBinder = (application as BinderExampleApplication).imagesBinder

        gallery.setOnClickListener {
            imagesBinder.pickImageFromGallery { imageFile ->
                image.setImageURI(Uri.fromFile(imageFile))
            }
        }
        thumbnail_camera.setOnClickListener {
            imagesBinder.takeThumbnailFromCamera("rationale text for active permission", "settings")
            { bitmap ->
                image.setImageBitmap(bitmap)
            }
        }
        multiple_gallery.setOnClickListener {
            imagesBinder.multiplePickFromGallery {
                Toast.makeText(this, it.size.toString(), Toast.LENGTH_SHORT).show()
            }
        }

//        full_size_from_camera.setOnClickListener {
//            imagesBinder.takeFullSizeFromCamera("rationale text for active permission", "settings")
//            { photoFile ->
////                image.setImageURI(Uri.fromFile(photoFile))
////                val decodeFile = BitmapFactory.decodeFile(photoFile.absolutePath)
////                image.setImageBitmap(decodeFile)
//                Picasso.get().load(photoFile).into(image)
//            }
//        }

        full_size_from_camera.setOnClickListener {
            try {
                //                    File path = Environment.getExternalStoragePublicDirectory(
                //                            Environment.DIRECTORY_PICTURES);
                val path = Environment.getExternalStorageDirectory()
                val pathExists = path.exists() || !path.exists() && path.mkdirs()
                if (!pathExists) {
//                    subscriber.onError(FileNotFoundException("Specified path " +
//                            path.toString() + " not exists"))
//                    return
                }
                val fileName = System.currentTimeMillis().toString() + "_confa"
                val file = File.createTempFile(fileName, ".jpg", path)

                file.deleteOnExit()
                mCurrentPhotoPath = FileProvider.getUriForFile(this,
                        applicationContext.packageName + ".provider", file)
                val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoPath)
                startActivityForResult(captureIntent, 1234)

            } catch (e: IOException) {
            }

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
        if (requestCode == 1234) {
            Picasso.get().load(mCurrentPhotoPath).into(image)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imagesBinder.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
