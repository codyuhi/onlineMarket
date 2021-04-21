// this page is used for users to login and receive a session token from the server
// define the package
package com.example.onlinemarket;

// import the intent module to get intent extras and navigate to other pages
import android.content.Intent;
// import the asynctask module for network calls to the API
import android.os.AsyncTask;
// import the bundle module for base android methods
import android.os.Bundle;
// import log module to log errors
import android.util.Log;
// import the view module to work with views
import android.view.View;
// import the button widget for the login submit or create new account intent
import android.widget.Button;
// import the edittext module to take user input
import android.widget.EditText;
// import the progressbar to provide user feedback during network calls
import android.widget.ProgressBar;
// import the toast module to provide user feedback
import android.widget.Toast;

// import the appcompatactivity module for easier coding
import androidx.appcompat.app.AppCompatActivity;

// import the json object module to work with json data and parse json strings
import org.json.JSONObject;

// import the url module to work with url objects and methods
import java.net.URL;

// instantiate class definition
public class loginPage extends AppCompatActivity {

// create a few public/private variables/views for use across the class
    public String username = "";
    public String password = "";
    private ProgressBar mLoadingProgress;

// this function runs when the class is instantiated
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // the content view for this class is login_page.xml
        setContentView(R.layout.login_page);

// define the public/private variables for the views
        Button submitLogin = (Button) findViewById(R.id.submit);
        final EditText usernameTextView = (EditText) findViewById(R.id.username);
        final EditText passwordTextView = (EditText) findViewById(R.id.password);
        final Button createAccount = (Button) findViewById(R.id.createAccount);
        mLoadingProgress = (ProgressBar) findViewById(R.id.mLoadingProgress);
        // hide the progress bar
        mLoadingProgress.setVisibility(View.INVISIBLE);

// create listener for if the submit login button is pressed
        submitLogin.setOnClickListener(new View.OnClickListener(){
            // this function runs when the button is pressed
            public void onClick(View arg0) {
                // try/catch in case getting the text view text has errors or if there are problems with toasting
                try {
                    username = usernameTextView.getText().toString();
                    password = passwordTextView.getText().toString();
                    // these verify that the username length and password lengths are valid before even doing a network call
                    if (username.length() < 4) {
                        Toast.makeText(loginPage.this, "Username too short. Please give a username with at least 4 characters", Toast.LENGTH_LONG).show();
                    } else if (username.length() > 64) {
                        Toast.makeText(loginPage.this, "Username too long. Please give a username with at most 64 characters", Toast.LENGTH_LONG).show();
                    } else if (password.length() < 8) {
                        Toast.makeText(loginPage.this, "Password too short.  Please give a password with at least 8 characters", Toast.LENGTH_LONG).show();
                    } else if (password.length() > 128) {
                        Toast.makeText(loginPage.this, "Password too long.  Please give a password with at most 128 characters", Toast.LENGTH_LONG).show();
                    } else {
                        // try catch in case there are errors with calling the network calls
                        try {
                            new loginPage.loginFunction().execute(username, password);
                        } catch (Exception e) {
                            // log error and provide user feedback
                            Log.d("Error: ", e.getMessage());
                            Toast.makeText(loginPage.this, "Account login failed at location 1", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    // log any errors
                    Log.d("Error: ", e.getMessage());
                }
            }
        });

// create an event listener for the create account button
        createAccount.setOnClickListener(new View.OnClickListener(){
            // this function runs when the create account button is pressed
            public void onClick(View arg0) {
                // try/catch in case there are issues with making the intent
                try {
                    // create new intent and navigate to the create account page
                    Intent loginIntent = new Intent(loginPage.this, CreateAccount.class);
                    startActivity(loginIntent);
                } catch (Exception e) {
                    // log any errors
                    Log.d("Error: ", e.getMessage());
                }
            }
        });
    }

// this function performs the POST request to log the user in and get the session set up
    public class loginFunction extends AsyncTask<String, Void, String> {
        // this function runs on a background thread to perform the POST request
        @Override
        protected String doInBackground(String... inputs) {
            String result = null;
            // try/catch in case there are errors with the network call
            try {
                // build url to login the user
                URL loginURL = ApiUtil.buildUrl("user/login");
                // network call to the api POSTing the provided user info
                result = ApiUtil.loginPOST(loginURL, username, password, loginPage.this);
            } catch (Exception e ) {
                // log any errors
                Log.d("Error: ", e.getMessage());
            }
            // return the POST request's response
            return result;
        }

// This function runs when the network call to the API is done
        @Override
        protected void onPostExecute(String result) {
            // hide the progress bar
            mLoadingProgress.setVisibility(View.INVISIBLE);
            // try/catch to handle any errors with providing user feedback or changing pages
            try {
                // if nothing was returned from the POST request,
                if(result == null){
                    // provide user feedback
                    Toast.makeText(loginPage.this, "Username/password combination not found.", Toast.LENGTH_LONG).show();
                } else {
                    // provide user feedback that they are logged in
                    if(result != null) {
                        Toast.makeText(loginPage.this, "Successfully logged in!", Toast.LENGTH_LONG).show();
                        // try/catch in case there are errors with creating the intent
                        try {
                            // parse the result and grab the token data for auth
                            JSONObject token = new JSONObject(result);
                            // create new intent for navigating to the viewallproducts class
                            Intent loginIntent = new Intent(loginPage.this, ViewAllProducts.class);
                            // include the token with the intent
                            loginIntent.putExtra("token","Bearer " + token.getString("token"));
                            // navigate to next page
                            startActivity(loginIntent);
                        } catch (Exception e ) {
                            // log any errors and provide user feedback
                            Log.d("Error: ", e.getMessage());
                            Toast.makeText(loginPage.this, "Username/password combination not found.", Toast.LENGTH_LONG).show();
                        }
                        // handle a bunch of other cases based on the POST response
                    } else if(result.equals("Unauthorized")) {
                        Toast.makeText(loginPage.this, "Username/password combination not found.", Toast.LENGTH_LONG).show();
                    } else if(result.equals("Internal Server Error")){
                        Toast.makeText(loginPage.this, "Internal Server Error.  Verify that "+username+" is a valid email address.", Toast.LENGTH_LONG).show();
                    } else if(result.equals(null)){
                        Toast.makeText(loginPage.this, "Result returned NULL.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(loginPage.this, "Result is " + result + " and typeOf result is " + result.getClass(), Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                // log any errors
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
}
