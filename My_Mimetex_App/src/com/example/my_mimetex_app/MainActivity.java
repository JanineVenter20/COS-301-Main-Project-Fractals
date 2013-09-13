package com.example.my_mimetex_app;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.widget.ImageView;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //String exp = "x=\\frac{-b\\pm\\sqrt{b^2-4ac}}{2a}";
        String exp = "x^2+y^2+z^2";
        ImageView img1 = (ImageView)findViewById(R.id.expression);
        
        Mimetex mine = new Mimetex();
        try {
			img1.setImageBitmap(mine.getBitmap(exp));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
 }
