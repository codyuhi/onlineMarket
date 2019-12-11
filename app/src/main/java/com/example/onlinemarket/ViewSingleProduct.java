package com.example.onlinemarket;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.net.URL;

public class ViewSingleProduct extends AppCompatActivity {

    private ProgressBar mLoadingProgress;
    public TextView productName;
    public TextView productPrice;
    public TextView userId;
    public TextView seller;
    public Intent intent;
    public Button deleteButton;
    public Button putButton;
    public String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_single_product);

        productName = (TextView) findViewById(R.id.productName);
        productPrice = (TextView) findViewById(R.id.productPrice);
        userId = (TextView) findViewById(R.id.userId);
        seller = (TextView) findViewById(R.id.seller);
        mLoadingProgress = (ProgressBar) findViewById(R.id.mLoadingProgress);
        intent = getIntent();
        deleteButton = (Button) findViewById(R.id.deleteButton);
        putButton = (Button) findViewById(R.id.editButton);

        try {
            new getProduct().execute("");
        } catch (Exception e) {
            Log.d("Error: ", e.getMessage());
        }

        deleteButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                try {
                    try {
                        new ViewSingleProduct.deleteFunction().execute("");
                    } catch (Exception e) {
                        Log.d("Error: ", e.getMessage());
                    }
                } catch (Exception e) {
                    Log.d("Error: ", e.getMessage());
                }
            }
        });

        putButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                try {
                    try {
                        Intent editIntent = new Intent(ViewSingleProduct.this, EditProduct.class);
                        editIntent.putExtra("productId", productId);
                        startActivity(editIntent);
                    } catch (Exception e) {
                        Log.d("Error: ", e.getMessage());
                    }
                } catch (Exception e) {
                    Log.d("Error: ", e.getMessage());
                }
            }
        });

    }

    public class getProduct extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... input) {
            String result = null;

            try {
//                Intent intent = getIntent();
                String message = intent.getStringExtra("com.example.onlinemarket.MESSAGE");
                URL getProductUrl = ApiUtil.buildUrl("products/" + message);
                result = ApiUtil.singleProductGET(getProductUrl);
            } catch (Exception e) {
                Log.d("Error: ", e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            mLoadingProgress.setVisibility(View.INVISIBLE);
            try {
                JSONObject jsonProduct = new JSONObject(result);
                Product product = new Product(
                        jsonProduct.getString("_id"),
                        jsonProduct.getString("name"),
                        Float.parseFloat(jsonProduct.getString("price")),
                        jsonProduct.getString("productImage"),
                        jsonProduct.getString("userId"),
                        jsonProduct.getString("email")
                );
                productId = jsonProduct.getString("_id");
                productName.setText(product.name);
                productPrice.setText("Price: $" + product.price.toString());
                userId.setText("User ID: " + product.userId);
                seller.setText("Seller: " + product.email);

//                Toast.makeText(ViewSingleProduct.this, intent.getStringExtra("auth"), Toast.LENGTH_LONG).show();
//                Toast.makeText(ViewSingleProduct.this, ViewAllProducts.auth, Toast.LENGTH_LONG).show();

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

    public class deleteFunction extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... inputs){
            String result = null;

            try{
                String message = intent.getStringExtra("com.example.onlinemarket.MESSAGE");
                URL deleteUrl = ApiUtil.buildUrl("products/" + message);
                result = ApiUtil.productDELETE(deleteUrl);
            } catch (Exception e){
                Log.d("Error: ", e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            mLoadingProgress.setVisibility(View.INVISIBLE);
            if(result.equals("OK")){
                Toast.makeText(ViewSingleProduct.this, "Product Deleted Successfully", Toast.LENGTH_LONG).show();
                Intent returnIntent = new Intent(ViewSingleProduct.this, ViewAllProducts.class);
                startActivity(returnIntent);
            } else if(result.equals("Forbidden")){
                Toast.makeText(ViewSingleProduct.this, "You do not have permission to delete this product.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}
