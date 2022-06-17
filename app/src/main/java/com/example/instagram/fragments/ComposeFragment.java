package com.example.instagram.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.instagram.FeedActivity;
import com.example.instagram.LoginActivity;
import com.example.instagram.MainActivity;
import com.example.instagram.Post;
import com.example.instagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class ComposeFragment extends Fragment {
    public final String APP_TAG = "Instagram";
    final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    String photoFileName = "photo.jpg";
    File photoFile;

    Button logoutButton;
    EditText etDescription;
    Button takePicButton;
    ImageView ivPostImage;
    Button postButton;
    Button feedButton;

    public ComposeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etDescription = view.findViewById(R.id.etDescription);
        takePicButton = view.findViewById(R.id.picBtn);
        ivPostImage = view.findViewById(R.id.ivPicture);
        postButton = view.findViewById(R.id.postBtn);
        feedButton = view.findViewById(R.id.feedBtn);
        logoutButton = view.findViewById(R.id.logOutBtn);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground();
                ParseUser currentUser = ParseUser.getCurrentUser();
                goLoginActivity();
            }
        });

        takePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });


        postButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String dsc = etDescription.getText().toString();
                if (dsc.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a caption!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ((photoFile == null) || (ivPostImage.getDrawable() == null)) {
                    Toast.makeText(getContext(), "no image found!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(dsc, currentUser, photoFile);
            }
        });

        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goFeedActivity();
            }
        });
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivPostImage.setImageBitmap(takenImage);
            } else {
                Toast.makeText(getContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
        }

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void savePost(String dsc, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(dsc);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(getContext(), "Error posting", Toast.LENGTH_SHORT).show();
                }
                etDescription.setText("");
                ivPostImage.setImageResource(0);
            }
        });
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    return;
                }

                for (Post post : posts) {
                    // do something
                }
            }
        });
    }

    private void goLoginActivity() {
        Intent i = new Intent(getContext(), LoginActivity.class);
        startActivity(i);
    }

    private void goFeedActivity() {
        Intent i = new Intent(getContext(), FeedActivity.class);
        startActivity(i);
    }
}