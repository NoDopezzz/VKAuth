package nodopezzz.android.vkauthorization.VK;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VKApi {

    private static VKApi instance;
    public static VKApi getInstance(){
        if(instance == null){
            instance = new VKApi();
        }
        return instance;
    }

    private static final String ENDPOINT = "https://api.vk.com/method/";

    private Retrofit retrofit;

    private VKApi(){

        //Перехват каждого запроса к серверу и добавление параметра версии api и требуемых полей
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("v", "5.52")
                        .build();

                Request.Builder requestBuilder = original.newBuilder().url(url);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        //Создание метода retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build();
    }

    public VKApiAdapter getAdapter(){
        return retrofit.create(VKApiAdapter.class);
    }
}
