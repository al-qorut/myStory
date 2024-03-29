package com.alqorut.mystory.helpers

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.alqorut.mystory.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

private var progress: Progress? = null

fun Context.getSharedPref() = getSharedPreferences(PREFS,Context.MODE_PRIVATE)

fun createTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

fun toas(context: Context, msg:String){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
fun isProgress(context: Context,visible: Boolean) {
        if (visible) progress = Progress(context, R.string.please_wait, cancelable = true)
        progress?.apply { if (visible) show() else dismiss() }
    }
fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

fun msg(info:String){
        Log.e("debug", info)
    }

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
        mediaDir != null && mediaDir.exists()
    ) mediaDir else application.filesDir

    return File(outputDirectory, "$timeStamp.jpg")
}

fun rotateFile(file: File, isBackCamera: Boolean = false) {
    val matrix = Matrix()
    val bitmap = BitmapFactory.decodeFile(file.path)
    val rotation = if (isBackCamera) 90f else -90f
    matrix.postRotate(rotation)
    if (!isBackCamera) {
        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
    }
    val result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    result.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
}
fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while(inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun Activity.setupDialogStuff(
    view: View,
    dialog: AlertDialog.Builder,
    titleId: Int = 0,
    cancelOnTouchOutside: Boolean = true,
    callback: ((alertDialog: AlertDialog) -> Unit)? = null
) {
    if (isDestroyed || isFinishing) {
        return
    }
    val textColor = getColor(R.color.white)
    val backgroundColor = getColor(R.color.navi)
    val primaryColor = getColor(R.color.grey)

    var title: TextView? = null
    if (titleId != 0) {
        title = layoutInflater.inflate(R.layout.dialog_title, null) as TextView
        title.setText(titleId)
      }
    dialog.create().apply {
            setView(view)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCustomTitle(title)
            setCanceledOnTouchOutside(cancelOnTouchOutside)
            if (!isFinishing) {
                show()
            }
            val bgDrawable = resources.getDrawable(R.drawable.dialog_you_background, theme)
            window?.setBackgroundDrawable(bgDrawable)
            callback?.invoke(this)
        }
    }