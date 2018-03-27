package test.supervised;

import core.wtaexample.TrainingMode;
import core.wtaexample.WTATrainer;
import core.wtaexample.WTATrainingParameters;
import java.io.File;
import java.io.IOException;

public class WTAExample {


  public static void main(String[] args)
  {
    WTATrainer trainer = new WTATrainer(TrainingMode.MNIST_FULL);
    trainer.setIDXImageFile(new File("train-images.idx3-ubyte"));
    trainer.setIDXLabelFile(new File("train-labels.idx1-ubyte"));

    try {
      trainer.train(new WTATrainingParameters(28, 4));
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
