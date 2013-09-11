package com.example.my_mimetex_app;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.ByteBuffer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

public class Mimetex {
	
	//CONSTRUCTOR
	public Mimetex(Context c, ImageView img1, String expression)
	{
		Bitmap bitmap = sendRequest("http://10.186.47.151/cgi-bin/mimetex.cgi?"+URLEncoder.encode(expression));
		//Bitmap bitmap1 = sendRequest("http://10.186.47.151/cgi-bin/mimetex.cgi?"+URLEncoder.encode("x^2+y^2+z^2"));
        //Bitmap bitmap2 = sendRequest("http://10.186.47.151/cgi-bin/mimetex.cgi?"+URLEncoder.encode("x=\\frac{-b\\pm\\sqrt{b^2-4ac}}{2a}"));
        img1.setImageBitmap(bitmap);
        //img2.setImageBitmap(bitmap2);
	}
	
    //OPEN THE CONNECTION TO THE LOCALHOST
    private InputStream OpenHttpConnection(String urlString) throws IOException 
    {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
        {
            throw new IOException("Not a valid HTTP connection");
        }
        
        try 
        {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            
            if (response == HttpURLConnection.HTTP_OK) 
            {
                in = httpConn.getInputStream();
            }
        } 
        catch (Exception ex) 
        {
            throw new IOException("Error connecting to the server!");
        }
        return in;
    }
   
    //GET THE IMAGE
    private Bitmap sendRequest(String URL) 
    {
        Bitmap bitmap = null;
        InputStream in = null;
        
        try 
        {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } 
        catch (IOException e1) 
        {
            e1.printStackTrace();
        }
        return bitmap;
    }
}
