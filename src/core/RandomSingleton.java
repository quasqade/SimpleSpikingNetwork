package core;
//this class provides an instance of Random generator to all classes

import java.util.Random;

public class RandomSingleton {

  private static final long seed = 1;//seed
  private static final Random instance = new Random(seed);

  public static Random getInstance(){
    return instance;
  }

}
