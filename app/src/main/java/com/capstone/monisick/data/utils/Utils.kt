package com.capstone.monisick.data.utils

import android.app.Application
import com.capstone.monisick.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())
fun createFile(application: Application): File {
    val mediasDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    val outputsDirectory = if (
        mediasDir != null && mediasDir.exists()
    ) mediasDir else application.filesDir
    return File(outputsDirectory, "$timeStamp.jpg")
}

fun formatDate(
    dateString: String,
    currentFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
    desiredFormat: String = "dd MMM yyyy"
): String {
    return try {
        val inputFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
        val outputFormat = SimpleDateFormat(desiredFormat, Locale.getDefault())
        val date = inputFormat.parse(dateString)
        date?.let {
            outputFormat.format(it)
        } ?: ""
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}