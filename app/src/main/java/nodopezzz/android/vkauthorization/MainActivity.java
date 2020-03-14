package nodopezzz.android.vkauthorization;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import nodopezzz.android.vkauthorization.VK.VKAuthLocal;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "vkauthorization";

    private VKAuthLocal local;
    private String token;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Получение сохраненных token и userid
        local = new VKAuthLocal(this);
        token = local.getToken();
        userId = local.getUserId();

        if(token == null || userId == null){
            //открытие фрагмента авторизаци при отсутствии сохраненных значений
            createAuthFragment();
        } else{
            createMainFragment(userId, token);
        }
    }

    private void createAuthFragment(){
        AuthFragment fragment = AuthFragment.newInstance();
        fragment.setOnAuthSuccessListener(new AuthFragment.OnAuthSuccessListener() {
            @Override
            public void onAuthSuccess(String userId, String token) {
                //обработка успешной авторизации
                createMainFragment(userId, token);
            }
        });
        createFragment(fragment);
    }

    private void createMainFragment(String userId, String token){
        MainFragment fragment = MainFragment.newInstance(userId, token);
        fragment.setOnFailListener(new MainFragment.OnFailListener() {
            @Override
            public void onFailed() {
                //Обработка ошибки (направильный токен, устек срок годности и т.д.)

                showFailSnackbar();
                local.deleteToken();
                local.deleteUserId();

                createAuthFragment();
            }
        });

        fragment.setOnQuitListener(new MainFragment.OnQuitListener() {
            @Override
            public void onQuit() {
                //Обработка нажатия на кнопку "Выйти"

                local.deleteToken();
                local.deleteUserId();
                createAuthFragment();
            }
        });
        createFragment(fragment);
    }

    private void createFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main, fragment)
                .commit();
    }

    private void showFailSnackbar(){
        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.error, Snackbar.LENGTH_SHORT).show();
    }
}
