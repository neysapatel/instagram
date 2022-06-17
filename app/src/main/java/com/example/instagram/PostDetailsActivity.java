package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.Date;

public class PostDetailsActivity extends AppCompatActivity {

    TextView tvUsername;
    TextView tvUsername2;
    ImageView ivPic;
    TextView tvCaption;
    TextView tvTimestamp;
    ImageButton ibLike;
    TextView tvLikes;
    ImageView ivProfilPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        Post post = getIntent().getParcelableExtra("post");

        tvUsername = findViewById(R.id.tvUsername);
        tvUsername2 = findViewById(R.id.tvUsername2);
        ivPic = findViewById(R.id.ivImage);
        tvCaption = findViewById(R.id.tvDescription);
        tvTimestamp = findViewById(R.id.tvTimestap);
        ibLike = findViewById(R.id.ivLike);
        tvLikes = findViewById(R.id.tvLikes);
        ivProfilPic = findViewById(R.id.ivProfilePic);

        String uname = post.getUser().getUsername();
        tvUsername.setText(uname);
        tvUsername2.setText(uname);
        tvCaption.setText(post.getDescription());

        Date createdAt = post.getCreatedAt();
        String timeAgo = Post.calculateTimeAgo(createdAt);
        tvTimestamp.setText(timeAgo);

        Glide.with(this).load(post.getImage().getUrl()).apply(new RequestOptions().centerCrop().transform(new RoundedCorners(10))).into(ivPic);

        ParseFile profilePic = post.getUser().getParseFile("profilePic");
        if (profilePic != null) {
            Glide.with(this).load(profilePic.getUrl()).apply(RequestOptions.circleCropTransform()).into(ivProfilPic);
        }

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
    }
}