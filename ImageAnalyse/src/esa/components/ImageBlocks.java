package esa.components;

import esa.components.exceptions.NoSuchBlockException;

import java.awt.*;
import java.awt.image.BufferedImage;
import static esa.utilities.ImageUtilities.newSizeImage;

/**
 * Created by SnakE on 14.11.2015.
 */
public class ImageBlocks {
    private BufferedImage [][] subs;
    private int lines;
    private int rows;
    private int side;


    /**
     * Block size constants
     */
    public enum BlockSize {
        BS_1(1), BS_2(2), BS_4(4), BS_8(8), BS_16(16), BS_32(32), BS_64(64);

        private int bsize;

        BlockSize(int bsize) {
            this.bsize = bsize;
        }

        public int getBlockSize() {
            return bsize;
        }
    }


    /**
     * Creates ImageBlock object for given image holder.
     * ImageBlock stores 2D array of separate images (blocks) size given by BlockSize
     * @param imageHolder   ImageHolder object
     * @param bsize         side size of block
     */
    public ImageBlocks(ImageHolder imageHolder, BlockSize bsize) {
        side = bsize.getBlockSize();
        BufferedImage b = imageHolder.getImage();
        BufferedImage bn = newSizeImage(b, side);

        lines = bn.getHeight() / side;
        rows = bn.getWidth() / side;

        subs = new BufferedImage[lines][rows];

        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < rows; j++) {
                subs[i][j] = bn.getSubimage(j*side, i*side, side, side);
            }
        }
    }


    /**
     * Returns ColorMatrix object for given block
     * @param l     line
     * @param r     row
     * @return      ColorMatrix object
     * @throws NoSuchBlockException     if l > lines in ImageBlock or r > rows in ImageBlock
     */
    public ColorMatrix getColorMatrix(int l, int r) throws NoSuchBlockException {
        isBlock(l, r);
        ColorMatrix cm = new ColorMatrix(side);
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                cm.getColorsArray()[i][j] = new Color(subs[l][r].getRGB(j,i));
            }
        }
        return cm;
    }


    /**
     * Returns image form separate block
     * @param l    line
     * @param r    row
     * @return     image
     */
    public BufferedImage getBlock(int l, int r) {
        if ((l > lines -1) || (r > rows - 1)) {
            return null;
        }
        return subs[l][r];
    }



    /**
     * Build image from block and returns it
     * @return  comlpete image
     */
    public BufferedImage makeImage() {
        BufferedImage newImg = new BufferedImage(rows*side, lines*side, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImg.createGraphics();
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < rows; j++) {
                g.drawImage(subs[i][j], j*side, i*side, null);
            }
        }
        return newImg;
    }


    /**
     * General exception thrower :)
     * @param l     line
     * @param r     row
     * @throws NoSuchBlockException
     */
    private void isBlock(int l, int r) throws NoSuchBlockException {
        if ((l > lines) || (r > rows)) {
            throw new NoSuchBlockException();
        }
    }


    /**
     * Marks block (l, r) with red square
     * @param l    line
     * @param r    row
     * @throws NoSuchBlockException
     */
    public void selectBlock(int l, int r) throws NoSuchBlockException {
        isBlock(l,r);
        Graphics2D g = subs[l][r].createGraphics();
        g.setColor(Color.RED);
        g.drawRect(0,0,side-1,side-1);
    }


    /**
     * Returns total number of blocks in ImageBlocs object
     * @return total number of blocks
     */
    public int getBlockCount() {
        return lines * rows;
    }


    /**
     * Returns number of rows in ImageBlocs object
     * @return number of rows
     */
    public int getRows() {
        return rows;
    }


    /**
     * Returns number of lines in ImageBlocs object
     * @return number of lines
     */
    public int getLines() {
        return lines;
    }


}
