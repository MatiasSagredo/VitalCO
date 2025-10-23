package com.example.vitalco

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.vitalco.ui.theme.VitalCOTheme
import com.example.vitalco.ui.screens.Home

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VitalCOTheme {
                Home()
            }
        }
    }
}
