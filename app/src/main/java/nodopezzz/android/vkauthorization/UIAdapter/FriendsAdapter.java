package nodopezzz.android.vkauthorization.UIAdapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nodopezzz.android.vkauthorization.Models.UsersContainer;
import nodopezzz.android.vkauthorization.Network.ThumbnailDownloader;
import nodopezzz.android.vkauthorization.R;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendHolder> {
    private static final String TAG = "FriendAdapter";

    private Context context;
    private List<UsersContainer.User> users;

    private ThumbnailDownloader downloader;

    public FriendsAdapter(Context context, List<UsersContainer.User> users){
        this.context = context;
        this.users = users;

        downloader = new ThumbnailDownloader(TAG, new Handler());
        downloader.start();
        downloader.getLooper();
    }

    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false);
        return new FriendHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {
        holder.bindView(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class FriendHolder extends RecyclerView.ViewHolder{

        private CircleImageView profileImageView;
        private TextView profileNameView;

        public FriendHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profile_image);
            profileNameView = itemView.findViewById(R.id.profile_name);
        }

        public void bindView(UsersContainer.User user){
            downloader.putInQueue(profileImageView, user.getPhoto100Url());
            profileNameView.setText(user.getFirstName() + " " + user.getLastName());
        }
    }

    public void quitThumbnailDownloader(){
        downloader.clearQueue();
        downloader.quit();
    }
}
