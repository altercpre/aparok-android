package io.aparok.app.ui.onboarding

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max

object ProfileImageHelper {
    /**
     * Downscales the image from the given URI to <= 512x512, re-encodes to PNG to strip all EXIF/GPS,
     * and saves to the internal cache directory. Returns the absolute path of the processed image.
     */
    fun processAndSaveProfileImage(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val boundsOptions = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, boundsOptions)
            inputStream.close()

            val origWidth = boundsOptions.outWidth
            val origHeight = boundsOptions.outHeight
            if (origWidth <= 0 || origHeight <= 0) return null

            var inSampleSize = 1
            val maxDim = max(origWidth, origHeight)
            while (maxDim / inSampleSize > 512) {
                inSampleSize *= 2
            }

            val decodeOptions = BitmapFactory.Options().apply {
                this.inSampleSize = inSampleSize
            }
            val secondStream = context.contentResolver.openInputStream(uri) ?: return null
            val sampledBitmap = BitmapFactory.decodeStream(secondStream, null, decodeOptions) ?: return null
            secondStream.close()

            // Scale precisely to max 512x512 preserving aspect ratio
            val width = sampledBitmap.width
            val height = sampledBitmap.height
            val scale = if (max(width, height) > 512) 512f / max(width, height) else 1f
            val finalBitmap = if (scale < 1f) {
                val scaled = Bitmap.createScaledBitmap(sampledBitmap, (width * scale).toInt(), (height * scale).toInt(), true)
                if (scaled != sampledBitmap) {
                    sampledBitmap.recycle()
                }
                scaled
            } else {
                sampledBitmap
            }

            val outFile = File(context.cacheDir, "profile_avatar_${System.currentTimeMillis()}.png")
            FileOutputStream(outFile).use { out ->
                finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            finalBitmap.recycle()
            outFile.absolutePath
        } catch (e: Exception) {
            null
        }
    }
}
