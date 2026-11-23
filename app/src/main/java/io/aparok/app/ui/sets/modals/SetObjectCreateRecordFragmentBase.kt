package io.aparok.app.ui.sets.modals

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.annotation.CallSuper
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import io.aparok.app.core_models.Id
import io.aparok.app.core_ui.reactive.editorActionEvents
import io.aparok.app.core_utils.ext.argString
import io.aparok.app.core_utils.ext.hideKeyboard
import io.aparok.app.core_utils.ext.toast
import io.aparok.app.core_utils.ui.BaseBottomSheetFragment
import io.aparok.app.presentation.sets.SetDataViewObjectNameViewModelBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class SetObjectCreateRecordFragmentBase<T: ViewBinding> :
    BaseBottomSheetFragment<T>() {

    protected abstract val textInputType: Int
    protected abstract val textInputField: EditText
    protected abstract val button: View

    protected abstract val vm: SetDataViewObjectNameViewModelBase

    abstract fun onButtonClicked()
    abstract fun onKeyboardActionDone()

    protected val ctx: String get() = argString(CTX_KEY)
    protected val space: String get() = argString(SPACE_KEY)

    private val handler: (Int) -> Boolean = { action ->
        action == EditorInfo.IME_ACTION_DONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textInputField.apply { setRawInputType(textInputType) }
        button.setOnClickListener { onButtonClicked() }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                onStart(this)
            }
        }
    }

    @CallSuper
    protected open fun onStart(scope: CoroutineScope) {
        with(scope) {
            launch { subscribeTextInputActions() }
            launch { subscribeIsCompleted() }
            launch { subscribeToasts() }
        }
    }

    private suspend fun subscribeToasts() {
        vm.toasts.collect { toast(it) }
    }

    private suspend fun subscribeTextInputActions() {
        textInputField.editorActionEvents(handler).collect {
            textInputField.clearFocus()
            textInputField.hideKeyboard()
            onKeyboardActionDone()
        }
    }

    private suspend fun subscribeIsCompleted() {
        vm.isCompleted.collect { isCompleted -> if (isCompleted) dismiss() }
    }

    companion object {
        private const val CTX_KEY = "arg.object-set-record.context"
        private const val SPACE_KEY = "arg.object-set-record.space"
        const val TARGET_KEY = "arg.object-set-record.target"

        fun args(
            ctx: Id,
            target: Id,
            space: Id,
        ) = bundleOf(
            CTX_KEY to ctx,
            TARGET_KEY to target,
            SPACE_KEY to space
        )

        fun args(
            ctx: Id,
            space: Id,
        ) = bundleOf(
            CTX_KEY to ctx,
            SPACE_KEY to space
        )
    }
}