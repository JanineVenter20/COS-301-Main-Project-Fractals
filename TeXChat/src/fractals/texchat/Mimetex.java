package fractals.texchat;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Xml.Encoding;

public class Mimetex {
	
	static {
		System.loadLibrary("chatTex") ;
	}
	
	public native int[] mime(String s);
	
	private static String expression;
	private static Bitmap bitmap = null;

	
	//CONSTRUCTOR
	public Mimetex()
	{}
	
	//TO RETURN THE CURRENT EXPRESSION
	public String getExpresion()
	{
		return expression;
	}
	
	//SET THE EXPRESION
	private void setExpression(String exp)
	{
		expression = exp;
	}
	
	//TO RETURN THE BITMAP FOR THE EXPRESSION
	public Bitmap getBitmap(String exp) throws UnsupportedEncodingException
	{
		//THE IP NEEDS TO REFERENCE YOUR OWN PC's IP (THE EMULATOR USES 127.0.0.1 WHILE RUNNING)
		this.setExpression(exp);
		bitmap = sendRequest("http://192.168.137.1/cgi-bin/mimetex.cgi?"+URLEncoder.encode(exp, Encoding.UTF_8.toString()));
		return bitmap;
	}
	
	public Bitmap getLocalBitmap(String exp) {
		int[] j = mime(exp);
		Bitmap bmp = Bitmap.createBitmap(j[0], j[1], Bitmap.Config.ARGB_8888);
		for(int i =0; i < j[1]; i++)
		{
			for(int k = 0; k < j[0]; k++)
			{
				if ((j[(i)*j[0] + k +2]) == 0)
					bmp.setPixel(k, i, Color.argb(255, j[(i)*j[0] + k +2], j[(i)*j[0] + k +2], j[(i)*j[0] + k +2]));
				else bmp.setPixel(k, i, Color.argb(0, j[(i)*j[0] + k +2], j[(i)*j[0] + k +2], j[(i)*j[0] + k +2]));
			  //returnMe += j[(i)*j[0] + k +2 ] + " " + j[(i)*j[0] + k +2] + " " + j[(i)*j[0] + k +2] + " ";
			}
		}
		
		return bmp;
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
   
    //GET THE IMAGE BITMAP
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
