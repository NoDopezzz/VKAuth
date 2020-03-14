package nodopezzz.android.vkauthorization.VK;

import nodopezzz.android.vkauthorization.Models.ResponseUsers;
import nodopezzz.android.vkauthorization.Models.UsersContainer;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VKApiAdapter {
    @GET("users.get")
    public Call<UsersContainer> getUser(@Query("user_id") String userId, @Query("access_token") String token, @Query("fields") String fields);

    @GET("friends.get")
    public Call<ResponseUsers> getFriends(@Query("user_id") String userId,
                                          @Query("access_token") String token,
                                          @Query("fields") String fields,
                                          @Query("order") String order,
                                          @Query("count") String count);
}
