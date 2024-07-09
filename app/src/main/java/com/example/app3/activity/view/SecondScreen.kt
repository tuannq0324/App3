package com.example.app3.activity.view

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app3.activity.MainViewModel
import com.example.app3.activity.MainViewModelFactory
import com.example.app3.database.AppDatabase
import com.example.app3.database.MainRepository
import com.example.app3.model.ImageViewItem
import com.example.app3.ui.theme.App3Theme

@Composable
fun SecondScreen(context: Context) {

    val mainViewModel = viewModel<MainViewModel>(
        factory = MainViewModelFactory(
            MainRepository(
                AppDatabase.getInstance(context)
            )
        )
    )

    val imageItems = remember { mutableStateListOf<ImageViewItem>() }

    val isLoading = mainViewModel.isLoading.collectAsStateWithLifecycle(initialValue = false)

    App3Theme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LaunchedEffect(key1 = Unit) { // Key ensures recomposition on config changes
                mainViewModel.fetchData() // Fetch data when LaunchedEffect starts
                mainViewModel.imageViewItemsSelected.collect { items ->
                    imageItems.clear()
                    imageItems.addAll(items)
                }
            }
            LazyVerticalStaggeredGrid(modifier = Modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                state = rememberLazyStaggeredGridState(),
                content = {
                    items(count = imageItems.size, key = {
                        imageItems[it].item.id
                    }, itemContent = {
                        ItemImage(imageItems[it]) { item ->
                            mainViewModel.updateSelect(item)
                        }
                    })
                    item {
                        LaunchedEffect(key1 = true) {
                            if (!isLoading.value) {
                                mainViewModel.loadMore()
                            }
                        }
                    }
                })
        }
    }
}