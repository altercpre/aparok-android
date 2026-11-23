package io.aparok.app.presentation.util

import io.aparok.app.domain.base.AppCoroutineDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
val dispatchers = AppCoroutineDispatchers(
    io = UnconfinedTestDispatcher(),
    main = UnconfinedTestDispatcher(),
    computation = UnconfinedTestDispatcher()
)
