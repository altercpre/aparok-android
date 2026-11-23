package io.aparok.app.feature_object_type.ui.alerts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.aparok.app.core_ui.common.DefaultPreviews
import io.aparok.app.core_ui.foundation.AlertConfig
import io.aparok.app.core_ui.foundation.BUTTON_PRIMARY
import io.aparok.app.core_ui.foundation.BUTTON_SECONDARY
import io.aparok.app.core_ui.foundation.Dragger
import io.aparok.app.core_ui.foundation.GenericAlert
import io.aparok.app.feature_object_type.R
import io.aparok.app.feature_object_type.ui.TypeEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAlertScreen(
    onTypeEvent: (TypeEvent) -> Unit
) {

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        dragHandle = {
            Column {
                Spacer(modifier = Modifier.height(6.dp))
                Dragger()
                Spacer(modifier = Modifier.height(6.dp))
            }
        },
        scrimColor = colorResource(id = R.color.modal_screen_outside_background),
        containerColor = colorResource(id = R.color.background_secondary),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetState = bottomSheetState,
        onDismissRequest = {
            onTypeEvent(TypeEvent.OnAlertDeleteDismiss)
        },
        content = {
            GenericAlert(
                config = AlertConfig.WithTwoButtons(
                    title = stringResource(R.string.are_you_sure_delete_one_object),
                    description = stringResource(R.string.delete_irrevocably_one_object),
                    firstButtonText = stringResource(R.string.cancel),
                    secondButtonText = stringResource(R.string.delete),
                    icon = R.drawable.ic_popup_question_56,
                    firstButtonType = BUTTON_SECONDARY,
                    secondButtonType = BUTTON_PRIMARY,
                ),
                onFirstButtonClicked = {
                    onTypeEvent(TypeEvent.OnAlertDeleteDismiss)
                },
                onSecondButtonClicked = {
                    onTypeEvent(TypeEvent.OnAlertDeleteConfirm)
                }
            )
        }
    )
}

@DefaultPreviews
@Composable
fun DeleteAlertScreenPreview() {
    DeleteAlertScreen(
        onTypeEvent = {}
    )
}
