package core.synapse;

import core.neuron.Neuron;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 21-Feb-18. Synapse represents synaptic connection between two neurons that
 * propagates Spikes. It does not modify spike voltage values. It introduces a constant delay
 * defined at creation.
 */
public class Synapse {

  private Neuron presynaptic, postsynaptic; // two connected neurons
  private List<Spike> spikes; //list of spikes currently propagating along synapse;
  private int delay; //synaptic delay (in ms);

  public Synapse(Neuron presynaptic, Neuron postsynaptic, int delay) {
    this.presynaptic = presynaptic;
    this.postsynaptic = postsynaptic;
    this.delay = delay;
    spikes = new ArrayList<>();
  }

  public void addSpike(Spike spike) {
    spikes.add(new Spike(spike.getVoltage(), delay));
  }

  //propagates all currently travelling spikes and disposes of spikes that have reached destination
  public void propagateSpikes() {
    //remove all spikes that have finished travelling
    List<Spike> toBeRemoved = getArrivedSpikes();
    spikes.removeAll(toBeRemoved);

    //advance all remaining spikes
    for (Spike spike : spikes
        ) {
      spike.advance();
    }
  }

  public List<Spike> getArrivedSpikes() {
    List<Spike> result = new ArrayList<>();
    for (Spike spike : spikes
        ) {
      if (spike.getCounter() < 1) {
        result.add(spike);
      }
    }
    return result;
  }

  public int getDelay() {
    return delay;
  }

  public Neuron getPresynaptic() {
    return presynaptic;
  }

  public Neuron getPostsynaptic() {
    return postsynaptic;
  }

  public List<Spike> getSpikes() {
    return spikes;
  }
}
