package com.example.gettogether

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.gettogether.ui.Navegacion
import com.example.gettogether.ui.theme.GetTogetherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GetTogetherTheme { // tema definido en ui/theme/Theme.kt
                Navegacion()
            }
        }
    }
}
