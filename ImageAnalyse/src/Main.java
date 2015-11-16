import esa.components.*;
import esa.components.exceptions.NoSuchBlockException;
import esa.utilities.ImageUtilities;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) throws NoSuchBlockException {

        // Change these parameters for better result.
        //
        // To compare BMP or PNG images (images without compression)
        // allowable error parameter must be 0.
        //
        // For JPG change OVERALL_ALLOWABLE_ERROR (1 - 5 is adequate value)
        //
        // Smaller block size = bigger allowable error value
        //
        // WARNING! There are no image comparsion by size and so on.

        final int OVERALL_ALLOWABLE_ERROR = 2;
        final ImageBlocks.BlockSize blockSize = ImageBlocks.BlockSize.BS_8;

        // Open two images
        ImageHolder i1 = new ImageHolder("wbear1.jpg");
        ImageHolder i2 = new ImageHolder("wbear2.jpg");

        // Split images for blocks
        ImageBlocks blocks1 = new ImageBlocks(i1, blockSize);
        ImageBlocks blocks2 = new ImageBlocks(i2, blockSize);

        int lines = blocks1.getLines();
        int rows = blocks2.getRows();

        // Compare color matrix for separate blocks and marks different ones
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < rows; j++) {
                ColorMatrix c1 = blocks1.getColorMatrix(i,j);
                ColorMatrix c2 = blocks2.getColorMatrix(i,j);
                if (!(c1.equals(c2, OVERALL_ALLOWABLE_ERROR))) {
                    blocks1.markBlock(i, j, ImageBlocks.FillType.MARK_FILL);
                    blocks2.markBlock(i, j, ImageBlocks.FillType.MARK_FILL);
                }
            }
        }

        // Creates new result image
        BufferedImage resultImage = new BufferedImage(i1.getWidth()+i2.getWidth(), i1.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resultImage.createGraphics();
        g2.drawImage(blocks1.makeImage(), 0, 0, null);
        g2.drawImage(blocks2.makeImage(), i2.getWidth(), 0, null);

        // Save result image to disk
        ImageUtilities.savePNGImage(resultImage, "result.png");


    }
}
