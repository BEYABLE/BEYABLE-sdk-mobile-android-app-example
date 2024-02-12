package com.beyable.sdkdemo.ui.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.beyable.beyable_sdk.Beyable;
import com.beyable.beyable_sdk.models.BYCartItem;
import com.beyable.beyable_sdk.models.BYProductAttributes;
import com.beyable.sdkdemo.databinding.FragmentProductBinding;
import com.beyable.sdkdemo.models.Product;
import com.beyable.sdkdemo.ui.adapters.CarouselImageAdapter;
import com.beyable.sdkdemo.utils.Cart;
import com.beyable.sdkdemo.utils.StringUtils;

public class ProductFragment extends Fragment {

    protected final static String LOG_TAG = ProductFragment.class.getSimpleName();

    private FragmentProductBinding binding;

    private Product product;
    private RecyclerView carouselView;
    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get the data from activity
        product = (Product) getActivity().getIntent().getSerializableExtra(ProductActivity.PRODUCT_INTENT_KEY);
        // Init Views
        carouselView = binding.carouselView;
        setCarouselView();
        binding.titleView.setText(product.getTitle());
        binding.descriptionView.setText(product.getDescription());
        binding.priceView.setText(StringUtils.doubleToPrice(product.getPrice(), '€'));
        binding.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add to cart instance
                Cart.getSharedInstance().addProduct(product);
                // Prevent Beyable
                sendCartItemToBeyable();
                Toast.makeText(getContext(), "Produit ajouter au panier", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // We call Beyable only after the view as been created
        sendPageViewToBeyable();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void sendPageViewToBeyable() {
        // CALL Beyable SDK to inform that we are viewing a product page
        BYProductAttributes attributes = new BYProductAttributes();
        attributes.setReference(product.getId());
        attributes.setName(product.getTitle());
        attributes.setStock(product.getStock());
        attributes.setSellingPrice(product.getPrice());
        attributes.setThumbnailUrl(product.getThumbnail());
        attributes.setPriceBeforeDiscount(product.getDiscountPercentage());
        attributes.setTags(new String[]{
                product.getCategory(),
                product.getBrand()
        });
        Beyable.getSharedInstance().sendPageView(getView(), "product/"+product.getTitle(), attributes);
    }

    private void sendCartItemToBeyable() {
        BYCartItem item = new BYCartItem(
                product.getId(),
                product.getTitle(),
                "product/"+product.getTitle(),
                product.getPrice(),
                1,
                product.getThumbnail(),
                new String[]{product.getCategory(), product.getBrand()}
        );
        Beyable.getSharedInstance().addItemToCart(item);
    }


    private void setCarouselView() {
        CarouselImageAdapter adapter = new CarouselImageAdapter(getContext(), product.getImages());
        carouselView.setAdapter(adapter);
    }


}
