package com.example.alphabetlauncher.data.repository

import android.content.Intent
import android.content.pm.PackageManager
import com.example.alphabetlauncher.data.model.AppInfo

class AppRepository (
    private val packageManager : PackageManager
){
    fun getLaunchableApps() : List<AppInfo>{
        val launcherIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        return packageManager
            .queryIntentActivities(launcherIntent, PackageManager.MATCH_ALL)
            .map { resolveInfo ->

                val applicationInfo = resolveInfo.activityInfo.applicationInfo

                AppInfo (
                    name = packageManager
                        .getApplicationLabel(applicationInfo)
                        .toString(),
                    packageName  = applicationInfo.packageName,

                    icon = packageManager.getApplicationIcon(applicationInfo)
                )
        }
            .distinctBy{ it.packageName }
            .sortedBy { it.name.lowercase()}
    }
}