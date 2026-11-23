package io.aparok.app.presentation.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.aparok.app.core_utils.common.EventWrapper
import timber.log.Timber

interface SupportCommand<Command> {
    val commands: MutableLiveData<EventWrapper<Command>>
    fun receive(): LiveData<EventWrapper<Command>> = commands
    fun dispatch(command: Command) {
        Timber.d("Dispatch from  EditorViewModel, command:[$command]")
        commands.postValue(EventWrapper(command))
    }
}