package com.example.onlinemarket;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import static android.content.Context.MODE_PRIVATE;

public class ApiUtil {

    private ApiUtil(){}

    public static final String PI_BASE_API_URL = "http://192.168.4.1:3000/";
    public static final String PC_BASE_API_URL = "http://localhost:3000/";
    public static final String PC_REMOTE_BASE_API_URL = "http://192.168.1.16:3000/";
    public static final String FILE_NAME = "auth.txt";

    public static URL buildUrl(String title) {
//        String fullUrl = PC_REMOTE_BASE_API_URL + title;
        String fullUrl = PI_BASE_API_URL + title;
        URL url = null;

        try {
            url = new URL(fullUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getJson(URL url) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);

            boolean hasData = scanner.hasNext();
            if(hasData) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.d("Error: ", e.toString());
            return null;
        }
        finally {
            connection.disconnect();
        }
    }

    public static String loginPOST(URL loginUrl, String username, String password, Context ctx) {
        String loginJsonString = "{\"email\": \"" +
                username +
                "\",\"password\": \"" +
                password + "\"}";
        try{
            HttpURLConnection connection = (HttpURLConnection) loginUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            DataOutputStream write = new DataOutputStream(connection.getOutputStream());
            write.writeBytes(loginJsonString);
            write.flush();
            write.close();
            InputStreamReader read = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(read);
            String text = "";
            String json_response = "";
            while((text = br.readLine()) != null) {
                json_response += text;
            }
            Log.d("Response: ", connection.getResponseMessage() + "");

            try {
               FileOutputStream fOut = ctx.openFileOutput(FILE_NAME, MODE_PRIVATE);
               fOut.write(json_response.getBytes());
               fOut.close();
            } catch (Exception e) {
                Log.d("Error: ", e.getMessage());
            } finally {
                try {
//                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

//            return connection.getResponseMessage();
            return json_response;
        } catch (Exception e) {
            Log.d("Error: ", e.getMessage());
        }

        return null;
    }

    public static String createAccountPOST(URL createAccountUrl, String username, String password) {
        try {
            String createAccountJsonString = "{\"email\": \"" +
                    username +
                    "\",\"password\": \"" +
                    password + "\"}";
//            JSONObject createAccountObject = new JSONObject(createAccountJsonString);
            HttpURLConnection connection = (HttpURLConnection) createAccountUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            DataOutputStream write = new DataOutputStream(connection.getOutputStream());
            write.writeBytes(createAccountJsonString);
            write.flush();
            write.close();
            InputStreamReader read = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(read);
            String text = "";
            String json_response = "";
            while((text = br.readLine()) != null) {
                json_response += text;
            }
//            Toast.makeText(getApplicationContext(), "Account creation failed at location 2", Toast.LENGTH_LONG).show();
//            String test = connection.getResponseMessage();
            Log.d("Response: ", connection.getResponseMessage() + "");
            return connection.getResponseMessage();
        } catch (Exception e){
            Log.d("Error: ", e.getMessage());
        }
        return null;
    }

    public static String allProductsGET(URL getProductsUrl) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) getProductsUrl.openConnection();

        try{
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasData = scanner.hasNext();
            if(hasData){
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.d("Error: ", e.getMessage());
        }finally{
            connection.disconnect();
        }
        return null;
    }

    public static String singleProductGET(URL getProductUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) getProductUrl.openConnection();

        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasData = scanner.hasNext();
            if(hasData){
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.d("Error: ", e.getMessage());
        }
        return null;
    }

    public static String addPOST(URL addUrl, String name, String price) {
        try{
            HttpURLConnection connection = (HttpURLConnection) addUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Authorization", ViewAllProducts.auth);
            connection.setDoOutput(true);
            DataOutputStream write = new DataOutputStream(connection.getOutputStream());
            write.writeBytes(
                    "{\"name\": \"" +
                    name +
                    "\", \"price\": \"" +
                    price +
                    "\"}");
            write.flush();
            write.close();
//            InputStreamReader read = new InputStreamReader(connection.getInputStream());
//            BufferedReader br = new BufferedReader(read);
//            String text = "";
//            String json_response ="";
//            while((text = br.readLine()) != null) {
//                json_response += text;
//            }
            Log.d("Response: ", connection.getResponseMessage() + "");
            return connection.getResponseMessage();
        } catch (Exception e) {
            Log.d("Error: ", e.getMessage());
        }
        return null;
    }

    public static String productDELETE(URL deleteUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) deleteUrl.openConnection();
        try{
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", ViewAllProducts.auth);
            connection.setUseCaches(false);
        } catch (Exception e) {
            Log.d("Error: ", e.getMessage());
        }
        return connection.getResponseMessage();
    }

    public static String editPUT(URL putUrl, String propName, String productValue) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) putUrl.openConnection();
        String putJson = "[{\"propName\": \"" +
                propName + "\",\"value\": \"" +
                productValue + "\"}]";
        try{
            connection.setDoInput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", ViewAllProducts.auth);
            connection.setRequestProperty("Content-Type", "application/json");
            OutputStreamWriter write = new OutputStreamWriter(connection.getOutputStream());
            write.write(putJson);
            write.close();
        } catch (Exception e) {
            Log.d("Error: ", e.getMessage());
        }
        return connection.getResponseMessage();
    }
}
