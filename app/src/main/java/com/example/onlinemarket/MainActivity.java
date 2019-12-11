package com.example.onlinemarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ProgressBar mLoadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingProgress = (ProgressBar) findViewById(R.id.mLoadingProgress);
        mLoadingProgress.setVisibility(View.INVISIBLE);

        try {
            URL queryURL = ApiUtil.buildUrl("checkConnection");
            new getProjects().execute(queryURL);
        } catch (Exception e ){
            Log.d("Error: ", e.getMessage());
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public class getProjects extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String result = null;
            try {
                result = ApiUtil.getJson(searchURL);
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result == null){
                Toast.makeText(getApplicationContext(), "Unable to establish connection with the server", Toast.LENGTH_LONG).show();
                return;
            }
            try {
//                Testing intent
//                Intent loginPageIntent = new Intent(MainActivity.this, ViewAllProducts.class);
                Intent loginPageIntent = new Intent(MainActivity.this, loginPage.class);
//                loginPageIntent.putExtra();
                startActivity(loginPageIntent);
            } catch (Exception e) {
                Log.d("Error: ",e.getMessage());
                Toast.makeText(getApplicationContext(), "Unable to load login page", Toast.LENGTH_LONG).show();
                return;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}
