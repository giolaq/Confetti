@file:OptIn(ExperimentalHorologistComposeLayoutApi::class)

package dev.johnoreilly.confetti.wear.sessiondetails.navigation

import android.net.Uri
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import com.google.android.horologist.compose.navscaffold.composable
import androidx.navigation.navArgument
import com.google.android.horologist.compose.navscaffold.ExperimentalHorologistComposeLayoutApi
import dev.johnoreilly.confetti.wear.navigation.ConfettiNavigationDestination
import dev.johnoreilly.confetti.wear.sessiondetails.SessionDetailsRoute


object SessionDetailsDestination : ConfettiNavigationDestination {
    const val sessionIdArg = "sessionId"
    override val route = "session_details_route/{$sessionIdArg}"
    override val destination = "person_details_destination"

    fun createNavigationRoute(sessionId: String): String {
        val encodedId = Uri.encode(sessionId)
        return "session_details_route/$encodedId"
    }

    fun fromNavArgs(entry: NavBackStackEntry): String {
        val encodedId = entry.arguments?.getString(sessionIdArg)!!
        return Uri.decode(encodedId)
    }
}


fun NavGraphBuilder.sessionDetailsGraph(onBackClick: () -> Unit) {
    composable(
        route = SessionDetailsDestination.route,
        arguments = listOf(
            navArgument(SessionDetailsDestination.sessionIdArg) { type = NavType.StringType }
        )
    ) {
        SessionDetailsRoute(onBackClick)
    }
}
