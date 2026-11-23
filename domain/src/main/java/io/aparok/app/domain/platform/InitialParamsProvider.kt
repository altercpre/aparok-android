package io.aparok.app.domain.platform

import io.aparok.app.core_models.Command

interface InitialParamsProvider {
    fun getVersion(): String
    fun getPlatform(): String
    val workDir: String
    val defaultLogLevel: String
    fun toCommand(): Command.SetInitialParams
}