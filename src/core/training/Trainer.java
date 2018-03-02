package core.training;

/*Trainer is a class that facilitates supervised training of an FFNetwork object by using artificial current
 * stimulus for purposes of modifying weights according to dataset provided.*/

import core.network.FFNetwork;
import java.io.File;

public class Trainer {

  FFNetwork network;

  //constructor accepts FFNetwork object that represents network to be trained
  public Trainer(FFNetwork network) {
    this.network = network;
  }

  //accepts MNIST formatted image and label files
  public void loadIDXDataset(File images, File labels) {

  }

}
