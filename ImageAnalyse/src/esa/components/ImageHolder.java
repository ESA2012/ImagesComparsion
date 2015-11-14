package esa.components;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by SnakE on 14.11.2015.
 */
public class ImageHolder {

    private BufferedImage bi;


    /**
     * Creates ImageHolder object for given image file
     * @param fileName    image file name
     */
    public ImageHolder(String fileName) {
        File f = new File(fileName);
        try {
            bi = ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Returns BufferedImage object
     * @return BufferedImage
     */
    public BufferedImage getImage() {
        return bi;
    }


    /**
     * Returns image width
     * @return image width
     */
    public int getWidth() {
        return bi.getWidth();
    }


    /**
     * Returns image height
     * @return image height
     */
    public int getHeight() {
        return bi.getHeight();
    }

}
