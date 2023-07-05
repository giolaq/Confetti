@file:OptIn(ExperimentalHorologistApi::class)
@file:Suppress("RemoveExplicitTypeArguments")

package dev.johnoreilly.confetti.di

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import dev.johnoreilly.confetti.ConfettiRepository
import dev.johnoreilly.confetti.auth.Authentication
import dev.johnoreilly.confetti.auth.DefaultAuthentication
import dev.johnoreilly.confetti.decompose.ConferenceRefresh
import dev.johnoreilly.confetti.decompose.SettingsComponent
import dev.johnoreilly.confetti.settings.DefaultSettingsComponent
import dev.johnoreilly.confetti.work.WorkManagerConferenceRefresh
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single<SettingsComponent> {
        val lifecycle = LifecycleRegistry()
        DefaultSettingsComponent(
            componentContext = DefaultComponentContext(lifecycle),
            appSettings = get(),
            authentication = get(),
        ).also { lifecycle.resume() }
    }

    single<ConferenceRefresh> { WorkManagerConferenceRefresh(get()) }

    singleOf(::WorkManagerConferenceRefresh) { bind<ConferenceRefresh>() }
    singleOf(::DefaultAuthentication) { bind<Authentication>() }

}
