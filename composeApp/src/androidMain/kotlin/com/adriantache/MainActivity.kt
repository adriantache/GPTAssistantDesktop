package com.adriantache

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import presentation.App
import theme.AppColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initContextProvider()

        setContent {
            val systemUiController = rememberSystemUiController()
            val bgColor = AppColor.background()
            val isDark = isSystemInDarkTheme()

            SideEffect {
                systemUiController.setStatusBarColor(
                    color = bgColor,
                    darkIcons = isDark
                )
            }

            App()
        }
    }

    private fun initContextProvider() {
        ContextProvider.init(this)
    }
}
