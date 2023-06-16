package org.calamarfederal.fetchexercise

import androidx.compose.runtime.getValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import org.calamarfederal.fetchexercise.domain.GetProcessedItems
import org.calamarfederal.fetchexercise.domain.GetProcessedItemsImpl
import org.calamarfederal.fetchexercise.presentation.MainScreen
import org.calamarfederal.fetchexercise.presentation.MainScreenTags
import org.calamarfederal.fetchexercise.presentation.UIFetchItem
import org.calamarfederal.fetchexercise.presentation.UILoadState
import org.calamarfederal.fetchexercise.ui.theme.FetchExerciseTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests for [MainScreen]
 *
 * tests back button, refresh button, list id button, loading indicator ...
 */
class MainScreenUnitTests {
    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var getProcessedItems: GetProcessedItems
    private val itemsState = MutableStateFlow(mapOf<Int, List<UIFetchItem>>())
    private val loadStateFlow = MutableStateFlow<UILoadState>(UILoadState.NotLoading)

    @Before
    fun setUp() {
        getProcessedItems = GetProcessedItemsImpl(MockFetchExerciseApi())
        itemsState.value = runBlocking { getProcessedItems() }
        loadStateFlow.value = UILoadState.NotLoading

        composeRule.setContent {
            FetchExerciseTheme {
                val items by itemsState.collectAsStateWithLifecycle()
                val loadState by loadStateFlow.collectAsStateWithLifecycle()

                MainScreen(
                    items = items,
                    loadState = loadState,
                    onRefresh = {
                        loadStateFlow.update { UILoadState.Loading }
                        itemsState.value = runBlocking { getProcessedItems() }
                        loadStateFlow.update { UILoadState.NotLoading }
                    },
                )
            }
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `Clicking List Id Group and Clicking Back Arrow properly navigate`() {
        composeRule.onNodeWithTag(MainScreenTags.TopBarBackButton).assertDoesNotExist()
        composeRule.onAllNodesWithTag(MainScreenTags.ListIdGroupItem).onFirst().performClick()
        composeRule.waitUntilAtLeastOneExists(hasTestTag(MainScreenTags.ItemInGroupListItem))
        composeRule.onNodeWithTag(MainScreenTags.TopBarBackButton).performClick()
    }

    @Test
    fun `All listId groups are present`() {
        val keys = itemsState.value.keys
        composeRule.onAllNodesWithTag(MainScreenTags.ListIdGroupItem).assertCountEquals(keys.size)
        for (k in keys) {
            composeRule.onAllNodes(
                hasTestTag(MainScreenTags.ListIdGroupItem) and hasAnyDescendant(
                    hasText(
                        text = "List id: $k",
                        substring = true,
                        ignoreCase = true,
                    )
                )
            ).assertCountEquals(1)
        }
    }

    @Test
    fun `Clicking List Id Group leads to proper items`() {
        val items = itemsState.value

        for (group in items.keys) {
            composeRule.onNode(
                hasTestTag(MainScreenTags.ListIdGroupItem) and hasAnyDescendant(
                    hasText(
                        text = "List id: $group",
                        substring = true,
                        ignoreCase = true,
                    )
                )
            ).assertExists().performClick()

            for (item in items[group]!!) {
                composeRule.onNode(hasScrollToNodeAction() and hasAnyDescendant(hasTestTag(MainScreenTags.ItemInGroupListItem)))
                    .performScrollToNode(
                        hasTestTag(MainScreenTags.ItemInGroupListItem) and hasAnyDescendant(
                            hasText(
                                text = item.name,
                                substring = true,
                                ignoreCase = true,
                            )
                        )
                    )
            }
            composeRule.onNodeWithTag(MainScreenTags.TopBarBackButton).performClick()
        }
    }

    @Test
    fun `Clicking refresh changes content`() {
        val oldItems = itemsState.value
        composeRule.onNodeWithTag(MainScreenTags.TopBarRefreshButton).performClick()
        val newItems = itemsState.value

        assert(oldItems != newItems)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `Click refresh navigates back to group view`() {
        composeRule.onAllNodesWithTag(MainScreenTags.ListIdGroupItem).onFirst().performClick()
        composeRule.onAllNodesWithTag(MainScreenTags.ListIdGroupItem).assertCountEquals(0)
        composeRule.onNodeWithTag(MainScreenTags.TopBarRefreshButton).performClick()
        composeRule.waitUntilAtLeastOneExists(hasTestTag(MainScreenTags.ListIdGroupItem))
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `Error message is shown with working refresh on error`() {
        val message: String = "test-error"
        loadStateFlow.value = UILoadState.Error(message)
        composeRule.onNodeWithText(message, substring = true).assertIsDisplayed()
        composeRule.onNodeWithTag(MainScreenTags.LoadErrorRefreshButton).assertIsDisplayed().performClick()
        composeRule.waitUntilAtLeastOneExists(hasTestTag(MainScreenTags.ListIdGroupItem))
    }

    @Test
    fun `On Loading indicator is shown and refresh and back are hidden`() {
        loadStateFlow.value = UILoadState.Loading
        composeRule.onNodeWithTag(MainScreenTags.TopBarRefreshButton).assertDoesNotExist()
        composeRule.onNodeWithTag(MainScreenTags.TopBarBackButton).assertDoesNotExist()
        composeRule.onNodeWithTag(MainScreenTags.LoadingIndicator).assertIsDisplayed()
        loadStateFlow.value = UILoadState.NotLoading
        composeRule.onNodeWithTag(MainScreenTags.LoadingIndicator).assertDoesNotExist()
    }
}
