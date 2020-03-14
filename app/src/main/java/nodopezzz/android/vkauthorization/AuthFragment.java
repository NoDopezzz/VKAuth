package nodopezzz.android.vkauthorization;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
                VKAuth.startAuthorizationActivity(getActivity(), "friends");
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Обработка данных, которые вернулись после перехвата активности с ответом
        String token = VKAuth.getAccessToken(getActivity().getIntent().getData());
        String userId = VKAuth.getUserId(getActivity().getIntent().getData());

        //Обнуление data для того, чтобы не было повторного запуска onAuthSuccess при ошибке
        getActivity().getIntent().setData(null);

        Log.i(TAG, userId + ": " + token);

        if(token != null && userId != null){
            local.saveToken(token);
            local.saveUserId(userId);
            onAuthSuccessListener.onAuthSuccess(userId, token);
        }
    }

}
