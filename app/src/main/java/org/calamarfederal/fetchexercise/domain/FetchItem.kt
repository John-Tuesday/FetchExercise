package org.calamarfederal.fetchexercise.domain

import androidx.compose.runtime.Stable

/**
 * Source Independent representation of the objects from the Exercise
 */
@Stable
data class FetchItem(
    val id: Int,
    val listId: Int,
    val name: String?,
)
