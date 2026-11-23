package io.aparok.app.presentation.editor.cover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.UnsplashImage
import io.aparok.app.domain.unsplash.SearchUnsplashImage
import io.aparok.app.presentation.common.Action
import io.aparok.app.presentation.common.BaseViewModel
import io.aparok.app.presentation.common.Delegator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import timber.log.Timber

class UnsplashViewModel(
    private val search: SearchUnsplashImage,
    private val delegator: Delegator<Action>
) : BaseViewModel() {

    val isCompleted = MutableStateFlow(false)
    val isFailed = MutableStateFlow(false)

    private val input = MutableStateFlow("")

    private val query = input.take(1).onCompletion {
        emitAll(
            input.drop(1).debounce(DEBOUNCE_DURATION).distinctUntilChanged()
        )
    }

    val images = MutableStateFlow<List<UnsplashImage>>(emptyList())
    val isLoading = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            query.mapLatest { q ->
                isLoading.value = true
                isFailed.value = false
                search(
                    SearchUnsplashImage.Params(
                        query = q
                    )
                ).process(
                    failure = {
                        Timber.e(it, "Error while searching unsplash pictures")
                        isFailed.value = true
                    },
                    success = {
                        images.value = it
                        isFailed.value = false
                    }
                ).also {
                    isLoading.value = false
                }
            }.collect()
        }
    }

    fun onImageSelected(ctx: Id, img: UnsplashImage) {
        viewModelScope.launch {
            delegator.delegate(
                Action.SetUnsplashImage(img.id)
            )
            isCompleted.value = true
        }
    }

    fun onQueryChanged(query: String) {
        viewModelScope.launch {
            input.emit(query)
        }
    }

    class Factory(
        private val search: SearchUnsplashImage,
        private val delegator: Delegator<Action>,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UnsplashViewModel(
                search = search,
                delegator = delegator,
            ) as T
        }
    }

    companion object {
        const val DEBOUNCE_DURATION = 300L
    }
}