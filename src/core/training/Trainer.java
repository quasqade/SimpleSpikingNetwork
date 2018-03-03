package core.training;

/*Trainer is a class that facilitates supervised training of an FFNetwork object by using artificial current
 * stimulus for purposes of modifying weights according to dataset provided. Trainer works by extending
 * SwingWorker where interim results are published in the form of TrainingSample objects.*/

import core.network.FFNetwork;
import java.io.File;
import java.io.IOException;
import javax.swing.SwingWorker;

public class Trainer extends SwingWorker<Void, TrainingSample> {

  private FFNetwork network;

  ;
  private DatasetType datasetType;
  private IDXImageReader idxImageReader;
  private IDXLabelReader idxLabelReader;
  private TrainingParameters parameters;
  //constructor accepts FFNetwork object that represents network to be trained
  public Trainer(FFNetwork network) {
    this.network = network;
  }

  //accepts MNIST formatted image and label files
  public void loadIDXDataset(File images, File labels) {
    this.datasetType = DatasetType.IDX;
    try {
      idxImageReader = new IDXImageReader(images);
      idxLabelReader = new IDXLabelReader(labels);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setTrainingParameters(TrainingParameters parameters) {
    this.parameters = parameters;
  }

  @Override
  protected Void doInBackground() throws Exception {
    return null;
  }

  enum DatasetType {IDX}


}
