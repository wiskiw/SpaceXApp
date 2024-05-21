package dev.wiskiw.spacexapp.app

import com.apollographql.apollo3.ApolloClient
import dev.wiskiw.spacexapp.app.logger.AndroidLogger
import dev.wiskiw.spacexapp.app.logger.AppLogger
import dev.wiskiw.spacexapp.data.datasource.SpaceXLaunchesApolloDataSource
import dev.wiskiw.spacexapp.data.datasource.SpaceXLaunchesRemoteDataSource
import dev.wiskiw.spacexapp.data.repository.SpaceXLaunchesRepository
import dev.wiskiw.spacexapp.domain.repository.LaunchesRepository
import dev.wiskiw.spacexapp.domain.usecase.LaunchDetailsUseCase
import dev.wiskiw.spacexapp.domain.usecase.LaunchListUseCase
import dev.wiskiw.spacexapp.presentation.screen.launchdetails.LaunchDetailsViewModel
import dev.wiskiw.spacexapp.presentation.screen.launches.LaunchesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LaunchesViewModel(get(), get()) }
    viewModel { LaunchDetailsViewModel(get(), get(), get()) }
}

val appModule = module {
    single<AppLogger> { AndroidLogger() }

    single {
        ApolloClient.Builder()
            .serverUrl("https://apollo-fullstack-tutorial.herokuapp.com/graphql")
            .build()
    }

    // creating DataSources
    single<SpaceXLaunchesRemoteDataSource> { SpaceXLaunchesApolloDataSource(get(), get()) }

    // creating Repositories
    single<LaunchesRepository> { SpaceXLaunchesRepository(get()) }

    // creating UseCases
    single<LaunchListUseCase> { LaunchListUseCase(get()) }
    single<LaunchDetailsUseCase> { LaunchDetailsUseCase(get()) }
}
