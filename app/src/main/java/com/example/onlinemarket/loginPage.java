package com.example.onlinemarket;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URL;

public class loginPage extends AppCompatActivity {

    public String username = "";
    public String password = "";
    public String globalResult = "";
    private ProgressBar mLoadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        Button submitLogin = (Button) findViewById(R.id.submit);
        final EditText usernameTextView = (EditText) findViewById(R.id.username);
        final EditText passwordTextView = (EditText) findViewById(R.id.password);
        final Button createAccount = (Button) findViewById(R.id.createAccount);
        mLoadingProgress = (ProgressBar) findViewById(R.id.mLoadingProgress);
        mLoadingProgress.setVisibility(View.INVISIBLE);

        submitLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                try {
                    username = usernameTextView.getText().toString();
                    password = passwordTextView.getText().toString();
                    if (username.length() < 4) {
                        Toast.makeText(loginPage.this, "Username too short. Please give a username with at least 4 characters", Toast.LENGTH_LONG).show();
                    } else if (username.length() > 64) {
                        Toast.makeText(loginPage.this, "Username too long. Please give a username with at most 64 characters", Toast.LENGTH_LONG).show();
                    } else if (password.length() < 8) {
                        Toast.makeText(loginPage.this, "Password too short.  Please give a password with at least 8 characters", Toast.LENGTH_LONG).show();
                    } else if (password.length() > 128) {
                        Toast.makeText(loginPage.this, "Password too long.  Please give a password with at most 128 characters", Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            new loginPage.loginFunction().execute(username, password);
                        } catch (Exception e) {
                            Log.d("Error: ", e.getMessage());
                            Toast.makeText(loginPage.this, "Account login failed at location 1", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d("Error: ", e.getMessage());
                }
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                try {
                    Intent loginIntent = new Intent(loginPage.this, CreateAccount.class);
                    startActivity(loginIntent);
                } catch (Exception e) {
                    Log.d("Error: ", e.getMessage());
                }
            }
        });
    }

    public class loginFunction extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... inputs) {
            String result = null;

            try {
                URL loginURL = ApiUtil.buildUrl("user/login");
                result = ApiUtil.loginPOST(loginURL, username, password, loginPage.this);
            } catch (Exception e ) {
                Log.d("Error: ", e.getMessage());
            }
            globalResult = result;
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            mLoadingProgress.setVisibility(View.INVISIBLE);
            try {
                if(result == null){
                    Toast.makeText(loginPage.this, "Username/password combination not found.", Toast.LENGTH_LONG).show();
                } else {
                    if(result.equals("OK")) {
                        Toast.makeText(loginPage.this, "Successfully logged in!", Toast.LENGTH_LONG).show();
                        try {
                            Intent loginIntent = new Intent(loginPage.this, ViewAllProducts.class);
                            startActivity(loginIntent);
                        } catch (Exception e ) {
                            Log.d("Error: ", e.getMessage());
                            Toast.makeText(loginPage.this, "Username/password combination not found.", Toast.LENGTH_LONG).show();
                        }
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
                Log.d("Error: ", e.getMessage());
            }
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}
