package org.calamarfederal.fetchexercise.domain

import org.calamarfederal.fetchexercise.data.remote.FetchExerciseApi
import org.calamarfederal.fetchexercise.data.remote.FetchItemDto
import org.calamarfederal.fetchexercise.data.remote.toFetchItem
import org.calamarfederal.fetchexercise.presentation.UIFetchItem
import javax.inject.Inject


/**
 * Convert to its valid UI representation or `null` if impossible
 */
private fun FetchItem.toUIOrNull(): UIFetchItem? {
    return UIFetchItem(
        id = id,
        listId = listId,
        name = name?.ifBlank { return null } ?: return null
    )
}

fun interface GetProcessedItems {
    suspend operator fun invoke(): Map<Int, List<UIFetchItem>>
}

class GetProcessedItemsImpl @Inject constructor(
    private val fetchExerciseApi: FetchExerciseApi,
) : GetProcessedItems {
    /**
     * Convert, cull, and sort according to Exercise
     *
     * Convert to [UIFetchItem], dropping any item with a `null` or blank [name][UIFetchItem.name]
     * Sort by [listId][UIFetchItem.listId] then by [name][UIFetchItem.name]
     */
    private fun processItemsForUI(inputList: List<FetchItem>): Map<Int, List<UIFetchItem>> {
        println("processing...")
        return inputList
            .asSequence()
            .mapNotNull { it.toUIOrNull() }
            .sortedWith { a, b ->
                when (val diff = a.listId - b.listId) {
                    0 -> a.name.compareTo(b.name)
                    else -> diff
                }
            }
            .groupBy { it.listId }
    }

    override suspend fun invoke(): Map<Int, List<UIFetchItem>> {
        return processItemsForUI(fetchExerciseApi.getItems().map { it.toFetchItem() })
    }
}
