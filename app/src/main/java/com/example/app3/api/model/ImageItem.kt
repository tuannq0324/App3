package com.example.app3.api.model


sealed class ImageResponse {
    data class Success(val items: List<ImageItem>) : ImageResponse()

    data object Failed : ImageResponse()
}

data class ImageItem(
    val id: String, val urls: List<String>
)