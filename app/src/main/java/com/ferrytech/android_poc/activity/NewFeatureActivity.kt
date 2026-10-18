package com.ferrytech.android_poc.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ferrytech.android_poc.screen.NewFeatureScreen

class NewFeatureActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge system bars inside Compose
        enableEdgeToEdge()

        setContent {
            NewFeatureScreen(
                onBackClicked = { finish() }
            )
        }
    }
}
