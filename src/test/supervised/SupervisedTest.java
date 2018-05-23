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
    FFNetwork network = new FFNetwork(196, 9);
    network.addNextLayer(300);

    Trainer trainer = new Trainer(network);
    TrainingParameters parameters = new TrainingParameters(100, 2, 0.000001, 10, 10, 250.0);
    trainer.setTrainingParameters(parameters);
    trainer.loadIDXDataset(new File("train-images.idx3-ubyte"), new File("train-labels.idx1-ubyte"),
        19, 10);
     trainer.execute();
     while (!trainer.isDone())
     {
       try {
         Thread.sleep(500);
       } catch (InterruptedException e) {
         e.printStackTrace();
       }
     }

     int j;


  }
}
