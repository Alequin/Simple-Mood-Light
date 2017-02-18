package com.alequinonboard.simplemoodlight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alequinonboard.simplemoodlight.colours.ColourManager;
import com.alequinonboard.simplemoodlight.colours.RGBColour;
import com.alequinonboard.simplemoodlight.interfaceControl.DoubleTapCheck;

public class MainActivity extends Activity implements View.OnTouchListener{

    private RelativeLayout background;
    private LinearLayout introLayout;
    private boolean isIntroLayoutVisible = true;
    private ColourManager colourManager;

    private final DoubleTapCheck doubleTapCheck = new DoubleTapCheck();

    private final Handler autoColourHandler = new Handler();

    private static final String RED_SAVED = "RED_SAVED";
    private static final String GREEN_SAVED = "GREEN_SAVED";
    private static final String BLUE_SAVED = "BLUE_SAVED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        stopAutoColourMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        if(view.getId() == R.id.attribution_text){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.freepik.com/free-vector/man-head-forming-a-bulb_718375.htm")));
            return true;
        }else {

            if (isIntroLayoutVisible) {
                toggleIntroVisibility(View.GONE);
            }

            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                stopAutoColourMode();
                if(doubleTapCheck.isDoubleTap()) {
                    startAutoColourMode();
                }
            }

            setBackgroundFromPosition(motionEvent.getX(), motionEvent.getY());
        }
        return true;
    }

    private void startAutoColourMode(){
        autoColourHandler.post(new Runnable() {

            private int xIndex = colourManager.numberOfColours / 2;
            private int yIndex = 0;

            @Override
            public void run() {

                long time1 = System.currentTimeMillis();
                setBackgroundColour(colourManager.getColourAt(xIndex, yIndex));
                yIndex += 3;

                if (yIndex >= colourManager.numberOfColours) {
                    yIndex = 0;
                }
                long time2 = System.currentTimeMillis();

                autoColourHandler.postDelayed(this, 100-(time2 - time1));
            }
        });
    }

    private void stopAutoColourMode(){
        autoColourHandler.removeCallbacksAndMessages(null);
    }

    private void setBackgroundFromPosition(float x, float y){

        int xPosition = getXPositionAsPercentage(x);
        int yPosition = getYPositionAsPercentage(y);

        setBackgroundColour(colourManager.getColourAt(xPosition, yPosition));
    }

    private void toggleIntroVisibility(int visibility){

        if(visibility != View.VISIBLE && visibility != View.GONE){
            throw new IllegalArgumentException("input must either be View.VISIBLE or View.GONE.");
        }

        introLayout.setVisibility(visibility);
        isIntroLayoutVisible = visibility == View.VISIBLE;

    }

    private void setBackgroundColour(RGBColour colour){
        background.setBackgroundColor(Color.rgb(colour.getRed(), colour.getGreen(), colour.getBlue()));
    }

    private int getXPositionAsPercentage(float xValues){
        float xAsPercent = (xValues / background.getWidth())*colourManager.numberOfColours;
        return checkValueInRange((int)xAsPercent);
    }
    private int getYPositionAsPercentage(float yValue){
        float yAsPercent = (yValue / background.getHeight())*colourManager.numberOfColours;
        return checkValueInRange((int)yAsPercent);
    }

    /**
     * Checks if the given value is in the range of 0 - 100. If it is outside this
     * range the returned value will be either 0 or 100 depending on which side it
     * is on.
     */
    private int checkValueInRange(int value){

        final int upperBound = colourManager.numberOfColours-1;

        if(value > upperBound){
            return upperBound;
        }else
        if(value < 0){
            return 0;
        }else{
            return value;
        }
    }
}
