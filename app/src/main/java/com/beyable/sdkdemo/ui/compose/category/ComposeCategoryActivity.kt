package com.beyable.sdkdemo.ui.compose.category

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beyable.beyable_sdk.Beyable
import com.beyable.beyable_sdk.Beyable.OnSendPageView
import com.beyable.beyable_sdk.models.BYCategoryAttributes
import com.beyable.beyable_sdk.ui.compose.BYInCollectionPlaceHolder
import com.beyable.sdkdemo.models.Category
import com.beyable.sdkdemo.models.Product
import com.beyable.sdkdemo.tools.Requester
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

/**
 *
 * Created by MarKinho on 01/08/2024.
 *
 * Wisepear Techlab
 * All rights reserved
 *
 **/

class ComposeCategoryActivity : AppCompatActivity() {

    companion object {
        const val CATEGORY_INTENT_KEY = "category_activity.category"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val category = intent.getSerializableExtra(CATEGORY_INTENT_KEY) as? Category
        // Set Action bar
        // Set Action bar
        if (supportActionBar != null && category != null) {
            supportActionBar!!.title = category.title
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        setContent {
            // Display the CategoryScreen composable
            category?.let {
                CategoryScreen(category = it)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}


@Composable
fun CategoryScreen(category: Category, viewModel: CategoryViewModel = remember { CategoryViewModel(category) }) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    // Display a loading indicator while data is being fetched
    if (state.isLoading) {
        CircularProgressIndicator()
    }
    // Display products
    LazyColumn {
        items(state.products) { product ->
            ProductRow(product = product) {
                // Handle product click
                // Example: navigate to ProductDetail screen
            }
        }
    }

    LaunchedEffect(category) {

        val attributes = BYCategoryAttributes()
        attributes.name = category.title
        attributes.setTags(arrayOf(category.category))
        try {
            attributes.contextData = JSONObject()
                .put("magasin", "Carrefour Aulnay-sous-Bois")
        } catch (e: JSONException) {
        }
        Beyable.getSharedInstance().sendPageView(
            context,
            "xml_category/" + category.category,
            attributes,
            object : OnSendPageView {
                override fun onResponse() {
                    viewModel.loadCategoryData(context)
                }

                override fun onError() {
                    viewModel.onRequestError("BEYABLE ERROR")
                }
            })
    }
}

@Composable
fun ProductRow(product: Product, onClick: () -> Unit) {
    // Example UI for a product row
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardColors(contentColor = Color.Transparent, containerColor = Color.Transparent, disabledContainerColor = Color.Transparent, disabledContentColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image
            GlideImage(
                imageModel = { product.thumbnail },
                modifier = Modifier
                    .size(120.dp) // Fixed size for the image
                    .clip(RoundedCornerShape(8.dp)), //
            )

            // Text content
            Column(
                modifier = Modifier
                    .padding(start = 8.dp) // Space between image and text
                    .weight(1f) // Allow the text to take the remaining space
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.bodyLarge, // Larger font size for the title
                    maxLines = 1, // Ellipsis if title is too long
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )
                BYInCollectionPlaceHolder(placeHolderId = "product_content", elementId = product.title, callback = { elementId, placeHolderId ->

                })
                Text(
                    text = product.category,
                    style = MaterialTheme.typography.bodyMedium, // Medium font size for the category
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), // Slightly muted color
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$${product.price}", // Adding a dollar sign for the price
                    style = MaterialTheme.typography.bodyLarge, // Smaller font size for the price
                    color = MaterialTheme.colorScheme.primary // Highlighting the price
                )
                BYInCollectionPlaceHolder(placeHolderId = "product_price", elementId = product.title, callback = { elementId, placeHolderId ->

                })
            }
        }
    }
}

class CategoryViewModel(private val category: Category) : ViewModel() {
    private val _state = MutableStateFlow(CategoryState(isLoading = true, products = emptyList()))
    val state: StateFlow<CategoryState> = _state


    fun loadCategoryData(context: Context) {
        // Simulating network request
        viewModelScope.launch {
            try {
                // Replace with actual network request
                val endpoint = Requester.CATEGORY_PAGE + category.category;
                Requester.getSharedInstance(context).makeObjGetRequest(endpoint,
                    JSONObject(),
                    { response -> onRequestDone(response!!) }
                ) { error ->
                    error.printStackTrace()
                    onRequestError(error.message!!)
                }
            } catch (e: Exception) {
                onRequestError(e.message ?: "Unknown error")
            }
        }
    }

    private fun onRequestDone(result: JSONObject) {
        val dataSet = mutableListOf<Product>()
        val productsArray = result.optJSONArray("products")
        productsArray?.let {
            for (i in 0 until it.length()) {
                val productJson = it.optJSONObject(i)
                dataSet.add(Product(productJson))
            }
        }
        _state.value = CategoryState(isLoading = false, products = dataSet)
    }

    fun onRequestError(errorMessage: String) {
        //Toast.makeText(LocalContext., errorMessage, Toast.LENGTH_LONG).show()
        _state.value = _state.value.copy(isLoading = false)
    }
}

data class CategoryState(
    val isLoading: Boolean,
    val products: List<Product>
)