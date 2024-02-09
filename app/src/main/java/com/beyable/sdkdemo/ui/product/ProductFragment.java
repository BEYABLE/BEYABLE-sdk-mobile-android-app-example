package com.beyable.sdkdemo.ui.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.beyable.sdkdemo.R;
import com.beyable.sdkdemo.databinding.FragmentProductBinding;
import com.beyable.sdkdemo.models.Product;
import com.beyable.sdkdemo.ui.adapters.CarouselImageAdapter;
import com.beyable.sdkdemo.utils.Cart;

import java.util.ArrayList;

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
        binding.priceView.setText(Double.toString(product.getPrice())+"€");
        binding.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart.getSharedInstance().addProduct(product);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void setCarouselView() {
        CarouselImageAdapter adapter = new CarouselImageAdapter(getContext(), product.getImages());
        carouselView.setAdapter(adapter);
    }

}


class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private ArrayList<Object> dataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            titleView = (TextView) view.findViewById(R.id.titleView);
        }

        public void setContent(Object object) {
            if (object instanceof Product) {

            }
        }

    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet ArrayList containing the data to populate views to be used
     * by RecyclerView
     */
    public ProductAdapter(ArrayList<Object> dataSet) {
        this.dataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NonNull @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.setContent(dataSet.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
