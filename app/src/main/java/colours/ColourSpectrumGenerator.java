
package colours;

/**
 * Created by Alequin on 16/12/2016.
 * updated 17/02/17
 */

public class ColourSpectrumGenerator {

    private RGBColour[] colours;
    private int coloursGenerated;
    private final int totalColours;
    
    private final RGBColour baseColour;
    private RGBColour RED;
    private RGBColour YELLOW;
    private RGBColour GREEN;
    private RGBColour CYAN;
    private RGBColour BLUE;
    private RGBColour MAGENTA;
    
    private ColourSpectrumGenerator(int numberOfColours, RGBColour baseColour){
        
        this.totalColours = numberOfColours;
        this.baseColour = baseColour;
        
        setPrimaryColours();
        colours = new RGBColour[totalColours];
        coloursGenerated = 0;
        
    }
            
    public static RGBColour[] generateColours(int numberOfColours){
        return generateColours(numberOfColours, new RGBColour(255,0,0));
    }
            
    public static RGBColour[] generateColours(int numberOfColours, RGBColour baseColour){
        
        if(numberOfColours <=0){
            throw new IllegalArgumentException("Number of colours must be greater than 0");
        }
        
        final ColourSpectrumGenerator generator = new ColourSpectrumGenerator(numberOfColours, baseColour);
        
        if(baseColour.getRed() == baseColour.getGreen() && baseColour.getRed() == baseColour.getBlue()){
            //If all colour values are equal the produced array will contain duplicates of the same colour  
            return generator.produceArrayOfSimilarColours();
        }else{
            return generator.produceColours();  
        }
    }
    
    /**
     * Generates an array of colours across the spectrum
     */
    private RGBColour[] produceColours(){

        final double incrementValue = calculateStepCount() / (double)totalColours;
        
        double red = RED.getRed();
        double green = RED.getGreen();
        double blue = RED.getBlue();

        final int minMaxRange = RED.getMaxColourValue() - RED.getMinColourValue();
        
        double leftOverIncrement = 0;

        PRIMARY_LOOP:do {
            while (green < YELLOW.getGreen()) {
                green += incrementValue;
                if (green <= YELLOW.getGreen()) {
                    if(addColour(red, green, blue)){
                        break PRIMARY_LOOP;
                    }
                }
            }

            leftOverIncrement = (green - YELLOW.getGreen());
            green = YELLOW.getGreen();
            if (leftOverIncrement > 0) {
                red -= leftOverIncrement;
                if (leftOverIncrement <= minMaxRange) {
                    if(addColour(red, green, blue)){
                        break PRIMARY_LOOP;
                    }
                }
            }

            while (red > GREEN.getRed()) {
                red -= incrementValue;
                if (red >= GREEN.getRed()) {
                    if(addColour(red, green, blue)){
                        break PRIMARY_LOOP;
                    }
                }
            }

            leftOverIncrement = GREEN.getRed() - red;
            red = GREEN.getRed();
            if (leftOverIncrement > 0) {
                blue += leftOverIncrement;
                if (leftOverIncrement <= minMaxRange) {
                    if(addColour(red, green, blue)){
                        break PRIMARY_LOOP;
                    }
                }
            }

            while (blue < CYAN.getBlue()) {
                blue += incrementValue;
                if (blue <= CYAN.getBlue()) {
                    if(addColour(red, green, blue)){
                        break PRIMARY_LOOP;
                    }
                }
            }

            leftOverIncrement = (blue - CYAN.getBlue());
            blue = CYAN.getBlue();
            if (leftOverIncrement > 0) {
                green -= leftOverIncrement;
                if (leftOverIncrement <= minMaxRange) {
                    if(addColour(red, green, blue)){
                        break PRIMARY_LOOP;
                    }
                }
            }

            while (green > BLUE.getGreen()) {
                green -= incrementValue;
                if (green >= BLUE.getGreen()) {
                    if(addColour(red, green, blue)){
                        break PRIMARY_LOOP;
                    }
                }
            }

            leftOverIncrement = BLUE.getGreen() - green;
            green = BLUE.getGreen();
            if (leftOverIncrement > 0) {
                red += leftOverIncrement;
                if (leftOverIncrement <= minMaxRange) {
                    if(addColour(red, green, blue)){
                        break PRIMARY_LOOP;
                    }
                }
            }

            while (red < MAGENTA.getRed()) {
                red += incrementValue;
                if (red <= MAGENTA.getRed()) {
                    if(addColour(red, green, blue)){
                        break PRIMARY_LOOP;
                    }
                }
            }

            leftOverIncrement = red - MAGENTA.getRed();
            red = MAGENTA.getRed();
            if (leftOverIncrement > 0) {
                blue -= leftOverIncrement;
                if (leftOverIncrement <= minMaxRange) {
                    if(addColour(red, green, blue)){
                        break PRIMARY_LOOP;
                    }
                }
            }

            while (blue > RED.getBlue()) {
                blue -= incrementValue;
                if (blue >= RED.getBlue()) {
                    if(addColour(red, green, blue)){
                        break PRIMARY_LOOP;
                    }
                }
            }
        }while(coloursGenerated < colours.length);

        return colours;
    };
    
    private RGBColour[] produceArrayOfSimilarColours(){
        for(int a=0; a<colours.length; a++){
                colours[a] = baseColour.clone();
            }
            return colours;
    }

    private boolean addColour(double red, double green, double blue){
        colours[coloursGenerated++] = new RGBColour(red, green, blue);
        return coloursGenerated >= colours.length;
    }
    
    private void setPrimaryColours(){
        
        final int min = baseColour.getMinColourValue();
        final int max = baseColour.getMaxColourValue();
        
        RED = new RGBColour(max, min, min);
        YELLOW = new RGBColour(max, max, min);
        GREEN = new RGBColour(min, max, min);
        CYAN = new RGBColour(min, max,max);
        BLUE = new RGBColour(min, min, max);
        MAGENTA = new RGBColour(max, min, max);
    }
    
    private int calculateStepCount(){

        return Math.abs(RED.getGreen() - YELLOW.getGreen()) +
                Math.abs(YELLOW.getRed() - GREEN.getRed()) +
                Math.abs(GREEN.getBlue() - CYAN.getBlue()) +
                Math.abs(CYAN.getGreen() - BLUE.getGreen()) +
                Math.abs(BLUE.getRed() - MAGENTA.getRed()) +
                Math.abs(MAGENTA.getBlue() - RED.getBlue());
    }
}
