package org.calamarfederal.fetchexercise.data.remote

import com.squareup.moshi.Json
import org.calamarfederal.fetchexercise.domain.FetchItem

data class FetchItemDto (
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "listId")
    val listId: Int,
    @field:Json(name = "name")
    val name: String?,
)

fun FetchItemDto.toFetchItem() = FetchItem(
    id = id,
    listId = listId,
    name = name,
)
