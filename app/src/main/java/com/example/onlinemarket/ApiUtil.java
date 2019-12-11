//This file is used to handle the API network calls,
// build urls, and most of the connectivity-related stuff.
//define the package
package com.example.onlinemarket;

//import the context module to be able to access the caller's context
import android.content.Context;
//import the log module to log errors and connection responses
import android.util.Log;

//import the buffered reader module to read API responses
import java.io.BufferedReader;
//import the dataoutputstream module to be able to create a stream of data
//to be able to write stuff via POSTs
import java.io.DataOutputStream;
//fileoutputstream is same as above but with files
import java.io.FileOutputStream;
//import ioexception to deal with IO exceptions in the network calls
import java.io.IOException;
//import the inputstream function to be able to establish stream for data to be received
import java.io.InputStream;
//import the reader to be able to actually access the data on the stream
import java.io.InputStreamReader;
//import the write to be able to actually write to the output stream
import java.io.OutputStreamWriter;
//import the httpurlconnection module to be able to connect to the server via http
import java.net.HttpURLConnection;
//import the URL module to be able to create URL objects/access methods
import java.net.URL;
//import the scanner module to b able to scan a stream for relevant data
import java.util.Scanner;

//import the modeprivate module to get the mode that was passed in the context
import static android.content.Context.MODE_PRIVATE;

//initiate the class definition
public class ApiUtil {

//    constructor (empty)
    private ApiUtil(){}

//    Below are some url strings that can be easily switched out based on which device I'm using
//    at the moment to host the API (Rpi, PC, etc.)
    public static final String PI_BASE_API_URL = "http://192.168.4.1:3000/";
    public static final String PC_BASE_API_URL = "http://localhost:3000/";
    public static final String PC_REMOTE_BASE_API_URL = "http://192.168.1.16:3000/";
//    I originally wanted to access authentication via a file stored in
//    the phone's file localstorage.  This idea was scrapped and this is now just an artifact
    public static final String FILE_NAME = "auth.txt";

//    this method builds a url from the original URL base and the given string parameters/path
    public static URL buildUrl(String title) {
//        whichever of these is commented out will determine which device I'm trying to connect to
//        plus whatever subsequent path and parameters the calling method passed
//        String fullUrl = PC_REMOTE_BASE_API_URL + title;
        String fullUrl = PI_BASE_API_URL + title;
//        declare a url object that will be defined later
        URL url = null;
//        try/catch to try to create the new URL
        try {
//            define the url based on the chosen device above
            url = new URL(fullUrl);
        } catch (Exception e) {
//            if this fails, print the stack trace for logging failure
            e.printStackTrace();
        }
//        return the newly created url object
        return url;
    }

//    This method gets the json data from a GET request
    public static String getJson(URL url) throws IOException{
//        establish the http connection from the given URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        try/catch just in case the inputstream fails to work with the connection or if the scanner has problems
        try {
//            define the input stream to be from the http connection
            InputStream stream = connection.getInputStream();
//            create a new scanner to scan the input stream
            Scanner scanner = new Scanner(stream);
//            define boolean to remember whether the scanner found any data
            boolean hasData = scanner.hasNext();
//            If there's data, return that json data to the calling function
            if(hasData) {
                return scanner.next();
            } else {
//                if no data, return null
                return null;
            }
        } catch (Exception e) {
//            log errors and return null
            Log.d("Error: ", e.toString());
            return null;
        }
        finally {
//            disconnect from the http connection that was established.
            connection.disconnect();
        }
    }

//    This method handles a POST request to the API to login and establish a session
    public static String loginPOST(URL loginUrl, String username, String password, Context ctx) {
//        This string takes the passed email and password combination and puts it in a
//        stringified json form so it can be passed with the POST request
        String loginJsonString = "{\"email\": \"" +
                username +
                "\",\"password\": \"" +
                password + "\"}";
//        try/catch to handle exceptions (especially io from the connection establishment)
        try{
//            establish the connection
            HttpURLConnection connection = (HttpURLConnection) loginUrl.openConnection();
//            define that the request will be a POST request to write data to the API
            connection.setRequestMethod("POST");
//            set the content type to be json data
            connection.setRequestProperty("Content-Type", "application/json");
//            verify that output will be executed
            connection.setDoOutput(true);
//            create dataoutputstream which will write to the http url connection
            DataOutputStream write = new DataOutputStream(connection.getOutputStream());
//            write the prepared json data to the API
            write.writeBytes(loginJsonString);
//            flush
            write.flush();
//            close the outputstream
            write.close();
//            create input stream reader to read the response from the http url connection
            InputStreamReader read = new InputStreamReader(connection.getInputStream());
//            the buffered reader will handle the read information from the connection
            BufferedReader br = new BufferedReader(read);
//            initialize empty string which will hold the response a line at a time
            String text = "";
//            initialy empty string which will hold the full response after everything has been read
            String json_response = "";
//            while the buffered reader is reading lines
            while((text = br.readLine()) != null) {
//                append the line to the full json response string
                json_response += text;
            }
//            log the response returned from the server
            Log.d("Response: ", connection.getResponseMessage() + "");
//            below is a try/catch block that currently does not work. It was meant as a way to store
//            the auth info in localstorage on the client device
//try/catch to open another fileoutputstream based on context file name and mode
            try {
               FileOutputStream fOut = ctx.openFileOutput(FILE_NAME, MODE_PRIVATE);
//               write the json response to the file
               fOut.write(json_response.getBytes());
//               close outputstream
               fOut.close();
            } catch (Exception e) {
//                log error if this failed
                Log.d("Error: ", e.getMessage());
            } finally {
//                after all the above is done, close the fileOutputStream and print the stacktrace if there is an error
                try {
//                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

//            return the json response so it can be handled by the calling function
            return json_response;
        } catch (Exception e) {
//            log any errors
            Log.d("Error: ", e.getMessage());
        }

//        if anything failed above, return null
        return null;
    }

//    This method is used to initiate a POST request to the API for a new account to be created
    public static String createAccountPOST(URL createAccountUrl, String username, String password) {
//        This string takes the passed email and password combination and puts it in a
//        stringified json form so it can be passed with the POST request
        try {
            String createAccountJsonString = "{\"email\": \"" +
                    username +
                    "\",\"password\": \"" +
                    password + "\"}";
//            establish url connection based on the given url
            HttpURLConnection connection = (HttpURLConnection) createAccountUrl.openConnection();
//            define the request method to be a POST
            connection.setRequestMethod("POST");
//            define the content-type to be json data
            connection.setRequestProperty("Content-Type", "application/json");
//            define that you want the output to be executed
            connection.setDoOutput(true);
//            create an output stream on the http url connection
            DataOutputStream write = new DataOutputStream(connection.getOutputStream());
//            write the prepared json string to the outputstream
            write.writeBytes(createAccountJsonString);
//            flush the outputstream
            write.flush();
//            close the outputstream
            write.close();
//            create an input stream on the http url connection
            InputStreamReader read = new InputStreamReader(connection.getInputStream());
//            create a new buffered reader to read the data on the inputstream
            BufferedReader br = new BufferedReader(read);
//            initialize empty string which will hold the response a line at a time
            String text = "";
//            initially empty string which will hold the full response after everything has been read
            String json_response = "";
//            While the buffered reader can find lines,
            while((text = br.readLine()) != null) {
//                append the line to the full string of json response data
                json_response += text;
            }
//            log the server's response
            Log.d("Response: ", connection.getResponseMessage() + "");
//            return the server's response so it can be worked with by the caller
            return connection.getResponseMessage();
        } catch (Exception e){
//            log any errors
            Log.d("Error: ", e.getMessage());
        }
//        if something failed above, return null
        return null;
    }

//    This method handles GET requests to get all the products in the mongodb
    public static String allProductsGET(URL getProductsUrl) throws IOException {

//        create http connection to the provided url
        HttpURLConnection connection = (HttpURLConnection) getProductsUrl.openConnection();
//        try/catch block to handle issues if the inputstream or scanner have issues
        try{
//            create input stream for the http url connection
            InputStream stream = connection.getInputStream();
//            create scanner that will scan the inputstream for data
            Scanner scanner = new Scanner(stream);
//            define that the scanner should find anything
            scanner.useDelimiter("\\A");
//            if the scanner found anything, this will be true
            boolean hasData = scanner.hasNext();
//            if the scanner found anything, return what the scanner found
            if(hasData){
                return scanner.next();
            } else {
//                else return null
                return null;
            }
        } catch (Exception e) {
//            log any errors with the inputstream or scanner
            Log.d("Error: ", e.getMessage());
        }finally{
//            close the connection
            connection.disconnect();
        }
//        if something went wrong above, return null
        return null;
    }

//    this method handles GET requests for a certain product
    public static String singleProductGET(URL getProductUrl) throws IOException {

//        Establish http connection to the provided url
        HttpURLConnection connection = (HttpURLConnection) getProductUrl.openConnection();

//        try/catch to handle issues with the inputstream or scanner
        try {
//            create an input stream for the http url connection
            InputStream stream = connection.getInputStream();
//            create a scanner to scan the input stream for any data
            Scanner scanner = new Scanner(stream);
//            tell the scanner to find any kind of data
            scanner.useDelimiter("\\A");
//            if the scanner found something, this will be true
            boolean hasData = scanner.hasNext();
//            if the scanner found something, return what it found
            if(hasData){
                return scanner.next();
            } else {
//                if the scanner didn't find anything, return null
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
