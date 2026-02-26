package com.example.ethelutilityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ethelutilityapp.screen.EthelUtilityApp
import com.example.ethelutilityapp.ui.theme.EthelUtilityAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EthelUtilityAppTheme {
                EthelUtilityApp()
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun EthelUtilityAppPreview() {
    EthelUtilityAppTheme {
        EthelUtilityApp()
    }
}

