package com.beyable.sdkdemo.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Gol D. Marko on 29/01/2024.
 * <p>
 * Wisepear Techlab
 * All rights reserved
 **/

public class Product implements Serializable {

    static final long serialVersionUID = 727566123075950654L;

    private final String id;
    private final String title;
    private final String description;
    private final double price;
    private final int discountPercentage;
    private final double rating;
    private final int stock;
    private final String brand;
    private final String category;
    private final String thumbnail;
    private final ArrayList<String> images = new ArrayList<>();

    public Product(JSONObject serverData) {
        this.id                 = serverData.optString("id");
        this.title              = serverData.optString("title");
        this.description        = serverData.optString("description");
        this.price              = serverData.optDouble("price");
        this.discountPercentage = serverData.optInt("discountPercentage");
        this.rating             = serverData.optDouble("rating");
        this.stock              = serverData.optInt("stock");
        this.brand              = serverData.optString("brand");
        this.category           = serverData.optString("category");
        this.thumbnail          = serverData.optString("thumbnail");
        JSONArray imagesArray   = serverData.optJSONArray("images");
        if (imagesArray != null) {
            for (int i=0; i < imagesArray.length(); i++) {
                this.images.add(imagesArray.optString(i));
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public double getRating() {
        return rating;
    }

    public int getStock() {
        return stock;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public ArrayList<String> getImages() {
        return images;
    }
}
