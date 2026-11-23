package io.aparok.app.domain.misc

interface Reducer<STATE, EVENT> {
    fun reduce(state: STATE, event: EVENT): STATE
}