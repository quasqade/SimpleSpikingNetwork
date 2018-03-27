package core.wtaexample;

/*SpikeTrain represents a train of spikes representing a row in 28x28 image
 and is stored in fixed 28 size boolean array*/

import java.util.Arrays;

public class SpikeTrain {

  private boolean[] train;

  public SpikeTrain() {
    train = new boolean[28];
    Arrays.fill(train, false);
  }

  public void addSpikeAt(int time)
  {
    train[time] = true;
  }

  public boolean hasSpikeAt(int time)
  {
    return train[time];
  }

  public int length()
  {
    return train.length;
  }

}
