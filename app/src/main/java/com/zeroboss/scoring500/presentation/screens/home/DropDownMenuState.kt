package com.zeroboss.scoring500.presentation.screens.home

data class DropDownMenuState(
    var expanded: Boolean = false,
    var showAbout: Boolean = false,
    var showScoringRules: Boolean = false,
    var showStatistics: Boolean = false,
    var showBackupDialog: Boolean = false,
    var showRestoreDataDialog: Boolean = false,
    var showClearBackupsDialog: Boolean = false,
    var showClearDataDialog: Boolean = false,
)
