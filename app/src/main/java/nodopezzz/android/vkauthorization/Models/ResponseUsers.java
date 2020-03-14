package nodopezzz.android.vkauthorization.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseUsers {

    @SerializedName("response")
    @Expose
    private Response response;

    public Response getResponse() {
        return response;
    }

    public class Response{
        @SerializedName("items")
        @Expose
        private List<UsersContainer.User> users;

        public List<UsersContainer.User> getUsers() {
            return users;
        }
    }
}
