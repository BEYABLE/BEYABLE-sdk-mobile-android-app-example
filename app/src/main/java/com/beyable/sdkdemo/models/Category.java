package com.beyable.sdkdemo.models;

import java.io.Serializable;

/**
 * Created by MarKinho on 29/01/2024.
 * <p>
 * TiVine Technologies
 * All rights reserved
 **/

public class Category implements Serializable {

    static final long serialVersionUID = 727566123075950653L;

    final private String category;
    final private String title;

    /**
     * Construct a Category object with the data from server
     *
     * @param category the category
     */
    public Category(String category) {
        this.category   = category;
        this.title      = category.substring(0, 1).toUpperCase() + category.substring(1);
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }
}
