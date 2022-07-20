package com.example.medibuddy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.medibuddy.util.Util;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        ImageView mSplashText = (ImageView) findViewById(R.id.splash_logo1);
        ImageView mSplashLogo = (ImageView) findViewById(R.id.splash_logo);

        Animation uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        Animation downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);

        mSplashText.setAnimation(downtoup);
        mSplashLogo.setAnimation(uptodown);


            if(Util.isConnected(SplashActivity.this))
            {

            }
            else
            {
                Toast.makeText(SplashActivity.this, "No internet available", Toast.LENGTH_SHORT).show();
            }

        new CountDownTimer(3000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Intent i = new Intent(SplashActivity.this, SigninActivity.class);
                startActivity(i);
            }
        }.start();


    }



    }
