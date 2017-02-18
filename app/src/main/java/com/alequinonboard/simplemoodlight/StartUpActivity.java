package com.alequinonboard.simplemoodlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.alequinonboard.simplemoodlight.colours.ColourManager;
import com.google.android.gms.ads.MobileAds;

public class StartUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        final Intent toMainActivity = new Intent(this, MainActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MobileAds.initialize(getApplicationContext(), getString(R.string.app_id_ad_mob));
                ColourManager.getInstance().buildColours();
                startActivity(toMainActivity);
                finish();
            }
        },1000);
    }
}
