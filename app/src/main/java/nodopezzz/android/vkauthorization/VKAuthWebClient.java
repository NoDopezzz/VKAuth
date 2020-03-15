package nodopezzz.android.vkauthorization;

import android.app.Activity;
import android.content.Context;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

abstract  public class VKAuthWebClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if(url.contains("https://oauth.vk.com/blank.html")){
            onSuccessAuth(url);
        }
    }

    abstract public void onSuccessAuth(String url);
}
