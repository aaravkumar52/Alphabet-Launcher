package com.example.alphabetlauncher.ui

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.example.alphabetlauncher.data.model.AppInfo
import com.example.alphabetlauncher.ui.components.AlphabetBar
import com.example.alphabetlauncher.ui.theme.AlphabetLauncherTheme
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun HomeScreen(
    apps: List<AppInfo>
){
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp)
    ){
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(
                    top = 48.dp,
                    end = 16.dp
                )
        ){
            ClockAndDate()
            Spacer(modifier = Modifier.size(32.dp))
            Text(text = "Favourites")
            Spacer(modifier = Modifier.size(16.dp))
            FavouritesApps(
                apps = apps.take(6)
            )
        }
        AlphabetBar()
    }
}



@Composable
fun ClockAndDate() {
    
    var currentDateTime by remember { mutableStateOf(LocalDateTime.now())}

    LaunchedEffect(Unit){
        while (true){
            currentDateTime = LocalDateTime.now()
            delay(1000.milliseconds)

        }
    }
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM")

    Column{
        Text(
            text = currentDateTime.format(timeFormatter)
        )
        Text(
            text = currentDateTime.format(dateFormatter)
        )
    }
}


@Composable
fun FavouritesApps(apps: List<AppInfo>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(
            items = apps,
            key = {it.packageName}
        ){app ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    bitmap = app.icon
                        .toBitmap()
                        .asImageBitmap(),
                    contentDescription = app.name,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(
                    modifier = Modifier.width(16.dp)
                )
                Text(
                    text = app.name
                )
            }
        }
    }
}
