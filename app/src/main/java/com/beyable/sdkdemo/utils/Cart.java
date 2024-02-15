package com.beyable.sdkdemo.utils;

import android.content.Context;

import com.beyable.beyable_sdk.Beyable;
import com.beyable.sdkdemo.models.CartItem;
import com.beyable.sdkdemo.models.Product;

import java.util.ArrayList;

/**
 * Created by Gol D. Marko on 09/02/2024.
 * <p>
 **/

public class Cart {

    private static Cart instance;

    public static Cart getSharedInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    private ArrayList<CartItem> cartItems = new ArrayList<>();

    public void addProduct(Product product) {
        // Check if product is already there
        for (int i=0; i< cartItems.size(); i++) {
            if (product.getId().equals(cartItems.get(i).getProduct().getId())) {
                // Update quantity
                cartItems.get(i).setQuantity(cartItems.get(i).getQuantity()+1);
                return;
            }
        }
        // Not found on items, add new one
        cartItems.add(new CartItem(product.getTitle(), product));
    }

    public void removeItem(CartItem cartItem) {
        for (int i=0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getProduct().getId().equals(cartItem.getProduct().getId())) {
                cartItems.remove(i);
                // Prevent Beyable SDK
                Beyable.getSharedInstance().removeAllItemsFormCart(cartItem.getProduct().getId());
                return;
            }
        }
    }

    public void addQuantity(CartItem cartItem) {
        for (int i=0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getProduct().getId().equals(cartItem.getProduct().getId())) {
                cartItems.get(i).setQuantity(cartItems.get(i).getQuantity()+1);
                // Prevent Beyable SDK
                Beyable.getSharedInstance().setItemCartQuantity(
                        cartItems.get(i).getProduct().getId(),
                        cartItems.get(i).getQuantity());
                return;
            }
        }
    }

    public void removeQuantity(CartItem cartItem) {
        for (int i=0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getProduct().getId().equals(cartItem.getProduct().getId())) {
                if (cartItems.get(i).getQuantity() > 1) {
                    cartItems.get(i).setQuantity(cartItems.get(i).getQuantity()-1);
                    // Prevent Beyable SDK
                    Beyable.getSharedInstance().setItemCartQuantity(
                            cartItems.get(i).getProduct().getId(),
                            cartItems.get(i).getQuantity());
                }
                return;
            }
        }
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    public void eraseData(Context context) {

    }
}
