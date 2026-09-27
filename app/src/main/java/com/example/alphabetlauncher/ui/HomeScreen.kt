package com.example.alphabetlauncher.ui


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.milliseconds
import androidx.compose.ui.platform.LocalContext

@Composable
fun HomeScreen(
    apps: List<AppInfo>
) {
    val context = LocalContext.current

    var selectedLetter by remember { mutableStateOf<Char?>(null) }
    var fingerX by remember { mutableFloatStateOf(0f) }
    var fingerY by remember { mutableFloatStateOf(0f) }

    BackHandler(enabled = selectedLetter != null) {
        selectedLetter = null
    }

    fun launchApp(app: AppInfo) {
        val intent = context.packageManager
            .getLaunchIntentForPackage(app.packageName)

        if (intent != null) {
            context.startActivity(intent)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            if (selectedLetter == null) {
                HomeContent(
                    apps = apps,
                    onAppClick = { app ->
                        launchApp(app)
                    }
                )
            } else {
                LetterContent(
                    letter = selectedLetter!!,
                    apps = apps,
                    onAppClick = { app ->
                        launchApp(app)
                    }
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(72.dp)
        ) {
            AlphabetBar(
                onLetterSelected = { letter ->
                    selectedLetter = letter
                },
                onDragPosition = { x, y->
                    fingerX = x
                    fingerY = y
                },
                onDragEnd = {
                }
            )
        }
    }
}


@Composable
private fun HomeContent(
    apps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 24.dp,
                top = 48.dp,
                end = 16.dp
            )
    ){
        ClockAndDate()

        Spacer(modifier = Modifier.size(32.dp))

        Text(text = "Favourites")

        Spacer(modifier = Modifier.size(16.dp))

        FavouriteApps(
            apps = apps.take(6),
            onAppClick = onAppClick
        )

    }
}

@Composable
private fun LetterContent(
    letter: Char,
    apps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit
) {

    val filteredApps = remember(letter, apps) {
        apps.filter { app ->

            app.name
                .firstOrNull()?.uppercaseChar() == letter
        }
            .sortedBy { it.name.lowercase() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 24.dp,
                top = 48.dp,
                end = 16.dp
            )
    ) {
        Text(text = letter.toString())
        Spacer(modifier = Modifier.size(24.dp))
        if (filteredApps.isEmpty()){
            Text(
                text = "No Apps"
            )
        }
        else {
            FilteredApps(
                apps = filteredApps,
                onAppClick = onAppClick
                )
        }
    }
}

@Composable
private fun FilteredApps(apps: List<AppInfo>, onAppClick: (AppInfo) -> Unit) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items (
            items = apps,
            key = { it.packageName }
        ){app ->

            val iconBitmap = remember( app.packageName){
                app.icon
                    .toBitmap()
                    .asImageBitmap()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onAppClick(app)
                    },
                verticalAlignment =  Alignment.CenterVertically
            ){
                Image(
                    bitmap = iconBitmap,
                    contentDescription = app.name,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = app.name
                )
            }
        }
    }
}


@Composable
fun ClockAndDate() {

            var currentDateTime by remember { mutableStateOf(LocalDateTime.now()) }

            LaunchedEffect(Unit) {
                while (true) {
                    currentDateTime = LocalDateTime.now()
                    delay(1000.milliseconds)

                }
            }
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM")

            Column {
                Text(
                    text = currentDateTime.format(timeFormatter)
                )
                Text(
                    text = currentDateTime.format(dateFormatter)
                )
            }
        }


@Composable
fun FavouriteApps(apps: List<AppInfo>,
                  onAppClick: (AppInfo) -> Unit) {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(
                    items = apps,
                    key = { it.packageName }
                ) { app ->

                    val iconBitmap = remember( app.packageName){
                        app.icon
                            .toBitmap()
                            .asImageBitmap()
                    }


                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onAppClick(app)
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            bitmap = iconBitmap,
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




