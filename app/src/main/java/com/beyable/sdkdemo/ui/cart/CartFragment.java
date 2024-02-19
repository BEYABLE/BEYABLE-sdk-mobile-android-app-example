package com.beyable.sdkdemo.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.beyable.beyable_sdk.Beyable;
import com.beyable.beyable_sdk.models.BYCartAttributes;
import com.beyable.sdkdemo.R;
import com.beyable.sdkdemo.databinding.FragmentCartBinding;
import com.beyable.sdkdemo.models.CartItem;
import com.beyable.sdkdemo.tools.Requester;
import com.beyable.sdkdemo.utils.Cart;
import com.beyable.sdkdemo.utils.StringUtils;

public class CartFragment extends Fragment {

    protected final static String LOG_TAG = CartFragment.class.getSimpleName();

    private FragmentCartBinding binding;

    private RecyclerView recyclerView;
    private TextView totalView, subTotalView, discountView, shippingView;
    private double shipping = 3.99;

    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Init Views
        totalView       = binding.totalValue;
        subTotalView    = binding.subtotalValue;
        discountView    = binding.promoValue;
        shippingView    = binding.shippingValue;

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Set the recycler view with the data collected
        CartAdapter cartAdapter = new CartAdapter(new CartAdapter.OnCartChangeListener() {
            @Override
            public void onChange() {
                valueChanges();
            }
        });
        recyclerView.setAdapter(cartAdapter);
        // Set the values to views
        valueChanges();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // We call Beyable only after the view as been created
        sendPageViewToBeyable();
    }

    private void sendPageViewToBeyable() {
        BYCartAttributes cartAttributes = new BYCartAttributes();
        Beyable.getSharedInstance().sendPageView(getView(), "cart/", cartAttributes);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void valueChanges() {
        double subTotal = 0;
        for (int i=0; i<Cart.getSharedInstance().getCartItems().size(); i++) {
            subTotal += Cart.getSharedInstance().getCartItems().get(i).getProduct().getPrice() * Cart.getSharedInstance().getCartItems().get(i).getQuantity();
        }
        subTotalView.setText(StringUtils.doubleToPrice(subTotal, '€'));
        shippingView.setText(StringUtils.doubleToPrice(shipping, '€'));
        totalView.setText(StringUtils.doubleToPrice(subTotal+shipping, '€'));
    }

}


class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    interface OnCartChangeListener {
        void onChange();
    }
    private OnCartChangeListener callback;
    /**
     * by RecyclerView
     */
    public CartAdapter(OnCartChangeListener callback) {
        this.callback = callback;
    }

    // Create new views (invoked by the layout manager)
    @NonNull @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final int finalPosition = position;
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.setContent(Cart.getSharedInstance().getCartItems().get(position));
        viewHolder.setListeners(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCartItem(finalPosition);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuantity(finalPosition);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeQuantity(finalPosition);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return Cart.getSharedInstance().getCartItems().size();
    }

    public void removeCartItem(int position) {
        CartItem item = Cart.getSharedInstance().getCartItems().remove(position);
        notifyItemRemoved(position);
        Cart.getSharedInstance().removeItem(item);
        callback.onChange();
    }

    public void addQuantity(int position) {
        notifyItemChanged(position);
        Cart.getSharedInstance().addQuantity(Cart.getSharedInstance().getCartItems().get(position));
        callback.onChange();
    }

    public void removeQuantity(int position) {
        if (Cart.getSharedInstance().getCartItems().get(position).getQuantity() > 1) {
            Cart.getSharedInstance().removeQuantity(Cart.getSharedInstance().getCartItems().get(position));
            notifyItemChanged(position);
            callback.onChange();
        }
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final NetworkImageView imageView;
        private final View removeButton, addQtyButton, removeQtyButton;
        private final TextView priceView;
        private final TextView quantityView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            titleView       = view.findViewById(R.id.title_view);
            imageView       = view.findViewById(R.id.image_view);
            removeButton    = view.findViewById(R.id.remove_button);
            priceView       = view.findViewById(R.id.price_view);
            quantityView    = view.findViewById(R.id.quantity_view);
            addQtyButton    = view.findViewById(R.id.add_qty_button);
            removeQtyButton = view.findViewById(R.id.remove_qty_button);
        }

        public void setContent(CartItem cartItem) {
            titleView.setText(cartItem.getTitle());
            priceView.setText(StringUtils.doubleToPrice(cartItem.getProduct().getPrice(), '€'));
            quantityView.setText(String.format("%s", cartItem.getQuantity()));
            Requester.getSharedInstance(itemView.getContext()).setImageForNetworkImageView(
                    imageView,
                    cartItem.getProduct().getThumbnail(),
                    R.drawable.img_placeholder,
                    R.drawable.img_placeholder);
        }

        public void setListeners(View.OnClickListener removeProductListener, View.OnClickListener addQtyListener, View.OnClickListener removeQtyListener) {
            removeButton.setOnClickListener(removeProductListener);
            addQtyButton.setOnClickListener(addQtyListener);
            removeQtyButton.setOnClickListener(removeQtyListener);
        }
    }
}
