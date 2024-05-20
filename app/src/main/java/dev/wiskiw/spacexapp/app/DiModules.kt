package dev.wiskiw.spacexapp.app

import com.apollographql.apollo3.ApolloClient
import dev.wiskiw.spacexapp.app.logger.AndroidLogger
import dev.wiskiw.spacexapp.app.logger.AppLogger
import dev.wiskiw.spacexapp.domain.repository.LaunchesRepository
import dev.wiskiw.spacexapp.presentation.screen.launches.LaunchesViewModel
import dev.wiskiw.spacexapp.repository.SpaceXLaunchesRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LaunchesViewModel() }
}

val appModule = module {
    single<AppLogger> { AndroidLogger() }

    single {
        ApolloClient.Builder()
            .serverUrl("https://apollo-fullstack-tutorial.herokuapp.com/graphql")
            .build()
    }

    single<LaunchesRepository> { SpaceXLaunchesRepository(get(), get()) }
}
