package com.example.onlinemarket;

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

public class AddNewProduct extends AppCompatActivity {

    private ProgressBar mLoadingProgress;
    public EditText productName;
    public EditText productPrice;
    public Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_product);

        try{
            productName = (EditText) findViewById(R.id.productName);
            productPrice = (EditText) findViewById(R.id.productPrice);
            button = (Button) findViewById(R.id.button);
            mLoadingProgress = (ProgressBar) findViewById(R.id.mLoadingProgress);
            mLoadingProgress.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            Log.d("Error: ", e.getMessage());
        }

        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                try {
//                    Float test;
//                    try{
//                        test = Float.parseFloat(productPrice.toString());
//                    } catch (Exception e){
//                        Log.d("Error: ",e.getMessage());
//                        return;
//                    }
                    try {
                        new AddNewProduct.submitProduct().execute(productName.getText().toString(), productPrice.getText().toString());
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
                URL addUrl = ApiUtil.buildUrl("products");
                result = ApiUtil.addPOST(addUrl, inputs[0], inputs[1]);
            } catch (Exception e) {
                Log.d("Error: ", e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            mLoadingProgress.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}
