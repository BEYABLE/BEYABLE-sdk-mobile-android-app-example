package com.beyable.sdkdemo.utils;

import com.beyable.sdkdemo.models.Product;

import java.util.ArrayList;

/**
 * Created by Gol D. Marko on 09/02/2024.
 * <p>
 **/

public class Cart {

    public static Cart instance;

    public static Cart getSharedInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    private ArrayList<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        // Check if product is already there
        for (int i=0; i< products.size(); i++) {
            if (product.getId().equals(products.get(i).getId())) {
                // Update quantity
                //products.get(i).
            }
        }
        products.add(product);
    }
}
