package core.training;

/*IDXImage represents image stored in IDX format*/

import java.awt.image.BufferedImage;

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
}
