package core.wtaexample;

/*WTANetwork is a 3 layer spiking network for processing of MNIST images*/

import java.util.ArrayList;
import java.util.List;

public class WTANetwork {

  private List<List<WTANeuron>> layers;

  public WTANetwork() {
    layers = new ArrayList<>();
  }

  public void addLayer(int size) {
    List<WTANeuron> layer = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      layer.add(new WTANeuron());
    }
    layers.add(layer);
  }

  public List<List<WTANeuron>> getLayers() {
    return layers;
  }

  public void simulateTick() {
    for (List<WTANeuron> layer : layers
        ) {
      for (WTANeuron neuron: layer
          ) {
          neuron.simulateTick();
      }

    }
  }

}
