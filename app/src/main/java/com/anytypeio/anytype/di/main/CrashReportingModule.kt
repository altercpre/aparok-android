package com.anytypeio.anytype.di.main

import com.anytypeio.anytype.CrashReporter
import com.anytypeio.anytype.NoOpCrashReporter
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
