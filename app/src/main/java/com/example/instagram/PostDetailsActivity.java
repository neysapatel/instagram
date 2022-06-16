package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.Date;

public class PostDetailsActivity extends AppCompatActivity {

    TextView tvUsername;
    TextView tvUsername2;
    ImageView ivPic;
    TextView tvCaption;
    TextView tvTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        Post post = getIntent().getParcelableExtra("post");

        tvUsername = findViewById(R.id.tvUsername);
        tvUsername2 = findViewById(R.id.tvUsername2);
        ivPic = findViewById(R.id.ivPic);
        tvCaption = findViewById(R.id.tvCaption);
        tvTimestamp = findViewById(R.id.tvTimestap);

        String uname = post.getUser().getUsername();
        tvUsername.setText(uname);
        tvUsername2.setText(uname);
        tvCaption.setText(post.getDescription());

        Date createdAt = post.getCreatedAt();
        String timeAgo = Post.calculateTimeAgo(createdAt);
        tvTimestamp.setText(timeAgo);

        Glide.with(this).load(post.getImage().getUrl()).apply(new RequestOptions().centerCrop().transform(new RoundedCorners(10))).into(ivPic);
    }
}