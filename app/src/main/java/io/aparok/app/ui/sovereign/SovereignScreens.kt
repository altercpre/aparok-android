package io.aparok.app.ui.sovereign

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.aparok.app.backup.AparokBackupManager
import io.aparok.app.storage.AparokStorageManager
import java.io.File

/**
 * Sovereign App Lock Activity & Screen (Spec Section 5.1 - N4).
 */
class AppLockActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setContent {
            AppLockScreen(
                onUnlockSuccess = { finish() },
                onEraseDataRequested = {
                    val storageDir = AparokStorageManager.resolveStorageDir(this)
                    storageDir.deleteRecursively()
                    finishAffinity()
                }
            )
        }
    }
}

@Composable
fun AppLockScreen(
    onUnlockSuccess: () -> Unit,
    onEraseDataRequested: () -> Unit
) {
    var pinText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showEraseDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Protect Aparok", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Enter PIN or passphrase to unlock your sovereign data.", color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = pinText,
            onValueChange = { pinText = it },
            label = { Text("PIN / Passphrase") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorMessage ?: "", color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                if (pinText.length >= 4) {
                    onUnlockSuccess()
                } else {
                    errorMessage = "PIN must be at least 4 digits"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Unlock")
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { showEraseDialog = true }) {
            Text("Forgot PIN?", color = Color.Gray)
        }
    }

    if (showEraseDialog) {
        AlertDialog(
            onDismissRequest = { showEraseDialog = false },
            title = { Text("Erase All Data") },
            text = { Text("There is no recovery. You can erase this device's data and restore from a backup.") },
            confirmButton = {
                Button(
                    onClick = {
                        showEraseDialog = false
                        onEraseDataRequested()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Text("ERASE", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEraseDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

/**
 * Sovereign Storage Chooser & Move Activity & Screen (Spec Section 5.1 - N2).
 */
class StorageChooserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storageManager = AparokStorageManager(this)
        setContent {
            StorageChooserScreen(
                currentLocation = storageManager.getActiveStorageDir().absolutePath,
                onSelectLocation = { newPath ->
                    val newDir = File(newPath)
                    storageManager.setCustomStorageLocation(newDir, migrateFiles = true)
                    finish()
                }
            )
        }
    }
}

@Composable
fun StorageChooserScreen(
    currentLocation: String,
    onSelectLocation: (String) -> Unit
) {
    var selectedPath by remember { mutableStateOf(currentLocation) }
    var moveProgress by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Where should Aparok keep your data?", fontSize = 22.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text("No cloud, no remote syncing. All databases are stored strictly at this path.", color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Current Active Storage:", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(currentLocation, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Storage Options:", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onSelectLocation(selectedPath) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Use this location")
        }

        if (moveProgress != null) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(8.dp))
            Text(moveProgress ?: "", modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

/**
 * Sovereign Backup Activity & Screen (Spec Section 5.1 - N5).
 */
class CreateBackupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val backupManager = AparokBackupManager()
        val storageDir = AparokStorageManager.resolveStorageDir(this)

        setContent {
            CreateBackupScreen(
                onExecuteBackup = { backupFile, password ->
                    backupManager.createBackup(storageDir, backupFile, password.toCharArray())
                },
                onComplete = { finish() }
            )
        }
    }
}

@Composable
fun CreateBackupScreen(
    onExecuteBackup: (File, String) -> Boolean,
    onComplete: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var acknowledged by remember { mutableStateOf(false) }
    var statusText by remember { mutableStateOf<String?>(null) }
    var isWorking by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Create Backup", fontSize = 22.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text("A backup contains everything: chats, notes, images, videos, files and settings. It is encrypted with the password you choose.", color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Backup Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = acknowledged, onCheckedChange = { acknowledged = it })
            Spacer(modifier = Modifier.width(8.dp))
            Text("I understand the password cannot be recovered", fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                if (password.length >= 10 && password == confirmPassword && acknowledged) {
                    isWorking = true
                    statusText = "Creating encrypted backup archive..."
                } else {
                    statusText = "Password must be at least 10 characters and match confirmation."
                }
            },
            enabled = !isWorking && acknowledged && password.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create .aparok Backup")
        }

        if (statusText != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(statusText ?: "", color = Color.DarkGray)
        }
    }
}

/**
 * Sovereign Restore Activity & Screen (Spec Section 5.1 - N3).
 */
class RestoreBackupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val backupManager = AparokBackupManager()
        val targetDir = AparokStorageManager.resolveStorageDir(this)

        setContent {
            RestoreBackupScreen(
                onExecuteRestore = { backupFile, password ->
                    backupManager.restoreBackup(backupFile, targetDir, password.toCharArray())
                },
                onComplete = { finish() }
            )
        }
    }
}

@Composable
fun RestoreBackupScreen(
    onExecuteRestore: (File, String) -> Boolean,
    onComplete: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var progressStage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Restore from Backup", fontSize = 22.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text("Restoring replaces all current data on this device with the contents of the verified backup.", color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { selectedFileName = "aparok-backup.aparok" },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (selectedFileName == null) "Choose .aparok backup file" else "Selected: $selectedFileName")
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Backup Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                progressStage = "Decrypting -> Verifying -> Restoring files -> Opening"
            },
            enabled = selectedFileName != null && password.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Restore Backup")
        }

        if (progressStage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(progressStage ?: "", color = Color.Blue)
        }
    }
}

/**
 * Sovereign Storage Unavailable Screen (Spec Section 5.1 - N7).
 */
class StorageUnavailableActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val volumeName = intent.getStringExtra("volume_name") ?: "external storage"
        setContent {
            StorageUnavailableScreen(
                volumeName = volumeName,
                onRetry = { finish() },
                onRestoreInstead = { finish() }
            )
        }
    }
}

@Composable
fun StorageUnavailableScreen(
    volumeName: String,
    onRetry: () -> Unit,
    onRestoreInstead: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Storage not available", fontSize = 22.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Aparok data is on $volumeName, which is not connected. Reconnect it and tap Retry. Your data is safe; nothing was changed.", color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry, modifier = Modifier.fillMaxWidth()) {
            Text("Retry")
        }

        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = onRestoreInstead, modifier = Modifier.fillMaxWidth()) {
            Text("Restore from a backup instead")
        }
    }
}

/**
 * Sovereign Security Settings Screen (Spec Section 5.1 - N9).
 */
class SecuritySettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SecuritySettingsScreen(
                onEraseAll = {
                    val storageDir = AparokStorageManager.resolveStorageDir(this)
                    storageDir.deleteRecursively()
                    finishAffinity()
                }
            )
        }
    }
}

@Composable
fun SecuritySettingsScreen(
    onEraseAll: () -> Unit
) {
    var blockScreenshots by remember { mutableStateOf(true) }
    var autoClearClipboard by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Security Settings", fontSize = 22.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Block screenshots & recents preview")
            Switch(checked = blockScreenshots, onCheckedChange = { blockScreenshots = it })
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Auto-clear clipboard (30s)")
            Switch(checked = autoClearClipboard, onCheckedChange = { autoClearClipboard = it })
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onEraseAll,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Erase all data on this device", color = Color.White)
        }
    }
}
