package com.beyable.sdkdemo.ui.compose.categories

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.android.volley.Response
import com.android.volley.VolleyError
import com.beyable.beyable_sdk.Beyable
import com.beyable.beyable_sdk.models.BYCategoryAttributes
import com.beyable.sdkdemo.R
import com.beyable.sdkdemo.models.Category
import com.beyable.sdkdemo.tools.Requester
import com.beyable.sdkdemo.ui.compose.category.ComposeCategoryActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONArray
import org.json.JSONException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 *
 * Created by MarKinho on 01/08/2024.
 *
 * Wisepear Techlab
 * All rights reserved
 *
 **/


@Composable
fun CategoriesScreen(viewModel: CategoriesViewModel) {

    val context = LocalContext.current
    val viewModel: CategoriesViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(CategoriesViewModel::class.java)
    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        // CALL Beyable SDK to inform that we are viewing the page
        val attributes = BYCategoryAttributes()
        Beyable.getSharedInstance().sendPageView(context, "compose_categories/", attributes)
        viewModel.loadCategories(context)
    }

    Scaffold(
        topBar = {
            Text(
                text = "Jetpack Compose",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start   = dimensionResource(id = R.dimen.padding_big),
                        end     = dimensionResource(id = R.dimen.padding_big),
                        top     = dimensionResource(id = R.dimen.padding_xxbig),
                        bottom  = dimensionResource(id = R.dimen.padding_big)
                    )
            )
        },
        content = { padding ->
            Box(modifier = Modifier
                .fillMaxSize() // Pour remplir toute la taille de l'écran
                .padding(padding)
            ) {
                if (state.value.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(state.value.categories) { category ->
                            CategoryRow(category) {
                                viewModel.onCategoryClicked(context, category)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun CategoryRow(category: Category, onClick: () -> Unit) {
    Card(
        elevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Text(
            text = category.title,
            fontSize = 22.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}

data class CategoriesState(
    public val isLoading: Boolean = true,
    public val categories: List<Category> = emptyList()
)

class CategoriesViewModel : ViewModel() {

    companion object {
        private const val LOG_TAG = "ComposeCategoriesVM"
    }

    private val _state = MutableStateFlow(CategoriesState())
    public val state: StateFlow<CategoriesState> = _state

    fun loadCategories(context: Context) {
        _state.value = _state.value.copy(isLoading = true)
        Requester.getSharedInstance(context).makeArrayGetRequest(
            Requester.CATEGORIES_PAGE,
            JSONArray(),
            { response ->
                onRequestDone(response)
            },
            { error ->
                onRequestError(context, error)
            }
        )
    }

    private fun onRequestDone(result: JSONArray) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        try {
            Log.d(LOG_TAG, result.toString(4))
        } catch (e: JSONException) {
            throw RuntimeException(e)
        }

        executor.execute {
            val dataSet = ArrayList<Category>()
            for (i in 0 until result.length()) {
                dataSet.add(Category(result.optJSONObject(i)))
            }

            handler.post {
                _state.value = CategoriesState(isLoading = false, categories = dataSet)
            }
        }
    }

    private fun onRequestError(context: Context, error: VolleyError) {
        Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
        _state.value = _state.value.copy(isLoading = false)
    }

    fun onCategoryClicked(context: Context, category: Category) {
        val intent = Intent(context, ComposeCategoryActivity::class.java)
        intent.putExtra(ComposeCategoryActivity.CATEGORY_INTENT_KEY, category)
        context.startActivity(intent)
    }

}