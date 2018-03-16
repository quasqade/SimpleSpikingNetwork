package test;

import core.network.FFNetwork;
import core.neuron.Neuron;
import core.synapse.Spike;
import core.synapse.Synapse;
import javax.swing.SwingUtilities;
import org.jfree.data.category.DefaultCategoryDataset;
import test.gui.LineChartFrame;

public class NetworkPlayground {

  private static FFNetwork network;
  private static DefaultCategoryDataset dataset;
  private static int counter=0;

  public static void main(String[] args)
  {
    network = new FFNetwork(1, 9);
    network.addNextLayer(1);

    Neuron inputNeuron = network.inputLayer().getNeurons().get(0);
    Neuron outputNeuron = network.outputLayer().getNeurons().get(0);

    final int sampleProcessingTime = 50;

    Synapse inputSynapse = new Synapse(null, inputNeuron, 1);
    outputNeuron.addPreSynapse(inputSynapse);

    dataset = new DefaultCategoryDataset(); //recording synaptic voltage

    for (int i = 0; i < 30; i++) {
      //inputNeuron.reset();
      //outputNeuron.reset();

      simulateCycles(sampleProcessingTime*2);

      inputSynapse.addSpike(new Spike(250));


      simulateCycles(sampleProcessingTime*2);

      simulateCycles(sampleProcessingTime);
    }

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new LineChartFrame(dataset, "Synaptic voltage", "Time (ms)",
            "Voltage (mV)");
      }
    });

  }

  private static void simulateCycles(int cycles)
  {
    for (int i = 0; i < cycles; i++) {
      network.performIdleSimulation();
      dataset.addValue(network.inputLayer().getNeurons().get(0).getNeuronModel().getV(), "Membrane Voltage", (Integer)counter);
      counter++;
    }
  }
}
