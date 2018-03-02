package core.network;

/*FFNetwork represents a feedforward network consisting of fully interconnected layers of spiking neurons
 * with defined input and output layers. Conceptually it is a list of layers and methods providing access
 * to specific layers and neurons within*/

import core.layer.Layer;
import java.util.ArrayList;
import java.util.List;

public class FFNetwork {

  private List<Layer> layers;
  private Layer input, output;


  public FFNetwork(int inputLayerSize) {
    layers = new ArrayList<>();
    layers.add(new Layer(inputLayerSize));
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
}