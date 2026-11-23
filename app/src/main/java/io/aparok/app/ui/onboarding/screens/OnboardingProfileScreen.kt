package io.aparok.app.ui.onboarding.screens

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.aparok.app.R
import io.aparok.app.core_ui.views.ButtonOnboardingPrimaryLarge
import io.aparok.app.core_ui.views.ButtonOnboardingSecondaryLarge
import io.aparok.app.core_ui.views.ButtonSize
import io.aparok.app.core_ui.views.Caption1Regular
import io.aparok.app.ui.onboarding.ProfileImageHelper

private const val MAX_NAME_LENGTH = 32

@Composable
fun OnboardingProfileScreen(
    isLoading: Boolean,
    onCreateProfileClicked: (name: String, avatarPath: String?) -> Unit,
    onRestoreBackupClicked: () -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var avatarPath by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val processedPath = ProfileImageHelper.processAndSaveProfileImage(context, uri)
            avatarPath = processedPath
        }
    }

    fun submit() {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) {
            isError = true
            return
        }
        isError = false
        focusManager.clearFocus()
        keyboardController?.hide()
        onCreateProfileClicked(trimmed, avatarPath)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Mark / Icon
            Image(
                painter = painterResource(id = R.drawable.ic_anytype_logo),
                contentDescription = "Aparok",
                modifier = Modifier.size(56.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Welcome to Aparok",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.text_white),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Sovereign, offline, encrypted on this device.",
                fontSize = 14.sp,
                color = colorResource(id = R.color.text_secondary),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Avatar Picker
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(colorResource(id = R.color.shape_tertiary))
                    .border(2.dp, colorResource(id = R.color.border_gray), CircleShape)
                    .clickable {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                val currentPath = avatarPath
                if (currentPath != null) {
                    val bitmap = remember(currentPath) { BitmapFactory.decodeFile(currentPath) }
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        AvatarPlaceholder()
                    }
                } else {
                    AvatarPlaceholder()
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (avatarPath == null) "Add photo (optional)" else "Change photo",
                fontSize = 12.sp,
                color = colorResource(id = R.color.text_secondary),
                modifier = Modifier.clickable {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Profile Name Input
            OutlinedTextField(
                value = name,
                onValueChange = { input ->
                    if (input.length <= MAX_NAME_LENGTH) {
                        name = input
                        isError = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Your Name",
                        color = Color(0xFF646464)
                    )
                },
                supportingText = {
                    if (isError) {
                        Text(
                            text = "Name is required",
                            color = colorResource(id = R.color.palette_system_red),
                            style = Caption1Regular
                        )
                    }
                },
                singleLine = true,
                isError = isError,
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colorResource(id = R.color.text_white),
                    unfocusedTextColor = colorResource(id = R.color.text_white),
                    disabledTextColor = colorResource(id = R.color.text_primary),
                    cursorColor = Color(0xFFC2C2C2),
                    focusedContainerColor = Color(0xFF212121),
                    unfocusedContainerColor = Color(0xFF212121),
                    errorContainerColor = Color(0xFF212121),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions {
                    submit()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Create Profile Button
            ButtonOnboardingPrimaryLarge(
                text = "Create profile",
                onClick = { submit() },
                enabled = !isLoading && name.trim().isNotBlank(),
                loading = isLoading,
                size = ButtonSize.Large,
                modifierBox = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Restore from Backup Button
            ButtonOnboardingSecondaryLarge(
                text = "Restore from backup",
                onClick = onRestoreBackupClicked,
                enabled = !isLoading,
                size = ButtonSize.Large,
                modifierBox = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun AvatarPlaceholder() {
    Image(
        painter = painterResource(id = R.drawable.ci_camera),
        contentDescription = "Pick Avatar",
        modifier = Modifier.size(36.dp)
    )
}
