@file:Suppress("RemoveExplicitTypeArguments")

package dev.johnoreilly.confetti.di

import dev.johnoreilly.confetti.ConfettiRepository
import dev.johnoreilly.confetti.auth.Authentication
import org.koin.dsl.module

val appModule = module {
    single<Authentication> { Authentication.Disabled }

    single {
        ConfettiRepository()
    }

}
