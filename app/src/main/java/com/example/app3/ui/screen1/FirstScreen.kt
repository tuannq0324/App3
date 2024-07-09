package com.example.app3.ui.screen1

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app3.R
import com.example.app3.model.ImageViewItem
import com.example.app3.model.id
import com.example.app3.ui.common.ItemImage
import com.example.app3.ui.common.LoadMoreFailedItem
import com.example.app3.ui.common.LoadMoreItem
import com.example.app3.ui.viewmodel.ViewState

@Composable
fun FirstScreen(
    viewModel: FirstViewModel, openSecondScreen: () -> Unit
) {

    val state by viewModel.viewState.collectAsStateWithLifecycle(initialValue = ViewState.Loading)

    val items by viewModel.imageViewItems.collectAsStateWithLifecycle(initialValue = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Crossfade(
            modifier = Modifier.weight(1f), targetState = state, label = ""
        ) {
            when (it) {
                ViewState.Loading -> {
                    // Loading state - No changes needed
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Loading", modifier = Modifier.padding(16.dp))
                        CircularProgressIndicator()
                    }
                }

                ViewState.Success -> {
                    ImageGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        items = items,
                        onSelect = viewModel::updateSelect,
                        onLoadMore = viewModel::loadMore
                    )
                }

                ViewState.Failed, ViewState.Empty -> {
                    ErrorState(onRetry = viewModel::fetchData)
                }
            }
        }

        // Button - Consider using Navigation component for screen transitions
        Button(modifier = Modifier
            .wrapContentWidth()
            .height(48.dp), onClick = {
            openSecondScreen()
        }) {
            Text(text = "Activity Second")
        }
    }
}

// Extracted composable for image grid display
@Composable
fun ImageGrid(
    modifier: Modifier,
    items: List<ImageViewItem>,
    onSelect: (ImageViewItem) -> Unit = {},
    onLoadMore: () -> Unit = {}
) {
    LazyVerticalStaggeredGrid(modifier = modifier,
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        state = rememberLazyStaggeredGridState(),
        content = {
            items.forEachIndexed { index, imageViewItem ->
                when (imageViewItem) {
                    is ImageViewItem.Image -> {
                        item(key = imageViewItem.id) {
                            ItemImage(item = imageViewItem, onItemClicked = onSelect)
                        }
                    }

                    ImageViewItem.LoadMore -> {
                        item(span = StaggeredGridItemSpan.FullLine) {
                            LoadMoreItem()
                        }
                    }

                    ImageViewItem.LoadMoreFailed -> {
                        item(span = StaggeredGridItemSpan.FullLine) {
                            LoadMoreFailedItem(onLoadMore)
                        }
                    }
                }

                if (index == items.size - 1) {
                    onLoadMore()
                }
            }
        })
}

// Composable for error state
@Composable
fun ErrorState(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Consider using a vector drawable for better scalability
        val painter = painterResource(id = R.drawable.ic_error)
        Image(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .wrapContentHeight(),
            painter = painter,
            contentDescription = null, // Provide a meaningful description if applicable
            contentScale = ContentScale.Crop
        )

        Text(
            text = stringResource(R.string.load_failed),
            modifier = Modifier.clickable { onRetry() })
    }
}