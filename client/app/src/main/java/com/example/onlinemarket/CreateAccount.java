// this class is used to create a new user account
// Define the package
package com.example.onlinemarket;

// import the context module to allow for context method access
import android.content.Context;
// import the intent module to be able to access intent extras and change activities
import android.content.Intent;
// import the asynctask module for network calls to the API
import android.os.AsyncTask;
// import the bundle module to access base android methods
import android.os.Bundle;
// import the log module to log errors
import android.util.Log;
// import the view module to work with views
import android.view.View;
// import the inputmethodmanager module to hide the password as it's typed in
import android.view.inputmethod.InputMethodManager;
// import the button widget to allow for the submit button
import android.widget.Button;
// import the edittext widget to allow for the edittext views for username and password
import android.widget.EditText;
// import the progressbar widget for network calls
import android.widget.ProgressBar;
// import the textview for the title
import android.widget.TextView;
// import the toast widget for providing user feedback
import android.widget.Toast;

// import appcompatactivity for easier coding
import androidx.appcompat.app.AppCompatActivity;

// import jsonobjects to create json objects/parse json strings
import org.json.JSONObject;

// import url to work with urls
import java.net.URL;
// import preference change event to edit preference change events
import java.util.prefs.PreferenceChangeEvent;

// import https connection in case I wanted to do https connection
import javax.net.ssl.HttpsURLConnection;

// Initiate the class definition
public class CreateAccount extends AppCompatActivity {

// create progress circle
    private ProgressBar mLoadingProgress;
// create username that is publicly accessible
    public String username = "";
    // create password that is publicly accessible
    public String password = "";

// onCreate runs when the class is instantiated
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The layout used for this class is create_account.xml
        setContentView(R.layout.create_account);

// define the textviews for username and password
        final EditText usernameTextView = (EditText) findViewById(R.id.username);
        final EditText passwordTextView = (EditText) findViewById(R.id.password);
        final Button createAccount = (Button) findViewById(R.id.createAccount);
        // Define the progress bar for network calls
        mLoadingProgress = (ProgressBar) findViewById(R.id.mLoadingProgress);
        // make the loading bar invisible until a network call is performed
        mLoadingProgress.setVisibility(View.INVISIBLE);

// the button to create an account is pressed, the function below will run
        createAccount.setOnClickListener(new View.OnClickListener(){
            // the onclick function runs when clicked
            public void onClick(View arg0){
                // try/catch to catch any errors with setting the inputmethod service
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(createAccount.getWindowToken(),0);
                } catch (Exception e) {
                    // log any errors
                    Log.d("Error: ", e.getMessage());
                    return;
                }
                // try/catch for any errors that happen when verifying valid regex/length
                try {
                    username = usernameTextView.getText().toString();
                    password = passwordTextView.getText().toString();
                    // make sure the usernames are between length 4 and 64 chars, and the passwords are between 8 and 128 chars
                    if(username.length() < 4){
                        Toast.makeText(CreateAccount.this, "Username too short. Please give a username with at least 4 characters", Toast.LENGTH_LONG).show();
                    } else if (username.length() > 64){
                        Toast.makeText(CreateAccount.this, "Username too long. Please give a username with at most 64 characters", Toast.LENGTH_LONG).show();
                    } else if (password.length() < 8){
                        Toast.makeText(CreateAccount.this, "Password too short.  Please give a password with at least 8 characters", Toast.LENGTH_LONG).show();
                    } else if (password.length() > 128){
                        Toast.makeText(CreateAccount.this, "Password too long.  Please give a password with at most 128 characters", Toast.LENGTH_LONG).show();
                    } else {
                        // if none of the above conditions are met, call the API to create a user
                        try {
                            // create a user by using the create user function
                            new createAccountFunction().execute(username,password);
                            // if the create user function was successful, login with the newly created user
                            new loginFunction().execute(username,password);
                        } catch (Exception e) {
                            // log any errors and provide user feedback
                            Log.d("Error: ", e.getMessage());
                            Toast.makeText(CreateAccount.this, "Account creation failed at location 1", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    // log any errors and provide user feedback
                    Log.d("Error: ", e.getMessage());
                    Toast.makeText(CreateAccount.this, "Account creation failed at location 0", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

// The createAccountFunction performs the network call to the API
    public class createAccountFunction extends AsyncTask<String, Void,String> {

// This code runs on a seperate thread for network connection
        @Override
        protected String doInBackground(String... inputs) {
            String result = null;
// try/catch block in case there are errors
            try{
                // build the url for signing up a new user
                URL createAccountURL = ApiUtil.buildUrl("user/signup");
                // perform the POST request
                result = ApiUtil.createAccountPOST(createAccountURL, username, password);
            } catch (Exception e) {
                // log any errors and provide user feedback
                Log.d("Error: ", e.getMessage());
                Toast.makeText(CreateAccount.this, "Account creation failed at location 2", Toast.LENGTH_LONG).show();
            }
// return the result of the API call
            return result;
        }

// this function runs after the network call is complete
        @Override
        protected void onPostExecute(String result) {
            // hide the progress bar
            mLoadingProgress.setVisibility(View.INVISIBLE);
            // try/catch in case an error occurs
            try {
                // if nothing was returned from the API call
                if(result == null){
                    // provide user feedback and verify correct regex/length requirements are met
                    Toast.makeText(CreateAccount.this, "Please Enter a Valid Email Address and Password.", Toast.LENGTH_LONG).show();
                } else {
                    // if the returned value is correct, provide user feedback
                    // otherwise, provide user feedback telling of any deficiencies
                    if(result.equals("Created")) {
                        Toast.makeText(CreateAccount.this, "Account Created Successfully!", Toast.LENGTH_LONG).show();
                    } else if(result.equals("Conflict")) {
                        Toast.makeText(CreateAccount.this, "Please Enter a Valid Email Address and Password.", Toast.LENGTH_LONG).show();
                    } else if(result.equals("Internal Server Error")){
                        Toast.makeText(CreateAccount.this, "Internal Server Error.  Verify that "+username+" is a valid email address.", Toast.LENGTH_LONG).show();
                    } else if(result.equals(null)){
                        Toast.makeText(CreateAccount.this, "Result returned NULL.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CreateAccount.this, "Result is " + result + " and typeOf result is " + result.getClass(), Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                // log any errors
                Log.d("Error: ", e.getMessage());
            }
        }

// this function runs before the network call is made
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // make the loading bar visible
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }

// this function is called after the new user is created
// it logs the user in by obtaining an auth token and creating a session w/ the server
    public class loginFunction extends AsyncTask<String, Void, String> {
        // This function makes the network call
        @Override
        protected String doInBackground(String... inputs) {
            String result = null;
// try/catch block for any errors with the API call
            try {
                // create a url for logging in the user
                URL loginURL = ApiUtil.buildUrl("user/login");
                // perform the POST request
                result = ApiUtil.loginPOST(loginURL, username, password, CreateAccount.this);
            } catch (Exception e ) {
                // log any errors
                Log.d("Error: ", e.getMessage());
            }
            // return the result of the API call
            return result;
        }

// This function runs after the network call is complete
        @Override
        protected void onPostExecute(String result) {
            // hide the progress bar
            mLoadingProgress.setVisibility(View.INVISIBLE);
            // try/catch block to handle any errors in working with the returned data
            try {
                // if there was nothing returned from the network call,
                if(result == null){
                    // let the user know that the username/password combo wasn't found
                    Toast.makeText(CreateAccount.this, "Username/password combination not found.", Toast.LENGTH_LONG).show();
                } else {
                    // if there was a result, 
                    if(result != null) {
                        // provide user feedback
                        Toast.makeText(CreateAccount.this, "Successfully logged in!", Toast.LENGTH_LONG).show();
                        // try/catch while working with json objects and creating intents
                        try {
                            // grab the auth token provided in the response
                            JSONObject token = new JSONObject(result);
                            // create an intent to load the ViewAllProducts page
                            Intent loginIntent = new Intent(CreateAccount.this, ViewAllProducts.class);
                            // pass the authentication info to the next intent
                            loginIntent.putExtra("token","Bearer " + token.getString("token"));
                            // navigate to the next page
                            startActivity(loginIntent);
                        } catch (Exception e ) {
                            // log any errors and provide user feedback
                            Log.d("Error: ", e.getMessage());
                            Toast.makeText(CreateAccount.this, "Username/password combination not found.", Toast.LENGTH_LONG).show();
                        }
                        // handle any other incorrect responses from the server
                    } else if(result.equals("Unauthorized")) {
                        Toast.makeText(CreateAccount.this, "Username/password combination not found.", Toast.LENGTH_LONG).show();
                    } else if(result.equals("Internal Server Error")){
                        Toast.makeText(CreateAccount.this, "Internal Server Error.  Verify that "+username+" is a valid email address.", Toast.LENGTH_LONG).show();
                    } else if(result.equals(null)){
                        Toast.makeText(CreateAccount.this, "Result returned NULL.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CreateAccount.this, "Result is " + result + " and typeOf result is " + result.getClass(), Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                // log any errors
                Log.d("Error: ", e.getMessage());
            }
        }

// This function is called before the network call is executed
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            // show the progress bar
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}
