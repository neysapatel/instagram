package com.example.instagram.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.instagram.LoginActivity;
import com.example.instagram.Post;
import com.example.instagram.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class SettingsFragment extends Fragment {
    public final String APP_TAG = "Instagram";
    final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    String photoFileName = "photo.jpg";
    File photoFile;

    TextView tvUsername;
    TextView tvName;
    ImageView ivProfilePic;
    Button newProfilePicButton;
    ImageView ivNewProfilePic;
    Button setNewProfilePicBtn;
    Button logoutButton;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUsername = view.findViewById(R.id.tvUserName);
        tvName = view.findViewById(R.id.tvName);
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        ivNewProfilePic = view.findViewById(R.id.ivNewProfilePic);
        setNewProfilePicBtn = view.findViewById(R.id.setNewProfilePicBtn);
        newProfilePicButton = view.findViewById(R.id.newProfilePicBtn);
        logoutButton = view.findViewById(R.id.logoutBtn);

        ParseUser currentUser = ParseUser.getCurrentUser();

        tvUsername.setText(currentUser.getUsername());
        tvName.setText(currentUser.getString("name"));

        ParseFile profilePic = currentUser.getParseFile("profilePic");
        if (profilePic != null) {
            Glide.with(this).load(profilePic.getUrl()).apply(RequestOptions.circleCropTransform()).into(ivProfilePic);
        }

        newProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        setNewProfilePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((photoFile == null) || (ivNewProfilePic.getDrawable() == null)) {
                    Toast.makeText(getContext(), "no image found!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                saveProfilePic(currentUser, photoFile);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground();
                ParseUser currentUser = ParseUser.getCurrentUser();
                goLoginActivity();
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
                ivNewProfilePic.setImageBitmap(takenImage);
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

    private void saveProfilePic(ParseUser currentUser, File photoFile) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bitmapData = bos.toByteArray();

        ParseFile newImageFile = new ParseFile(bitmapData);

        currentUser.put("profilePic", newImageFile);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Toast.makeText(getContext(), "Error posting", Toast.LENGTH_SHORT).show();
                } else {
                    ivNewProfilePic.setImageResource(0);
                }
            }
        });
    }

    private void goLoginActivity() {
        Intent i = new Intent(getContext(), LoginActivity.class);
        startActivity(i);
    }
}
