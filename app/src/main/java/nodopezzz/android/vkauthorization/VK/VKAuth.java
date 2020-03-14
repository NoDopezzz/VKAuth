package nodopezzz.android.vkauthorization.VK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.sax.EndElementListener;

import java.util.List;

import nodopezzz.android.vkauthorization.BuildConfig;

public class VKAuth {

    private static Uri ENDPOINT = Uri.parse("https://oauth.vk.com/authorize");

    public static void startAuthorizationActivity(Context context, String ... scopes){
        //Подготовка url для получения токена
        Uri uri = ENDPOINT.buildUpon()
                .appendQueryParameter("client_id", BuildConfig.CLIENT_ID)
                .appendQueryParameter("display", "mobile")
                .appendQueryParameter("redirect_uri", "https://oauth.vk.com/blank.html")
                .appendQueryParameter("scope", scopesToString(scopes))
                .appendQueryParameter("response_type", "token")
                .appendQueryParameter("v", "5.52").build();

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static String getAccessToken(Uri uri){
        if(uri == null || !uri.toString().startsWith("https://oauth.vk.com/blank.html")) return null;
        uri = Uri.parse(uri.toString().replace("#","?"));
        return uri.getQueryParameter("access_token");
    }

    public static String getUserId(Uri uri){
        if(uri == null || !uri.toString().startsWith("https://oauth.vk.com/blank.html")) return null;
        uri = Uri.parse(uri.toString().replace("#","?"));
        return uri.getQueryParameter("user_id");
    }

    private static String scopesToString(String[] scopes){
        if(scopes.length == 0) return null;

        StringBuilder result = new StringBuilder();
        for (String scope : scopes){
            result.append(scope).append(",");
        }
        return result.substring(0, result.length() - 1);
    }

}
