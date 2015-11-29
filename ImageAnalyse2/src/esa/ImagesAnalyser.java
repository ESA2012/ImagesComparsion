package esa;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Created by SnakE on 26.11.2015.
 * @author SnakE
 * @version 1.01
 */
public class ImagesAnalyser {

    private BufferedImage image1;
    private BufferedImage image2;
    private Map<Integer, List<Point>> diffs;



    /**
     * Constructor
     * @param image1    first image
     * @param image2    second image
     */
    public ImagesAnalyser(BufferedImage image1, BufferedImage image2) {
        this.image1 = image1;
        this.image2 = image2;
        diffs = new HashMap<>();
    }



    /**
     * Compares two images
     * @return  false if images sizes is not matches,
     *          or true - when comparsion is complete
     */
    public boolean compareImages() {
        if ((image1.getHeight() != image2.getHeight()) || (image1.getWidth() != image2.getWidth())) {
            return false;
        }
        for (int y = 0; y < image1.getHeight(); y++) {
            for (int x = 0; x < image1.getWidth(); x++) {
                Color color1 = new Color(image1.getRGB(x, y));
                Color color2 = new Color(image2.getRGB(x, y));
                if (isDifferent(color1, color2)) {
                    addToMap(new Point(x, y));
                }
            }
        }
        return true;
    }



    /**
     * Adds point to map of differences
     * @param point
     * @see ImagesAnalyser#compareImages()
     */
    private void addToMap(Point point) {
        Integer block = getBlock(point);
        if (diffs.get(block) == null) {
            List<Point> pointList = new ArrayList<>();
            pointList.add(point);
            diffs.put(block, pointList);
        } else {
            diffs.get(block).add(point);
        }
    }



    /**
     * Returns key of map for given point
     * @param point    point
     * @return         key for map of differences
     * @see esa.ImagesAnalyser#addToMap(Point)
     */
    private Integer getBlock(Point point) {
        Integer block = 0;
        if (diffs.isEmpty()) {
            diffs.put(block,null);
        } else {
            for (Integer i : diffs.keySet()) {
                if (findPoint(point, diffs.get(i))) {
                    block = i;
                    break;
                } else {
                    block = diffs.keySet().size();
                }
            }
        }
        return block;
    }



    /**
     * Check collection of points for nearest given point (radius = 10)
     * @param point1    point
     * @param list      collection of points
     * @return          true if collection has nearest point
     *                  false if collection has no nearest point
     * @see esa.ImagesAnalyser#getBlock(Point)
     */
    private boolean findPoint(Point point1, List<Point> list) {
        boolean result = false;
        for (Point point2: list) {
            double dist = point1.distance(point2);
            if (dist < 10) {
                result = true;
                break;
            } else {
                result = false;
            }
        }
        return result;
    }



    /**
     * Checks two colors for differences.
     * Maximum allowed difference is 10% for each color channel (R,G,B).
     * This method used by {@link esa.ImagesAnalyser#compareImages()}
     * @param colorA    first color
     * @param colorB    second color
     * @return          true - if colors are different
     *                  false - if colors are same
     */
    private boolean isDifferent(Color colorA, Color colorB) {
        return (Math.abs(colorA.getRed() - colorB.getRed()) > 25)
                || (Math.abs(colorA.getGreen() - colorB.getGreen()) > 25)
                || (Math.abs(colorA.getBlue() - colorB.getBlue()) > 25);
    }



    /**
     * Builds a rectangle around points in collection.
     * This method using by {@link ImagesAnalyser#getRectangles()}
     * @param pointList    collection of points
     * @return             rectangle
     */
    private Rectangle rectangleBlocks(List<Point> pointList) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = 0;
        int maxY = 0;
        for (Point p : pointList) {
            if (p.x < minX) {
                minX = p.x;
            } else {
                if (p.x > maxX) {
                    maxX = p.x;
                }
            }
            if (p.y < minY) {
                minY = p.y;
            } else {
                if (p.y > maxY) {
                    maxY = p.y;
                }
            }
        }
        return new Rectangle(minX-2, minY-2, maxX - minX + 4, maxY - minY + 4);
    }



    /**
     * Converts collection of points (from map of differences) to rectangles
     * @return  list of rectangles
     */
    private List<Rectangle> getRectangles() {
        List<Rectangle> rectangles = new ArrayList<>();
        for (Integer i : diffs.keySet()) {
            rectangles.add(rectangleBlocks(diffs.get(i)));
        }
        return optimizeRectangles(rectangles);
    }



    /**
     * Removes from list rectangles contained by other rectangles,
     * and unite intersected rectangles. This method using by {@link ImagesAnalyser#getRectangles()}
     * @param rects     list of rectangles
     * @return          "compressed" list of rectangles
     */
    private List<Rectangle> optimizeRectangles(List<Rectangle> rects) {
        List<Rectangle> optimized = new ArrayList<>();
        for (Rectangle a: rects) {
            boolean contains = false;
            boolean intersect = false;
            Rectangle union = null;
            for (Rectangle b: rects) {
                if (a == b) continue;
                if (b.contains(a)) {
                    contains = true;
                    break;
                }
                if (a.intersects(b)) {
                    union = a.union(b);
                    intersect = true;
                    break;
                }
            }
            if (!contains & !intersect) optimized.add(a);
            if (intersect) optimized.add(union);

        }
        return optimized;
    }



    public enum Result {FIRST_IMAGE, SECOND_IMAGE}



    /**
     * Draws rectangles on image
     * @param resultImage    select: first image or second image
     * @return               result image with rectangles around different pixels
     */
    public BufferedImage getResultImage(Result resultImage) {
        BufferedImage image = null;
        switch (resultImage) {
            case FIRST_IMAGE:  image = image1;
                break;
            case SECOND_IMAGE: image = image2;
                break;
        }
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = result.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.setColor(Color.RED);
        for (Rectangle rectangle : getRectangles()) {
            g2d.draw(rectangle);
        }
        return result;
    }
}
