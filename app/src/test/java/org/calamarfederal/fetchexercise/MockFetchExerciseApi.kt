package org.calamarfederal.fetchexercise

import org.calamarfederal.fetchexercise.data.remote.FetchExerciseApi
import org.calamarfederal.fetchexercise.data.remote.FetchItemDto

/**
 * ## Test Implementation of [FetchExerciseApi]
 *
 * returns [mockData] instead of a network request
 */
class MockFetchExerciseApi(private val mockData: List<FetchItemDto>) : FetchExerciseApi {
    override suspend fun getItems(): List<FetchItemDto> = mockData
}
