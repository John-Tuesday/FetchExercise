package org.calamarfederal.fetchexercise

import kotlinx.coroutines.runBlocking
import org.calamarfederal.fetchexercise.data.remote.FetchExerciseApi
import org.calamarfederal.fetchexercise.data.remote.FetchItemDto
import org.calamarfederal.fetchexercise.domain.GetProcessedItems
import org.calamarfederal.fetchexercise.domain.GetProcessedItemsImpl
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class GetProcessedItemsTest {
    private lateinit var getProcessedItems: GetProcessedItems
    private lateinit var mockFetchExerciseApi: FetchExerciseApi

    private fun generateName(length: Int = 5): String = buildString(length) {
        for (i in 0 until length) {
            if (Random.nextBoolean())
                append(('a'..'z').random())
            else
                append(('A'..'Z').random())
        }
    }

    @Before
    fun setUp() {
        val nameLengths = (5..10)
        val groups = 4
        mockFetchExerciseApi = MockFetchExerciseApi(
            (0..100).map {
                FetchItemDto(
                    id = it,
                    listId = Random.nextInt(groups),
                    name = if (Random.nextBoolean()) null else generateName(nameLengths.random()),
                )
            }.shuffled()
        )

        getProcessedItems = GetProcessedItemsImpl(mockFetchExerciseApi)
    }

    @Test
    fun `Remove items with a blank or null name - correct`() {
        val uiItems = runBlocking { getProcessedItems() }

        for (item in uiItems.values.flatten())
            assert(item.name.isNotBlank())
    }

    @Test
    fun `Items grouped by their listId - correct`() {
        val uiItems = runBlocking { getProcessedItems() }

        for ((key, items) in uiItems)
            assert(items.all { it.listId == key })
    }

    @Test
    fun `Items are sorted by listId then by name - correct`() {
        val uiItems = runBlocking { getProcessedItems() }

        for (items in uiItems.values) {
            for (nextIndex in 1 until items.size) {
                val current = items[nextIndex - 1]
                val next = items[nextIndex]
                assert(current.listId <= next.listId && current.name <= next.name)
            }
        }
    }

}
