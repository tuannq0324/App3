package com.example.app3.activity.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.app3.R
import com.example.app3.activity.MainViewModel
import com.example.app3.activity.MainViewModelFactory
import com.example.app3.activity.ViewState
import com.example.app3.activity.activity1.MainActivity.Screens
import com.example.app3.database.AppDatabase
import com.example.app3.database.MainRepository
import com.example.app3.model.ImageViewItem
import com.example.app3.utils.Constants.LOAD_MORE
import com.example.app3.utils.Constants.LOAD_MORE_FAILED

@Composable
fun MainScreen(navController: NavController, context: Context) {
    // ViewModel instantiation - Good practice to use 'by viewModels()'
    val mainViewModel = viewModel<MainViewModel>(
        factory = MainViewModelFactory(
            MainRepository(
                AppDatabase.getInstance(context)
            )
        )
    )

    // State for image items - Consider renaming for clarity
    val imageItems = remember { mutableStateListOf<ImageViewItem>() }

    val state by mainViewModel.viewState.collectAsStateWithLifecycle(initialValue = ViewState.Loading)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Data collection - Combine fetchData and collection
        LaunchedEffect(key1 = Unit) { // Key ensures recomposition on config changes
            mainViewModel.fetchData() // Fetch data when LaunchedEffect starts
            mainViewModel.imageViewItems.collect { items ->
                imageItems.clear()
                imageItems.addAll(items)
            }
        }

        when (state) {
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
                // Success state - Extract item display logic to a separate composable
                ImageGrid(
                    context,
                    imageItems,
                    mainViewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(bottom = 16.dp)
                )
            }

            ViewState.Failed, ViewState.Empty -> {
                // Error/Empty states - Consolidate similar handling
                ErrorState(onRetry = { mainViewModel.fetchData() })
            }
        }

        // Button - Consider using Navigation component for screen transitions
        Button(modifier = Modifier
            .wrapContentWidth()
            .height(48.dp), onClick = {
            navController.navigate(Screens.SecondScreen.route)
        }) {
            Text(text = "Activity Second")
        }
    }
}

// Extracted composable for image grid display
@Composable
fun ImageGrid(
    context: Context, items: List<ImageViewItem>, viewModel: MainViewModel, modifier: Modifier
) {

    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle(initialValue = false)

    LazyVerticalStaggeredGrid(modifier = modifier,
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        state = rememberLazyStaggeredGridState(),
        content = {
            items.forEach {
                when (it.item.id) {
                    LOAD_MORE -> {
                        //load more
                        item(
                            key = it.item.id, content = {
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
                            }, span = StaggeredGridItemSpan.FullLine
                        )
                    }

                    LOAD_MORE_FAILED -> {
                        item(key = it.item.id, content = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = context.getString(R.string.load_failed),
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .clickable {
                                            viewModel.loadMore()
                                        })
                            }
                        }, span = StaggeredGridItemSpan.FullLine)
                    }

                    else -> {
                        item(key = it.item.id, content = {
                            ItemImage(item = it) { item ->
                                viewModel.updateSelect(item)
                            }
                        })
                    }
                }
            }
            item {
                LaunchedEffect(key1 = true) {
                    if (!isLoading.value) {
                        viewModel.loadMore()
                    }
                }
            }
        })
}

//imageItem
@Composable
fun ItemImage(item: ImageViewItem, onItemClicked: (ImageViewItem) -> Unit) {
    val drawableResource = if (item.isSelected) R.drawable.ic_tick else R.drawable.ic_untick

    Box {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable {
                    onItemClicked.invoke(item)
                },
            model = item.item.qualityUrls?.thumb,
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