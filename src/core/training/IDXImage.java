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

  //constructor for raw ARGB pixel data from file, converts data to grayscale byte array
  public IDXImage(int[] pixels, int columns, int rows) {
    BufferedImage original = new BufferedImage(columns, rows,
        BufferedImage.TYPE_INT_ARGB);//get ARGB representation
    original.setRGB(0, 0, columns, rows, pixels, 0, columns);
    BufferedImage grayscale = new BufferedImage(columns, rows,
        BufferedImage.TYPE_BYTE_GRAY); //create grayscale representation

    //convert original to grayscale
    ColorConvertOp op = new ColorConvertOp(original.getColorModel().getColorSpace(),
        grayscale.getColorModel().getColorSpace(), null);
    op.filter(original, grayscale);
    DataBufferByte dbb = (DataBufferByte) grayscale.getRaster().getDataBuffer();
    this.pixels = dbb.getData();
  }

  //constructor for already converted GS pixel data, simply sets pixel data
  public IDXImage(byte[] pixels) {
    this.pixels = pixels;
  }

  //converts pixel data to BufferedImage by specifying numbers of rows and columns for visualization purposes

  public BufferedImage getBufferedImage(int columns, int rows) {
    BufferedImage image = new BufferedImage(columns, rows, BufferedImage.TYPE_BYTE_GRAY);
    image.setData(Raster
        .createRaster(image.getSampleModel(), new DataBufferByte(pixels, pixels.length),
            new Point()));
    return image;
  }

  public byte[] getPixels() {
    return pixels;
  }

  //returns a downscaled version of this IDXImage
  public IDXImage getDownscaled(int originalColumns, int originalRows,
      int newColumns, int newRows) {
    double scaleY = (double) newRows / (double) originalRows;
    double scaleX = (double) newColumns / (double) originalColumns;
    BufferedImage originalImage = getBufferedImage(originalRows, originalColumns);
    AffineTransform transform = new AffineTransform();
    transform.scale(scaleX, scaleY);
    AffineTransformOp op = new AffineTransformOp(transform, null);
    BufferedImage scaledImage = new BufferedImage(newColumns, newRows, originalImage.getType());
    op.filter(originalImage, scaledImage);

    byte[] pixels = ((DataBufferByte) scaledImage.getRaster().getDataBuffer()).getData();
    return new IDXImage(pixels);
  }


}
