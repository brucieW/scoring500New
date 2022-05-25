package com.zeroboss.scoring500.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.zeroboss.scoring500.ui.common.menu.DropdownMenuExt
import com.zeroboss.scoring500.ui.common.menu.DropdownMenuItemExt
import com.zeroboss.scoring500.R
import com.zeroboss.scoring500.presentation.dialogs.*
import com.zeroboss.scoring500.presentation.dialogs.scoring_rules.ScoringRulesDialog
import com.zeroboss.scoring500.presentation.screens.destinations.ManageTeamsScreenDestination
import com.zeroboss.scoring500.presentation.screens.destinations.StatisticsScreenDestination
import com.zeroboss.scoring500.ui.theme.menuColors
import org.koin.androidx.compose.get
import org.koin.androidx.compose.viewModel

@Composable
fun DropDownMenu(
    navigator: DestinationsNavigator
) {
    val homeViewModel by viewModel<HomeViewModel>()
    var expanded by remember { mutableStateOf(false) }
    val menuState by homeViewModel.menuState

    Column {
        Image(
            painterResource(R.drawable.menu), "",
            modifier = Modifier
                .size(48.dp)
                .clickable { expanded = true }
        )

        MaterialTheme(colors = menuColors) {
            DropdownMenuExt(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.padding(end = 10.dp)
            ) {
                DropdownMenuItemExt(
                    onClick = {
                        navigator.navigate(ManageTeamsScreenDestination)
                    }
                ) {
                    Text(stringResource(R.string.manage_teams))
                }

                DropdownMenuItemExt(
                    onClick = {
                        homeViewModel.onMenuEvent(MenuEvent.ShowScoringRules(true))
                        expanded = false
                    }
                ) {
                    Text(stringResource(R.string.preferences))
                }

                DropdownMenuItemExt(
                    onClick = {
                        navigator.navigate(StatisticsScreenDestination)
                    }
                ) {
                    Text(stringResource(R.string.statistics))
                }

                Divider()

                DropdownMenuItemExt(
                    onClick = {
                        homeViewModel.onMenuEvent(MenuEvent.ShowBackupDialog(true))
                        expanded = false
                    }
                ) {
                    Text(stringResource(R.string.backup_database))
                }

                DropdownMenuItemExt(
                    onClick = {
                        homeViewModel.onMenuEvent(MenuEvent.ShowRestoreDataDialog(true))
                        expanded = false
                    }
                ) {
                    Text(stringResource(R.string.restore_database))
                }

                DropdownMenuItemExt(
                    onClick = {
                        homeViewModel.onMenuEvent(MenuEvent.ShowClearBackupsDialog(true))
                        expanded = false
                    }
                ) {
                    Text(stringResource(R.string.clear_backups))
                }

                Divider()

                DropdownMenuItemExt(
                    onClick = {
                        homeViewModel.onMenuEvent(MenuEvent.ShowClearDataDialog(true))
                        expanded = false
                    }
                ) {
                    Text(stringResource(R.string.clear_database))
                }

                Divider()

                DropdownMenuItemExt(
                    onClick = {
                        homeViewModel.onMenuEvent(MenuEvent.ShowAboutDialog(true))
                        expanded = false
                    }
                ) {
                    Text(stringResource(R.string.about))
                }
            }
        }

        ScoringRulesDialog(
            showDialog = menuState.showScoringRules,
            homeViewModel
        )

        BackupDataDialog(
            showBackupDialog = menuState.showBackupDialog,
            homeViewModel,
            get()
        )

        RestoreDataDialog(
            showRestoreDialog = menuState.showRestoreDataDialog,
            homeViewModel,
            get()
        )

        ClearBackupsDialog(
            showClearBackupsDialog = menuState.showClearBackupsDialog,
            homeViewModel
        )

        ClearDataDialog(
            showClearData = menuState.showClearDataDialog,
            homeViewModel,
            get()
        )

        if (menuState.showAbout) {
            AboutDialog(homeViewModel)
        }
    }
}



