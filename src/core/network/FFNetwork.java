package core.network;

/*FFNetwork represents a feedforward network consisting of fully interconnected layers of spiking neurons
 * with defined input and output layers. Conceptually it is a list of layers and methods providing access
 * to specific layers and neurons within*/

//TODO store weights in file

import core.layer.Layer;
import core.neuron.Neuron;
import java.util.ArrayList;
import java.util.List;

public class FFNetwork {

  private List<Layer> layers;
  private Layer input, output;
  private int globalDelay; //default delay that will be applied to all connections


  public FFNetwork(int inputLayerSize, int delay) {
    layers = new ArrayList<>();
    this.globalDelay = delay;
    layers.add(new Layer(inputLayerSize, globalDelay));
    input = layers.get(0);
    output = layers.get(0);
  }

  public void addNextLayer(Layer layer) {
    layers.add(layer);
    layer.interconnectToPrevious(output);
    output = layer;
  }

  public void addNextLayer(int size) {
    Layer layer = new Layer(size);
    addNextLayer(layer);
  }

  public List<Layer> getLayers() {
    return layers;
  }

  public Layer inputLayer() {
    return input;
  }

  public Layer outputLayer() {
    return output;
  }

  public int getGlobalDelay() {
    return globalDelay;
  }

  public void performIdleSimulation()
  {
    for (Layer layer: layers
    ) {
      for (Neuron neuron: layer.getNeurons()
      ) {
        neuron.simulateTick();
      }
    }
  }
}
