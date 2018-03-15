package core.layer;

/*Layer represents a list of neurons for layered networks with methods to facilitate convenient connection*/

import core.neuron.IzhikevichIC;
import core.neuron.IzhikevichNeuronModel;
import core.neuron.IzhikevichParameters;
import core.neuron.Neuron;
import core.neuron.Neuron.NeuronType;
import core.synapse.STDPSynapse;
import core.synapse.SynapseType;
import java.util.ArrayList;
import java.util.List;

public class Layer {

  private List<Neuron> neuronList;
  private Layer previous, next;
  private double initialWeight, minWeight, maxWeight; //default parameters for new synapses

  //constructor for existing list of neurons
  public Layer(List<Neuron> neurons) {
    this.neuronList = neurons;
  }

  //constructor for creating a layer with new set of neurons of specified size
  public Layer(int neuronAmount) {
    this(neuronAmount, 3);
  }

  //constructor for a layer with a new set of neurons of specified size and fixed delay
  public Layer(int neuronAmount, int delay) {
    this();

    for (int i = 0; i < neuronAmount; i++) {
      Neuron neuron = new Neuron(NeuronType.IZHIKEVICH, delay);
      IzhikevichParameters params = new IzhikevichParameters(0.1, 0.2, -65, 2, 30, 0.1); //parameters for a fast spiking neuron
      IzhikevichIC ic  = new IzhikevichIC(-65, params.b()*-65, 0);
      neuron.setNeuronModel(new IzhikevichNeuronModel(params, ic));
      neuronList.add(neuron);
    }

  }

  //constructor for creating a layer with new set of neurons of specified size that performs interconnection on creation
  public Layer(int neuronAmount, Layer previous) {
    this(neuronAmount);
    interconnectToPrevious(previous);
  }

  //constructor creating an empty layer
  public Layer() {
    this.neuronList = new ArrayList<>();
  }

  //constructor for existing list of neurons that performs interconnection on creation
  public Layer(List<Neuron> neurons, Layer previous) {
    this(neurons);
    interconnectToPrevious(previous);
  }

  public void interconnectToPrevious(Layer layer) {
    interconnectToPrevious(layer, 0.5);
  }

  public void interconnectToPrevious(Layer layer, double initialWeight) {
    interconnectToPrevious(layer, initialWeight, -1, 1);
  }

  public void interconnectToPrevious(Layer layer, double initialWeight, double minWeight,
      double maxWeight) {
    this.initialWeight = initialWeight;
    this.minWeight = minWeight;
    this.maxWeight = maxWeight;
    setPrevious(layer);
    previous.setNext(this);
    for (Neuron presynaptic : previous.getNeurons()
        ) {
      for (Neuron postsynaptic : neuronList
          ) {
        connectNeurons(presynaptic, postsynaptic);
      }
    }
  }

  private void connectNeurons(Neuron presynaptic, Neuron postsynaptic) {
    presynaptic.addPostsynapticNeuron(postsynaptic, SynapseType.STDP);
    STDPSynapse synapse = (STDPSynapse) presynaptic.getPostSynapse(postsynaptic);
    synapse.setWeight(initialWeight);
    synapse.setWeightMin(minWeight);
    synapse.setWeightMax(maxWeight);
  }

  public void addNeuron(Neuron neuron) {
    neuronList.add(neuron);
    for (Neuron presynaptic : previous.getNeurons()
        ) {
      connectNeurons(presynaptic, neuron);
    }
  }

  public List<Neuron> getNeurons() {
    return neuronList;
  }

  public Layer getPrevious() {
    return previous;
  }

  public void setPrevious(Layer previous) {
    this.previous = previous;
  }

  public Layer getNext() {
    return next;
  }

  public void setNext(Layer next) {
    this.next = next;
  }
}
