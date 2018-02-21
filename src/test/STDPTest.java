package test;

import core.Neuron;

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
		Neuron postsynaptic = new Neuron(Neuron.NeuronType.IZHIKEVICH, 5); //postsynaptic neuron that will be firing constantly.

	}
}
