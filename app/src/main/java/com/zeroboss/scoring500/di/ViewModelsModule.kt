package com.zeroboss.scoring500.di

import com.zeroboss.scoring500.presentation.dialogs.scoring_rules.ScoringRulesViewModel
import com.zeroboss.scoring500.presentation.screens.game_screen.GameViewModel
import com.zeroboss.scoring500.presentation.screens.home.HomeViewModel
import com.zeroboss.scoring500.presentation.screens.login.LoginViewModel
import com.zeroboss.scoring500.presentation.screens.new_hand.NewHandViewModel
import com.zeroboss.scoring500.presentation.screens.statistics.StatisticsViewModel
import com.zeroboss.scoring500app.presentation.screens.new_match.NewMatchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginViewModelModule = module {
    viewModel {
        LoginViewModel()
    }
}
val homeViewModelModule = module {
    viewModel {
        HomeViewModel(get())
    }
}

val gameViewModelModule = module {
    viewModel {
        GameViewModel()
    }
}

val newMatchViewModelModule = module {
    viewModel {
        NewMatchViewModel(get())
    }
}

val newHandViewModelModule = module {
    viewModel {
        NewHandViewModel(get())
    }
}

val statisticsViewModelModule = module {
    viewModel {
        StatisticsViewModel(get())
    }
}

val scoringRulesViewModelModule = module {
    viewModel {
        ScoringRulesViewModel(get())
    }
}
