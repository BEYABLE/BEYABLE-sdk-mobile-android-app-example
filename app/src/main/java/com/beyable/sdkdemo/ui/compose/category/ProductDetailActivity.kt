package com.beyable.sdkdemo.ui.compose.category

/**
 *
 * Created by MarKinho on 08/08/2024.
 *
 * Wisepear Techlab
 * All rights reserved
 *
 **/

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.beyable.beyable_sdk.Beyable
import kotlin.math.max
import kotlin.math.min

import com.beyable.beyable_sdk.models.BYProductAttributes
import com.beyable.beyable_sdk.ui.compose.BYInPagePlaceHolder
import com.beyable.sdkdemo.models.Category
import com.beyable.sdkdemo.models.Product
import com.beyable.sdkdemo.utils.JetsnackTheme
import com.beyable.sdkdemo.utils.LightColorPalette
import com.beyable.sdkdemo.utils.ProvideJetsnackColors
import com.beyable.sdkdemo.utils.Shapes
import com.beyable.sdkdemo.utils.debugColors
import com.beyable.sdkdemo.utils.formatPrice
import com.skydoves.landscapist.glide.GlideImage

private val BottomBarHeight = 56.dp
private val TitleHeight = 128.dp
private val GradientScroll = 180.dp
private val ImageOverlap = 115.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val ExpandedImageSize = 300.dp
private val CollapsedImageSize = 150.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)

class ProductDetailActivity : AppCompatActivity() {

    companion object {
        const val PRODUCT_INTENT_KEY = "product_activity.product"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val product = intent.getSerializableExtra(PRODUCT_INTENT_KEY) as? Product
        // Set Action bar
        // Set Action bar
        if (supportActionBar != null && product != null) {
            supportActionBar!!.title = product.title
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        setContent {
            // Display the CategoryScreen composable
            product?.let {
                val colors = LightColorPalette

                ProvideJetsnackColors(colors) {
                    MaterialTheme(
                        colors = debugColors(false),
                        shapes = Shapes,
                        content = { ProductDetail(product=product) }
                    )
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}




@Composable
fun ProductDetail(
    product: Product ) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        // CALL Beyable SDK to inform that we are viewing the page
        // This need only to be calculated 1 time
        val attributes = BYProductAttributes(
            product.id.toString(),
            product.title,
            "product_detail/\$(product.title)",
            product.price.toDouble(),
            product.price.toDouble(),
            "",
            1.0,
            arrayOf<String>()
        )

        val path = "product_detail/$(product.title)"
        // Send to beyable
        Beyable.getSharedInstance().sendPageView(context, path, attributes)
    }

    Box(Modifier.fillMaxSize()) {
        val scroll = rememberScrollState(0)
        Header()
        Body(scroll)
        Title(product) { scroll.value }

        Image(product.thumbnail) { scroll.value }
        Up()
        CartBottomBar(modifier = Modifier.align(Alignment.BottomCenter))
    }
}


@Composable
private fun Header() {
    Spacer(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .background(Brush.horizontalGradient(JetsnackTheme.colors.tornado1))
    )
}

@Composable
private fun Up() {
    val activity = (LocalContext.current as? Activity)
    IconButton(
        onClick = {
            if (activity != null) {
                activity.onBackPressed()
            } // Simule le comportement du bouton de retour
        },
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .size(36.dp)
            .background(
                color = JetsnackTheme.colors.uiBackground,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = mirroringBackIcon(),
            tint = JetsnackTheme.colors.iconInteractive,
            contentDescription = "back"
        )
    }
}

@Composable
private fun Body(scroll: ScrollState) {
    JetsnackSurface(Modifier.fillMaxWidth()) {
        var seeMore by remember { mutableStateOf(true) }
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {

            item {
                Spacer(Modifier.height(ImageOverlap))
            }
            item {
                Spacer(Modifier.height(TitleHeight))
            }
            item {
                Spacer(Modifier.height(16.dp))
            }
            item {
                Text(
                    text = "Details",
                    style = MaterialTheme.typography.overline,
                    color = JetsnackTheme.colors.textHelp,
                    modifier = HzPadding
                )
            }
            item {
                BYInPagePlaceHolder(path="product_detail/$(product.title)", placeHolderId = "carousel_container")
            }
            item {
                Spacer(Modifier.height(16.dp))
            }

            item {
                Text(
                    text = "Pa",
                    style = MaterialTheme.typography.body1,
                    color = JetsnackTheme.colors.textHelp,
                    maxLines = if (seeMore) 5 else Int.MAX_VALUE,
                    overflow = TextOverflow.Ellipsis,
                    modifier = HzPadding
                )
            }
            item {
                val textButton = if (seeMore) {
                    "Voir plus"
                } else {
                    "Voir moins"
                }
                Text(
                    text = textButton,
                    style = MaterialTheme.typography.button,
                    textAlign = TextAlign.Center,
                    color = JetsnackTheme.colors.textLink,
                    modifier = Modifier
                        .heightIn(20.dp)
                        .fillMaxWidth()
                        .padding(top = 15.dp)
                        .clickable {
                            seeMore = !seeMore
                        }
                )
            }

            item {
                Spacer(Modifier.height(40.dp))
            }
            item {
                Text(
                    text = "Ingrédients",
                    style = MaterialTheme.typography.overline,
                    color = JetsnackTheme.colors.textHelp,
                    modifier = HzPadding
                )
            }

            item {
                Spacer(Modifier.height(4.dp))
            }

            item {
                Text(
                    text ="asdsakdn",
                    style = MaterialTheme.typography.body1,
                    color = JetsnackTheme.colors.textHelp,
                    modifier = HzPadding
                )
            }

            item {
                Spacer(Modifier.height(16.dp))
            }

            item {
                Spacer(
                    modifier = Modifier
                        .padding(bottom = BottomBarHeight)
                        .navigationBarsPadding()
                        .height(8.dp)
                )
            }
        }
    }
}

@Composable
private fun Title(product: Product, scrollProvider: () -> Int) {
    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .heightIn(min = TitleHeight)
            .statusBarsPadding()
            .offset {
                val scroll = scrollProvider()
                val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
                IntOffset(x = 0, y = offset.toInt())
            }
            .background(color = JetsnackTheme.colors.uiBackground)
    ) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = product.title,
            style = MaterialTheme.typography.h4,
            color = JetsnackTheme.colors.textSecondary,
            modifier = HzPadding
        )
        Text(
            text = product.description,
            style = MaterialTheme.typography.subtitle2,
            fontSize = 20.sp,
            color = JetsnackTheme.colors.textHelp,
            modifier = HzPadding
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = formatPrice(product.price),
            style = MaterialTheme.typography.h6,
            color = JetsnackTheme.colors.textPrimary,
            modifier = HzPadding
        )

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun Image(
    imageUrl: String,
    scrollProvider: () -> Int) {
    val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
    val collapseFractionProvider = {
        (scrollProvider() / collapseRange).coerceIn(0f, 1f)
    }

    CollapsingImageLayout(
        collapseFractionProvider = collapseFractionProvider,
        modifier = HzPadding.statusBarsPadding()
    ) {

        GlideImage(
            imageModel = { imageUrl },
            modifier = Modifier
                .fillMaxSize()
                .height(120.dp) // Fixed size for the image
                .clip(RoundedCornerShape(8.dp)), //
        )
    }
}

@Composable
private fun CollapsingImageLayout(
    collapseFractionProvider: () -> Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 1)

        val collapseFraction = collapseFractionProvider()

        val imageMaxSize = min(ExpandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = max(CollapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)
        val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth))

        val imageY = lerp(MinTitleOffset, MinImageOffset, collapseFraction).roundToPx()
        val imageX = lerp(
            (constraints.maxWidth - imageWidth) / 2, // centered when expanded
            constraints.maxWidth - imageWidth, // right aligned when collapsed
            collapseFraction
        )
        layout(
            width = constraints.maxWidth,
            height = imageY + imageWidth
        ) {
            imagePlaceable.placeRelative(imageX, imageY)
        }
    }
}

@Composable
private fun CartBottomBar(modifier: Modifier = Modifier) {
    val (count, updateCount) = remember { mutableStateOf(1) }
    JetsnackSurface(modifier) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .navigationBarsPadding()
                    .then(HzPadding)
                    .heightIn(min = BottomBarHeight)
            ) {
                Spacer(Modifier.width(16.dp))
                Text(
                    text = "Ajouter au panier",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                Spacer(Modifier.width(16.dp))
            }
        }
    }
}

@Composable
fun mirroringIcon(ltrIcon: ImageVector, rtlIcon: ImageVector): ImageVector =
    if (LocalLayoutDirection.current == LayoutDirection.Ltr) ltrIcon else rtlIcon

@Composable
fun mirroringBackIcon() = mirroringIcon(
    ltrIcon = Icons.Outlined.ArrowBack, rtlIcon = Icons.Outlined.ArrowForward
)

