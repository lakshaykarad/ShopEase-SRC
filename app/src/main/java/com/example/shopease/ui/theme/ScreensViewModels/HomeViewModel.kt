package com.example.shopease.ui.theme.ScreensViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.model.Category
import com.example.shopease.data.model.Products
import com.example.shopease.data.repository.HomeRepository
import com.example.shopease.data.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


data class HomeData(
    val products: List<Products> = emptyList(),
    val categories: List<Category> = emptyList()
)

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private val _homeDataState = MutableStateFlow<Resource<HomeData>>(Resource.Loading)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    val posters = listOf(
        "https://img.freepik.com/free-psd/landing-page-template-fashion-shopping-store_23-2148786817.jpg?t=st=1760121890~exp=1760125490~hmac=f1b8d7352b31df2e00767b1fc2c3b5f2580a8604f3c824f51aface692d6a0652&w=1480",
        "https://img.freepik.com/premium-psd/summer-sale-social-media-post-template_23-2149170774.jpg",
        "https://img.freepik.com/free-vector/flat-design-spring-instagram-stories-collection_23-2148822126.jpg?t=st=1760122056~exp=1760125656~hmac=b4efea69246f54e5d8148dd55508d294247868396b27fb169d696a99b2742ec8&w=1480",
        "https://img.freepik.com/free-vector/horizontal-banner-template-11-11-single-s-day-sales-event_23-2150884666.jpg?t=st=1760122133~exp=1760125733~hmac=fa92ddb5da600c9b14e9f903a6e6646f81e9ef010e30bb976e1e083f84caabe7&w=1480",
        "https://img.freepik.com/premium-vector/gradient-horizontal-banner-template-11-11-sale-event_23-2150841726.jpg?w=1480",
    )


    val filtersProducts = combine(
        flow = _homeDataState,
        flow2 = _searchQuery,
        flow3 = _selectedCategory
    ) { resource, query, category ->
        when (resource) {
            is Resource.Success -> {
                val products = resource.data.products
                val filterByCategory = if (category == null) products
                else {
                    products.filter { it.category.id == category.id }
                }
                val filterBySearch = if (query.isBlank()) filterByCategory
                else {
                    filterByCategory.filter {
                        it.title.contains(query, ignoreCase = true) ||
                                it.description.contains(query, ignoreCase = true)
                    }
                }
                Resource.Success(filterBySearch)
            }

            is Resource.Loading -> Resource.Loading
            is Resource.Error -> Resource.Error(resource.message)
        }
    }

    val categories = combine(_homeDataState){resource->
        when(resource.first()){
            is Resource.Success -> Resource.Success((resource.first() as Resource.Success<HomeData>).data.categories )
            is Resource.Loading -> Resource.Loading
            is Resource.Error -> Resource.Error( (resource.first() as Resource.Error).message )
        }
    }

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            _homeDataState.value = Resource.Loading
            try {
                val productDeferred = async { repository.getAllProduct() }
                val categoriesDeferred = async { repository.getAllCategory() }
                val products = productDeferred.await()
                val categories = categoriesDeferred.await()
                _homeDataState.value = Resource.Success(HomeData(products,categories))
            }catch (e: Exception){
                _homeDataState.value = Resource.Error(e.message?:"An unknown error occurred")
            }
        }
    }

    fun onSearchQueryChange(query:String){
        _searchQuery.value = query
    }

    fun onCategorySelected(category: Category?){
        if(_selectedCategory.value?.id == category?.id){
            _selectedCategory.value = null
        }else{
            _selectedCategory.value = category
        }
    }

}

 
class HomeViewModelFactory(private val repository: HomeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

