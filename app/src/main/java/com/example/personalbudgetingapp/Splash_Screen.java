package com.example.personalbudgetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Splash_Screen extends AppCompatActivity {
    private static int splash = 4000;
    Animation animation;
    private TextView app_name;
    private ImageView image_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

    animation= AnimationUtils.loadAnimation(this, R.anim.animation);

    app_name=findViewById(R.id.s_app_name_id);
        image_view=findViewById(R.id.s_image_view_id);

        image_view.setAnimation(animation);
        app_name.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =  new Intent(Splash_Screen.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }
        }, splash);


    }


    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            startActivity(new Intent(Splash_Screen.this, MainActivity.class));
            finish();
        }
    }
}