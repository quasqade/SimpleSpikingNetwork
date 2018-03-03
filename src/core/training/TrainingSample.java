package core.training;

import java.awt.image.BufferedImage;

public class TrainingSample {

  private BufferedImage image;
  private int networkAnswer, label;

  public TrainingSample(BufferedImage image, int networkAnswer, int label) {
    this.image = image;
    this.networkAnswer = networkAnswer;
    this.label = label;
  }

  public BufferedImage getImage() {
    return image;
  }

  public int getNetworkAnswer() {
    return networkAnswer;
  }

  public int getLabel() {
    return label;
  }
}
