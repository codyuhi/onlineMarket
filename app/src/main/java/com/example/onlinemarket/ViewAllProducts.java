package com.example.onlinemarket;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;

public class ViewAllProducts extends AppCompatActivity {

    private ProgressBar mLoadingProgress;
    public TextView noDataTextView;
    private RecyclerView rvFullList;
    public Intent intent = getIntent();
    public static String auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_products);

        mLoadingProgress = (ProgressBar) findViewById(R.id.mLoadingProgress);
        mLoadingProgress.setVisibility(View.INVISIBLE);
        noDataTextView = (TextView) findViewById(R.id.no_data);
        noDataTextView.setVisibility(View.INVISIBLE);
        intent = getIntent();
        if(intent.getStringExtra("token") != null){
            auth = intent.getStringExtra("token");
        }

        rvFullList = (RecyclerView) findViewById(R.id.rv_fullList);
// give the recyclerview some style by giving a divider between products
        rvFullList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));;
        LinearLayoutManager fullListLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        rvFullList.setLayoutManager(fullListLayoutManager);

        try {
            new getProducts().execute("");
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                    Intent addProductIntent = new Intent(ViewAllProducts.this, AddNewProduct.class);
                    startActivity(addProductIntent);
                }
            });
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
//                    noDataTextView.setVisibility(View.VISIBLE);
                    Toast.makeText(ViewAllProducts.this, "Unable to get product list data", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(ViewAllProducts.this, "Product list data obtained successfully!", Toast.LENGTH_LONG).show();
                    noDataTextView.setText(result);
//                    noDataTextView.setVisibility(View.VISIBLE);

                    try{
                        ArrayList<Product> fullListProducts = new ArrayList<Product>();

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
                            fullListProducts.add(productInfo);
                        }

                        try{
                            ArrayList<Product> productArrayList = fullListProducts;
                            AllProductAdapter adapter = new AllProductAdapter(productArrayList);
                            rvFullList.setAdapter(adapter);
                        } catch (Exception e) {
                            Log.d("Error: ", e.getMessage());
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
