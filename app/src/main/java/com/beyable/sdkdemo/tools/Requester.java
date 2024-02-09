package com.beyable.sdkdemo.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gol D. Marko on 26/01/2024.
 * <p>
 * Wisepear Techlab
 * All rights reserved
 **/

public class Requester {

    private final static String BASE_URL = "https://dummyjson.com/";

    public final static String PRODUCTS_PAGE = "products";
    public final static String CATEGORIES_PAGE = "products/categories";
    public final static String CATEGORY_PAGE = "products/category/";

    public final static String CARTS_PAGE = "carts";

    private static Requester instance;
    private final RequestQueue requestQueue;
    private final ImageLoader imageLoader;

    // PRIVATE constructor
    private Requester(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>  cache = new LruCache<String, Bitmap>(20);
                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }
                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static Requester getSharedInstance(Context context) {
        if (instance == null) {
            instance = new Requester(context);
        }
        return instance;
    }

    public void makeObjGetRequest(String endpoint, JSONObject body, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + endpoint;
        // Request a json response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, body, responseListener, errorListener);
        // Add the request to the RequestQueue.
        requestQueue.add(request);
    }

    public void makeArrayGetRequest(String endpoint, JSONArray body, Response.Listener<JSONArray> responseListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + endpoint;
        // Request a json response from the provided URL.
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, body, responseListener, errorListener);
        // Add the request to the RequestQueue.
        requestQueue.add(request);
    }

    public void makePostRequest(String endpoint, JSONObject body, Response.Listener<JSONObject> responseListener,  Response.ErrorListener errorListener) {
        String url = BASE_URL + endpoint;
        // Request a json response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, responseListener, errorListener){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<String, String>();
                return params;
            }
        };
        // Add the request to the RequestQueue.
        requestQueue.add(request);
    }


    public void setImageForNetworkImageView(NetworkImageView networkImageView, String imageUrl, int defaultImageId, int errorImageId) {
        imageLoader.get(imageUrl, ImageLoader.getImageListener(networkImageView, defaultImageId, errorImageId));
        networkImageView.setImageUrl(imageUrl, imageLoader);
    }
}
