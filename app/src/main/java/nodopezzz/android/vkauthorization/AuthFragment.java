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

    public interface OnAuthButtonClickListener{
        void onAuthButtonClick();
    }

    private OnAuthButtonClickListener onAuthButtonClickListener;
    public void setOnAuthButtonClickListener(OnAuthButtonClickListener listener){
        onAuthButtonClickListener = listener;
    }

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
                if(onAuthButtonClickListener != null){
                    onAuthButtonClickListener.onAuthButtonClick();
                }
            }
        });

        return v;
    }

}
