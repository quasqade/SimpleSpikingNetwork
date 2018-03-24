package test;

import core.neuron.Neuron;
import core.synapse.SynapseType;
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

  public static void main(String[] args) {
    //TODO reimplement this in NeuralNetwork if implemented correctly

    //create a set of 1000 randomly interconnected neurons
    List<Neuron> neuronList = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      neuronList.add(new Neuron(Neuron.NeuronType.IZHIKEVICH, 5));
    }

    Random random = new Random();

    //simulate network for 1000 ms
    XYSeries dataset = new XYSeries("Simulation");
    Instant before = Instant.now();

    for (int i = 0; i < 6000; i++) {
      //calculate thalamic input
      double I = 0;
      for (int j = 0; j < neuronList.size(); j++) {
        Neuron neuron = neuronList.get(j);
        if (neuron.isSpiking()) {
          I += 0.5 * random.nextGaussian();
        }
      }

      //add random thalamic input
      for (int j = 0; j < neuronList.size(); j++) {
        Neuron neuron = neuronList.get(j);
        //TODO determine why neurons are not spiking without additional stimulation
        double kek = 5 * random.nextGaussian() + I
            + 10; //+10 is not reflective of original Izhikevich example
        neuron.setI(kek);
      }

      //simulate each neuron
      for (int j = 0; j < neuronList.size(); j++) {
        Neuron neuron = neuronList.get(j);
        neuron.simulateTick();
        if (neuron.isSpiking()) {
          dataset.add(i, j); //Y axis charts neurons by number and X axis charts their spike timings
        }
      }

    }
    Instant after = Instant.now();
    Duration elapsed = Duration.between(before, after);
    System.out.println("Execution took " + elapsed.toMillis() + " milliseconds");

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new XYChartFrame(dataset, "Random connections", "Time (ms)", "Neuron",
            XYChartFrame.MarkerType.SMALL_CROSS);
      }
    });

  }
}
