package colours;

import android.util.Log;

import java.math.BigDecimal;

/**
 * Created by Alequin on 17/02/2017.
 */

public class ColourManager {

    private static final ColourManager singleton = new ColourManager();

    private final RGBColour[][] colours;
    public final int numberOfColours = 100;

    public static ColourManager getInstance(){
        return singleton;
    }

    private ColourManager() {
        colours = new RGBColour[numberOfColours][numberOfColours];
    }

    public void buildColours(){

        BigDecimal red = new BigDecimal(255);
        BigDecimal green = new BigDecimal(255);
        BigDecimal blue = new BigDecimal(255);

        final BigDecimal interval = new BigDecimal(255d / (double)(numberOfColours/2));

        RGBColour baseColour = new RGBColour(0,0,0);
        for(int a=0; a<colours.length; a++){

            if(green.intValue() > 0 && blue.intValue() > 0){
                baseColour.setColour(red.intValue(), green.intValue(), blue.intValue());
                addColourSpectrum(a, baseColour);
                green = green.subtract(interval);
                blue = blue.subtract(interval);
            }else{
                baseColour.setColour(red.intValue(), green.intValue(), blue.intValue());
                addColourSpectrum(a, baseColour);
                red = red.subtract(interval);
            }
        }
    }

    public RGBColour getColourAt(int column, int row){

        if(column < 0 || column > 100 || row < 0 || row > 100){
            throw new IllegalArgumentException("All values must be between 0 and 100 inclusively");
        }

        return colours[column][row];
    }

    private void addColourSpectrum(int index, RGBColour baseColour){
        colours[index] = ColourSpectrumGenerator.generateColours(numberOfColours, baseColour);
    }
}
