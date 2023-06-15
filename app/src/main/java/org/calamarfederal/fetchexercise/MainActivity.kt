package org.calamarfederal.fetchexercise

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import org.calamarfederal.fetchexercise.presentation.FetchExerciseViewModel
import org.calamarfederal.fetchexercise.presentation.MainScreen
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
                val loadState by viewModel.loadingState.collectAsStateWithLifecycle()

                MainScreen(items = uiItemState, onRefresh = viewModel::refreshItems, loadState = loadState)
            }
        }
    }
}
