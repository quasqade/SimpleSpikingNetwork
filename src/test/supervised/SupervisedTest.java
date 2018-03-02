package test.supervised;

import core.network.FFNetwork;
import core.training.IDXImage;
import core.training.IDXImageReader;
import core.training.IDXLabelReader;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SupervisedTest {

  public static void main(String[] args) {
    FFNetwork network = new FFNetwork(196);
    network.addNextLayer(300);

    try {
      IDXImageReader imageReader = new IDXImageReader(new File("train-images.idx3-ubyte"));
      IDXLabelReader labelReader = new IDXLabelReader(new File("train-labels.idx1-ubyte"));

      IDXImage image;
      int label;

      while ((image = imageReader.readNext()) != null) {
        label = labelReader.readNext();
        BufferedImage bufferedImage = image
            .getBufferedImage(imageReader.getRows(), imageReader.getColumns());
        continue;
      }
      imageReader.close();
      labelReader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
