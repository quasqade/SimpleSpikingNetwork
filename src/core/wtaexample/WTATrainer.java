package core.wtaexample;

/*WTATrainer performs training on MNIST dataset for network of suitable parameters*/

import core.synapse.Spike;
import core.training.IDXImage;
import core.training.IDXImageReader;
import core.training.IDXLabelReader;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class WTATrainer {

  private TrainingMode trainingMode;
  private File images, labels;
  private WTANetwork network;
  private WTATrainingParameters parameters;

  public WTATrainer(TrainingMode mode)
  {
    trainingMode = mode;
  }

  public void setIDXImageFile(File file)
  {
    images = file;
  }

  public void setIDXLabelFile(File file)
  {
    labels = file;
  }

  public void train(WTATrainingParameters WTATrainingParameters) throws IllegalStateException, IOException
  {
    network = new WTANetwork();
    network.addLayer(WTATrainingParameters.getN() / WTATrainingParameters.getK());

    switch (trainingMode) {
      case MNIST_FULL:
        network.addLayer(10);
        break;
      case MNIST_BINARY:
        network.addLayer(2);
        break;
    }


    //set up readers
    IDXImageReader imageReader = new IDXImageReader(images);
    IDXLabelReader labelReader = new IDXLabelReader(labels);
    IDXImage image = imageReader.readNext();

    //process each sample
    processSample(SpikeGenerator.processIDX(image), labelReader.readNext());

  }

  public int processSample(List<SpikeTrain> rawTrain, int correctAnswer)
  {
    //compress input down to n/k
    List<SpikeTrain> compressedInput = SpikeGenerator.compressSpikeTrains(rawTrain, parameters.getK());

    //feed input to first layer while simulating network and simulatenously form

  }

}
