package com.alibagherifam.mentha.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alibagherifam.mentha.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        lifecycleScope.launch {
            delay(with(Duration) { 2.seconds + 500.milliseconds })
            finishSplash()
        }
    }

    private fun finishSplash() {
        val i = Intent(this, CameraActivity::class.java)
        startActivity(i)
        finish()
    }
}
