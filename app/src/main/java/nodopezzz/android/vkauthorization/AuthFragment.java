package nodopezzz.android.vkauthorization;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import nodopezzz.android.vkauthorization.VK.VKAuth;
import nodopezzz.android.vkauthorization.VK.VKAuthLocal;

public class AuthFragment extends Fragment {
    private static final String TAG = "vkauthorization";

    //Слушатель успешной авторизации
    public void setOnAuthSuccessListener(OnAuthSuccessListener onAuthSuccessListener) {
        this.onAuthSuccessListener = onAuthSuccessListener;
    }

    public interface OnAuthSuccessListener{
        void onAuthSuccess(String userId, String token);
    }

    private OnAuthSuccessListener onAuthSuccessListener;

    public static AuthFragment newInstance(){
        return new AuthFragment();
    }

    private Button buttonAuth;
    private VKAuthLocal local;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_auth, container, false);

        buttonAuth = v.findViewById(R.id.button_auth);

        local = new VKAuthLocal(getActivity());
        buttonAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Открытие активности для обработки авторизации
                createAuthWebViewFragment();
            }
        });

        return v;
    }

    private void createAuthWebViewFragment(){
        String url = VKAuth.createAuthorizationUrl( "friends");
        WebViewFragment fragment = WebViewFragment.newInstance(url);
        fragment.setOnSuccessAuthListener(new WebViewFragment.OnSuccessAuthListener() {
            @Override
            public void onSuccessAuth(String url) {

                String token = VKAuth.getAccessToken(url);
                String userId = VKAuth.getUserId(url);

                if(token != null && userId != null){
                    local.saveToken(token);
                    local.saveUserId(userId);
                    onAuthSuccessListener.onAuthSuccess(userId, token);
                }

            }
        });
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main,fragment)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        //Обработка данных, которые вернулись после перехвата активности с ответом

    }

}
