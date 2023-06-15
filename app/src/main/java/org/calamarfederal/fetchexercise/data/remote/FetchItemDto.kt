package org.calamarfederal.fetchexercise.data.remote

import com.squareup.moshi.Json
import org.calamarfederal.fetchexercise.domain.FetchItem

/**
 * Models the Json objects returned by server
 */
data class FetchItemDto (
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "listId")
    val listId: Int,
    @field:Json(name = "name")
    val name: String?,
)

/**
 * Convert to the source independent representation
 */
fun FetchItemDto.toFetchItem() = FetchItem(
    id = id,
    listId = listId,
    name = name,
)
