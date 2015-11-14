package esa.components;

import java.awt.*;

/**
 * Created by SnakE on 14.11.2015.
 */
public class ColorMatrix {
    private Color[][] colors;

    /**
     * Creates ColorMatrix object with given size
     * @param size  side size of 2d array
     */
    public ColorMatrix(int size) {
        colors = new Color[size][size];
    }


    /**
     * Returns 2D array of Colors
     * @return Color[][]
     */
    public Color[][] getColorsArray() {
        return colors;
    }


    /**
     * Calculate average color value for matrix
     * @return Color
     */
    public Color getAverageColor() {
        int size = colors.length;
        int red = 0;
        int green = 0;
        int blue = 0;
        for (Color[] color : colors) {
            for (int b = 0; b < size; b++) {
                red += color[b].getRed();
                green += color[b].getGreen();
                blue += color[b].getBlue();
            }
        }
        red /=size*size;
        green /=size*size;
        blue /=size*size;
        return new Color(red, green, blue);
    }



    public int hashCode() {
        int z = 31;
        for (Color[] color : colors) {
            for (int b = 0; b < color.length; b++) {
                z += color[b].getBlue() + color[b].getRed() + color[b].getGreen();
            }
        }
        return z;
    }


    /**
     * Compares objects
     * @param o Object
     * @return true or false
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColorMatrix)) {
            return false;
        }
        return  (this.getAverageColor().equals(((ColorMatrix) o).getAverageColor()));
    }


    /**
     * Compares Color matrix objects with given allowable error (delta) for each color
     * @param o                      ColorMatrix object
     * @param allowableErrorRed      allowable error (delta) for R (red)
     * @param allowableErrorGreen    allowable error (delta) for G (green)
     * @param allowableErrorBlue     allowable error (delta) for B (blue)
     * @return                       true or false
     */
    public boolean equals(ColorMatrix o, int allowableErrorRed, int allowableErrorGreen, int allowableErrorBlue) {
        int currentR = this.getAverageColor().getRed();
        int currentG = this.getAverageColor().getGreen();
        int currentB = this.getAverageColor().getBlue();

        int r = o.getAverageColor().getRed();
        int g = o.getAverageColor().getGreen();
        int b = o.getAverageColor().getBlue();

        int deltaR = Math.abs(currentR - r);
        int deltaG = Math.abs(currentG - g);
        int deltaB = Math.abs(currentB - b);

        return !((deltaR > allowableErrorRed) | (deltaG > allowableErrorGreen) | (deltaB > allowableErrorBlue));
    }


    /**
     * Compares Color matrix objects with given overall allowable error (delta)
     * @param o                 ColorMatrix object
     * @param allowableError    overall allowable error (delta) (for each color)
     * @return                  true or false
     */
    public boolean equals(ColorMatrix o, int allowableError) {
        return this.equals(o, allowableError, allowableError,allowableError);
    }



}
