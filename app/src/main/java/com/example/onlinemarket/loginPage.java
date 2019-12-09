package com.example.onlinemarket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class loginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        Button submitLogin = (Button) findViewById(R.id.submit);

        Button createAccount = (Button) findViewById(R.id.createAccount);

        submitLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                try {
                    Intent loginIntent = new Intent(loginPage.this, CreateAccount.class);
                    startActivity(loginIntent);
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
}
