package com.beyable.sdkdemo.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

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

    public CartItem(JSONObject data) {
        this.title      = data.optString("title");
        this.product    = new Product(Objects.requireNonNull(data.optJSONObject("product")));
        this.quantity   = data.optInt("quantity");
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

    public JSONObject toJSONObject() throws JSONException {
        return new JSONObject()
                .put("title", title)
                .put("quantity", quantity)
                .put("product", product.toJSONObject());
    }
}
