package core.wtaexample;

public class WTATrainingParameters {

  private final int n; //represents amount of rows in image data
  private final int k; //every first layer neuron receives K spike trains that are merged
  private final double k_syn; //alpha-function shape form parameter
  private final int r; //refractory period

  public WTATrainingParameters(int n, int k, double k_syn, int r) {
    this.n = n;
    this.k = k;
    this.k_syn = k_syn;
    this.r = r;
  }

  public int getN() {
    return n;
  }

  public int getK() {
    return k;
  }

  public double getK_syn() {
    return k_syn;
  }
}
