package nodopezzz.android.vkauthorization.Network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitmapDownloader {

    public static Bitmap loadBitmap(String url) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
            throw new IOException(connection.getResponseMessage());
        }

        InputStream input = connection.getInputStream();

        Bitmap bitmap = BitmapFactory.decodeStream(input);
        input.close();
        return bitmap;
    }
}
