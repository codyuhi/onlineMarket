package com.example.onlinemarket;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URL;
import java.util.prefs.PreferenceChangeEvent;

import javax.net.ssl.HttpsURLConnection;

public class CreateAccount extends AppCompatActivity {

    private ProgressBar mLoadingProgress;

    public String username = "";
    public String password = "";
    public String globalResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        final EditText usernameTextView = (EditText) findViewById(R.id.username);
        final EditText passwordTextView = (EditText) findViewById(R.id.password);
        final Button createAccount = (Button) findViewById(R.id.createAccount);
        mLoadingProgress = (ProgressBar) findViewById(R.id.mLoadingProgress);
        mLoadingProgress.setVisibility(View.INVISIBLE);

        createAccount.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(createAccount.getWindowToken(),0);
                } catch (Exception e) {
                    Log.d("Error: ", e.getMessage());
                    return;
                }
                try {
                    username = usernameTextView.getText().toString();
                    password = passwordTextView.getText().toString();
//                    Toast.makeText(CreateAccount.this, username + " " + password, Toast.LENGTH_SHORT).show();
                    if(username.length() < 4){
                        Toast.makeText(CreateAccount.this, "Username too short. Please give a username with at least 4 characters", Toast.LENGTH_LONG).show();
                    } else if (username.length() > 64){
                        Toast.makeText(CreateAccount.this, "Username too long. Please give a username with at most 64 characters", Toast.LENGTH_LONG).show();
                    } else if (password.length() < 8){
                        Toast.makeText(CreateAccount.this, "Password too short.  Please give a password with at least 8 characters", Toast.LENGTH_LONG).show();
                    } else if (password.length() > 128){
                        Toast.makeText(CreateAccount.this, "Password too long.  Please give a password with at most 128 characters", Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            new createAccountFunction().execute(username,password);
                            if(globalResult.equals("Created")){
                                Intent loginIntent = new Intent(CreateAccount.this, ViewAllProducts.class);
                                startActivity(loginIntent);
                            }
                        } catch (Exception e) {
                            Log.d("Error: ", e.getMessage());
                            Toast.makeText(CreateAccount.this, "Account creation failed at location 1", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d("Error: ", e.getMessage());
                    Toast.makeText(CreateAccount.this, "Account creation failed at location 0", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public class createAccountFunction extends AsyncTask<String, Void,String> {

        @Override
        protected String doInBackground(String... inputs) {
            String result = null;

            try{
                URL createAccountURL = ApiUtil.buildUrl("user/signup");
                result = ApiUtil.createAccountPOST(createAccountURL, username, password);
//                Toast.makeText(CreateAccount.this, postresult, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.d("Error: ", e.getMessage());
                Toast.makeText(CreateAccount.this, "Account creation failed at location 2", Toast.LENGTH_LONG).show();
            }

            globalResult = result;

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            mLoadingProgress.setVisibility(View.INVISIBLE);

            if(result.equals("Created")) {
                Toast.makeText(CreateAccount.this, "New Account created for "+username+".", Toast.LENGTH_LONG).show();
            } else if(result.equals("Conflict")) {
                Toast.makeText(CreateAccount.this, "Unable to create account with this email (Email already in use). "+username+".", Toast.LENGTH_LONG).show();
            } else if(result.equals("Internal Server Error")){
                Toast.makeText(CreateAccount.this, "Internal Server Error.  Verify that "+username+" is a valid email address.", Toast.LENGTH_LONG).show();
            } else if(result.equals(null)){
                Toast.makeText(CreateAccount.this, "Result returned NULL.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(CreateAccount.this, "Result is "+result+" and typeOf result is " + result.getClass(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}
