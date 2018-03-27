package core.wtaexample;

import core.neuron.IzhikevichIC;
import core.neuron.IzhikevichNeuronModel;
import core.neuron.IzhikevichParameters;

public class IzhikevichWTAModel extends IzhikevichNeuronModel {
private final int Vrest = -60;
private final int C = 100;
private final double k = 0.7;
private final int Vth = -40;

  public IzhikevichWTAModel(IzhikevichParameters parameters,
      IzhikevichIC ic) {
    super(parameters, ic);
  }

  @Override
  protected double dv(double v, double u) {
    return dt * (k*(v-Vrest)*(v-Vth)-u+I) / C;
  }

  @Override
  protected double du(double v, double u) {
    return dt * (a * (b * (v-Vrest) - u));
  }
}
