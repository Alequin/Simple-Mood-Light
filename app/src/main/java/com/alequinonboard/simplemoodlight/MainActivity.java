package com.alequinonboard.simplemoodlight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import colours.ColourManager;
import colours.RGBColour;

public class MainActivity extends Activity implements View.OnTouchListener{

    private RelativeLayout background;
    private LinearLayout introLayout;
    private boolean isIntroLayoutVisible = true;
    private ColourManager colourManager;

    private SharedPreferences lastUsedColour;
    private static final String RED_SAVED = "RED_SAVED";
    private static final String GREEN_SAVED = "GREEN_SAVED";
    private static final String BLUE_SAVED = "BLUE_SAVED";

    private RGBColour currentColour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lastUsedColour = this.getSharedPreferences("lastUsedColour", Context.MODE_PRIVATE);

        colourManager = ColourManager.getInstance();

        background = (RelativeLayout) findViewById(R.id.main_background);
        introLayout = (LinearLayout) findViewById(R.id.intro_layout);

        findViewById(R.id.attribution_text).setOnTouchListener(this);
        background.setOnTouchListener(this);
        introLayout.setOnTouchListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveColourAsLastUsed(currentColour);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBackgroundColour(loadLastUsedColour());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if(view.getId() == R.id.attribution_text){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.freepik.com/free-vector/man-head-forming-a-bulb_718375.htm")));
            return true;
        }else{
            if(isIntroLayoutVisible){
                toggleIntroVisibility(View.GONE);
            }

            int xPosition = getXPositionAsPercentage(motionEvent.getX());
            int yPosition = getYPositionAsPercentage(motionEvent.getY());

            setBackgroundColour(colourManager.getColourAt(xPosition, yPosition));

            return true;
        }
    }

    private void toggleIntroVisibility(int visibility){

        if(visibility != View.VISIBLE && visibility != View.GONE){
            throw new IllegalArgumentException("input must either be View.VISIBLE or View.GONE.");
        }

        introLayout.setVisibility(visibility);
        isIntroLayoutVisible = visibility == View.VISIBLE;

    }

    private void setBackgroundColour(RGBColour colour){
        currentColour = colour;
        background.setBackgroundColor(Color.rgb(colour.getRed(), colour.getGreen(), colour.getBlue()));
    }

    private int getXPositionAsPercentage(float xValues){
        float xAsPercent = (xValues / background.getWidth())*colourManager.numberOfColours;
        int result = checkValueInRange((int)xAsPercent);
        return result;
    }
    private int getYPositionAsPercentage(float yValue){
        float yAsPercent = (yValue / background.getHeight())*colourManager.numberOfColours;
        int result = checkValueInRange((int)yAsPercent);
        return result;
    }

    /**
     * Checks if the given value is in the range of 0 - 100. If it is outside this
     * range the returned value will be either 0 or 100 depending on which side it
     * is on.
     */
    private int checkValueInRange(int value){
        if(value > colourManager.numberOfColours-1){
            return colourManager.numberOfColours-1;
        }else
        if(value < 0){
            return 0;
        }else{
            return value;
        }
    }

    private RGBColour loadLastUsedColour(){
        return new RGBColour(
                lastUsedColour.getInt(RED_SAVED, 255),
                lastUsedColour.getInt(GREEN_SAVED, 255),
                lastUsedColour.getInt(BLUE_SAVED, 255)
        );
    }

    private void saveColourAsLastUsed(RGBColour colour){

        SharedPreferences.Editor edit = lastUsedColour.edit();
        edit.putInt(RED_SAVED, colour.getRed());
        edit.putInt(GREEN_SAVED, colour.getGreen());
        edit.putInt(BLUE_SAVED, colour.getBlue());
        edit.apply();
    }

}
