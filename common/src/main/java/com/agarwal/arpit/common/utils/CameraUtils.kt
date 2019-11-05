package com.agarwal.arpit.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.webkit.MimeTypeMap
import com.agarwal.arpit.common.logging.HubLog
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


fun Context.getTempOutputMediaPathForAllFiles(): File? {

    //for camera is set true for those case in which we are converting file object to URI object (ex- when photo is clicked)
    try {
        val mediaStorageDir: File
        val fileType: String
        val cacheDir = applicationContext.getExternalCacheDir()
        mediaStorageDir = File("${cacheDir} /IMAGE_DIRECTORY_NAME + File.separator + Environment.DIRECTORY_PICTURES")
        //  mediaStorageDir = AppController.getContext().getExternalFilesDir(IMAGE_DIRECTORY_NAME + File.separator + Environment.DIRECTORY_PICTURES);
        fileType = "image/jpeg"

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        return File(mediaStorageDir.path + File.separator + "ANDROID_HUB_TEMP" + timeStamp + "." + MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType))
    } catch (e: Exception) {
        HubLog.e(e.message!!)
    }

    return null
}

fun scaleBitmap(bm: Bitmap): Bitmap {
    var bm = bm
    var width = bm.width
    var height = bm.height

    if (width > height) {
        // landscape
        val ratio = width.toFloat() / 500
        width = 500
        height = (height / ratio).toInt()
    } else if (height > width) {
        // portrait
        val ratio = height.toFloat() / 700
        height = 700
        width = (width / ratio).toInt()
    }

    bm = Bitmap.createScaledBitmap(bm, width, height, true)
    return bm
}

/**
 * This method is responsible for solving the rotation issue if exist. Also scale the images to
 * 600x600 resolution
 *
 * @param context       The current context
 * @param selectedImage The Image URI
 * @return Bitmap image results
 * @throws IOException
 */
@Throws(IOException::class)
fun handleSamplingAndRotationBitmap(context: Context, selectedImage: Uri): Bitmap? {
    val MAX_HEIGHT = 600
    val MAX_WIDTH = 600

    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    var imageStream = context.contentResolver.openInputStream(selectedImage)
    BitmapFactory.decodeStream(imageStream, null, options)
    imageStream!!.close()

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    imageStream = context.contentResolver.openInputStream(selectedImage)
    var img = BitmapFactory.decodeStream(imageStream, null, options)

    img = rotateImageIfRequired(context, img!!, selectedImage)
    return img
}

fun calculateInSampleSize(
        options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val height = options.outHeight
    val width = options.outWidth

    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

        val halfHeight = height / 2
        val halfWidth = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}

/**
 * Rotate an image if required.
 *
 * @param img           The image bitmap
 * @param selectedImage Image URI
 * @return The resulted Bitmap after manipulation
 */
@Throws(IOException::class)
fun rotateImageIfRequired(context: Context, img: Bitmap, selectedImage: Uri): Bitmap {

    val input = context.contentResolver.openInputStream(selectedImage)
    val ei: ExifInterface
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
        ei = ExifInterface(input!!)
    else
        ei = ExifInterface(selectedImage.path!!)

    val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> return rotateImage(img, 90)
        ExifInterface.ORIENTATION_ROTATE_180 -> return rotateImage(img, 180)
        ExifInterface.ORIENTATION_ROTATE_270 -> return rotateImage(img, 270)
        else -> return img
    }
}

fun rotateImage(img: Bitmap, degree: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degree.toFloat())
    val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
    img.recycle()
    return rotatedImg
}

fun saveBitMapImage(file: File, bitmap: Bitmap): Boolean {
    try {
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        out.flush()
        out.close()
        return true
    } catch (e: Exception) {

        return false
    }

}

fun deleteFile(file: File) {
    if (file.exists()) file.delete()
}