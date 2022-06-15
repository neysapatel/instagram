package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class PostDetailsActivity extends AppCompatActivity {

    TextView tvUsername;
    ImageView ivPic;
    TextView tvCaption;
    TextView tvTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        Post post = getIntent().getParcelableExtra("post");

        tvUsername = findViewById(R.id.tvUsername);
        ivPic = findViewById(R.id.ivPic);
        tvCaption = findViewById(R.id.tvCaption);
        tvTimestamp = findViewById(R.id.tvTimestap);

        tvUsername.setText(post.KEY_USER);
        tvCaption.setText(post.KEY_DESCRIPTION);
       // tvTimestamp.setText(post.getUser().getUsername());

        Glide.with(this).load(post.KEY_IMAGE).apply(new RequestOptions().centerCrop().transform(new RoundedCorners(10))).into(ivPic);
    }
}