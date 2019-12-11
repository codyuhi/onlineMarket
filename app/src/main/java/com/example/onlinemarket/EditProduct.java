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

public class EditProduct extends AppCompatActivity {

    private ProgressBar mLoadingProgress;
    public EditText productName;
    public EditText productPrice;
    public Button button;
    public Integer count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product);

        try{
            productName = (EditText) findViewById(R.id.productName);
            productPrice = (EditText) findViewById(R.id.productPrice);
            button = (Button) findViewById(R.id.button);
            mLoadingProgress = (ProgressBar) findViewById(R.id.mLoadingProgress);
            mLoadingProgress.setVisibility(View.INVISIBLE);
            count = 0;
        } catch (Exception e) {
            Log.d("Error: ", e.getMessage());
        }

        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                try {
                    try {
                        new EditProduct.submitProduct().execute("name", productName.getText().toString());
                    } catch (Exception e) {
                        Log.d("Error: ", e.getMessage());
                    }
                } catch (Exception e) {
                    Log.d("Error: ", e.getMessage());
                }
            }
        });
    }

    public class submitProduct extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... inputs) {
            String result = null;

            try {
                Intent grabIntent = getIntent();
                URL addUrl = ApiUtil.buildUrl("products/" + grabIntent.getStringExtra("productId"));
                result = ApiUtil.editPUT(addUrl, inputs[0], inputs[1]);
            } catch (Exception e) {
                Log.d("Error: ", e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            mLoadingProgress.setVisibility(View.INVISIBLE);
            if(result.equals("Forbidden")){
                Toast.makeText(EditProduct.this, "You do not have permissions to change this product.",Toast.LENGTH_LONG).show();
                Intent returnIntent = new Intent(EditProduct.this, ViewAllProducts.class);
                startActivity(returnIntent);
            } else {
                count++;
                if(count >= 2){
                    Intent returnIntent = new Intent(EditProduct.this, ViewAllProducts.class);
                    startActivity(returnIntent);
                } else {
                    new EditProduct.submitProduct().execute("price", productPrice.getText().toString());
                }
            }
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}
