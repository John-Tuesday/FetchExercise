package org.calamarfederal.fetchexercise

import kotlinx.coroutines.delay
import org.calamarfederal.fetchexercise.data.remote.FetchExerciseApi
import org.calamarfederal.fetchexercise.data.remote.FetchItemDto
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

/**
 * # Test Implementation of [FetchExerciseApi]
 * ## randomly generates a new set of items every call to [getItems]
 */
class MockFetchExerciseApi : FetchExerciseApi {
    override suspend fun getItems(): List<FetchItemDto> {
        return generateListOfFetchItemDto(100)
    }
}

/**
 * Generates a random string of letters [a-Z] with [size]
 */
private fun randomString(size: Int): String = buildString(size) {
    for (c in 0 until size) {
        val code = Random.nextInt(0, 26 * 2).let { (if (it >= 26) 'A' else 'a') + it % 26 }
        append(code)
    }
}

/**
 * Generates [count] number of [FetchItemDto] with random values into a list in a random order
 *
 * keeps [FetchItemDto.id] unique
 */
private fun generateListOfFetchItemDto(
    count: Int,
    listIdRange: IntRange = 0..8,
    nameLengthRange: IntRange = 5..15,
    nameNullChance: Double = 0.25,
): List<FetchItemDto> {
    return buildList(count) {
        for (n in 0 until count) {
            add(
                FetchItemDto(
                    id = n,
                    listId = listIdRange.random(),
                    name = if (Random.nextDouble() <= nameNullChance) null else randomString(nameLengthRange.random())
                )
            )
        }
    }
}
