package core.training;

/*Utility class for reading IDX format MNIST dataset*/

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class IDXLabelReader {

  private FileInputStream inputFIS;
  private int labels;
  private int currentLabel;
  private boolean initialized = false;

  public IDXLabelReader(File file) throws IOException, IllegalArgumentException {
    this.inputFIS = new FileInputStream(file);
    int labelMagicNumbers =
        (inputFIS.read() << 24) | (inputFIS.read() << 16) | (inputFIS.read() << 8) | (inputFIS
            .read());
    labels = (inputFIS.read() << 24) | (inputFIS.read() << 16) | (inputFIS.read() << 8) | (inputFIS
        .read());

    if (labels == 0) {
      throw new IllegalArgumentException();
    }

    initialized = true;
    currentLabel = 0;
  }

  public int readNext() throws IndexOutOfBoundsException, IOException {
    if (currentLabel > labels) {
      throw new IndexOutOfBoundsException();
    }

    if (!initialized) {
      throw new IOException();
    }

    int label = inputFIS.read();
    currentLabel++;
    return label;
  }

  public void close() throws IOException {
    inputFIS.close();
  }
}
