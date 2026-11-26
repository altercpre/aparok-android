package io.aparok.app.ui.editor

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import io.aparok.app.R
import io.aparok.app.core_utils.const.FileConstants
import io.aparok.app.presentation.editor.model.CopyFileStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowDialog

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class PickerDelegateSafeDialogTest {

    @Test
    fun `PickerDelegate suppresses dialogs when fragment is detached`() {
        ShadowDialog.reset()
        val detachedFragment = Fragment()
        val delegate = PickerDelegate.Impl(detachedFragment) {}

        delegate.PickiTonUriReturned()
        assertNull("Detached fragment must not show PickiT progress dialog", ShadowDialog.getLatestDialog())

        delegate.PickiTonStartListener()
        assertNull("Detached fragment must not show PickiT alert dialog", ShadowDialog.getLatestDialog())
    }

    @Test
    fun `PickerDelegate suppresses dialogs when host activity is finishing`() {
        ShadowDialog.reset()
        val activity = Robolectric.buildActivity(AppCompatActivity::class.java).setup().get()
        activity.setTheme(R.style.AppTheme)
        val fragment = Fragment()
        activity.supportFragmentManager.beginTransaction().add(fragment, "picker").commitNow()
        activity.finish()

        val delegate = PickerDelegate.Impl(fragment) {}

        delegate.PickiTonUriReturned()
        assertNull("Finishing activity must not show PickiT progress dialog", ShadowDialog.getLatestDialog())

        delegate.PickiTonStartListener()
        assertNull("Finishing activity must not show PickiT alert dialog", ShadowDialog.getLatestDialog())
    }

    @Test
    fun `PickerDelegate shows and cleans up dialogs safely when fragment and activity are active`() {
        ShadowDialog.reset()
        val activity = Robolectric.buildActivity(AppCompatActivity::class.java).setup().get()
        activity.setTheme(R.style.AppTheme)
        val fragment = Fragment()
        activity.supportFragmentManager.beginTransaction().add(fragment, "picker").commitNow()

        val delegate = PickerDelegate.Impl(fragment) {}

        delegate.PickiTonUriReturned()
        val progressDialog = ShadowDialog.getLatestDialog()
        assertNotNull("Active fragment must show progress dialog", progressDialog)
        assertTrue(progressDialog!!.isShowing)

        delegate.PickiTonStartListener()
        val alertDialog = ShadowDialog.getLatestDialog()
        assertNotNull("Active fragment must show alert dialog", alertDialog)
        assertNotSame(progressDialog, alertDialog)
        assertTrue(alertDialog!!.isShowing)
        assertFalse(progressDialog.isShowing)

        delegate.cleanupDialogsAndResources()
        assertFalse("cleanupDialogsAndResources must dismiss alert dialog", alertDialog.isShowing)
    }

    @Test
    fun `PickerDelegate dispatches SAF copy and completion actions`() {
        val activity = Robolectric.buildActivity(AppCompatActivity::class.java).setup().get()
        val fragment = Fragment()
        activity.supportFragmentManager.beginTransaction().add(fragment, "picker").commitNow()

        val emittedActions = mutableListOf<PickerDelegate.Actions>()
        val delegate = PickerDelegate.Impl(fragment) { action ->
            emittedActions.add(action)
        }

        val testUri = Uri.parse("content://io.aparok.app.provider/cache/report.pdf")
        delegate.resolveActivityResult(
            requestCode = FileConstants.REQUEST_FILE_SAF_CODE,
            resultCode = Activity.RESULT_OK,
            data = Intent().setData(testUri)
        )
        delegate.onCopyFileCommand(CopyFileStatus.Completed("/cache/report.pdf"))
        delegate.clearOnCopyFile()

        assertEquals(
            listOf(
                PickerDelegate.Actions.OnStartCopyFileToCacheDir(testUri),
                PickerDelegate.Actions.OnProceedWithFilePath("/cache/report.pdf"),
                PickerDelegate.Actions.OnCancelCopyFileToCacheDir
            ),
            emittedActions
        )
    }
}
