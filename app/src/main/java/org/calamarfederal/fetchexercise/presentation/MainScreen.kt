package org.calamarfederal.fetchexercise.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.calamarfederal.fetchexercise.presentation.UILoadState.Error
import org.calamarfederal.fetchexercise.presentation.UILoadState.Loading
import org.calamarfederal.fetchexercise.presentation.UILoadState.NotLoading
import org.calamarfederal.fetchexercise.ui.theme.FetchExerciseTheme

object MainScreenTags {
    const val TopBarBackButton = "TopBar-Back"
    const val TopBarRefreshButton = "TopBar-Refresh"
    const val LoadErrorMessage = "LoadError-Message"
    const val LoadErrorRefreshButton = "LoadError-Refresh"
    const val LoadingIndicator = "Loading-Indicator"
    const val ListIdGroupItem = "List-ID:group"
    const val ItemInGroupListItem = "Item-in-Group"
}

/**
 * # Fetch Coding Exercise Solution Screen
 *
 * [items] should already be culled and sorted. The keys of [items] should be their [UIFetchItem.listId]
 */
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    items: Map<Int, List<UIFetchItem>>,
    loadState: UILoadState,
    onRefresh: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var expandGroup: Int? by remember { mutableStateOf(null) }

    BackHandler(enabled = expandGroup != null) { expandGroup = null }
    Scaffold(
        topBar = {
            MainScreenTopBar(
                title = expandGroup?.let { "List id: $expandGroup" } ?: "Items",
                showRefresh = loadState != Loading,
                enableBack = expandGroup != null,
                onBack = { expandGroup = null },
                onRefresh = { expandGroup = null; onRefresh() },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        MainScreenLayout(
            loadState = loadState,
            items = items,
            expandGroup = expandGroup,
            onExpandGroup = { expandGroup = it },
            onRefresh = { expandGroup = null; onRefresh() },
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
    title: String,
    enableBack: Boolean,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    showRefresh: Boolean = true,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (enableBack) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag(MainScreenTags.TopBarBackButton)
                ) {
                    Icon(Icons.Default.ArrowBack, "back")
                }
            }
        },
        title = { Text(title) },
        actions = {
            if (showRefresh) {
                IconButton(
                    onClick = onRefresh,
                    modifier = Modifier.testTag(MainScreenTags.TopBarRefreshButton)
                ) {
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
    items: Map<Int, List<UIFetchItem>>,
    loadState: UILoadState,
    expandGroup: Int?,
    onExpandGroup: (Int) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 8.dp),
        modifier = modifier,
    ) {
        when (loadState) {
            NotLoading -> {
                if (expandGroup == null) {
                    items(items = items.keys.toList(), key = { it }) { listId ->
                        ListItem(
                            headlineContent = { Text("List id: $listId") },
                            supportingContent = { Text("size: ${items[listId]?.size}") },
                            modifier = Modifier.clickable { onExpandGroup(listId) }.testTag(MainScreenTags.ListIdGroupItem)
                        )
                    }
                } else {
                    items(items = items[expandGroup]!!, key = { it.id }) { item ->
                        ListItem(
                            headlineContent = { Text(item.name) },
                            supportingContent = { Text("id: ${item.id}") },
                            modifier = Modifier.testTag(MainScreenTags.ItemInGroupListItem)
                        )
                    }
                }

                if (items.isEmpty()) {
                    item { Text("Empty List") }
                }
            }

            Loading -> {
                item {
                    CircularProgressIndicator(modifier = Modifier.testTag(MainScreenTags.LoadingIndicator))
                }
            }

            is Error -> {
                item {
                    Column {
                        Text(
                            text = loadState.message,
                            modifier = Modifier.testTag(MainScreenTags.LoadErrorMessage)
                        )
                        TextButton(
                            onClick = onRefresh,
                            modifier = Modifier.testTag(MainScreenTags.LoadErrorRefreshButton)
                        ) {
                            Text("try to refresh")
                        }
                    }
                }
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
        val items = (0..20).associateWith { listOf(UIFetchItem(id = it, listId = it, name = "$it")) }
        MainScreen(
            items = items,
            loadState = NotLoading,
            onRefresh = {},
        )
    }
}
