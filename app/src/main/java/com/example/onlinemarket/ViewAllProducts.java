package com.example.onlinemarket;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.net.URL;

public class ViewAllProducts extends AppCompatActivity {

    private ProgressBar mLoadingProgress;
    public TextView noDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_products);

        mLoadingProgress = (ProgressBar) findViewById(R.id.mLoadingProgress);
        mLoadingProgress.setVisibility(View.INVISIBLE);
        noDataTextView = (TextView) findViewById(R.id.no_data);
        noDataTextView.setVisibility(View.INVISIBLE);
    }

    public class getProducts extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... inputs){
            String result = null;

            try {
                URL getProductsUrl = ApiUtil.buildUrl("products");
                result = ApiUtil.allProductsGET(getProductsUrl);
            } catch (Exception e) {
                Log.d("Error: ", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            mLoadingProgress.setVisibility(View.INVISIBLE);
            try {
                if(result == null){
                    Toast.makeText(ViewAllProducts.this, "Unable to get product list data", Toast.LENGTH_LONG).show();
                } else if(result.equals("OK")) {
                    Toast.makeText(ViewAllProducts.this, "Product list data obtained successfully!", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.d("Error: ", e.getMessage());
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}
