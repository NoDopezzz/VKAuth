package nodopezzz.android.vkauthorization;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nodopezzz.android.vkauthorization.Models.ResponseUsers;
import nodopezzz.android.vkauthorization.Models.UsersContainer;
import nodopezzz.android.vkauthorization.VK.VKApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {
    private static final String TAG = "vkauthorization";

    //Слушатель ошибки
    public void setOnFailListener(OnFailListener onFailListener) {
        this.onFailListener = onFailListener;
    }

    public interface OnFailListener{
        void onFailed();
    }

    private OnFailListener onFailListener;

    //Слушатель кнопки "Выйти"
    public void setOnQuitListener(OnQuitListener onQuitListener) {
        this.onQuitListener = onQuitListener;
    }

    public interface OnQuitListener{
        void onQuit();
    }

    private OnQuitListener onQuitListener;

    private static final String ARG_USER_ID = "ARG_USER_ID";
    private static final String ARG_TOKEN = "ARG_TOKEN";

    public static MainFragment newInstance(String userId, String token){
        Bundle bundle = new Bundle();
        bundle.putString(ARG_USER_ID, userId);
        bundle.putString(ARG_TOKEN, token);

        MainFragment fragment = new MainFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private TextView profileNameView;
    private CircleImageView profileImage;
    private RecyclerView listFriends;
    private Button quitButton;

    private String userId;
    private String token;

    private UsersContainer.User user;

    private boolean isFailed = false; //Не дает вызваться двум ошибкам вместе
    private FriendsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        profileNameView = v.findViewById(R.id.profile_name);
        profileImage = v.findViewById(R.id.profile_image);
        listFriends = v.findViewById(R.id.list_friends);
        quitButton = v.findViewById(R.id.quit_button);

        Bundle args = getArguments();
        userId = args.getString(ARG_USER_ID);
        token = args.getString(ARG_TOKEN);

        setupUser();
        setupFriends();
        setupQuit();

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(adapter != null) {
            adapter.quitThumbnailDownloader();
        }
    }

    //Получение данных о пользователе и заполнение UI
    private void setupUser(){
        VKApi.getInstance().getAdapter()
                .getUser(userId, token, "photo_100,photo_200")
                .enqueue(new Callback<UsersContainer>() {
            @Override
            public void onResponse(Call<UsersContainer> call, Response<UsersContainer> response) {
                if(response.body() == null ||
                        response.body().getUsers() == null ||
                        response.body().getUsers().get(0) == null){
                    //Обработка ошибки
                    if(!isFailed) {
                        isFailed = true;
                        onFailListener.onFailed();
                    }
                    return;
                }
                user = response.body().getUsers().get(0);
                profileNameView.setText(user.getFirstName() + " " + user.getLastName());
                new LoadProfileImage().execute();
            }

            @Override
            public void onFailure(Call<UsersContainer> call, Throwable t) {
                //Обработка ошибки
                if(!isFailed) {
                    isFailed = true;
                    onFailListener.onFailed();
                };
            }
        });
    }

    //Получение данных о друзьях и заполнение UI
    private void setupFriends(){
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        listFriends.setLayoutManager(manager);

        VKApi.getInstance().getAdapter().getFriends(
                userId,
                token,
                "photo_100,photo_200",
                "random",
                "5").enqueue(new Callback<ResponseUsers>() {
            @Override
            public void onResponse(Call<ResponseUsers> call, Response<ResponseUsers> response) {
                if(response.body() == null
                        || response.body().getResponse() == null
                        || response.body().getResponse().getUsers() == null){
                    //Обработка ошибки
                    if(!isFailed) {
                        isFailed = true;
                        onFailListener.onFailed();
                    }
                    return;
                }
                List<UsersContainer.User> friends = response.body().getResponse().getUsers();
                adapter = new FriendsAdapter(getActivity(), friends);
                listFriends.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseUsers> call, Throwable t) {
                //Обработка ошибки
                if(!isFailed) {
                    isFailed = true;
                    onFailListener.onFailed();
                }
            }
        });
    }

    private void setupQuit(){
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onQuitListener != null){
                    onQuitListener.onQuit();
                }
            }
        });
    }

    //Загрузка картинки профиля
    public class LoadProfileImage extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                return BitmapDownloader.loadBitmap(user.getPhoto200Url());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap == null) return;
            profileImage.setImageBitmap(bitmap);
        }
    }

}
