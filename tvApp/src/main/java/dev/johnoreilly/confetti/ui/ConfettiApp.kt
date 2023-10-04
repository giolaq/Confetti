package dev.johnoreilly.confetti.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import dev.johnoreilly.confetti.decompose.AppComponent
import dev.johnoreilly.confetti.decompose.AppComponent.Child

@Composable
fun ConfettiApp(
    component: AppComponent) {
    Children(
        stack = component.stack,
    ) {
        when (val child = it.instance) {
//            is Child.Loading -> LoadingView()
//            //is Child.Conferences -> ConferencesRoute(child.component)
//            //is Child.Conference -> ConferenceRoute(child.component, windowSizeClass)
//            else -> { LoadingView()
//            }
        }
    }
}
