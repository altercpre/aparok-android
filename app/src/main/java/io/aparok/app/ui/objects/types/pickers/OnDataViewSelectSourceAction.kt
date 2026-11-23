package io.aparok.app.ui.objects.types.pickers

import io.aparok.app.core_models.Id

interface OnDataViewSelectSourceAction {
    fun onProceedWithSelectSource(id: Id)
}