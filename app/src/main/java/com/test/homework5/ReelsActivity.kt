package com.test.homework5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ReelsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reels)
        supportActionBar?.hide()
    }
}