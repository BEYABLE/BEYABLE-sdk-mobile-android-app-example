package com.beyable.sdkdemo.ui.products;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beyable.beyable_sdk.Beyable;
import com.beyable.beyable_sdk.models.BYPage;
import com.beyable.sdkdemo.R;
import com.beyable.sdkdemo.databinding.FragmentCategoriesBinding;
import com.beyable.sdkdemo.models.Category;
import com.beyable.sdkdemo.tools.Requester;
import com.beyable.sdkdemo.ui.categories.CategoriesViewModel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductsFragment extends Fragment {

    protected final static String LOG_TAG = ProductsFragment.class.getSimpleName();

    private FragmentCategoriesBinding binding;

    private View progressBar;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        CategoriesViewModel categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);

        binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Init Views
        progressBar = binding.progressBar;
        recyclerView = binding.recyclerViewCategories;
        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));


        // Make request to get all the categories
        progressBar.setVisibility(View.VISIBLE);
        Requester.getSharedInstance(root.getContext()).makeArrayGetRequest(
                Requester.CATEGORIES_PAGE,
                new JSONArray(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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

    private void onRequestDone(JSONArray result) {
        // Prepare a executor to parse the data in background
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                //Background work here
                ArrayList<Category> dataSet = new ArrayList<>();
                for (int i=0; i < result.length(); i++) {
                    dataSet.add(new Category(result.optString(i)));
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Set the recycler view with the data collected
                        ProductsAdapter categoriesAdapter = new ProductsAdapter(dataSet);
                        recyclerView.setAdapter(categoriesAdapter);
                        // Hide the progress view
                        progressBar.setVisibility(View.GONE);
                        // Call the BEYABLE sdk
                        callBeyableSDK();
                    }
                });
            }
        });
    }

    private void onRequestError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.GONE);
    }

    private void callBeyableSDK() {
        // CALL Beyable SDK to inform that we are viewing a page
        try {
            Beyable.getSharedInstance().sendPageView(getView(), new BYPage(
                    BYPage.BYPageType.CATEGORY,
                    "https://dummy_app.com",
                    "categories/"
            ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private ArrayList<Category> dataSet;

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

        public void setContent(Category category) {
            titleView.setText(category.getTitle());
        }

    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet ArrayList containing the data to populate views to be used
     * by RecyclerView
     */
    public ProductsAdapter(ArrayList<Category> dataSet) {
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
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClicked(view, viewHolder.getAdapterPosition());
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void categoryClicked(View view, int position) {

    }
}
