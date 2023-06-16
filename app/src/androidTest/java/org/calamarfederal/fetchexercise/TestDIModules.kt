package org.calamarfederal.fetchexercise

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.calamarfederal.fetchexercise.data.remote.FetchExerciseApi
import org.calamarfederal.fetchexercise.domain.GetProcessedItems
import org.calamarfederal.fetchexercise.domain.GetProcessedItemsImpl
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

/**
 * # Application level DI module
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [FetchExerciseProvider::class],
)
object TestFetchExerciseProvider {

    @Provides
    fun provideMockFetchExercise(): FetchExerciseApi = MockFetchExerciseApi()
}

/**
 * # View Model level DI Module
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [FetchExerciseViewModelBindings::class]
)
abstract class TestFetchExerciseViewModelBindings {
    @Binds
    abstract fun bindGetProcessedItems(impl: GetProcessedItemsImpl): GetProcessedItems
}
