package com.capstone.monisick.data.database.entity

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageResult(
    val imagePath: String,
    val imageUri: Uri? = null,
    val isFromCamera: Boolean = true,
    val imageBitmap: Bitmap? = null
) : Parcelable