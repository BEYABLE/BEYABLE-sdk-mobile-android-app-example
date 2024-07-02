package com.beyable.sdkdemo.models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Gol D. Marko on 29/01/2024.
 * <p>
 * TiVine Technologies
 * All rights reserved
 **/

public class Category implements Serializable {

    static final long serialVersionUID = 727566123075950653L;

    final private String category;
    final private String title;
    final private String url;

    /**
     * Construct a Category object with the data from server
     *
     * @param category the category
     */
    public Category(JSONObject obj) {
        this.category   = obj.optString("slug");
        this.title      = obj.optString("name");
        this.url        = obj.optString("url");
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }
}
