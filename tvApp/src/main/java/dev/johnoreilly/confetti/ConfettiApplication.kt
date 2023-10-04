package dev.johnoreilly.confetti

import android.app.Application
import androidx.work.WorkManager
import coil.ImageLoader
import coil.ImageLoaderFactory
import dev.johnoreilly.confetti.di.appModule
import dev.johnoreilly.confetti.di.initKoin
import dev.johnoreilly.confetti.work.setupDailyRefresh
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory

class ConfettiApplication : Application(), ImageLoaderFactory {

    private val isFirebaseInstalled = false

    override fun newImageLoader(): ImageLoader = get()

    override fun onCreate() {
        super.onCreate()

        // Initialize Logging.
        Napier.base(DebugAntilog())

        initKoin {
            androidLogger()
            androidContext(this@ConfettiApplication)
            modules(appModule)

//            workManagerFactory()
        }

        //       val workManager = get<WorkManager>()
        //      setupDailyRefresh(workManager)

//        ProcessLifecycleOwner.get().lifecycleScope.launch {
//            get<AppSettings>().experimentalFeaturesEnabledFlow.collect { isEnabled ->
//                if (isEnabled) {
//                    SessionNotificationWorker.startPeriodicWorkRequest(workManager)
//                } else {
//                    SessionNotificationWorker.cancelWorkRequest(workManager)
//                }
//            }
//        }
    }
}
