package com.livetyping.activitybinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.livetyping.images.ImagesBinder


class ImagesExampleActivity : AppCompatActivity() {

    private lateinit var imagesBinder: ImagesBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)
        imagesBinder = (application as BinderExampleApplication).imagesBinder
    }
}
