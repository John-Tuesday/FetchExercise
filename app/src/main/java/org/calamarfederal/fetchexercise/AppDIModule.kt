package org.calamarfederal.fetchexercise

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import org.calamarfederal.fetchexercise.data.remote.FetchExerciseApi
import org.calamarfederal.fetchexercise.domain.GetProcessedItems
import org.calamarfederal.fetchexercise.domain.GetProcessedItemsImpl
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class FetchExerciseViewModelBindings {
    @Binds
    @ViewModelScoped
    abstract fun bindGetProcessedItems(impl: GetProcessedItemsImpl): GetProcessedItems
}

@Module
@InstallIn(SingletonComponent::class)
object FetchExerciseProvider {

    @Provides
    @Singleton
    fun provideFetchExerciseApi(): FetchExerciseApi = Retrofit.Builder()
        .baseUrl(FetchExerciseApi.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create()
}
