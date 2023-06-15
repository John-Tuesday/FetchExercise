package org.calamarfederal.fetchexercise.data.remote

import retrofit2.http.GET


/**
 * Uses Retrofit to get [FetchItemDto]
 */
interface FetchExerciseApi {

    /**
     * Gets the [FetchItemDto] for the exercise
     */
    @GET("hiring.json")
    suspend fun getItems(): List<FetchItemDto>

    companion object {
        const val BASE_URL: String = "https://fetch-hiring.s3.amazonaws.com/"
    }
}
