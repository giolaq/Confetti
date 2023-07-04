@file:OptIn(ExperimentalTvMaterial3Api::class)

package dev.johnoreilly.confetti.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.Text
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import dev.johnoreilly.confetti.R
import dev.johnoreilly.confetti.account.AccountIcon
import dev.johnoreilly.confetti.account.AccountInfo
import dev.johnoreilly.confetti.account.WearUiState
import dev.johnoreilly.confetti.bookmarks.BookmarksRoute
import dev.johnoreilly.confetti.decompose.HomeComponent
import dev.johnoreilly.confetti.search.SearchRoute
import dev.johnoreilly.confetti.sessions.SessionsRoute
import dev.johnoreilly.confetti.speakers.SpeakersRoute

@Composable
fun HomeRoute(
    component: HomeComponent,
    windowSizeClass: WindowSizeClass,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    NavigationDrawer(
        component = component,
        content = {
            Scaffold(
                modifier = Modifier.imePadding(),
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxHeight(),
                ) {
                    Children(
                        component = component,
                        windowSizeClass = windowSizeClass,
                        snackbarHostState = snackbarHostState,
                        modifier = Modifier
                            .weight(1f)
                            .then(Modifier.consumeWindowInsets(NavigationBarDefaults.windowInsets)),
                    )
                }
            }
        }
    )
}

@Composable
private fun Children(
    component: HomeComponent,
    windowSizeClass: WindowSizeClass,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val wearUiState = WearUiState()

    val topBarActions: @Composable RowScope.() -> Unit = {
        AccountIcon(
            onSwitchConference = component::onSwitchConferenceClicked,
            onSignIn = component::onSignInClicked,
            onSignOut = component::onSignOutClicked,
            onShowSettings = component::onShowSettingsClicked,
            info = component.user?.let { user ->
                AccountInfo(photoUrl = user.photoUrl)
            },
            installOnWear = {}, // FIXME: handle
            wearSettingsUiState = wearUiState,
        )
    }

    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is HomeComponent.Child.Sessions ->
                SessionsRoute(
                    component = child.component,
                    windowSizeClass = windowSizeClass,
                    topBarActions = topBarActions,
                    snackbarHostState = snackbarHostState,
                )

            is HomeComponent.Child.Speakers ->
                SpeakersRoute(
                    component = child.component,
                    windowSizeClass = windowSizeClass,
                    topBarActions = topBarActions,
                )

            is HomeComponent.Child.Bookmarks ->
                BookmarksRoute(
                    component = child.component,
                    windowSizeClass = windowSizeClass,
                    topBarActions = topBarActions,
                )

            is HomeComponent.Child.Search ->
                SearchRoute(
                    component = child.component,
                    windowSizeClass = windowSizeClass,
                    topBarActions = topBarActions,
                )
        }
    }
}

@Composable
private fun NavigationDrawer(component: HomeComponent, content: @Composable () -> Unit) {
    NavigationDrawer(
        drawerContent = {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxHeight()
            ) {
                NavigationButtons(
                    drawerValue = it,
                    component = component
                ) { drawerValue, isSelected, selectedIcon, unselectedIcon, textId, onClick ->
                    Row(
                        modifier = Modifier
                            .focusable()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isSelected) selectedIcon else unselectedIcon,
                            contentDescription = stringResource(textId),
                        )
                        AnimatedVisibility(visible = drawerValue == DrawerValue.Open) {
                            Text(
                                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                                text = stringResource(textId),
                                softWrap = false,
                                style = MaterialTheme.typography.bodySmall
                                    .copy(
                                        color = MaterialTheme.colorScheme.onPrimary
                                    ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    ) {
        content()
    }
}

@Composable
private fun <T> T.NavigationButtons(
    component: HomeComponent,
    drawerValue: DrawerValue,
    content: @Composable T.(
        drawerValue: DrawerValue,
        isSelected: Boolean,
        selectedIcon: ImageVector,
        unselectedIcon: ImageVector,
        textId: Int,
        onClick: () -> Unit,
    ) -> Unit,
) {
    val stack by component.stack.subscribeAsState()
    val activeChild = stack.active.instance

    content(
        drawerValue = drawerValue,
        isSelected = activeChild is HomeComponent.Child.Sessions,
        selectedIcon = Icons.Filled.CalendarMonth,
        unselectedIcon = Icons.Outlined.CalendarMonth,
        textId = R.string.schedule,
        onClick = component::onSessionsTabClicked,
    )

    content(
        drawerValue = drawerValue,
        isSelected = activeChild is HomeComponent.Child.Speakers,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        textId = R.string.speakers,
        onClick = component::onSpeakersTabClicked,
    )

    content(
        drawerValue = drawerValue,
        isSelected = activeChild is HomeComponent.Child.Bookmarks,
        selectedIcon = Icons.Filled.Bookmarks,
        unselectedIcon = Icons.Outlined.Bookmarks,
        textId = R.string.bookmarks,
        onClick = component::onBookmarksTabClicked,
    )

    content(
        drawerValue = drawerValue,
        isSelected = activeChild is HomeComponent.Child.Search,
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
        textId = R.string.search,
        onClick = component::onSearchTabClicked,
    )
}
