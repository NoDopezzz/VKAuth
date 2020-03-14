package nodopezzz.android.vkauthorization;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThumbnailDownloader extends HandlerThread {

    private Handler responseHandler;
    private Handler requestHandler;

    private int MESSAGE_CODE = 47;
    private boolean isQuit = false;

    private Map<CircleImageView, String> map = new HashMap<>();

    public ThumbnailDownloader(String name, Handler responseHandler){
        super(name);
        this.responseHandler = responseHandler;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        requestHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(isQuit) return;
                if(msg.what == MESSAGE_CODE){
                    handle(msg);
                }
            }
        };
    }

    private void handle(Message msg){
        final CircleImageView imageView = (CircleImageView) msg.obj;
        String url = map.get(imageView);
        if(url == null) return;

        try {
            final Bitmap bitmap = BitmapDownloader.loadBitmap(url);
            responseHandler.post(new Runnable() {
                @Override
                public void run() {
                    map.remove(imageView);
                    imageView.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putInQueue(CircleImageView imageView, String url){
        if(requestHandler == null) return;

        map.put(imageView, url);
        requestHandler.obtainMessage(MESSAGE_CODE, imageView).sendToTarget();
    }

    public void clearQueue(){
        requestHandler.removeMessages(MESSAGE_CODE);
        map.clear();
    }

    @Override
    public boolean quit() {
        isQuit = true;
        return super.quit();
    }
}
