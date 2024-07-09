//package com.example.app3.activity.activity2
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
//import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
//import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.lifecycleScope
//import com.example.app3.activity.MainViewModel
//import com.example.app3.activity.ViewModelFactory
//import com.example.app3.database.AppDatabase
//import com.example.app3.database.MainRepository
//import com.example.app3.extention.launchWhenStarted
//import com.example.app3.model.ImageViewItem
//import com.example.app3.ui.theme.App3Theme
//import kotlinx.coroutines.launch
//
//class Activity2 : ComponentActivity() {
//
//    private val viewModel by lazy {
//        ViewModelProvider(
//            this, ViewModelFactory(MainRepository(AppDatabase.getInstance(this)))
//        )[MainViewModel::class.java]
//    }
//
//    private val listItem = mutableStateListOf<ImageViewItem>()
//
//    private fun initData() {
//        viewModel.fetchData()
//        lifecycleScope.launch {
//            viewModel.imageViewItemsSelected.collect {
//                launchWhenStarted {
//                    listItem.clear()
//                    listItem.addAll(it)
//                }
//            }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        initData()
//
//        setContent {
//            App3Theme {
//                LazyVerticalStaggeredGrid(modifier = Modifier.fillMaxSize(),
//                    columns = StaggeredGridCells.Fixed(2),
//                    verticalItemSpacing = 4.dp,
//                    horizontalArrangement = Arrangement.spacedBy(4.dp),
//                    state = rememberLazyStaggeredGridState().apply {
//                        if (!canScrollForward) {
//                            viewModel.loadMore()
//                        }
//                    },
//                    content = {
//                        items(count = listItem.size, key = {
//                            listItem[it].item.id
//                        }, itemContent = {
//                            ItemImage(listItem[it]) { item ->
//                                viewModel.updateSelect(item)
//                            }
//                        })
//                    })
//            }
//        }
//    }
//}