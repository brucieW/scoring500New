package com.zeroboss.scoring500.presentation.screens.tutorial

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import com.zeroboss.scoring500.R
import com.zeroboss.scoring500.data.common.EXPAND_ANIMATION_DURATION
import com.zeroboss.scoring500.ui.theme.normalText

@Composable
fun ExpandableTutorialItem(
    item: TutorialItem,
    onArrowClicked: (TutorialItem) -> Unit,
    expanded: Boolean
) {
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }

    val transition = updateTransition(transitionState, label = "transition")

    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "rotationDegreeTransition") {
        if (expanded) 0F else 180f
    }

    Box(
        modifier = Modifier
            .animateContentSize()
            .fillMaxWidth()
            .clickable { onArrowClicked(item) }
    ) {
        Column {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onArrowClicked(item) },
                    content = {
                        Icon(
                            painter = painterResource(R.drawable.ic_expand_less_24),
                            contentDescription = "Expandable Arrow",
                            modifier = Modifier.rotate(arrowRotationDegree),
                        )
                    }
                )

                Text(
                    text = item.title,
                    style = normalText
                )
            }

            if (expanded) {
                when (item) {
                    TutorialItem.Introduction -> IntroductionDetail()
                    TutorialItem.MainMenu -> MainMenuDetail()
                    TutorialItem.HomeScreen -> HomeScreenDetail()
                    TutorialItem.None -> {}
                    TutorialItem.NewMatchScreen -> {}
                    TutorialItem.NewHandScreen -> {}
                    TutorialItem.GameScreen -> {}
                    TutorialItem.ManageTeamsScreen -> {}
                    TutorialItem.PreferencesDialog -> {}
                    TutorialItem.StatisticsScreen -> {}
                    TutorialItem.BackupDatabaseDialog -> {}
                    TutorialItem.RestoreDatabaseDialog -> {}
                    TutorialItem.ClearBackupsDialog -> {}
                }
            }
        }
    }
}
