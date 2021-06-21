package com.gelato.picsum.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageData(
    @PrimaryKey(autoGenerate = true)
    val _id: Long,
    val id: String,
    val author: String? = null,
    val download_url: String? = null,
    val height: Int? = null,
    val url: String? = null,
    val width: Int? = null
)