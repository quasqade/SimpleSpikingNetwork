package core.training;

/*IDXImage represents image stored in IDX format*/

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;


public class IDXImage {

  private byte[] pixels; // raw pixel data
  private int columns, rows;

  //constructor for raw ARGB pixel data from file, converts data to grayscale byte array
  public IDXImage(int[] pixels, int columns, int rows) {
    BufferedImage original = new BufferedImage(columns, rows,
        BufferedImage.TYPE_INT_ARGB);//get ARGB representation
    original.setRGB(0, 0, columns, rows, pixels, 0, columns);
    BufferedImage grayscale = new BufferedImage(columns, rows,
        BufferedImage.TYPE_BYTE_BINARY); //create monochrome representation

    //convert original to monochrome
    ColorConvertOp op = new ColorConvertOp(original.getColorModel().getColorSpace(),
        grayscale.getColorModel().getColorSpace(), null);
    op.filter(original, grayscale);
    DataBufferByte dbb = (DataBufferByte) grayscale.getRaster().getDataBuffer();
    this.pixels = dbb.getData();
    this.columns = columns;
    this.rows = rows;
  }

  //constructor for already converted GS pixel data, simply sets pixel data
  public IDXImage(byte[] pixels, int columns, int rows) {
    this.pixels = pixels;
    this.columns = columns;
    this.rows = rows;
  }

  //converts pixel data to BufferedImage for visualization purposes

  public BufferedImage getBufferedImage() {
    BufferedImage image = new BufferedImage(columns, rows, BufferedImage.TYPE_BYTE_BINARY);
    image.setData(Raster
        .createRaster(image.getSampleModel(), new DataBufferByte(pixels, pixels.length),
            new Point()));
    return image;
  }

  public byte[] getPixels() {
    return pixels;
  }

  //returns a downscaled version of this IDXImage
  public IDXImage getDownscaled(
      int newColumns, int newRows) {
    double scaleY = (double) newRows / (double) rows;
    double scaleX = (double) newColumns / (double) columns;
    BufferedImage originalImage = getBufferedImage();
    AffineTransform transform = new AffineTransform();
    transform.scale(scaleX, scaleY);
    AffineTransformOp op = new AffineTransformOp(transform, null);
    BufferedImage scaledImage = new BufferedImage(newColumns, newRows, originalImage.getType());
    op.filter(originalImage, scaledImage);

    byte[] pixels = ((DataBufferByte) scaledImage.getRaster().getDataBuffer()).getData();
    return new IDXImage(pixels, newColumns, newRows);
  }

  public int getColumns() {
    return columns;
  }

  public int getRows() {
    return rows;
  }

}
