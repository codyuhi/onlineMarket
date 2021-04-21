// This is the first thing to load for the app.
// It has a splash page and sends a GET request to see if the server is responding
// define the package
package com.example.onlinemarket;

// import appcompatactivity for easier coding
import androidx.appcompat.app.AppCompatActivity;

// import the intent module to get intent extras and nav to other pages
import android.content.Intent;
// import the asynctask module for network call to the API
import android.os.AsyncTask;
// import base android methods
import android.os.Bundle;
// import the log module to log errors
import android.util.Log;
// import the view module to work with views
import android.view.View;
// import the progress bar module for the progress bar on network call
import android.widget.ProgressBar;
// import the toast module for user feedback
import android.widget.Toast;

// import the url module to work with urls
import java.net.URL;

// instantiate the class definition
public class MainActivity extends AppCompatActivity {

// create private variable for the class for a loading bar
    private ProgressBar mLoadingProgress;

// this function executes when the class is instantiated
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // the content view for this class is activity_main.xml
        setContentView(R.layout.activity_main);

// define the progress bar and hide it
        mLoadingProgress = (ProgressBar) findViewById(R.id.mLoadingProgress);
        mLoadingProgress.setVisibility(View.INVISIBLE);

// try/catch in case there are problems with the network call
        try {
            URL queryURL = ApiUtil.buildUrl("checkConnection");
            new getProjects().execute(queryURL);
        } catch (Exception e ){
            // log errors and provide user feedback
            Log.d("Error: ", e.getMessage());
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

// this function is used to perform async netowrk call to API
    public class getProjects extends AsyncTask<URL, Void, String> {
        // this function runs to execute a network call to API in background thread
        @Override
        protected String doInBackground(URL... urls) {
            // grab the urls that were passed
            URL searchURL = urls[0];
            String result = null;
            // try/catch in case errors happen with the network call
            try {
                // get the json returned by a GET request to the API
                result = ApiUtil.getJson(searchURL);
            } catch (Exception e) {
                // log error and provide user feedback
                Log.e("Error: ", e.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            // return the results from the GET request to the API
            return result;
        }

// This function runs when the network call is complete
        @Override
        protected void onPostExecute(String result) {
            // if the network call returned nothing, let the user know
            if(result == null){
                Toast.makeText(getApplicationContext(), "Unable to establish connection with the server", Toast.LENGTH_LONG).show();
                return;
            }
            // try catch in case there are errors with the intent
            try {
                // create intent to go to the login page class
                Intent loginPageIntent = new Intent(MainActivity.this, loginPage.class);
                // nav to login page
                startActivity(loginPageIntent);
            } catch (Exception e) {
                // log error and provide user feedback
                Log.d("Error: ",e.getMessage());
                Toast.makeText(getApplicationContext(), "Unable to load login page", Toast.LENGTH_LONG).show();
                return;
            }
        }

// this function runs before the network call starts
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // show progress bar
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}
