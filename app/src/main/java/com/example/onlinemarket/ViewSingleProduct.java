// This file is used to show a single product's info
// Define the package
package com.example.onlinemarket;

// import the intent module to allow to nav to other pages and access intent extras
import android.content.Intent;
// import the asynctask for network calls to the api
import android.os.AsyncTask;
// import base android methods
import android.os.Bundle;
// import log to log errors
import android.util.Log;
// import view to work with views
import android.view.View;
// import button for buttons
import android.widget.Button;
// import progress bar for the progressbar
import android.widget.ProgressBar;
// import textview for the title
import android.widget.TextView;
// import toast for user feedback
import android.widget.Toast;

// import this for easier coding
import androidx.appcompat.app.AppCompatActivity;

// import json object to work with json
import org.json.JSONObject;

// import url to work with urls
import java.net.URL;

// begin class definition
public class ViewSingleProduct extends AppCompatActivity {

// create a bunch of public/private vars and views for use across class
    private ProgressBar mLoadingProgress;
    public TextView productName;
    public TextView productPrice;
    public TextView userId;
    public TextView seller;
    public Intent intent;
    public Button deleteButton;
    public Button putButton;
    public String productId;

// this function is executed when the class is instantiated
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_single_product);

// define vars and views that were initialized earlier
        productName = (TextView) findViewById(R.id.productName);
        productPrice = (TextView) findViewById(R.id.productPrice);
        userId = (TextView) findViewById(R.id.userId);
        seller = (TextView) findViewById(R.id.seller);
        mLoadingProgress = (ProgressBar) findViewById(R.id.mLoadingProgress);
        intent = getIntent();
        deleteButton = (Button) findViewById(R.id.deleteButton);
        putButton = (Button) findViewById(R.id.editButton);

// try getting the product
        try {
            new getProduct().execute("");
        } catch (Exception e) {
            // log error
            Log.d("Error: ", e.getMessage());
        }

// create event listener for the delete button
        deleteButton.setOnClickListener(new View.OnClickListener(){
            // this function runs when the delete button is clicked
            public void onClick(View arg0) {
            // try/catch in case the delete function throws error
                try {
                    // call delete function to delete product
                    new ViewSingleProduct.deleteFunction().execute("");
                } catch (Exception e) {
                    // log errors
                    Log.d("Error: ", e.getMessage());
                }
            }
        });

// create event listener for the edit product button
        putButton.setOnClickListener(new View.OnClickListener(){
            // this function runs when the edit product button is clicked
            public void onClick(View arg0) {
            // try/catch in case the edit function throws error
                try {
                    // nav to the edit product activity
                    Intent editIntent = new Intent(ViewSingleProduct.this, EditProduct.class);
                    // pass the product id to the edit activity
                    editIntent.putExtra("productId", productId);
                    startActivity(editIntent);
                } catch (Exception e) {
                    // log errors
                    Log.d("Error: ", e.getMessage());
                }
            }
        });

    }

// This function gets the product's info
    public class getProduct extends AsyncTask<String, Void, String> {
        // This runs in background for network call to API
        @Override
        protected String doInBackground(String... input) {
            String result = null;
            // Try catch for errors
            try {
                // grab the product id and execute GET request
                String message = intent.getStringExtra("com.example.onlinemarket.MESSAGE");
                URL getProductUrl = ApiUtil.buildUrl("products/" + message);
                result = ApiUtil.singleProductGET(getProductUrl);
            } catch (Exception e) {
                // log error
                Log.d("Error: ", e.getMessage());
            }
            // return result of the GET request
            return result;
        }

// This function runs after the network call is complete
        @Override
        protected void onPostExecute(String result) {
            // hide the loading bar
            mLoadingProgress.setVisibility(View.INVISIBLE);
            // try catch for errors
            try {
                // parse the response string into a json object
                JSONObject jsonProduct = new JSONObject(result);
                Product product = new Product(
                        jsonProduct.getString("_id"),
                        jsonProduct.getString("name"),
                        Float.parseFloat(jsonProduct.getString("price")),
                        jsonProduct.getString("productImage"),
                        jsonProduct.getString("userId"),
                        jsonProduct.getString("email")
                );
                // get the id from the json object
                productId = jsonProduct.getString("_id");
                // update the textviews with the product info
                productName.setText(product.name);
                productPrice.setText("Price: $" + product.price.toString());
                userId.setText("User ID: " + product.userId);
                seller.setText("Seller: " + product.email);

            } catch (Exception e) {
                // log errors
                Log.d("Error: ", e.getMessage());
            }
        }

// This function runs before the network call starts
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            // show the progress bar
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }

// This function deletes a product
    public class deleteFunction extends AsyncTask<String,Void,String> {
        // This runs in the background to call network to api
        @Override
        protected String doInBackground(String... inputs){
            String result = null;
            // try catch for errors
            try{
                // grab the product id and pass it to the DELETE request
                String message = intent.getStringExtra("com.example.onlinemarket.MESSAGE");
                URL deleteUrl = ApiUtil.buildUrl("products/" + message);
                result = ApiUtil.productDELETE(deleteUrl);
            } catch (Exception e){
                // log error
                Log.d("Error: ", e.getMessage());
            }
// return result of DELETE request
            return result;
        }

// This function runs after the network call is complete
        @Override
        protected void onPostExecute(String result) {
            // hide the progress bar
            mLoadingProgress.setVisibility(View.INVISIBLE);
            // if the delete op was successful,
            if(result.equals("OK")){
                // provide user feedback
                Toast.makeText(ViewSingleProduct.this, "Product Deleted Successfully", Toast.LENGTH_LONG).show();
                // nav back to the view all products activity
                Intent returnIntent = new Intent(ViewSingleProduct.this, ViewAllProducts.class);
                startActivity(returnIntent);
            } else if(result.equals("Forbidden")){
                // tell the user they don' have permission
                Toast.makeText(ViewSingleProduct.this, "You do not have permission to delete this product.", Toast.LENGTH_SHORT).show();
            }
        }

// This function runs before the network call is started
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            // show the progress bar
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}
