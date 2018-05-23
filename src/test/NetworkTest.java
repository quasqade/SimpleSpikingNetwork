package test;

import core.RandomSingleton;
import core.neuron.Neuron;
import core.neuron.Neuron.NeuronType;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.SwingUtilities;
import org.jfree.data.xy.XYSeries;
import test.gui.XYChartFrame;

/**
 * Created by user on 16-Feb-18. NetworkTest is a way to test a random network using models defined
 * in core package. It does not utilize Synapses or spikes, it simply tests neural model and how a
 * group of interconnected neurons behaves.
 */
public class NetworkTest {

  private static final int SIZE = 1000;

  public static void main(String[] args) {
    //TODO reimplement this in NeuralNetwork if implemented correctly

    //create a set of 1000 randomly interconnected neurons
    List<Neuron> neuronList = new ArrayList<>();
    List<Double> I = new ArrayList<>(); //currents
    List<List<Double>> S = new ArrayList<>(); //connection weights

    Random random = RandomSingleton.getInstance();

    for (int i = 0; i < SIZE; i++) {
      neuronList.add(new Neuron(NeuronType.RANDOMIZED_IZHIKEVICH, 5));
      I.add(0.0);

      //initialize connection weights
      List<Double> s1 = new ArrayList<>();
      for (int j = 0; j < SIZE; j++) {
        s1.add(0.25*random.nextDouble());
      }
      S.add(s1);
    }



    //simulate network for 1000 ms
    XYSeries dataset = new XYSeries("Simulation");
    Instant before = Instant.now();

    for (int i = 0; i < 1000; i++) {

      //random thalamic input
      for (int j = 0; j < neuronList.size(); j++) {
        I.set(j, 5 * random.nextGaussian());
      }

      List<Integer> spikedIndeces = new ArrayList<>();
      //detect spikes
      for (int j = 0; j < neuronList.size(); j++) {
        Neuron neuron = neuronList.get(j);
        if (neuron.isSpiking()) {
          spikedIndeces.add(j);
          dataset.add(i, j); //Y axis charts neurons by number and X axis charts their spike timings
        }
      }

      //System.out.println(neuronList.get(0).getNeuronModel().getU());

      //additional input
      for (int j = 0; j < I.size(); j++) {
        double sum = 0;
        for (Integer index: spikedIndeces
        ) {
          //sum+=S.get(index).get(j);
        }
        I.set(j, I.get(j) + sum);
      }



      //simulate neurons
      for (int j = 0; j < neuronList.size(); j++) {
        Neuron neuron = neuronList.get(j);
        double kek = I.get(j);
        neuron.setI(kek);
        neuron.simulateTickNoExternalCurrent();
      }



    }
    Instant after = Instant.now();
    Duration elapsed = Duration.between(before, after);
    System.out.println("Execution took " + elapsed.toMillis() + " milliseconds");

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new XYChartFrame(dataset, "No connections", "Time (ms)", "Neuron",
            XYChartFrame.MarkerType.SMALL_CROSS);
      }
    });
    System.out.println();
  }

}
