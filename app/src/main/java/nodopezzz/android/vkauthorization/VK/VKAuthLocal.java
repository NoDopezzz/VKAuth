package nodopezzz.android.vkauthorization.VK;

import android.content.Context;
import android.content.SharedPreferences;

public class VKAuthLocal {
    private static final String NAME = "VK_AUTH";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_ID = "KEY_ID";

    private Context context;
    private SharedPreferences sharedPreferences;

    public VKAuthLocal(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token){
        sharedPreferences.edit()
                .putString(KEY_TOKEN, token)
                .apply();
    }

    public void saveUserId(String userId){
        sharedPreferences.edit()
                .putString(KEY_ID, userId)
                .apply();
    }

    public void deleteToken(){
        sharedPreferences.edit()
                .remove(KEY_TOKEN)
                .apply();
    }

    public void deleteUserId(){
        sharedPreferences.edit()
                .remove(KEY_ID)
                .apply();
    }

    public String getToken(){
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public String getUserId(){
        return sharedPreferences.getString(KEY_ID, null);
    }
}
