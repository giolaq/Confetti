@file:OptIn(ExperimentalHorologistComposeLayoutApi::class)

package dev.johnoreilly.confetti.wear.home

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.horologist.compose.layout.ScalingLazyColumnState
import com.google.android.horologist.compose.navscaffold.ExperimentalHorologistComposeLayoutApi
import dev.johnoreilly.confetti.navigation.ConferenceDayKey
import dev.johnoreilly.confetti.navigation.SessionDetailsKey
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeRoute(
    navigateToSession: (SessionDetailsKey) -> Unit,
    navigateToDay: (ConferenceDayKey) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToConferenceList: () -> Unit,
    columnState: ScalingLazyColumnState,
    viewModel: HomeViewModel = getViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    HomeListView(
        uiState = uiState,
        sessionSelected = {
            if (uiState is HomeUiState.Success) {
                navigateToSession(SessionDetailsKey(uiState.conference, it))
            }
        },
        daySelected = {
            if (uiState is HomeUiState.Success) {
                navigateToDay(ConferenceDayKey(uiState.conference, it))
            }
        },
        onSettingsClick = navigateToSettings,
        onRefreshClick = {
            viewModel.refresh()
        },
        columnState = columnState,
        navigateToConferenceList = navigateToConferenceList
    )
}
