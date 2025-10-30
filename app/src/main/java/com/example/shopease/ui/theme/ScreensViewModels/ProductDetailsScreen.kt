package com.example.shopease.ui.theme.ScreensViewModels

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shopease.data.Apis.RetrofitInstance
import com.example.shopease.data.model.Products
import com.example.shopease.data.repository.ProductDetailsRepository
import kotlinx.coroutines.launch
import androidx.compose.ui.text.style.TextAlign
import com.example.shopease.data.model.Review
import com.example.shopease.data.model.fakeReviews
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    productId: Int,
    navController: NavController,
    favoritesViewModel: FavoritesViewModel,
    cartViewModel : CartViewModel
) {
    val api = remember { RetrofitInstance.api }
    val repository = remember { ProductDetailsRepository(api) }
    val viewModel = remember { ProductDetailsViewModel(repository) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val randomReview = remember { fakeReviews.shuffled().take(5) }

    LaunchedEffect(productId) {
        viewModel.loadProductDetails(productId)
    }

    val isLoading = viewModel.isLoading
    val product = viewModel.product

    Column (
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ){

        Row (
            modifier = Modifier.fillMaxWidth().height(56.dp).padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(
                onClick = {navController.popBackStack()}
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null )
            }
            Text(
                text = "${product?.title}",
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }

        Divider()

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 4.dp, end = 4.dp, bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (product != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(1.dp)
            ) {
                ProductImagePager(images = product.images)
                ProductHeader(product = product)
                SizeSelector(oldSize = viewModel.size, newSize = { viewModel.selectSize(it) })
                ActionButtons(
                    product = product,
                    favoritesViewModel = favoritesViewModel,
                    onAddToCartClick = {
                        viewModel.product?.let { product ->
                            cartViewModel.addToCart(product, viewModel.size)
                            scope.launch {
                                snackbarHostState.showSnackbar("Added to Cart")
                            }
                        }
                    },
                    onOrderClick = {
                        viewModel.orderNow {
                            scope.launch {
                                snackbarHostState.showSnackbar("Order Placed")
                            }
                        }
                    }
                )
                ProductDescription(product = product)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Reviews",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 18.dp)
                )

                randomReview.forEach { review ->
                    ReviewItem(review = review)
                }
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color(0xFFFDFDFD))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = review.username.firstOrNull()?.uppercase() ?: "?",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = review.username,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.Black
                        )
                    )
                }
                Text(
                    text = review.date,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )
                )
            }


            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(5) { index ->
                    Icon(
                        imageVector = if(index < review.rating) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Star",
                        tint = if (index < review.rating) Color(0xFFFFC107) else Color(0xFFBDBDBD)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${review.rating}/5",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text ="${review.review}",
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                color = Color.Black
            )
        }


    }
}


@Composable
private fun ActionButtons(
    product: Products,
    onAddToCartClick: () -> Unit,
    onOrderClick: () -> Unit,
    favoritesViewModel: FavoritesViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { favoritesViewModel.toggleFavorite(product) },
            modifier = Modifier
                .size(52.dp)
                .border(
                    1.dp,
                    if (favoritesViewModel.isFavorite(product))
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.outline,
                    CircleShape
                )
        ) {
            Icon(
                imageVector = if (favoritesViewModel.isFavorite(product))
                    Icons.Filled.Favorite
                else
                    Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite Icon",
                tint = Color.Blue
            )
        }

        Button(
            onClick = onAddToCartClick,
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
        ) {
            Text(
                "Add to card",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        Button(
            onClick = onOrderClick,
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text("Order Now", fontWeight = FontWeight.Bold, color = Color.Black)
        }

    }
}

@Composable
fun ProductDescription(product: Products) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "${product.description}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
private fun SizeSelector(oldSize: String, newSize: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Select Size",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("M", "L", "X", "XXL").forEach { size ->
                FilterChip(
                    selected = oldSize == size,
                    onClick = { newSize(size) },
                    label = {
                        Text(
                            size, fontWeight = if (oldSize == size) FontWeight.Bold else {
                                FontWeight.Normal
                            }
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color.Blue,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }
}

@Composable
private fun ProductHeader(product: Products) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "${product.title}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Blue,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.ExtraBold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Star, contentDescription = "Star", tint = Color(0xFFFFC107))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "4.5 (125 reviews)",
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ProductImagePager(images: List<String>) {
    val pagerState = rememberPagerState(pageCount = { images.size })

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .size(300.dp)
        ) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pagerState.pageCount) { index ->
                val color = if (pagerState.currentPage == index)
                    Color.Gray
                else {
                    Color.LightGray
                }
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}
