package core.neuron;

public interface NeuronModel {

  public double getV();

  public double getU();

  public boolean isSpiking();

  public double getI();

  public void setI(double i);

  public void recalculate();

  public double getPreSpikeVoltage();

  public void reset();

}
