package dev.wiskiw.spacexapp.app

import dev.wiskiw.spacexapp.presentation.screen.launches.LaunchesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LaunchesViewModel() }
}

val appModule = module {

}
