package com.zeroboss.scoring500.presentation.screens.home

sealed class MenuEvent {
    data class MenuOpen(val open: Boolean): MenuEvent()
    data class ShowScoringRules(val show: Boolean): MenuEvent()
    data class ShowBackupDialog(val show: Boolean): MenuEvent()
    data class ShowRestoreDataDialog(val show: Boolean): MenuEvent()
    data class ShowClearBackupsDialog(val show: Boolean): MenuEvent()
    data class ShowClearDataDialog(val show: Boolean): MenuEvent()
    data class ShowAboutDialog(val show: Boolean): MenuEvent()
}
