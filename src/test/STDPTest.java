package test;

import core.neuron.IzhikevichIC;
import core.neuron.IzhikevichNeuronModel;
import core.neuron.IzhikevichParameters;
import core.neuron.Neuron;
import core.synapse.STDPSynapse;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 21-Feb-18.
 * This example uses one postsynaptic Izhikevich neuron firing at constant rate and multiple presynaptic neurons firing
 * at different delays with respect to postsynaptic neuron.
 * The various testing results are then represented visually.
 */
public class STDPTest
{
	public static void main(String[] args)
	{
		//be careful choosing lower currents, while lowering rate of spiking, floating point errors can lead to neuron desync
		final int INJECTED_CURRENT = 30; //Injected current is I in Izhikevich equations, this value affects the rate of spiking
		final int DELAY_VARIATION = 2; //how many ms of delay there should be between different presynaptic neurons
		final int PRESYNAPTIC_AMOUNT = 51; //how many presynaptic neurons there are

		//regular spiking Izhikevich neuron
		final IzhikevichParameters izhikevichParameters = new IzhikevichParameters(0.02, 0.2, -65, 2, 30, 0.1);
		final IzhikevichIC izhikevichIC = new IzhikevichIC(-65, -13, 0); //ICs correspond to a resting state

		Neuron postsynaptic = new Neuron(Neuron.NeuronType.IZHIKEVICH, 5); //postsynaptic neuron that will be firing constantly.
		postsynaptic.setNeuronModel(new IzhikevichNeuronModel(izhikevichParameters, izhikevichIC));
		List<Neuron> presynapticList = new ArrayList<>();

		//create presynaptic neurons


		for (int i = 0; i<PRESYNAPTIC_AMOUNT; i++)
		{
			Neuron neuron = new Neuron(Neuron.NeuronType.IZHIKEVICH, 1); //create a presynaptic neuron
			neuron.setNeuronModel(new IzhikevichNeuronModel(izhikevichParameters, izhikevichIC));
			presynapticList.add(neuron);
			neuron.addPostsynapticNeuron(postsynaptic); //connect neurons
		}


		//simulate network for 1000+ ms
		//Note on the simulation: introducing constant currents leads to rapid bursting before reaching stable
		//spiking, it might be beneficial to extend simulation and throw away first few results for more clear data


		int nextInjection = 0;
		int nextInjectedPresynaptic = 0;
		final int postsynapticInjection = PRESYNAPTIC_AMOUNT*DELAY_VARIATION/2;
		XYSeries presynapticSpikes = new XYSeries("Simulation"); //recording presynaptic spikes
		DefaultCategoryDataset postSynapticVoltage = new DefaultCategoryDataset(); //recording postsynaptic voltage

		for (int i = 0; i < 1000; i++)
		{
			//if we reached tick for postsynaptic injection, inject current
			if (i==postsynapticInjection)
			{
				postsynaptic.getNeuronModel().setI(INJECTED_CURRENT);
			}

			//if we reached tick for presynaptic injection, inject current to corresponding neuron
			if (i == nextInjection)
			{
				Neuron presynaptic = presynapticList.get(nextInjectedPresynaptic);
				presynaptic.getNeuronModel().setI(INJECTED_CURRENT);
				if (nextInjectedPresynaptic<presynapticList.size()-1)
				{
					nextInjectedPresynaptic++;
					nextInjection+=DELAY_VARIATION;
				}
			}

			//simulate all neurons
			for (Neuron presynaptic: presynapticList
				 )
			{
				presynaptic.simulateTick();
				if (presynaptic.isSpiking())
				{
					System.out.println("Neuron " + presynapticList.indexOf(presynaptic) + " is spiking at " + i + " ms.");
					presynapticSpikes.add(i,presynapticList.indexOf(presynaptic));
				}
			}

			postsynaptic.simulateTick();
			postSynapticVoltage.addValue(postsynaptic.getNeuronModel().getV(), "Membrane Voltage", Integer.toString(i));
			if (postsynaptic.isSpiking())
			{
				System.out.println("Postsynaptic neuron is spiking at " + i + " ms.");
			}
		}


		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				new XYChartFrame(presynapticSpikes, "Presynaptic spikes", "Time (ms)", "Neuron", XYChartFrame.MarkerType.TINY_DOT);
			}
		});

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				new LineChartFrame(postSynapticVoltage, "Postsynaptic voltage", "Time (ms)", "Voltage (mV)");
			}
		});
	}
}
