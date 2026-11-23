package io.aparok.app.domain.device

interface PathProvider {
    fun providePath(): String
    fun cachePath(): String
}