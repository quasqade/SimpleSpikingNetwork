package core.training;

/*IDXImage represents image stored in IDX format*/

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class IDXImage {

  private int[] pixels; // raw pixel data

  public IDXImage(int[] pixels) {
    this.pixels = pixels;
  }


  //converts pixel data to BufferedImage by specifying numbers of rows and columns for visualization purposes
  public BufferedImage getBufferedImage(int rows, int columns) {
    BufferedImage image = new BufferedImage(columns, rows, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, columns, rows, pixels, 0, columns);
    return image;
  }

  public int[] getPixels() {
    return pixels;
  }

  public IDXImage getDownscaled(int originalRows, int originalColumns, int newRows,
      int newColumns) {
    double scaleY = (double) newRows / (double) originalRows;
    double scaleX = (double) newColumns / (double) originalColumns;
    BufferedImage originalImage = getBufferedImage(originalRows, originalColumns);
    AffineTransform transform = new AffineTransform();
    transform.scale(scaleX, scaleY);
    AffineTransformOp op = new AffineTransformOp(transform, null);
    BufferedImage scaledImage = new BufferedImage(newColumns, newRows, originalImage.getType());
    op.filter(originalImage, scaledImage);

    int[] pixels = ((DataBufferInt) scaledImage.getRaster().getDataBuffer()).getData();
    return new IDXImage(pixels);
  }
}
