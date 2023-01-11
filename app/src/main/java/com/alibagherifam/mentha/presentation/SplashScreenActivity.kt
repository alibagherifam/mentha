package com.alibagherifam.mentha.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.alibagherifam.mentha.R
import com.alibagherifam.mentha.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SplashScreen() }
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

@Preview
@Composable
fun SplashScreen() {
    AppTheme {
        Surface {
            SplashContent()
        }
    }
}

@Composable
fun SplashContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(110.dp),
            painter = painterResource(id = R.drawable.img_mentha_logo),
            contentDescription = "logo"
        )
        Spacer(modifier = Modifier.size(120.dp))
        Text(
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.h1,
            text = stringResource(id = R.string.app_name)
        )
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                style = MaterialTheme.typography.h3,
                text = stringResource(id = R.string.message_commercial_slogan)
            )
        }
    }
}
