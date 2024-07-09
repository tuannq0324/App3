package com.example.app3.ui.screen2

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app3.R
import com.example.app3.ui.common.ItemImage
import com.example.app3.ui.theme.App3Theme

@Composable
fun SecondScreen(
    viewModel: SecondViewModel,
    onBack: () -> Unit
) {

    BackHandler {
        onBack()
    }

    val context = LocalContext.current

    val imageItems by viewModel.imageViewItemsSelected.collectAsStateWithLifecycle(initialValue = emptyList())

    App3Theme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (imageItems.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(16.dp),
                        text = context.getString(R.string.label_empty),
                        textAlign = TextAlign.Center,
                    )

                    Button(modifier = Modifier
                        .wrapContentWidth()
                        .height(48.dp), content = {
                        Text(text = "Back to First Screen")
                    }, onClick = {
                        onBack()
                    })
                }
            } else {
                LazyVerticalStaggeredGrid(modifier = Modifier.fillMaxSize(),
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = 4.dp,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    state = rememberLazyStaggeredGridState(),
                    content = {
                        items(count = imageItems.size, key = {
                            imageItems[it].item.item.id
                        }, itemContent = {
                            ItemImage(imageItems[it]) { item ->
                                viewModel.updateSelect(item)
                            }
                        })
                    })
            }
        }
    }
}