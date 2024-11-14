package com.adriantache

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import new_structure.data.migration.MigrationProcessor
import new_structure.presentation.App
import theme.AppColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initContextProvider()

        setContent {
            MaterialTheme {
                MigrationProcessor {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = AppColor.background(),
                    ) {
                        App()
                    }
                }
            }
        }
    }

    private fun initContextProvider() {
        ContextProvider.init(this)
    }
}
