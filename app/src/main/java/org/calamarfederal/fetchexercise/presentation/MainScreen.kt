package org.calamarfederal.fetchexercise.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import org.calamarfederal.fetchexercise.presentation.UILoadState.Error
import org.calamarfederal.fetchexercise.presentation.UILoadState.Loading
import org.calamarfederal.fetchexercise.presentation.UILoadState.NotLoading
import org.calamarfederal.fetchexercise.ui.theme.FetchExerciseTheme

/**
 * # Fetch Coding Exercise Solution Screen
 *
 * [items] should already be culled and sorted
 */
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    items: List<UIFetchItem>,
    loadState: UILoadState,
    onRefresh: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            MainScreenTopBar(
                showRefresh = loadState != Loading,
                onRefresh = onRefresh,
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        MainScreenLayout(
            loadState = loadState,
            items = items,
            onRefresh = onRefresh,
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
                .fillMaxSize(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenTopBar(
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    showRefresh: Boolean = true,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = { Text("Items") },
        actions = {
            if (showRefresh) {
                IconButton(onClick = onRefresh) {
                    Icon(Icons.Default.Refresh, "refresh")
                }
            }
        },
    )
}

/**
 * # Content of [MainScreen]
 */
@Composable
private fun MainScreenLayout(
    items: List<UIFetchItem>,
    loadState: UILoadState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        when(loadState) {
            NotLoading -> {
                ShowItems(items = items)
                if (items.isEmpty()) {
                    item { Text("Empty List") }
                }
            }

            Loading -> {
                item {
                    CircularProgressIndicator()
                }
            }

            is Error -> {
                item {
                    Column {
                        Text(text = loadState.message)
                        TextButton(onClick = onRefresh) {
                            Text("try to refresh")
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListScope.ShowItems(
    items: List<UIFetchItem>,
) {
    items(items = items, key = { it.id }) { item ->
        ListItem(
            overlineContent = { Text("listId: ${item.listId}") },
            headlineContent = {Text(item.name) },
            supportingContent = { Text("id: ${item.id}") }
        )
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
            loadState = UILoadState.NotLoading,
            onRefresh = {},
        )
    }
}
