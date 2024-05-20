package dev.wiskiw.spacexapp.app

import com.apollographql.apollo3.ApolloClient
import dev.wiskiw.spacexapp.app.logger.AndroidLogger
import dev.wiskiw.spacexapp.app.logger.AppLogger
import dev.wiskiw.spacexapp.data.datasource.SpaceXLaunchesRemoteDataSource
import dev.wiskiw.spacexapp.domain.repository.LaunchesRepository
import dev.wiskiw.spacexapp.domain.usecase.LaunchesUseCase
import dev.wiskiw.spacexapp.presentation.screen.launches.LaunchesViewModel
import dev.wiskiw.spacexapp.data.repository.SpaceXLaunchesRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LaunchesViewModel(get()) }
}

val appModule = module {
    single<AppLogger> { AndroidLogger() }

    single {
        ApolloClient.Builder()
            .serverUrl("https://apollo-fullstack-tutorial.herokuapp.com/graphql")
            .build()
    }

    // creating DataSources
    single<SpaceXLaunchesRemoteDataSource> { SpaceXLaunchesRemoteDataSource(get(), get()) }

    // creating Repositories
    single<LaunchesRepository> { SpaceXLaunchesRepository(get()) }

    // creating UseCases
    single<LaunchesUseCase> { LaunchesUseCase(get()) }
}
