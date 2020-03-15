package nodopezzz.android.vkauthorization.VK.Auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import nodopezzz.android.vkauthorization.R;

public class WebViewFragment extends Fragment {

    public interface OnSuccessAuthListener{
        void onSuccessAuth(String url);
    }

    private OnSuccessAuthListener onSuccessAuthListener;
    public void setOnSuccessAuthListener(OnSuccessAuthListener onSuccessAuthListener) {
        this.onSuccessAuthListener = onSuccessAuthListener;
    }

    private static final String ARG_URL = "ARG_URL";

    public static WebViewFragment newInstance(String url){
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_webview, container, false);
        WebView webView = v.findViewById(R.id.webview);

        String url = getArguments().getString(ARG_URL);

        VKAuthWebClient client = new VKAuthWebClient() {
            @Override
            public void onSuccessAuth(String url) {
                if(onSuccessAuthListener != null){
                    onSuccessAuthListener.onSuccessAuth(url);
                }
            }
        };
        webView.setWebViewClient(client);
        webView.loadUrl(url);

        return v;
    }
}
