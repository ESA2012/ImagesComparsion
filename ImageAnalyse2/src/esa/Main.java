package esa;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException{
        BufferedImage image1 = imageLoader(new File("image1.bmp"));
        BufferedImage image2 = imageLoader(new File("image2.bmp"));
        ImagesAnalyser holder = new ImagesAnalyser(image1, image2);
        if (holder.compareImages()) {
            imageSaver(new File("result1.png"), holder.getResultImage(ImagesAnalyser.Result.FIRST_IMAGE));
            imageSaver(new File("result2.png"), holder.getResultImage(ImagesAnalyser.Result.SECOND_IMAGE));
        }
    }


    public static BufferedImage imageLoader(File file) throws IOException {
        return ImageIO.read(file);
    }

    public static void imageSaver(File file, BufferedImage image) throws IOException {
        ImageIO.write(image, "PNG", file);
    }
}
