package com.example.alphabetlauncher.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.alphabetlauncher.data.model.AppInfo
import com.example.alphabetlauncher.data.repository.AppRepository

class LauncherViewModel (
    application: Application
): AndroidViewModel(application){

    private val repository = AppRepository(application.packageManager)

    val apps : List<AppInfo> by lazy {
        repository.getLaunchableApps()
    }
}