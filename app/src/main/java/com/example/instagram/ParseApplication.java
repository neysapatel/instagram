package com.example.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("fS8BvaN9guf97JdYIBVDrjv5N3ZK3N5hVnHCjIDf")
                .clientKey("sZJnsMmH4tCNNprLAqwJgEK54gEi2aOVsLvKA1a5")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
