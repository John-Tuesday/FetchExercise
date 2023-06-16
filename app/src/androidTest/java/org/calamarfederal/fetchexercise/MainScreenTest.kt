package org.calamarfederal.fetchexercise

import androidx.activity.ComponentActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import org.calamarfederal.fetchexercise.domain.GetProcessedItems
import org.calamarfederal.fetchexercise.presentation.*
import org.calamarfederal.fetchexercise.ui.theme.FetchExerciseTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

/**
 * ## Main Screen test
 *
 * test back, refresh, and list id buttons. test proper grouping. test presence of all items
 */
@HiltAndroidTest
class MainScreenTest {

    /**
     * rule support di
     *
     * [Rule.order] needs to be before [composeRule]
     */
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    /**
     * rules for compose
     *
     * [Rule.order] needs to be after [hiltRule] and use [createAndroidComposeRule] so Hilt finds the right entry point
     */
    @get:Rule(order = 1)
    val composeRule = createComposeRule()

    @Inject
    lateinit var getProcessedItems: GetProcessedItems
    private val itemsState = MutableStateFlow(mapOf<Int, List<UIFetchItem>>())
    private val loadStateFlow = MutableStateFlow<UILoadState>(UILoadState.NotLoading)

    @Before
    fun setUp() {
        hiltRule.inject()

        itemsState.value = runBlocking { getProcessedItems() }
        loadStateFlow.value = UILoadState.NotLoading

        composeRule.setContent {
            val items by itemsState.collectAsStateWithLifecycle()
            val loadState by loadStateFlow.collectAsStateWithLifecycle()

            MainScreen(
                items = items,
                loadState = loadState,
                onRefresh = {
                    loadStateFlow.value = UILoadState.Loading
                    itemsState.value = runBlocking { getProcessedItems() }
                    loadStateFlow.value = UILoadState.NotLoading
                },
            )
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
