package org.calamarfederal.fetchexercise

import android.app.Application
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import org.calamarfederal.fetchexercise.presentation.FetchExerciseViewModel
import org.calamarfederal.fetchexercise.presentation.MainScreen
import org.calamarfederal.fetchexercise.presentation.UIFetchItem
import org.calamarfederal.fetchexercise.ui.theme.FetchExerciseTheme

@HiltAndroidApp
class FetchExerciseApplication : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FetchExerciseTheme {
                val viewModel: FetchExerciseViewModel = hiltViewModel()
                val uiItemState by viewModel.fetchItemsState.collectAsStateWithLifecycle()

                MainScreen(items = uiItemState, refresh = viewModel::refreshItems)
            }
        }
    }
}
