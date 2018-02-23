package test;

import core.neuron.Neuron;
import core.synapse.STDPSynapse;

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
		final int INJECTED_CURRENT = 20; //Injected current is I in Izhikevich equations, this value affects the rate of spiking
		final int DELAY_VARIATION = 1; //how many ms of delay there should be between different presynaptic neurons
		final int PRESYNAPTIC_AMOUNT = 20; //how many presynaptic neurons there are

		Neuron postsynaptic = new Neuron(Neuron.NeuronType.IZHIKEVICH, 5); //postsynaptic neuron that will be firing constantly.
		List<Neuron> presynapticList = new ArrayList<>();

		//create presynaptic neurons


		for (int i = 0; i<PRESYNAPTIC_AMOUNT; i++)
		{
			Neuron neuron = new Neuron(Neuron.NeuronType.IZHIKEVICH, 1); //create a presynaptic neuron
			presynapticList.add(neuron);
			neuron.addPostsynapticNeuron(postsynaptic); //connect neurons
		}


		//simulate network for 1000+ms

		int nextInjection = 0;
		int nextInjectedPresynaptic = 0;
		final int postsynapticInjection = PRESYNAPTIC_AMOUNT*DELAY_VARIATION/2;

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
				}
			}

			postsynaptic.simulateTick();
			if (postsynaptic.isSpiking())
			{
				System.out.println("Postsynaptic neuron is spiking at " + i + " ms.");
			}
		}

	}
}
