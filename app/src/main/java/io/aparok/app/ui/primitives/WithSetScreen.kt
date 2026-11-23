package io.aparok.app.ui.primitives

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.compose.AndroidFragment
import io.aparok.app.feature_object_type.R
import io.aparok.app.feature_object_type.ui.BottomSyncStatus
import io.aparok.app.feature_object_type.ui.TopBarContent
import io.aparok.app.feature_object_type.ui.TypeEvent
import io.aparok.app.presentation.navigation.backstack.BackHistoryMenuState
import io.aparok.app.feature_object_type.ui.UiDeleteAlertState
import io.aparok.app.feature_object_type.ui.UiDeleteTypeAlertState
import io.aparok.app.feature_object_type.ui.UiHorizontalButtonsState
import io.aparok.app.feature_object_type.ui.UiIconState
import io.aparok.app.feature_object_type.ui.UiLayoutTypeState
import io.aparok.app.feature_object_type.ui.UiSyncStatusBadgeState
import io.aparok.app.feature_object_type.ui.UiTemplatesModalListState
import io.aparok.app.feature_object_type.ui.UiTitleState
import io.aparok.app.feature_object_type.ui.UiDescriptionState
import io.aparok.app.feature_object_type.ui.alerts.DeleteAlertScreen
import io.aparok.app.feature_object_type.ui.alerts.DeleteTypeAlertScreen
import io.aparok.app.feature_object_type.ui.header.DescriptionWidget
import io.aparok.app.feature_object_type.ui.header.HorizontalButtons
import io.aparok.app.feature_object_type.ui.header.IconAndTitleWidget
import io.aparok.app.feature_object_type.ui.layouts.TypeLayoutsScreen
import io.aparok.app.feature_object_type.ui.templates.TemplatesModalList
import io.aparok.app.presentation.sync.SyncStatusWidgetState
import io.aparok.app.ui.home.WidgetOverlayFragment
import io.aparok.app.ui.sets.ObjectSetFragment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithSetScreen(
    //top bar
    uiSyncStatusBadgeState: UiSyncStatusBadgeState,
    uiSyncStatusState: SyncStatusWidgetState,
    //header
    uiIconState: UiIconState,
    uiTitleState: UiTitleState,
    uiDescriptionState: UiDescriptionState,
    //layout, properties and templates buttons
    uiHorizontalButtonsState: UiHorizontalButtonsState,
    uiLayoutTypeState: UiLayoutTypeState,
    //templates modal list
    uiTemplatesModalListState: UiTemplatesModalListState,
    //delete alert
    uiDeleteAlertState: UiDeleteAlertState,
    //delete type alert (move to bin confirmation)
    uiDeleteTypeAlertState: UiDeleteTypeAlertState,
    //events
    onTypeEvent: (TypeEvent) -> Unit,
    objectId: String,
    space: String,
    view: String? = null,
    backHistoryMenu: BackHistoryMenuState = BackHistoryMenuState.Hidden,
) {
    val objectSetFragment: MutableState<ObjectSetFragment?> = remember(objectId, space) { mutableStateOf(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colorResource(id = R.color.background_primary),
        contentColor = colorResource(id = R.color.background_primary),
        topBar = {
            TopBarContent(
                uiSyncStatusBadgeState = uiSyncStatusBadgeState,
                uiTitleState = uiTitleState,
                uiIconState = uiIconState,
                onTypeEvent = { typeEvent ->
                    if (typeEvent is TypeEvent.OnBackClick) {
                        objectSetFragment.value?.onCloseCurrentObject()
                    }
                    if (typeEvent is TypeEvent.OnMenuClick) {
                        objectSetFragment.value?.onMenuClicked()
                    }
                    if (typeEvent is TypeEvent.OnTopBarTitleClick) {
                        objectSetFragment.value?.let { fragment ->
                            WidgetOverlayFragment.show(
                                fragment.parentFragmentManager,
                                space
                            )
                        }
                    }
                    onTypeEvent(typeEvent)
                },
                backHistoryMenu = backHistoryMenu
            )
        },
        content = { paddingValues ->
            MainContentSet(
                paddingValues = paddingValues,
                uiIconState = uiIconState,
                uiTitleState = uiTitleState,
                uiDescriptionState = uiDescriptionState,
                uiHorizontalButtonsState = uiHorizontalButtonsState,
                objectId = objectId,
                space = space,
                view = view,
                onTypeEvent = onTypeEvent,
                objectSetFragment = objectSetFragment
            )
        }
    )

    BottomSyncStatus(
        uiSyncStatusState = uiSyncStatusState,
        onDismiss = { onTypeEvent(TypeEvent.OnSyncStatusDismiss) }
    )

    if (uiDeleteAlertState is UiDeleteAlertState.Show) {
        DeleteAlertScreen(
            onTypeEvent = onTypeEvent
        )
    }

    if (uiDeleteTypeAlertState is UiDeleteTypeAlertState.Visible) {
        DeleteTypeAlertScreen(
            state = uiDeleteTypeAlertState,
            onTypeEvent = onTypeEvent
        )
    }

    if (uiLayoutTypeState is UiLayoutTypeState.Visible) {
        TypeLayoutsScreen(
            modifier = Modifier.fillMaxWidth(),
            uiState = uiLayoutTypeState,
            onTypeEvent = onTypeEvent
        )
    }

    if (uiTemplatesModalListState is UiTemplatesModalListState.Visible) {
        TemplatesModalList(
            modifier = Modifier.fillMaxWidth(),
            uiState = uiTemplatesModalListState,
            onTypeEvent = onTypeEvent
        )
    }
}


@Composable
private fun MainContentSet(
    paddingValues: PaddingValues,
    uiIconState: UiIconState,
    uiTitleState: UiTitleState,
    uiDescriptionState: UiDescriptionState,
    uiHorizontalButtonsState: UiHorizontalButtonsState,
    objectId: String,
    space: String,
    view: String?,
    onTypeEvent: (TypeEvent) -> Unit,
    objectSetFragment: MutableState<ObjectSetFragment?>
) {
    // The fragment applies the bottom inset to controls and scroll content, not its viewport.
    val contentModifier = Modifier
        .fillMaxSize()
        .padding(top = paddingValues.calculateTopPadding())
        .consumeWindowInsets(PaddingValues(top = paddingValues.calculateTopPadding()))

    val compositionContext = rememberCompositionContext()
    val fragment = objectSetFragment.value
    val density = LocalDensity.current
    val navigationBottom = WindowInsets.navigationBars.getBottom(density)
    val imeBottom = WindowInsets.ime.getBottom(density)
    // AndroidFragment does not receive insets consumed by its Compose host.
    // The host owns IME padding; forward only the remaining navigation clearance.
    val bottomClearance = (navigationBottom - imeBottom).coerceAtLeast(0)
    SideEffect { fragment?.setExternalBottomInset(bottomClearance) }
    val headerContent by rememberUpdatedState<@Composable () -> Unit> {
        TypeHeader(
            uiIconState = uiIconState,
            uiTitleState = uiTitleState,
            uiDescriptionState = uiDescriptionState,
            uiHorizontalButtonsState = uiHorizontalButtonsState,
            onTypeEvent = onTypeEvent,
            fragment = fragment
        )
    }
    DisposableEffect(fragment, compositionContext) {
        fragment?.bindExternalHeader(compositionContext) { headerContent() }
        onDispose { fragment?.clearExternalHeader(compositionContext) }
    }

    // The fragment's single host measures this header together with its pinned controls
    // and viewer. Scaffold owns only the fixed navigation and system/keyboard insets.
    key(objectId, space) {
        AndroidFragment<ObjectSetFragment>(
            modifier = contentModifier.imePadding(),
            arguments = ObjectSetFragment.args(
                ctx = objectId,
                space = space,
                view = view,
                embedded = true,
                externalHeader = true
            )
        ) { current -> objectSetFragment.value = current }
    }
}

@Composable
private fun TypeHeader(
    uiIconState: UiIconState,
    uiTitleState: UiTitleState,
    uiDescriptionState: UiDescriptionState,
    uiHorizontalButtonsState: UiHorizontalButtonsState,
    onTypeEvent: (TypeEvent) -> Unit,
    fragment: ObjectSetFragment?
) {
    val focusManager = LocalFocusManager.current
    DisposableEffect(fragment) {
        onDispose { fragment?.onExternalHeaderEditingChanged(false) }
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        IconAndTitleWidget(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 35.dp)
                .padding(horizontal = 20.dp),
            uiIconState = uiIconState,
            uiTitleState = uiTitleState,
            onTypeEvent = onTypeEvent
        )

        if (uiDescriptionState.isVisible) {
            Spacer(modifier = Modifier.height(8.dp))
            DescriptionWidget(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 20.dp)
                    .onFocusChanged { state ->
                        fragment?.onExternalHeaderEditingChanged(
                            editing = state.hasFocus && uiDescriptionState.isEditable,
                            clearFocus = { focusManager.clearFocus(force = true) }
                        )
                    },
                uiDescriptionState = uiDescriptionState,
                onDescriptionChanged = { text ->
                    onTypeEvent(TypeEvent.OnDescriptionChanged(text))
                }
            )
        }

        if (uiHorizontalButtonsState.isVisible) {
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalButtons(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .padding(horizontal = 20.dp),
                uiPropertiesButtonState = uiHorizontalButtonsState.uiPropertiesButtonState,
                uiLayoutButtonState = uiHorizontalButtonsState.uiLayoutButtonState,
                uiTemplatesButtonState = uiHorizontalButtonsState.uiTemplatesButtonState,
                onTypeEvent = onTypeEvent
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
