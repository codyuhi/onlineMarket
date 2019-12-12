// This file is used to view all products on screen at once
// define the package
package com.example.onlinemarket;

// import the intent module to access intent extras and nav to other activities
import android.content.Intent;
// import asynctask for network calls to the api
import android.os.AsyncTask;
// import base android methods
import android.os.Bundle;
// import log module to log errors
import android.util.Log;
// import the view module to work with views
import android.view.View;
// import the progressbar for the progressbar
import android.widget.ProgressBar;
// import the textview module for textviews
import android.widget.TextView;
// import toast for user feedback
import android.widget.Toast;

// import appcompatactivity for easier coding
import androidx.appcompat.app.AppCompatActivity;
// import recyclerview widgets for the full list
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// import floating action button for adding a new product
import com.google.android.material.floatingactionbutton.FloatingActionButton;
// snackbar was used in testing
import com.google.android.material.snackbar.Snackbar;

// import json array and object to work with json data
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

// import url to work with urls
import java.net.URL;
// import arraylist for the 
import java.util.ArrayList;

// begin class definition
public class ViewAllProducts extends AppCompatActivity {

// public and private variables/views for access across class
    private ProgressBar mLoadingProgress;
    public TextView noDataTextView;
    private RecyclerView rvFullList;
    // intent gets the intent that led to this page loading
    public Intent intent = getIntent();
    // auth will hold the user authentication info
    public static String auth;

// This function is executed when the class is instantiated
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // the content view for this class is view_all_products.xml
        setContentView(R.layout.view_all_products);

// define public/private variables and views that were initialized above
        mLoadingProgress = (ProgressBar) findViewById(R.id.mLoadingProgress);
        mLoadingProgress.setVisibility(View.INVISIBLE);
        noDataTextView = (TextView) findViewById(R.id.no_data);
        noDataTextView.setVisibility(View.INVISIBLE);
        intent = getIntent();
        // if the token was passed from teh login screen, store that token here in the auth public var
        if(intent.getStringExtra("token") != null){
            auth = intent.getStringExtra("token");
        }

        rvFullList = (RecyclerView) findViewById(R.id.rv_fullList);
// give the recyclerview some style by giving a divider between products
        rvFullList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));;
        LinearLayoutManager fullListLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        rvFullList.setLayoutManager(fullListLayoutManager);
// try/catch in case there are issues creating the fab or the intent to the new product page
        try {
            // get the products
            new getProducts().execute("");
            // create fab to create new product
            FloatingActionButton fab = findViewById(R.id.fab);
            // create listener for the fab
            fab.setOnClickListener(new View.OnClickListener() {
                // if the fab is pressed, this function runs
                @Override
                public void onClick(View view) {
                    // create intent and nav to the addproduct page
                    Intent addProductIntent = new Intent(ViewAllProducts.this, AddNewProduct.class);
                    startActivity(addProductIntent);
                }
            });
        } catch (Exception e) {
            // log any errors
            Log.d("Error: ", e.getMessage());
        }
    }

// this function is used to execute the GET request to grab all the products
    public class getProducts extends AsyncTask<String, Void, String> {
        // this runs in a background thread. used for the network call to API
        @Override
        protected String doInBackground(String... inputs){
            String result = null;
// try/catch block to handle errors with api call
            try {
                // build url for GET request for all products
                URL getProductsUrl = ApiUtil.buildUrl("products");
                // execute network call to API
                result = ApiUtil.allProductsGET(getProductsUrl);
            } catch (Exception e) {
                // log any errors
                Log.d("Error: ", e.getMessage());
            }
            // return the result of the GET request
            return result;
        }

// This function runs when the network call is complete
        @Override
        protected void onPostExecute(String result) {
            // hide the progress bar
            mLoadingProgress.setVisibility(View.INVISIBLE);
            // the noDataTextView was used for debugging
            noDataTextView.setText(result);
            // try catch in case of json errors or adapter errors
            try {
                // if there was no response from the server, give user feedback
                if(result == null){
//                    noDataTextView.setVisibility(View.VISIBLE);
                    Toast.makeText(ViewAllProducts.this, "Unable to get product list data", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(ViewAllProducts.this, "Product list data obtained successfully!", Toast.LENGTH_LONG).show();
                    noDataTextView.setText(result);
//                    noDataTextView.setVisibility(View.VISIBLE);

// try catch for errors
                    try{
                        // create array list for the recycler view
                        ArrayList<Product> fullListProducts = new ArrayList<Product>();
                        // parse the result into a json object
                        JSONObject jsonFullList = new JSONObject(result);
                        // grab the products from the json object and put them in a json array
                        JSONArray allProducts = jsonFullList.getJSONArray("products");
                        // get length of json array
                        int productCount = allProducts.length();
                        // create a new product array that will store data for all the products
                        Product[] products = new Product[productCount];
                        // iterate through and do this for every element in the json array
                        for(int i =0; i < productCount; i++){
                            // get all the attribute info from the json object
                            JSONObject productJson = allProducts.getJSONObject(i);
                            String nameJson = productJson.getString("name");
                            Float priceJson = Float.parseFloat(productJson.getString("price"));
                            String productImageJson = productJson.getString("productImage");
                            String _idJson = productJson.getString("_id");
                            String userIdJson = productJson.getString("userId");
                            String emailJson = productJson.getString("email");
                            Product productInfo = new Product (
                                    _idJson,
                                    nameJson,
                                    priceJson,
                                    productImageJson,
                                    userIdJson,
                                    emailJson
                            );
                            // store the new product in the product array
                            products[i] = productInfo;
                            // add the new product into the array list
                            fullListProducts.add(productInfo);
                        }

                        try{
                            // create a new adapter from the array list and set the adapter
                            ArrayList<Product> productArrayList = fullListProducts;
                            AllProductAdapter adapter = new AllProductAdapter(productArrayList);
                            rvFullList.setAdapter(adapter);
                        } catch (Exception e) {
                            // log errors
                            Log.d("Error: ", e.getMessage());
                        }
                    } catch (Exception e) {
                        // log errors
                        Log.d("Error: ", e.getMessage());
                    }
                }
            } catch (Exception e) {
                // log errors
                Log.d("Error: ", e.getMessage());
            }
        }

// This function runs before the network call is started
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // show the progress bar
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}
