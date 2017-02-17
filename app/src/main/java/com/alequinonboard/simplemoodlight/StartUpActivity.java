package com.alequinonboard.simplemoodlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import colours.ColourManager;

public class StartUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        final Intent toMainActivity = new Intent(this, MainActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ColourManager.getInstance().buildColours();
                startActivity(toMainActivity);
                finish();
            }
        },1000);
    }
}
