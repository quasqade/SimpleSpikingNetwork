package core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by user on 16-Feb-18.
 * Neuron wraps around NeuronModel, adding connections to other neurons, synaptic delay and learning mechanisms
 */
public class Neuron
{
	private List<Spike> incomingSpikes; //incomingSpikes is a list of Spikes that have been sent through axons of this neuron
	private NeuronModel neuronModel; //neuronModel is a NeuronModel that performs simulation of neuron state
	private Set<Neuron> postsynapticNeurons; //postsynapticNeurons is a list of Neurons that are connected to this neurons' dendrites
	//TODO maybe Set is not necessary

	public Neuron()
	{
		incomingSpikes = new ArrayList<>();
		neuronModel = new NeuronModel();
		postsynapticNeurons = new HashSet<>();
	}

	//simulateTick advances incoming spikes, sets input current accordingly and performs simulation on a model
	public void simulateTick()
	{
		double I=0; //I is a sum of presynaptic currents

		for (Spike spike: incomingSpikes
			 )
		{
			if (!spike.advance())
				I+=spike.getVoltage(); //TODO check voltage-current relationship on synaptic connection
		}

		neuronModel.setI(I); //sets neural current to sum of arrived spike currents
		neuronModel.recalculate(); //computes new state of a neuron

		//determine if spiking and if so, create a spike targeted for all postsynaptic neurons
		//TODO randomize or otherwise vary delay
		if (neuronModel.isSpiking())
		{
			for (Neuron neuron: postsynapticNeurons
				 )
			{
				addIncomingSpike(new Spike(neuronModel.getV(), 5));
			}
		}
	}

	public void addIncomingSpike(Spike spike)
	{
		incomingSpikes.add(spike);
	}

	public void addPostsynapticNeuron(Neuron neuron)
	{
		this.postsynapticNeurons.add(neuron);
	}



	public boolean isSpiking()
	{
		return neuronModel.isSpiking();
	}
}
