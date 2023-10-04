package dev.johnoreilly.confetti

import android.credentials.CredentialManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.arkivanov.decompose.defaultComponentContext
import dev.johnoreilly.confetti.decompose.DefaultAppComponent
import dev.johnoreilly.confetti.theme.ConfettiTheme
import dev.johnoreilly.confetti.ui.ConfettiApp
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        val appComponent =
            DefaultAppComponent(
                componentContext = defaultComponentContext(),
                onSignOut = {
                },
                onSignIn = {
                }
            )

        setContent {
            ConfettiApp(
                component = appComponent,
            )
        }
    }
}
