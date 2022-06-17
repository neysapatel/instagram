package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView tvUsername2;
        private TextView tvTimestamp;
        private ImageView ivProfilPic;
        private ImageButton ibLike;
        private TextView tvLikes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvUsername2 = itemView.findViewById(R.id.tvUsername2);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTimestamp = itemView.findViewById(R.id.tvTimestap);
            ivProfilPic = itemView.findViewById(R.id.ivProfilePic);
            ibLike = itemView.findViewById(R.id.ivLike);
            tvLikes = itemView.findViewById(R.id.tvLikes);
        }

        public void bind(Post post) {
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            tvUsername2.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }

            ParseFile profilePic = post.getUser().getParseFile("profilePic");
            if (profilePic != null) {
                Glide.with(context).load(profilePic.getUrl()).apply(RequestOptions.circleCropTransform()).into(ivProfilPic);
            }

            Date createdAt = post.getCreatedAt();
            String timeAgo = Post.calculateTimeAgo(createdAt);
            tvTimestamp.setText(timeAgo);

            int likes = 0;
            String numLikes = post.getString("likes");
            if (numLikes != null) {
                likes = new Integer(post.getString("likes"));
            }
            tvLikes.setText(likes + " likes");

            int updatedLikes = likes + 1;
            ibLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseUser.getCurrentUser().put("likes", updatedLikes);
                    tvLikes.setText(updatedLikes + " likes");
                    ibLike.setImageResource(R.drawable.ufi_heart_active);
                }
            });

            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, PostDetailsActivity.class);
                    i.putExtra("post", post);
                    context.startActivity(i);
                }
            });
        }
    }
}