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


//@Composable
//fun AppList(
//    apps: List<AppInfo>
//) {
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        items(apps) { app ->
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    bitmap = app.icon
//                        .toBitmap()
//                        .asImageBitmap(),
//                    contentDescription = app.name,
//                    modifier = Modifier.size(48.dp)
//                )
//                Text(
//                    text = app.name,
//                    modifier = Modifier.padding(start = 16.dp)
//                )
//            }
//        }
//    }



//@Preview(showBackground = true)
//@Composable
//fun AppListPreview() {
//    AlphabetLauncherTheme {
//        HomeScreen(apps = emptyList())
//    }
//}