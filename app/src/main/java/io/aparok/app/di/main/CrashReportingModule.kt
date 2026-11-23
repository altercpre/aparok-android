package io.aparok.app.di.main

import io.aparok.app.CrashReporter
import io.aparok.app.NoOpCrashReporter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object CrashReportingModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideCrashReporter(): CrashReporter = NoOpCrashReporter()
}
