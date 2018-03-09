package core.training;

/*Trainer is a class that facilitates supervised training of an FFNetwork object by using artificial current
 * stimulus for purposes of modifying weights according to dataset provided. Trainer works by extending
 * SwingWorker where interim results are published in the form of TrainingSample objects.*/

import core.network.FFNetwork;
import core.neuron.Neuron;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;

public class Trainer extends SwingWorker<Void, TrainingSample> {

  private FFNetwork network;

  ;
  private DatasetType datasetType;
  private IDXImageReader idxImageReader;
  private IDXLabelReader idxLabelReader;
  private int inputValues, outputValues;
  private List<List<Neuron>> outputNeuronGroups; //groups of output neurons where each group corresponds to one value
  private TrainingParameters parameters;

  //constructor accepts FFNetwork object that represents network to be trained
  public Trainer(FFNetwork network) {
    this.network = network;
  }


  //accepts MNIST formatted image and label files
  public void loadIDXDataset(File images, File labels, int inputValues, int outputValues) {
    this.datasetType = DatasetType.IDX;
    this.inputValues = inputValues;
    this.outputValues = outputValues;
    splitOutputs(outputValues);

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

  //form output neuron groups corresponding to amount of output values
  private void splitOutputs(int outputValues) {
    List<Neuron> outputNeurons = network.getLayers().get(network.getLayers().size() - 1)
        .getNeurons();
    outputNeuronGroups = new ArrayList<>();
    int outputNeuronsAmount = outputNeurons.size();
    int neuronsPerGroup = outputNeuronsAmount / outputValues;

    int neuronCounter = 0;
    while (neuronCounter < outputNeuronsAmount) {
      List<Neuron> groupList = new ArrayList<>();
      for (int i = 0; i < neuronsPerGroup; i++) {
        groupList.add(outputNeurons.get(neuronCounter));
        neuronCounter++;
      }
      outputNeuronGroups.add(groupList);
    }
  }

  @Override
  protected Void doInBackground() throws Exception {
    //main cycle performs training until reaching iteration limit or desired error rate
    for (int i = 0; i < parameters.getIterations(); i++) {


      //inner cycle is done per entry and processes each entry 2*trainStep times
      for (int j = 0; j < parameters.getTrainStep(); j++) {

        int[] inputData = new int[0];
        int correctAnswer=-1;
        //first feed entry to the network
        if (datasetType == DatasetType.IDX)
        {
          inputData = idxImageReader.readNext().getPixels();
          correctAnswer = idxLabelReader.readNext();
        }

        //convert entry data to input currents

        //run network for network global delay * number of layers to ensure all neurons have time to fire



        //TODO

        //then feed it again correcting weights by applying current

        //TODO


      }


      //TODO check error rate
    }
    return null;
  }

  enum DatasetType {IDX}


}
