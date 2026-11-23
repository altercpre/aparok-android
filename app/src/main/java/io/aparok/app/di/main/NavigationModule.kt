package io.aparok.app.di.main

import io.aparok.app.navigation.DefaultNavigationBackStackInspector
import io.aparok.app.presentation.navigation.backstack.NavigationBackStackInspector
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class NavigationModule {

    @Binds
    @Singleton
    abstract fun bindNavigationBackStackInspector(
        impl: DefaultNavigationBackStackInspector
    ): NavigationBackStackInspector
}
