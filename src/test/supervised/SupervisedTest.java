package test.supervised;

import core.network.FFNetwork;
import core.training.IDXImage;
import core.training.IDXImageReader;
import core.training.IDXLabelReader;
import core.training.Trainer;
import core.training.TrainingParameters;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SupervisedTest {

  public static void main(String[] args) {
    FFNetwork network = new FFNetwork(196, 2); //odd delays are better for supervised training
    network.addNextLayer(300);

    Trainer trainer = new Trainer(network);
    TrainingParameters parameters = new TrainingParameters(100, 2, 0.000001, 10, 10);
    trainer.setTrainingParameters(parameters);
    trainer.loadIDXDataset(new File("train-images.idx3-ubyte"), new File("train-labels.idx1-ubyte"),
        19, 10);

    try {
      IDXImageReader imageReader = new IDXImageReader(new File("train-images.idx3-ubyte"));
      IDXLabelReader labelReader = new IDXLabelReader(new File("train-labels.idx1-ubyte"));

      trainer.execute();

      IDXImage image;
      int label;

      while ((image = imageReader.readNext()) != null) {
        BufferedImage bufferedImage = image
            .getDownscaled(imageReader.getColumns(), imageReader.getRows(), 14, 14)
            .getBufferedImage(14, 14);
        label = labelReader.readNext();
        continue;
      }
      imageReader.close();
      labelReader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
