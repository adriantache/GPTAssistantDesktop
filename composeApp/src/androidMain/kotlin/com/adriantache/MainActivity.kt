package com.adriantache

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import old_code.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initContextProvider()

        setContent {
            App()
        }
    }

    private fun initContextProvider() {
        ContextProvider.init(this)
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
