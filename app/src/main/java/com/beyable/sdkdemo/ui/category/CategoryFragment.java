package com.beyable.sdkdemo.ui.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.beyable.sdkdemo.R;
import com.beyable.sdkdemo.databinding.FragmentCategoryBinding;
import com.beyable.sdkdemo.models.Category;
import com.beyable.sdkdemo.models.Product;
import com.beyable.sdkdemo.tools.Requester;
import com.beyable.sdkdemo.ui.product.ProductActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryFragment extends Fragment {

    protected final static String LOG_TAG = CategoryFragment.class.getSimpleName();

    private FragmentCategoryBinding binding;

    private Category category;
    private View progressBar;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get the data from activity
        category = (Category) getActivity().getIntent().getSerializableExtra(CategoryActivity.CATEGORY_INTENT_KEY);

        // Init Views
        progressBar = binding.progressBar;
        recyclerView = binding.recyclerViewCategory;
        binding.categoryTitleView.setText(category.getTitle());


        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Make request to get all the categories
        progressBar.setVisibility(View.VISIBLE);
        String endpoint = Requester.CATEGORY_PAGE + category.getCategory();
        Requester.getSharedInstance(root.getContext()).makeObjGetRequest(endpoint,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onRequestDone(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        onRequestError(error.getMessage());
                    }
                }
        );


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onRequestDone(JSONObject result) {
        // Prepare a executor to parse the data in background
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                //Background work here
                ArrayList<Product> dataSet = new ArrayList<>();
                JSONArray productsArray = result.optJSONArray("products");
                if (productsArray != null) {
                    for (int i=0; i < productsArray.length(); i++) {
                        dataSet.add(new Product(productsArray.optJSONObject(i)));
                    }
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Set the recycler view with the data collected
                        CategoryAdapter categoriesAdapter = new CategoryAdapter(dataSet);
                        recyclerView.setAdapter(categoriesAdapter);
                        // Hide the progress view
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void onRequestError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.GONE);
    }

}


class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private ArrayList<Product> dataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final NetworkImageView networkImageView;
        private final TextView titleView;

        public ViewHolder(View view) {
            super(view);
            networkImageView = view.findViewById(R.id.network_image_view);
            titleView = view.findViewById(R.id.title_text_view);
        }

        public void setContent(Product product) {
            titleView.setText(product.getTitle());
            Requester.getSharedInstance(itemView.getContext()).setImageForNetworkImageView(
                    networkImageView,
                    product.getThumbnail(),
                    R.drawable.ic_dashboard_black_24dp,
                    R.drawable.ic_notifications_black_24dp);
        }

    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet ArrayList containing the data to populate views to be used
     * by RecyclerView
     */
    public CategoryAdapter(ArrayList<Product> dataSet) {
        this.dataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NonNull @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.setContent(dataSet.get(position));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productClicked(view, viewHolder.getAdapterPosition());
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void productClicked(View view, int position) {
        // Launch the product activity
        Context context = view.getContext();
        Product product = dataSet.get(position);
        Intent intent = new Intent(context, ProductActivity.class);
        intent.putExtra(ProductActivity.PRODUCT_INTENT_KEY, product);
        context.startActivity(intent);
    }
}
