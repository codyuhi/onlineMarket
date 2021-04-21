//This class handles instances of where the user wants to create a new product
//Define the package
package com.example.onlinemarket;
//import intent to allow for intents and passing between layouts/classes
import android.content.Intent;
//import asynctask for network calls to the API
import android.os.AsyncTask;
//import bundle to get a bunch of base code method access
import android.os.Bundle;
//import log to log events
import android.util.Log;
//import view to be able to access view resources
import android.view.View;
//import the button widget for the layout's buttons
import android.widget.Button;
//import the edittext widget for the layout's edittext views
import android.widget.EditText;
//import the progressbar widget for the layout's loading circle
import android.widget.ProgressBar;

//import appcompatactivity to allow for greater compatiblity and easier coding based on my previous java experience
import androidx.appcompat.app.AppCompatActivity;

//import the url module to be able to create and work with URLs
import java.net.URL;

//initiate the class definition
public class AddNewProduct extends AppCompatActivity {

//    mLoadingProgress is the loading bar
    private ProgressBar mLoadingProgress;
//    productName holds the product name user input
    public EditText productName;
//    productPrice holds the product price user input
    public EditText productPrice;
//    button is the button used to submit the new product information
    public Button button;

//    onCreate is run when the class is called
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        The layout used for this class is the add_new_product.xml layout
        setContentView(R.layout.add_new_product);

//        try/catch to handle errors in defining the public variables
        try{
//            define the public variables
            productName = (EditText) findViewById(R.id.productName);
            productPrice = (EditText) findViewById(R.id.productPrice);
            button = (Button) findViewById(R.id.button);
            mLoadingProgress = (ProgressBar) findViewById(R.id.mLoadingProgress);
//            set the progress bar to be invisible until a network call is made
            mLoadingProgress.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
//            log the error if something goes wrong with defining the variables
            Log.d("Error: ", e.getMessage());
        }

//        if the submit button is pressed, run this code
//        this method is going to send an API call to POST a new product to the mongodb
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
//                try/catch block to work with the asynctask and network call error possibilities
                    try {
//                        call the submitProduct async method to add the product to the mongodb
                        new AddNewProduct.submitProduct().execute(productName.getText().toString(), productPrice.getText().toString());
                    } catch (Exception e) {
//                        if the product failed to be added, log the error
                        Log.d("Error: ", e.getMessage());
                    }
            }
        });
    }

//    submitProduct method POSTs the given user input data to the API
//    This will add the product to the mongodb
    public class submitProduct extends AsyncTask<String, Void, String> {
//        do the below protected function with the given user strings for product name and price
        @Override
        protected String doInBackground(String... inputs) {
            String result = null;
            try {
//                build a URL with the path http://<server_ip_address>:<port_number>/products
                URL addUrl = ApiUtil.buildUrl("products");
//                execute POST request and pass the desired URL and the product name and price
                result = ApiUtil.addPOST(addUrl, inputs[0], inputs[1]);
            } catch (Exception e) {
//                if any of the above fails, log the error
                Log.d("Error: ", e.getMessage());
            }
//            Return the result of the POST request
            return result;
        }

//        The below code will execute after the doInBackground method is done
        @Override
        protected void onPostExecute(String result) {
//            When the network call is over, hide the loading circle
            mLoadingProgress.setVisibility(View.INVISIBLE);
//            Create an intent to return to the ViewAllProducts screen and start that activity
            Intent returnIntent = new Intent(AddNewProduct.this, ViewAllProducts.class);
            startActivity(returnIntent);
        }

//        The below code will execute before the doInBackground method is started
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
//            make the loading circle visible during the network call
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}
