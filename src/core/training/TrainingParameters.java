package core.training;

public class TrainingParameters {

  private int iterations; //maximum amount of training samples
  private int trainStep; //how many training steps are taken per data sample
  private double stopErrorRate; //when this error rate is reached, training will stop
  private int errorRateSamples; //how many last samples to use for estimating error rate

  public TrainingParameters(int iterations, int trainStep, double stopErrorRate,
      int errorRateSamples) {
    this.iterations = iterations;
    this.trainStep = trainStep;
    this.stopErrorRate = stopErrorRate;
    this.errorRateSamples = errorRateSamples;
  }

  public int getIterations() {
    return iterations;
  }

  public double getStopErrorRate() {
    return stopErrorRate;
  }

  public int getErrorRateSamples() {
    return errorRateSamples;
  }

  public int getTrainStep() {
    return trainStep;
  }
}
