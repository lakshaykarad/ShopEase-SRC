@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
package com.example.shopease.ui.theme.ScreensViewModels


import android.R
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButtonDefaults.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.webauthn.Cbor
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.util.query
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.shopease.data.Apis.RetrofitInstance
import com.example.shopease.data.model.Category
import com.example.shopease.data.model.Products
import com.example.shopease.data.repository.HomeRepository
import com.example.shopease.data.util.Resource
import com.example.shopease.ui.theme.AuthViewModel.RegisterScreen
import com.google.firebase.ai.type.content
import kotlinx.coroutines.delay
import java.nio.file.WatchEvent
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController : NavController) {

    val repository = HomeRepository(RetrofitInstance.api)
    val factory = HomeViewModelFactory(repository)
    val viewModel: HomeViewModel = viewModel(factory = factory)

    val filteredProductsState by viewModel.filtersProducts.collectAsState(initial = Resource.Loading)
    val categoriesState by viewModel.categories.collectAsState(Resource.Loading)
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val posters = viewModel.posters


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF5F5F5))
            .navigationBarsPadding()
            .imePadding()
    ) {
        HomeTopAppBar(
            query = searchQuery,
            onQueryChange = viewModel::onSearchQueryChange
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item(span = { GridItemSpan(2) }) {
                Column {
                    PostersSection(posters = posters)
                    Spacer(modifier = Modifier.height(16.dp))
                    when (val result = categoriesState) {
                        is Resource.Success -> CategoriesSection(
                            categories = result.data,
                            selectedCategory = selectedCategory,
                            onCategorySelected = viewModel::onCategorySelected
                        )

                        is Resource.Loading -> CategoriesLoadingSkeleton()
                        is Resource.Error -> Text(
                            "Failed to load categories.",
                            fontWeight = FontWeight.Bold
                        )
                    }
                    SectionTitle(title = "New Arrivals")
                }
            }

            when (val result = filteredProductsState) {
                is Resource.Loading -> items(4) { ProductCardShimmer() }
                is Resource.Error -> item(span = {GridItemSpan(2)}) {
                    Text(
                        "Error loading products.",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth().padding(40.dp),
                        textAlign = TextAlign.Center
                    )
                }

                is Resource.Success -> {
                    if (result.data.isEmpty()) {
                        item(span = { GridItemSpan((2)) }) {
                            Text(
                                text = "No products found",
                                modifier = Modifier
                                    .padding(60.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        items(result.data) { product ->
                            ProductCard(product = product, onClick = { navController.navigate("productDetails/${product.id}") })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Products, onClick: () -> Unit) {

    Card (
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth(),
    ){
        Column {
            AsyncImage(
                model = product.images.firstOrNull(),
                contentDescription = product.description,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f)
            )
            Column (modifier = Modifier.padding(8.dp)){
                Text(
                    text = product.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${product.price}",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}
@Composable
fun ProductCardShimmer() {
    Card(
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .simpleShimmer()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(14.dp)
                    .simpleShimmer()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(16.dp)
                    .simpleShimmer()
            )
        }
    }
}

@Composable
fun CategoriesLoadingSkeleton() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(5) {
            Box(
                modifier = Modifier
                    .height(32.dp)
                    .width(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .simpleShimmer()
            )
        }
    }
}

fun Modifier.simpleShimmer() = composed {
    // size, transition, move, colors

    var size by remember { mutableStateOf(IntSize.Zero) }

    val transition = rememberInfiniteTransition()
    val move by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(1000))
    )

    val colors = listOf(
        Color.LightGray.copy(alpha = 0.9f),
        Color.Gray.copy(alpha = 0.3f),
        Color.LightGray.copy(alpha = 0.9f)
    )

    background(
        brush = Brush.linearGradient(
            colors = colors,
            start = Offset(x = move, y = 0f),
            end = Offset(x = move + size.width, y = size.height.toFloat())
        )
    ).onGloballyPositioned { layout ->
        size = layout.size
    }

}

@Composable
fun CategoriesSection(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit
) {

    Column {
        SectionTitle(title = "All Products")
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            item {
                val isSelected = selectedCategory == null
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelected(null) },
                    label = { Text("All", fontWeight = FontWeight.SemiBold) },
                    leadingIcon = if (isSelected) {
                        { Icon(Icons.Default.Done, contentDescription = null) }
                    } else null
                )
            }
            items(categories) { categories ->
                val isSelected = categories == selectedCategory
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelected(categories) },
                    label = { Text(categories.name, fontWeight = FontWeight.SemiBold) },
                    leadingIcon = if (isSelected) {
                        { Icon(Icons.Default.Done, contentDescription = null) }
                    } else null
                )
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(12.dp, 8.dp)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostersSection(posters: List<String>) {

    val pagerState = rememberPagerState(pageCount = { posters.size })

    LaunchedEffect(pagerState.pageCount) {
        while (true) {
            delay(1500)
            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp),
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            AsyncImage(
                model = posters[page],
                contentDescription = "Poster Description",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(pagerState.pageCount) { index ->
                val color = if (pagerState.currentPage == index)
                    MaterialTheme.colorScheme.primary
                else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                }
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class) // For TextFieldDefaults
@Composable
fun HomeTopAppBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Column(
        // Add padding to the whole block
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5)) // Match your screen background
            .padding(16.dp)
    ) {
        // 1. This is now just a Text composable, not a TopAppBar
        Text(
            text = "ShopEase",
            style = MaterialTheme.typography.headlineSmall, // Or titleLarge
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp) // Adjust spacing
        )

        // 2. This is your existing TextField
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text(text = "Search Products") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
    }
}

