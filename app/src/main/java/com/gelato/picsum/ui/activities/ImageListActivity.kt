package com.gelato.picsum.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gelato.picsum.R

/**
 * Main Activity to hold Image list and Image Detail Fragment
 */
class ImageListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_list)
    }
}