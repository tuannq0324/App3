package com.example.app3.activity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app3.BaseApplication.Companion.requester
import com.example.app3.api.model.ImageItem
import com.example.app3.api.model.ImageResponse
import com.example.app3.database.MainRepository
import com.example.app3.database.model.ImageEntity
import com.example.app3.model.ImageViewItem
import com.example.app3.utils.CommonFunction.convertToImageEntity
import com.example.app3.utils.CommonFunction.convertToImageResponse
import com.example.app3.utils.Constants.LOAD_MORE
import com.example.app3.utils.Constants.LOAD_MORE_FAILED
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

open class MainViewModel(
    private val repository: MainRepository
) : ViewModel() {

    private val imageEntities: Flow<List<ImageEntity>> = repository.getAll()

    private val imageItems: MutableStateFlow<List<ImageItem>> = MutableStateFlow(arrayListOf())

    private val _viewState = MutableStateFlow(ViewState.Loading)
    val viewState: Flow<ViewState> = _viewState

    val imageViewItems: Flow<List<ImageViewItem>> =
        combine(imageItems, imageEntities) { imageItems, imageEntities ->
            val selected = imageEntities.map { it.imageId }
            imageItems.map {
                ImageViewItem(item = it, isSelected = selected.contains(it.id))
            }
        }

    open val imageViewItemsSelected = repository.getAll().map { list ->
        list.map {
            ImageViewItem(item = it.convertToImageResponse(), isSelected = true)
        }
    }

    private var page = 1

    private var _isLoading = MutableStateFlow(false)
    var isLoading: Flow<Boolean> = _isLoading

    fun fetchData() {
        viewModelScope.launch {
            _isLoading.value = true

            val currentList = imageItems.value.toMutableList()

            when (val imageResponse = requester.loadImages(page, 10)) {

                is ImageResponse.Success -> {
                    delay(3000L)
                    currentList.find { it.id == LOAD_MORE || it.id == LOAD_MORE_FAILED }.let {
                        currentList.remove(it)
                    }
                    currentList.addAll(imageResponse.items)

                    imageItems.emit(currentList.distinctBy { it.id })

                    _viewState.emit(ViewState.Success)
                    _isLoading.value = false
                }

                is ImageResponse.Failed -> {
                    delay(3000L)
                    if (currentList.isEmpty()) {
                        //load failed
                        _viewState.emit(ViewState.Failed)
                    } else {
                        //load more failed
                        currentList.find { it.id == LOAD_MORE }?.let {
                            currentList.remove(it)
                        }
                        if (currentList.last().id != LOAD_MORE_FAILED) {
                            currentList += ImageItem(id = LOAD_MORE_FAILED, qualityUrls = null)
                            imageItems.emit(currentList)
                        }
                    }
                    _isLoading.value = false
                }
            }
            _isLoading.value = false
        }
    }

    fun loadMore() {
        if (_isLoading.value) return

        viewModelScope.launch {
            val currentList = imageItems.value.toMutableList()

            currentList.find { it.id == LOAD_MORE || it.id == LOAD_MORE_FAILED }.let {
                currentList.remove(it)
            }

            if (currentList.last().id == LOAD_MORE || currentList.last().id == LOAD_MORE_FAILED) {
                currentList.add(ImageItem(id = LOAD_MORE, qualityUrls = null))
                imageItems.emit(currentList)
                fetchData()
            } else {
                currentList.add(ImageItem(id = LOAD_MORE, qualityUrls = null))
                imageItems.emit(currentList)
                page++
                fetchData()
            }
        }
    }

    fun updateSelect(imageItem: ImageViewItem) {
        viewModelScope.launch {
            val item = imageItem.item

            val isExist = repository.isExisted(id = item.id)

            if (isExist) {
                repository.delete(item.id)
            } else {
                repository.insert(item.convertToImageEntity())
            }
        }
    }
}

enum class ViewState {
    Loading, Success, Failed, Empty,
}