package com.adriantache

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import presentation.App

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
