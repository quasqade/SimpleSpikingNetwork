package core.training;

/*Trainer is a class that facilitates supervised training of an FFNetwork object by using artificial current
 * stimulus for purposes of modifying weights according to dataset provided. Trainer works by extending
 * SwingWorker where interim results are published in the form of TrainingSample objects.*/

import core.layer.Layer;
import core.network.FFNetwork;
import core.neuron.Neuron;
import core.synapse.Spike;
import core.synapse.Synapse;
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
  private int inTarget = 20, deTarget = 0;
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

    //attach fake instantaneous synapses to input layer to feed data
    for (Neuron neuron : network.inputLayer().getNeurons()
        ) {
      neuron.addPreSynapse(new Synapse(null, neuron, 1));
    }

    //run training
    for (int i = 0; i < parameters.getIterations(); i++) {

      System.out.println("Running iteration " + i);

      //inner cycle is done per entry and processes each entry 2*trainStep times
      byte[] inputData = new byte[0];
      int correctAnswer = -1;
      //first feed entry to the network
      if (datasetType == DatasetType.IDX) {
        inputData = idxImageReader.readNext().getPixels();
        correctAnswer = idxLabelReader.readNext();
      }

      //convert entry data to input currents
      for (int k = 0; k < network.inputLayer().getNeurons().size() - 1; k++) {
        Synapse synapse = network.inputLayer().getNeurons().get(k).getPreSynapses().get(0);
        synapse.addSpike(new Spike(inputData[k] + 127)); //add 127 to convert to unsigned value
      }

      //run network for network for sample processing time

      for (int k = 0; k < parameters.getSampleProcessingTime(); k++) {

        //simulate all neurons for a single tick
        for (Layer layer : network.getLayers()
            ) {
          for (Neuron neuron : layer.getNeurons()
              ) {
            neuron.simulateTick();
          }
        }
      }

      //process output results
      List<Neuron> holdList = new ArrayList<>(); //list of neurons that should have their weights preserved
      List<Neuron> inList = new ArrayList<>(); //list of neurons that should be stimulated
      List<Neuron> deList = new ArrayList<>(); //list of neurons that should be depressed

      for (int l = 0; l < outputNeuronGroups.size() - 1; l++) {
        if (l == correctAnswer) {
          //if currently processed group corresponds to correct answer
          int spikeCounter = 0;
          for (Neuron neuron : outputNeuronGroups.get(l)
              ) {
            //add all spiking neurons to holdList
            if (neuron.isSpiking()) {
              holdList.add(neuron);
              spikeCounter++;
            }
          }

          System.out.println(spikeCounter + " output neurons have spiked in correct group");

          //if desired inTarget is not reached, add inTarget-spikeCounter nonspiking neurons to inList
          int x = inTarget - spikeCounter;
          if (spikeCounter < inTarget) {
            int neuronPointer = 0;
            //add neurons until spikeCounter+inList has reached inTarget
            while (x > 0) {
              //get next neuron in the list
              Neuron neuron = outputNeuronGroups.get(l).get(neuronPointer);
              //if neuron has not spiked
              if (!holdList.contains(neuron)) {
                //add it to inList
                inList.add(neuron);
                //decrease remaining neurons counter
                x--;
              }
              neuronPointer++;
            }
          }
        } else {
          //if currently processing group corresponding to wrong answer

          //count how many neurons have spiked
          int spikeCounter = 0;
          for (Neuron neuron : outputNeuronGroups.get(l)
              ) {
            if (neuron.isSpiking()) {
              spikeCounter++;
            }
          }

          System.out.println(spikeCounter + " output neurons have spiked in incorrect group");

          if (spikeCounter > deTarget) {
            //if more neurons have spiked than maximum required by deTarget
            int neuronPointer = 0;
            int y = spikeCounter - deTarget;
            //add neurons until deList has reached spikeCounter
            while (y > 0) {
              //get next neuron in the list
              Neuron neuron = outputNeuronGroups.get(l).get(neuronPointer);
              //if neuron has spiked
              if (neuron.isSpiking()) {
                //add it to deList
                deList.add(neuron);
                //decrease remaining neurons counter
                y--;
              }
              neuronPointer++;
            }
          }
        }
      }

      //TODO

      //then feed it again correcting weights by applying current
      for (int k = 0; k < parameters.getTrainStep(); k++) {

        //feed data to inputs again
        for (int j = 0; j < network.inputLayer().getNeurons().size() - 1; j++) {
          Synapse synapse = network.inputLayer().getNeurons().get(j).getPreSynapses().get(0);
          synapse.addSpike(new Spike(inputData[j] + 127)); //add 127 to convert to unsigned value
        }

        //simulate all neurons for a single tick
        for (Layer layer : network.getLayers()
            ) {
          for (Neuron neuron : layer.getNeurons()
              ) {
            neuron.simulateTick();
          }
        }
      }


    }
    //TODO

  //TODO check error rate
    return null;
}

enum DatasetType {IDX}


}
