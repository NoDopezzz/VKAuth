package nodopezzz.android.vkauthorization.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UsersContainer {

    @SerializedName("response")
    @Expose
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public class User {
        @SerializedName("id")
        @Expose
        private int id;

        @SerializedName("first_name")
        @Expose
        private String firstName;

        @SerializedName("last_name")
        @Expose
        private String lastName;

        @SerializedName("photo_100")
        @Expose
        private String photo100Url;

        @SerializedName("photo_200")
        @Expose
        private String photo200Url;

        public int getId() {
            return id;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getPhoto100Url() {
            return photo100Url;
        }

        public String getPhoto200Url() {
            return photo200Url;
        }
    }
}
