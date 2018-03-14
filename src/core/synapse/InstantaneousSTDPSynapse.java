package core.synapse;

import core.neuron.Neuron;
import java.util.ArrayList;
import java.util.List;

/*This is a variation of STDP Synapse without synaptic delay*/

public class InstantaneousSTDPSynapse extends STDPSynapse {

  public InstantaneousSTDPSynapse(Neuron presynaptic, Neuron postsynaptic) {
    super(presynaptic, postsynaptic, 2);
  }


  //always return full spike list
  @Override
  public List<Spike> getArrivedSpikes() {
   return super.getSpikes();
  }

}
