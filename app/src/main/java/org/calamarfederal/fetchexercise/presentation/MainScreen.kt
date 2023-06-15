package org.calamarfederal.fetchexercise.presentation

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.calamarfederal.fetchexercise.ui.theme.FetchExerciseTheme

/**
 * # Fetch Coding Exercise Solution Screen
 *
 * [items] should already be culled and sorted
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainScreen(
    items: List<UIFetchItem>,
    refresh: () -> Unit,
) {
    Scaffold { padding ->
        MainScreenLayout(
            items = items,
            refresh = refresh,
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
                .fillMaxSize(),
        )
    }
}

/**
 * # Content of [MainScreen]
 */
@Composable
private fun MainScreenLayout(
    items: List<UIFetchItem>,
    refresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(items = items, key = { it.id }) { item ->
            ListItem(
                headlineContent = { Text(item.name) },
                supportingContent = { Text("${item.id}") },
                leadingContent = { Text("${item.listId}") },
            )
        }
        if (items.isEmpty()) {
            item { Text("Empty List") }
        }
        item {
            Button(onClick = refresh) {
                Text("Load")
            }
        }
    }
}

/**
 * ## Preview
 */
@Preview
@Composable
private fun MainScreenPreview() {
    FetchExerciseTheme {
        val items = (0..20).map { UIFetchItem(id = it, listId = it, name = "$it") }
        MainScreen(
            items = items,
            refresh = {},
        )
    }
}
