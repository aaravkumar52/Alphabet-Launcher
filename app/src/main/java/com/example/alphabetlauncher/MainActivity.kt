package com.example.alphabetlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.alphabetlauncher.ui.HomeScreen
import com.example.alphabetlauncher.ui.theme.AlphabetLauncherTheme
import com.example.alphabetlauncher.viewmodel.LauncherViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AlphabetLauncherTheme {
                HomeScreen(
                    apps = viewModel.apps
                )
            }
        }
    }
}
