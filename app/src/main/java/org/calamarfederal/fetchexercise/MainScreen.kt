package org.calamarfederal.fetchexercise

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.calamarfederal.fetchexercise.ui.theme.FetchExerciseTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
    ) {
        Surface(modifier = Modifier.padding(it).consumeWindowInsets(it)) {

        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    FetchExerciseTheme {
        MainScreen()
    }
}
