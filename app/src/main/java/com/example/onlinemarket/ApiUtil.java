package com.example.onlinemarket;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class ApiUtil {

    private ApiUtil(){}

    public static final String PI_BASE_API_URL = "http://192.168.4.1:3000";
    public static final String PC_BASE_API_URL = "http://localhost:3000";
    public static final String PC_REMOTE_BASE_API_URL = "http://192.168.1.193:3000/products/";

    public static URL buildUrl(String title) {
        String fullUrl = PC_REMOTE_BASE_API_URL + title;
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
}
