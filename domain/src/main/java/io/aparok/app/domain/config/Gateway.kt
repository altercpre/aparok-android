package io.aparok.app.domain.config

interface Gateway {
    fun provide(): String
}