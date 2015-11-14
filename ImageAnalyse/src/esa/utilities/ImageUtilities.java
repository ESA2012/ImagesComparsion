package esa.utilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by SnakE on 14.11.2015.
 */
public class ImageUtilities {


    /**
     * Saves BufferedImage object to PNG image file
     * @param image        BufferedImage object
     * @param imageFile    filename
     * @return             true or false
     */
    public static boolean savePNGImage(BufferedImage image, String imageFile) {
        try {
            File outF = new File(imageFile);
            ImageIO.write(image, "PNG", outF);
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    /**
     * Extends image width and height (bSz multiple)
     * @param img    BufferdImage object
     * @param bSz    multiple
     * @return      new ufferdImage object
     */
    public static BufferedImage newSizeImage(BufferedImage img, int bSz) {
        int h = img.getHeight();
        int w = img.getWidth();

        int newHeight = h;
        if (h % bSz != 0) {
            newHeight = ((h / bSz) * bSz) + bSz;
        }
        int newWidth = w;
        if (w % bSz != 0) {
            newWidth = ((w / bSz) * bSz) + bSz;
        }

        BufferedImage newbi;
        if (h != newHeight || w != newWidth) {
            newbi = new BufferedImage(newWidth, newHeight, img.getType());
            Graphics2D g = newbi.createGraphics();
            g.drawImage(img, 0, 0, null);
        } else {
            newbi = img;
        }
        return newbi;
    }

}
