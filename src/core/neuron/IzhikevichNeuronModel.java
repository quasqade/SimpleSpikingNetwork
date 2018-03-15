package core.neuron;

/**
 * Created by user on 16-Feb-18. IzhikevichNeuronModel represents a spiking neuron that accepts
 * input and provides output based on used model
 */
public class IzhikevichNeuronModel implements NeuronModel {

  private double v, u, I; //model state variables
  private double a, b, c, d, v_th, dt; //model parameters

  private boolean spiking = false; //on the tick that neuron spikes this is set to true, otherwise false

  //default constructor creates a regular spiking neuron as defined by Izhikevich
  public IzhikevichNeuronModel() {
    //Model parameters
    a = 0.02;
    b = 0.2;
    c = -65;
    d = 6;
    v_th = 30;
    dt = 0.1;

    //Initial condition
    v = -65;
    u = b * v;
    I = 0;
  }

  //constructor accepts two parameter objects defining neuron parameters and initial conditions
  public IzhikevichNeuronModel(IzhikevichParameters parameters, IzhikevichIC initialConditions) {
    this.a = parameters.a();
    this.b = parameters.b();
    this.c = parameters.c();
    this.d = parameters.d();
    this.v_th = parameters.v_th();
    this.dt = parameters.dt();

    this.v = initialConditions.v();
    this.u = initialConditions.u();
    this.I = initialConditions.I();
  }

  //recalculate performs recalculation of a model state for the next tick
  public void recalculate() {

    //calculate new values
    double old_v = v;
    v += dv(v, u);
    u += du(old_v, u);

    if (v > v_th) {
      v = c;
      u += d;
      spiking = true;
    } else {
      spiking = false;
    }
  }




  //first equation in the Izhikevich model (multiplied by dt both sides)
  private double dv(double v, double u) {
    return dt * (0.04 * v * v + 5 * v + 140 - u + this.I);
  }

  //second equation in the Izhikevich model (multiplied by dt both sides)
  private double du(double v, double u) {
    return dt * (a * (b * v - u));
  }

  public boolean isSpiking() {
    return spiking;
  }

  public double getI() {
    return I;
  }

  public void setI(double i) {
    this.I = i;
  }

  public double getV() {
    return v;
  }

  public double getU() {
    return u;
  }

}
