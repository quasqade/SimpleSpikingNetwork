package test;

import core.neuron.IzhikevichIC;
import core.neuron.IzhikevichNeuronModel;
import core.neuron.IzhikevichParameters;
import core.neuron.Neuron;
import core.neuron.Neuron.NeuronType;
import core.synapse.STDPSynapse;
import core.synapse.Spike;
import core.synapse.Synapse;
import javax.swing.SwingUtilities;
import org.jfree.data.category.DefaultCategoryDataset;
import test.gui.LineChartFrame;

public class NeuronPlayground {


  public static void main(String[] args)
  {
    DefaultCategoryDataset synapticVoltage = new DefaultCategoryDataset(); //recording synaptic voltage
    Neuron neuron = new Neuron(NeuronType.IZHIKEVICH, 5);
    IzhikevichIC ic = new IzhikevichIC(-65, -13, 0);
    IzhikevichParameters parameters = new IzhikevichParameters(0.1, 0.2, -65, 2, 30, 0.1);
    neuron.setNeuronModel(new IzhikevichNeuronModel(parameters, ic));

    Synapse synapse = new Synapse(null, neuron, 1);
    neuron.addPreSynapse(synapse);

    int counter=0;
    for (int i = 0; i < 50; i++) {
      synapse.addSpike(new Spike(200));
      for (int j = 0; j < 50; j++) {
        counter++;
        neuron.simulateTick();
        synapticVoltage.addValue(neuron.getNeuronModel().getV(), "Membrane Voltage", (Integer)counter);
      }
    }

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new LineChartFrame(synapticVoltage, "Synaptic voltage", "Time (ms)",
            "Voltage (mV)");
      }
    });
  }
}
