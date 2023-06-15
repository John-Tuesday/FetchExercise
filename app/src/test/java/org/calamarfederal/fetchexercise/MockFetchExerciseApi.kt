package org.calamarfederal.fetchexercise

import org.calamarfederal.fetchexercise.data.remote.FetchExerciseApi
import org.calamarfederal.fetchexercise.data.remote.FetchItemDto

class MockFetchExerciseApi(private val mockData: List<FetchItemDto>) : FetchExerciseApi {
    override suspend fun getItems(): List<FetchItemDto> = mockData
}
