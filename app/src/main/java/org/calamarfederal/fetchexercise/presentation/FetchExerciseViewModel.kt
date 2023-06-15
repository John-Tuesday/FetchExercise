package org.calamarfederal.fetchexercise.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.calamarfederal.fetchexercise.domain.GetProcessedItems
import javax.inject.Inject

/**
 * # View model to serve the results of Fetch Exercise
 */
@HiltViewModel
class FetchExerciseViewModel @Inject constructor(
    private val _getProcessedItems: GetProcessedItems,
) : ViewModel() {
    private val _fetchItemsState: MutableStateFlow<List<UIFetchItem>> = MutableStateFlow(listOf())

    /**
     * Read-Only [StateFlow] providing the processed [UIFetchItem]
     */
    val fetchItemsState: StateFlow<List<UIFetchItem>> = _fetchItemsState

    private val networkScope get() =
        viewModelScope + SupervisorJob() + CoroutineExceptionHandler { _, err -> err.printStackTrace() }

    /**
     * Load and set [fetchItemsState]
     */
    fun refreshItems() {
        networkScope.launch {
            println("refreshItems()")
            _fetchItemsState.value = _getProcessedItems()
        }
    }

    init {
        refreshItems()
    }
}
