package core.wtaexample;

import core.training.IDXImage;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SpikeGenerator {

  //converts IDXImage to a list of SpikeTrains
  public static List<SpikeTrain> processIDX(IDXImage image)
  {
    List<SpikeTrain> output = new ArrayList<>();
    boolean[] imageArray = getBooleanArray(image);
    for (int i = 0; i < image.getRows(); i++) {
      SpikeTrain train = new SpikeTrain();
      for (int j = 0; j < image.getColumns(); j++) {
        if (imageArray[i*image.getColumns()+j])
        {
          train.addSpikeAt(j);
        }
      }
      output.add(train);
    }
    return output;
  }

  private static boolean[] getBooleanArray(IDXImage image)
  {
    boolean[] output = new boolean[image.getRows()*image.getColumns()];
    int[] test = new int[image.getRows()*image.getColumns()];
    int counter = 0;
    BufferedImage bimg = image.getBufferedImage();
    ColorSpace space = bimg.getColorModel().getColorSpace();
    for (int i = 0; i < bimg.getHeight(); i++) {
      for (int j = 0; j < bimg.getWidth(); j++) {
        output[i*bimg.getWidth()+j] = (bimg.getRGB(i,j) < -1);
      }
    }
    return output;
  }

  //compresses a list of spike trains of size n to n/k
  public static List<SpikeTrain> compressSpikeTrains(List<SpikeTrain> trains, int k)
  {
    List<SpikeTrain> output = new ArrayList<>();
    for (int i = 0; i < trains.size()/k; i++) {
      SpikeTrain train = new SpikeTrain();
      for (int j = 0; j < k; j++) {

        //merge spike trains
        for (int l = 0; l < train.length(); l++) {
          if (trains.get(i*k+j).hasSpikeAt(l))
          {
            train.addSpikeAt(l);
          }
        }
      }
      output.add(train);
    }
    return output;
  }
}
