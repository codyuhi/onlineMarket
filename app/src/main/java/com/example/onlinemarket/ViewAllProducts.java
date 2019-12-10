package com.example.onlinemarket;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;
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

        try {
            new getProducts().execute("");
        } catch (Exception e) {
            Log.d("Error: ", e.getMessage());
        }
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
            noDataTextView.setText(result);
            try {
                if(result == null){
                    noDataTextView.setVisibility(View.VISIBLE);
                    Toast.makeText(ViewAllProducts.this, "Unable to get product list data", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ViewAllProducts.this, "Product list data obtained successfully!", Toast.LENGTH_LONG).show();
                    noDataTextView.setText(result);
                    noDataTextView.setVisibility(View.VISIBLE);

                    try{
                        JSONObject jsonFullList = new JSONObject(result);
                        JSONArray allProducts = jsonFullList.getJSONArray("products");
                        int productCount = allProducts.length();
                        Product[] products = new Product[productCount];
                        for(int i =0; i < productCount; i++){
                            JSONObject productJson = allProducts.getJSONObject(i);
                            String nameJson = productJson.getString("name");
                            Float priceJson = Float.parseFloat(productJson.getString("price"));
                            String productImageJson = productJson.getString("productImage");
                            String _idJson = productJson.getString("_id");
//                            JSONObject requestJson = productJson.getJSONObject("request");
//                            String requestTypeJson = requestJson.getString("type");
//                            String requestUrlJson = requestJson.getString("url");
                            String userIdJson = productJson.getString("userId");
                            String emailJson = productJson.getString("email");
                            Product productInfo = new Product (
                                    _idJson,
                                    nameJson,
                                    priceJson,
                                    productImageJson,
                                    userIdJson,
                                    emailJson
                            );
                            products[i] = productInfo;
                        }
                    } catch (Exception e) {
                        Log.d("Error: ", e.getMessage());
                    }

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
