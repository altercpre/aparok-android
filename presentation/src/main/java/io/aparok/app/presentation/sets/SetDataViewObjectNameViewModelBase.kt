package io.aparok.app.presentation.sets

import io.aparok.app.core_models.Id
import io.aparok.app.presentation.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

abstract class SetDataViewObjectNameViewModelBase : BaseViewModel() {
    val isCompleted = MutableStateFlow(false)

    /**
     * @param [target] used when object is already created and we need to set name for it.
     */
    abstract fun onActionDone(target: Id, space: Id, input: String)
    abstract fun onActionDone(input: String)
    /**
     * @param [target] used when object is already created and we need to set name for it.
     */
    abstract fun onButtonClicked(target: Id, space: Id, input: String)
    abstract fun onButtonClicked(input: String)
}