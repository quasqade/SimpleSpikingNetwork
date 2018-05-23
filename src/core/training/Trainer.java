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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

        //reset neurons
        for (Layer layer : network.getLayers()
            ) {
          for (Neuron neuron : layer.getNeurons()
              ) {
            neuron.reset();
          }
        }

        //simulate network for two T windows
        simulateNetwork(parameters.getSampleProcessingTime() * 2);

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

        Set<Neuron> spikeSet = new HashSet<>(); //set of output neurons that have spiked

        //simulate for 2 more T windows while checking for spikes
        for (int k = 0; k < parameters.getSampleProcessingTime() * 2; k++) {

          //simulate all neurons except output for a single tick
          for (int j = 0; j < network.getLayers().size() - 2; j++) {
            for (Neuron neuron : network.getLayers().get(j).getNeurons()
                ) {
              neuron.simulateTick();
            }
          }

          //simulate all output neurons for a single tick and add them to spikeSet if they have spiked
          for (Neuron neuron : network.outputLayer().getNeurons()
              ) {
            neuron.simulateTick();
            if (neuron.isSpiking()) {
              spikeSet.add(neuron);
            }
          }
        }

        System.out.println(spikeSet.size() + " output neurons have spiked");

        //process output results
        Set<Neuron> holdSet = new HashSet<>(); //set of neurons that should have their weights preserved
        Set<Neuron> inSet = new HashSet<>(); //set of neurons that should be stimulated
        Set<Neuron> deSet = new HashSet<>(); //set of neurons that should be depressed

        //iterate over all groups putting neurons into corresponding sets
        for (int j = 0; j < outputNeuronGroups.size() - 1; j++) {
          //in case currently processed group corresponds to correct answer
          if (j == correctAnswer) {
            int spikeCounter = 0; //counts how many neurons have spiked
            for (Neuron neuron : outputNeuronGroups.get(j)
                ) {
              if (spikeSet.contains(neuron)) {
                holdSet.add(neuron);
                spikeCounter++;
              }
            }

            //check if enough neurons have spiked
            if (spikeCounter < inTarget) {
              //if not then add nonspiking neurons to inSet until desired inTarget is reached
              for (Neuron neuron : outputNeuronGroups.get(j)
                  ) {
                //check if neuron has already spiked
                if (spikeSet.contains(neuron)) {
                  continue;
                }

                //if not, then add it to inSet and increment spikeCounter
                inSet.add(neuron);
                spikeCounter++;
                //if desired target is reached, then stop further addition
                if (spikeCounter >= inTarget) {
                  break;
                }
              }
            }
          }
          //in case currently processed group corresponds to wrong answer
          else {
            int spikeCounter = 0; //counts how many neurons have spiked
            for (Neuron neuron : outputNeuronGroups.get(j)
                ) {
              if (spikeSet.contains(neuron)) {
                spikeCounter++;
              }
            }

            //check if more neurons have spiked than allowed by deTarget
            if (spikeCounter > deTarget) {
              //if yes then add spiking neurons to deSet until target is reached
              for (Neuron neuron : outputNeuronGroups.get(j)
                  ) {
                if (spikeSet.contains(neuron)) {
                  deSet.add(neuron);
                  spikeCounter--;

                  //check if desired target is reached
                  if (spikeCounter <= deTarget) {
                    break;
                  }
                }
              }
            }
          }
        }

        //now we have three lists and will perform weight corrections

        //simulate for one more T window before proceeding to training
        for (int k = 0; k < parameters.getSampleProcessingTime(); k++) {
          network.performIdleSimulation();
        }

        //TODO

        //feed it again correcting weights by applying current
        for (int k = 0; k < parameters.getTrainStep(); k++) {

          //reset neurons
          for (Layer layer : network.getLayers()
              ) {
            for (Neuron neuron : layer.getNeurons()
                ) {
              neuron.reset();
            }
          }

          //first simulate network for one T window
          simulateNetwork(parameters.getSampleProcessingTime());

          //stimulate neurons in deSet to decrease associated weights
          stimulateSet(deSet);

          //simulate network for one more T window
          simulateNetwork(parameters.getSampleProcessingTime());

          //feed data to inputs again
          for (int j = 0; j < network.inputLayer().getNeurons().size() - 1; j++) {
            Synapse synapse = network.inputLayer().getNeurons().get(j).getPreSynapses().get(0);
            synapse.addSpike(new Spike(inputData[j] + 127)); //add 127 to convert to unsigned value
          }

          //simulate network for one more T window
          simulateNetwork(parameters.getSampleProcessingTime());

          //now stimulate neurons in inSet to decrease associated weights
          stimulateSet(inSet);

          //simulate network for three more T windows
          simulateNetwork(parameters.getSampleProcessingTime() * 3);

          //stimulate neurons in holdSet
          stimulateSet(holdSet);

          //simulate network for one more T window
          simulateNetwork(parameters.getSampleProcessingTime());

          //stimulate neurons in deSet to decrease associated weights
          stimulateSet(deSet);

          //simulate network for one more T window
          simulateNetwork(parameters.getSampleProcessingTime());

          //feed data to inputs again
          for (int j = 0; j < network.inputLayer().getNeurons().size() - 1; j++) {
            Synapse synapse = network.inputLayer().getNeurons().get(j).getPreSynapses().get(0);
            synapse.addSpike(new Spike(inputData[j] + 127)); //add 127 to convert to unsigned value
          }

          //simulate network for one more T window
          simulateNetwork(parameters.getSampleProcessingTime());

          //now stimulate neurons in inSet to decrease associated weights
          stimulateSet(inSet);

          //simulate network for one more T window
          simulateNetwork(parameters.getSampleProcessingTime());

          //wait for 5 more T windows to recover state

          simulateNetwork(parameters.getSampleProcessingTime() * 5);
        }


      }
      //TODO

      //TODO check error rate

    return null;
  }

  //stimulate all neurons in this set while idly simulating other
  private void stimulateSet(Set<Neuron> set) {
    for (Neuron neuron : set
        ) {
      neuron.getNeuronModel().setI(parameters.getStimulationCurrent());
      neuron.simulateTickNoExternalCurrent();
    }

    //simulate all other neurons too
    for (Layer layer : network.getLayers()
        ) {
      for (Neuron neuron : layer.getNeurons()
          ) {
        if (set.contains(neuron)) {
          continue;
        }

        neuron.simulateTick();
      }
    }
  }

  //simulate network for a given number of cycles
  private void simulateNetwork(int cycles) {
    for (int j = 0; j < cycles; j++) {
      network.performIdleSimulation();
    }
  }

  enum DatasetType {IDX}


}
