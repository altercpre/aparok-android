package io.aparok.app.ui_settings.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.aparok.app.core_ui.foundation.Divider
import io.aparok.app.core_ui.foundation.Dragger
import io.aparok.app.core_ui.views.Caption1Regular
import io.aparok.app.core_ui.views.Caption2Regular
import io.aparok.app.core_ui.views.Title1
import io.aparok.app.core_ui.views.UXBody
import io.aparok.app.ui_settings.R

@Composable
fun AboutAppScreen(
    libraryVersion: String,
    accountId: String,
    analyticsId: String,
    deviceId: String,
    version: String,
    buildNumber: Int,
    ethereumKey: String,
    onMetaClicked: () -> Unit,
    onContactUsClicked: () -> Unit,
    onExternalLinkClicked: (AboutAppViewModel.ExternalLink) -> Unit,
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Dragger()
        }
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(
                    top = 18.dp,
                    bottom = 12.dp
                )
        ) {
            Text(
                text = stringResource(R.string.about),
                style = Title1,
                color = colorResource(R.color.text_primary)
            )
        }
        Section(title = "About & Legal")
        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
            Text(
                text = "Free and non-commercial. Based on Anytype by Any Association, licensed under the Any Source Available License 1.0.",
                style = UXBody,
                color = colorResource(R.color.text_primary)
            )
        }
        Divider()
        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
            Text(
                text = "Aparok has no accounts, no analytics and no network features.",
                style = UXBody,
                color = colorResource(R.color.text_secondary)
            )
        }
        Divider()
        Text(
            text = "System Information",
            style = Caption1Regular,
            color = colorResource(R.color.text_secondary),
            modifier = Modifier.padding(
                top = 26.dp,
                start = 20.dp
            )
        )
        Box(
            modifier = Modifier
                .clickable {
                    onMetaClicked()
                }
                .padding(
                    top = 16.dp,
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 20.dp
                )
        ) {
            Text(
                text = "Aparok v$version ($buildNumber)\nCore: $libraryVersion\nSovereign Local Mode (Zero Telemetry, Zero Cloud)",
                style = Caption2Regular.copy(
                    color = colorResource(id = R.color.text_secondary)
                )
            )
        }
    }
}

@Composable
fun Option(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() }
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Text(
            text = title,
            style = UXBody.copy(color = colorResource(id = R.color.text_primary))
        )
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_forward),
            contentDescription = "Arrow Forward",
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

@Composable
fun Section(
    modifier: Modifier = Modifier,
    title: String
) {
    Box(modifier = modifier.padding(start = 20.dp, end = 20.dp, top = 26.dp, bottom = 8.dp)) {
        Text(
            text = title,
            style = Caption1Regular.copy(color = colorResource(id = R.color.text_secondary))
        )
    }
}

@Preview
@Composable
fun PreviewAboutAppScreen() {
    AboutAppScreen(
        libraryVersion = "1.0.0",
        accountId = "1234567890",
        analyticsId = "1234567890",
        deviceId = "123132323",
        version = "1.0.0",
        buildNumber = 1,
        onMetaClicked = {},
        onExternalLinkClicked = {},
        onContactUsClicked = {},
        ethereumKey = "Some key"
    )
}