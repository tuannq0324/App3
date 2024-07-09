package com.example.app3.api.model

import com.example.app3.model.QualityUrls


sealed class ImageResponse {
    data class Success(val items: List<ImageItem>) : ImageResponse()

    data object Failed : ImageResponse()
}

data class ImageItem(
    var id: String, var qualityUrls: QualityUrls?
)