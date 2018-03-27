package core.wtaexample;

import core.neuron.IzhikevichIC;
import core.neuron.IzhikevichNeuronModel;
import core.neuron.IzhikevichParameters;

/*WTANeuron wraps around Izhikevich Neuron Model for WTA Training*/
public class WTANeuron {

  private IzhikevichNeuronModel model;

  public WTANeuron()
  {

    IzhikevichParameters parameters = new IzhikevichParameters(0.03, -2, -50, 100, 35, 0.01);
    IzhikevichIC ic = new IzhikevichIC(-60, 10, 0);
    model = new IzhikevichWTAModel(parameters, ic);
  }

  public void simulateTick()
  {
    model.recalculate();
  }

  public void simulateSecond()
  {
    for (int i = 0; i < 1/model.getDt(); i++) {
      model.recalculate();
    }
  }
}
