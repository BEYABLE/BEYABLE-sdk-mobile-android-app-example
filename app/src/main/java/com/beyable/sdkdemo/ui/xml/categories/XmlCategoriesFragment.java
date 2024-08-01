package com.beyable.sdkdemo.ui.xml.categories;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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
import com.beyable.beyable_sdk.models.BYCategoryAttributes;
import com.beyable.sdkdemo.R;
import com.beyable.sdkdemo.databinding.FragmentCategoriesBinding;
import com.beyable.sdkdemo.models.Category;
import com.beyable.sdkdemo.tools.Requester;
import com.beyable.sdkdemo.ui.xml.category.XmlCategoryActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class XmlCategoriesFragment extends Fragment {

    protected final static String LOG_TAG = XmlCategoriesFragment.class.getSimpleName();

    private FragmentCategoriesBinding binding;

    private View progressBar;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        XmlCategoriesViewModel categoriesViewModel = new ViewModelProvider(this).get(XmlCategoriesViewModel.class);

        binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Init Views
        progressBar = binding.progressBar;
        recyclerView = binding.recyclerViewCategories;

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

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

        try {
            Log.d(LOG_TAG, result.toString(4));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //Background work here
                ArrayList<Category> dataSet = new ArrayList<>();
                for (int i=0; i < result.length(); i++) {
                    dataSet.add(new Category(result.optJSONObject(i)));
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Set the recycler view with the data collected
                        XmlCategoriesAdapter categoriesAdapter = new XmlCategoriesAdapter(dataSet);
                        recyclerView.setAdapter(categoriesAdapter);
                        // Hide the progress view
                        progressBar.setVisibility(View.GONE);
                        sendPageViewToBeyable(getView());
                    }
                });
            }
        });
    }

    private void onRequestError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.GONE);
    }


    private void sendPageViewToBeyable(View view) {
        // CALL Beyable SDK to inform that we are viewing the home page
        BYCategoryAttributes attributes = new BYCategoryAttributes();
        Beyable.getSharedInstance().sendPageView(view, "xml_categories/", attributes);
    }
}


class XmlCategoriesAdapter extends RecyclerView.Adapter<XmlCategoriesAdapter.ViewHolder> {

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
            titleView = (TextView) view.findViewById(R.id.title);
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
    public XmlCategoriesAdapter(ArrayList<Category> dataSet) {
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

        // Send viewholder to Beyable to be handled
        Beyable.getSharedInstance().onBindingViewHolder(viewHolder, dataSet.get(position).getTitle());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void categoryClicked(View view, int position) {
        // Launch the category activity
        Context context = view.getContext();
        Category category = dataSet.get(position);
        Intent intent = new Intent(context, XmlCategoryActivity.class);
        intent.putExtra(XmlCategoryActivity.CATEGORY_INTENT_KEY, category);
        context.startActivity(intent);
    }
}
