package io.aparok.app.domain.icon

import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase

abstract class RemoveIcon<T> : BaseUseCase<Payload, T>()