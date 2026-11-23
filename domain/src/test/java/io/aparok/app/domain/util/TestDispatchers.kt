package io.aparok.app.domain.util

import io.aparok.app.domain.base.AppCoroutineDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
val dispatchers = AppCoroutineDispatchers(
    io = StandardTestDispatcher(),
    main = StandardTestDispatcher(),
    computation = StandardTestDispatcher()
)