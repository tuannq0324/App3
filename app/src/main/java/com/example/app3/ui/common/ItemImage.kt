package com.example.app3.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import com.example.app3.R
import com.example.app3.model.ImageViewItem

//imageItem
@Composable
fun ItemImage(
    item: ImageViewItem.Image,
    onItemClicked: (ImageViewItem) -> Unit
) {

    val drawableResource = if (item.item.isSelected) R.drawable.ic_tick else R.drawable.ic_untick

    val placeholder = painterResource(id = R.drawable.ic_image_default)
    val error = painterResource(id = R.drawable.ic_load_failed)

    Box {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable {
                    onItemClicked.invoke(item)
                },
            placeholder = placeholder,
            error = error,
            model = item.item.item.urls.last(),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )

        Image(
            painterResource(id = drawableResource),
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .align(Alignment.BottomEnd),
            contentDescription = "",
        )
    }
}

@Composable
fun LoadMoreItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .height(48.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun LoadMoreFailedItem(onRetry: () -> Unit) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = context.getString(R.string.load_failed),
            modifier = Modifier
                .align(Alignment.Center)
                .clickable {
                    onRetry()
                }
        )
    }
}