package com.beyable.sdkdemo.models;

/**
 * Created by Gol D. Marko on 12/02/2024.
 * <p>
 **/

public class CartItem {

    private String title;
    private Product product;
    private int quantity = 1;

    public CartItem(String title, Product product) {
        this.title      = title;
        this.product    = product;
    }

    public String getTitle() {
        return title;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
