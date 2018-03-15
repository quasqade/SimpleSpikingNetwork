package core.training;

public class TrainingParameters {

  private int iterations; //maximum amount of training samples
  private int trainStep; //how many training steps are taken per data sample
  private double stopErrorRate; //when this error rate is reached, training will stop
  private int errorRateSamples; //how many last samples to use for estimating error rate
  private int sampleProcessingTime; //how many cycles to run between first and last output
  private double stimulationCurrent; //value of current used to guarantee or speed up spike of a given neuron

  public TrainingParameters(int iterations, int trainStep, double stopErrorRate,
      int errorRateSamples, int sampleProcessingTime, double stimulationCurrent) {
    this.iterations = iterations;
    this.trainStep = trainStep;
    this.stopErrorRate = stopErrorRate;
    this.errorRateSamples = errorRateSamples;
    this.sampleProcessingTime = sampleProcessingTime;
    this.stimulationCurrent=stimulationCurrent;
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

  public int getSampleProcessingTime()
  {
    return sampleProcessingTime;
  }

  public double getStimulationCurrent() {
    return stimulationCurrent;
  }
}
