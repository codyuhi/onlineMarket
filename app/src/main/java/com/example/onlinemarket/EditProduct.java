// This page is used to edit any existing products with a PUT request
// define the package
package com.example.onlinemarket;

// import intent module to be able to get intent data and navigate to other activities
import android.content.Intent;
// import async task module to perform network calls to the API
import android.os.AsyncTask;
// import base android methods
import android.os.Bundle;
// import log module for logging errors
import android.util.Log;
// import view module for working with views
import android.view.View;
// import the button widget for the submit button
import android.widget.Button;
// import the edittext to take user input
import android.widget.EditText;
// import progress bar so the user can be notified of background threads
import android.widget.ProgressBar;
// import toast module for user feedback
import android.widget.Toast;

// import appcompatactivity for easier coding
import androidx.appcompat.app.AppCompatActivity;

// import URl to work with urls
import java.net.URL;

// begin class definition
public class EditProduct extends AppCompatActivity {

    // create some private/public variables for use in the class
    private ProgressBar mLoadingProgress;
    public EditText productName;
    public EditText productPrice;
    public Button button;
    public Integer count;

// this function is called when the class is instantiated
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // the layout for this class is edit_product.xml
        setContentView(R.layout.edit_product);
        // try/catch to handle errors with defining the variables
        try{
            // define the variables/views
            productName = (EditText) findViewById(R.id.productName);
            productPrice = (EditText) findViewById(R.id.productPrice);
            button = (Button) findViewById(R.id.button);
            mLoadingProgress = (ProgressBar) findViewById(R.id.mLoadingProgress);
            // hide the loading bar
            mLoadingProgress.setVisibility(View.INVISIBLE);
            // begin with 0 items being counted yet
            count = 0;
        } catch (Exception e) {
            // log any errors
            Log.d("Error: ", e.getMessage());
        }

// a listener to detect button clicks
        button.setOnClickListener(new View.OnClickListener(){
            // when button is clicked, the below function runs
            public void onClick(View arg0) {
                // try/catch to handle errors while doing network call
                try {
                    // call function to handle product changes
                    new EditProduct.submitProduct().execute("name", productName.getText().toString());
                } catch (Exception e) {
                    // log any errors
                    Log.d("Error: ", e.getMessage());
                    }
            }
        });
    }

// This function performs the network call to the API on a background thread
    public class submitProduct extends AsyncTask<String, Void, String> {
        // this function runs in the background and performs the network call
        @Override
        protected String doInBackground(String... inputs) {
            String result = null;
            // try/catch for any issues with the actual network call
            try {
                // grab information that was passed from the previous page (like which product id is going to be edited)
                Intent grabIntent = getIntent();
                // build a url with query string that has the desired product's id on it
                URL addUrl = ApiUtil.buildUrl("products/" + grabIntent.getStringExtra("productId"));
                // perform the actual network call PUT request
                result = ApiUtil.editPUT(addUrl, inputs[0], inputs[1]);
            } catch (Exception e) {
                // log any errors
                Log.d("Error: ", e.getMessage());
            }
// return the result of the PUT request
            return result;
        }

// This function runs after the network call is complete
        @Override
        protected void onPostExecute(String result) {
            // hide the progress bar
            mLoadingProgress.setVisibility(View.INVISIBLE);
            // if permissions weren't good for this request,
            if(result.equals("Forbidden")){
                // provide user feedback and return to the ViewAllProducts page
                Toast.makeText(EditProduct.this, "You do not have permissions to change this product.",Toast.LENGTH_LONG).show();
                Intent returnIntent = new Intent(EditProduct.this, ViewAllProducts.class);
                startActivity(returnIntent);
            } else {
                // if the request was for the correct user's permissions, increment the count
                count++;
                if(count >= 2){
                    // if the PUT requests are done, then return to the ViewAllProducts page
                    Intent returnIntent = new Intent(EditProduct.this, ViewAllProducts.class);
                    startActivity(returnIntent);
                } else {
                    // this means that one of the fields still needs to be updated (price).
                    // rerun the PUT request with the price now
                    new EditProduct.submitProduct().execute("price", productPrice.getText().toString());
                }
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
}
