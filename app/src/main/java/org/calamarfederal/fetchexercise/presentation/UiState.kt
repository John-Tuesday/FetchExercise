package org.calamarfederal.fetchexercise.presentation

import androidx.compose.runtime.Stable


/**
 * Fetch Item that has been processed and ready to show
 */
@Stable
data class UIFetchItem(
    val id: Int,
    val listId: Int,
    /**
     * Should not be blank
     */
    val name: String,
)
