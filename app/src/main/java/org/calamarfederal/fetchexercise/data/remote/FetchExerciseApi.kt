package org.calamarfederal.fetchexercise.data.remote

import retrofit2.http.GET


interface FetchExerciseApi {

    @GET("hiring.json")
    suspend fun getItems(): List<FetchItemDto>

    companion object {
        const val BASE_URL: String = "https://fetch-hiring.s3.amazonaws.com/"
    }
}
