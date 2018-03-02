package core.neuron;

/*
 * IzhikevichParameters is a helper class to provide parameters to a constructor of IzhikevichNeuronModel
 * */

public class IzhikevichParameters {

  private final double prA, prB, prC, prD, prV_th, prDt;

  public IzhikevichParameters(double a, double b, double c, double d, double v_th, double dt) {
    this.prA = a;
    this.prB = b;
    this.prC = c;
    this.prD = d;
    this.prV_th = v_th;
    this.prDt = dt;
  }

  public double a() {
    return prA;
  }

  public double b() {
    return prB;
  }

  public double c() {
    return prC;
  }

  public double d() {
    return prD;
  }

  public double v_th() {
    return prV_th;
  }

  public double dt() {
    return prDt;
  }
}
