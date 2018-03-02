package core.training;

/*Utility class for reading IDX format MNIST dataset*/

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class IDXImageReader {

  private FileInputStream inputFIS;
  private int rows;
  private int columns;
  private int pixelsPerImage;
  private int images;
  private int currentImage;
  private boolean initialized = false;

  public IDXImageReader(File file) throws IOException, IllegalArgumentException {
    this.inputFIS = new FileInputStream(file);
    int imageMagicNumbers =
        (inputFIS.read() << 24) | (inputFIS.read() << 16) | (inputFIS.read() << 8) | (inputFIS
            .read());
    this.images =
        (inputFIS.read() << 24) | (inputFIS.read() << 16) | (inputFIS.read() << 8) | (inputFIS
            .read());
    this.rows =
        (inputFIS.read() << 24) | (inputFIS.read() << 16) | (inputFIS.read() << 8) | (inputFIS
            .read());
    this.columns =
        (inputFIS.read() << 24) | (inputFIS.read() << 16) | (inputFIS.read() << 8) | (inputFIS
            .read());
    if (images == 0 || rows == 0 || columns == 0) {
      throw new IllegalArgumentException();
    }
    currentImage = 0;
    pixelsPerImage = rows * columns;
    initialized = true;
  }

  public IDXImage readNext() throws IOException, IndexOutOfBoundsException {
    if (!initialized) {
      return null;
    }

    if (currentImage > images) {
      throw new IndexOutOfBoundsException();
    }

    int[] pixels = new int[pixelsPerImage];
    for (int i = 0; i < pixelsPerImage; i++) {
      int gray = 255 - inputFIS.read();
      pixels[i] = 0xFF000000 | (gray << 16) | (gray << 8) | gray;
    }

    currentImage++;
    return new IDXImage(pixels);
  }

  public void close() throws IOException {
    inputFIS.close();
  }

  public int getRows() {
    return rows;
  }

  public int getColumns() {
    return columns;
  }

  public boolean isInitialized() {
    return initialized;
  }


}
