package core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by user on 16-Feb-18.
 * Neuron wraps around IzhikevichNeuronModel, adding connections to other neurons, synaptic delay and learning mechanisms
 */
public class Neuron
{
	public enum NeuronType{IZHIKEVICH};
	private List<Synapse> preSynapses; //preSynapses is a list of Synapses connecting to this neuron
	private List<Synapse> postSynapses; //postSynapses is a list of Synapses originating from this neuron
	private NeuronModel neuronModel; //neuronModel is a IzhikevichNeuronModel that performs simulation of neuron state
	private int postsynapticDelay; //postsynapticDelay determines what default delay should be used for axons


	public Neuron(NeuronType type, int postsynapticDelay)
	{
		preSynapses = new ArrayList<>();
		postSynapses = new ArrayList<>();
		this.postsynapticDelay = postsynapticDelay;
		switch (type)
		{

			case IZHIKEVICH:
				neuronModel = new IzhikevichNeuronModel();
				break;
		}
	}

	//simulateTick advances incoming spikes, sets input current accordingly and performs simulation on a model
	public void simulateTick()
	{
		double I=0; //I is a sum of presynaptic currents

		for (Synapse preSynapse: preSynapses)
		{
			preSynapse.propagateSpikes();
			List<Spike> arrivedSpikes = preSynapse.getArrivedSpikes();

			for (Spike spike: arrivedSpikes
				 )
			{
				I+=spike.getVoltage();
			}
		}

		//neuronModel.setI(I); //sets neural current to sum of arrived spike currents
		neuronModel.recalculate(); //computes new state of a neuron

		//determine if spiking and if so, create a spike targeted for all postsynaptic neurons
		//TODO randomize or otherwise vary delay
		if (neuronModel.isSpiking())
		{
			for (Synapse postSynapse: postSynapses
				 )
			{
				postSynapse.addSpike(new Spike(neuronModel.getV()));
			}
		}
	}

	public void addPostsynapticNeuron(Neuron neuron)
	{
		Synapse synapse = new Synapse(this, neuron, postsynapticDelay);
		this.postSynapses.add(synapse);
		neuron.addPreSynapse(synapse);
	}



	public boolean isSpiking()
	{
		return neuronModel.isSpiking();
	}

	public void setI(double I)
	{
		neuronModel.setI(I);
	}

	public NeuronModel getNeuronModel() {
		return neuronModel;
	}

	public void addPreSynapse(Synapse synapse)
	{
		preSynapses.add(synapse);
	}

	public List<Synapse> getPreSynapses()
	{
		return preSynapses;
	}

	public List<Synapse> getPostSynapses()
	{
		return postSynapses;
	}
}
