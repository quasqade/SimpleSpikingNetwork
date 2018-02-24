package core.synapse;

import core.neuron.Neuron;

public class STDPSynapse extends Synapse
{
	private double tauPlus, tauMinus, aPlus, aMinus; //STDP parameters
	private double weightMin, weightMax; //weight limits
	private double weight; //synaptic weight
	private int counter; //counts how many ticks have elapsed
	private int presynapticLastSpike; //last presynaptic spike timing
	private int postsynapticLastSpike; //last postynaptic spike timing
	private boolean presynapticLastSpikeRegistered=true;
	private boolean postSynapticLastSpikeRegistered=true;

	public STDPSynapse(Neuron presynaptic, Neuron postsynaptic, int delay)
	{
		super(presynaptic, postsynaptic, delay);
		tauPlus = 20.0;
		tauMinus = 20.0;
		aPlus = 0.01;
		aMinus = 0.012;

		weightMin=-1;
		weightMax=1;

		weight=0;
	}

	//Replaces spike that has been added to a new one with voltage modified by synaptic weight
	@Override
	public void addSpike(Spike spike)
	{
		super.addSpike(new Spike(spike.getVoltage()*0, super.getDelay()));
	}

	//Adds weight modification rule to a synapse
	//TODO possibly store last spike timings in neurons themselves, while this wastes memory for simple synapses, it saves it for STDP
	@Override
	public void propagateSpikes()
	{
		super.propagateSpikes();

		counter++;
		//check for spikes at both neurons and update weights
		boolean updateWeights = false;
		if (super.getPostsynaptic().isSpiking())
		{
			postsynapticLastSpike = counter;
			postSynapticLastSpikeRegistered = false;
			updateWeights=true;
		}
		if (super.getPresynaptic().isSpiking())
		{
			presynapticLastSpike = counter;
			presynapticLastSpikeRegistered = false;
			updateWeights=true;
		}

		if (updateWeights && !presynapticLastSpikeRegistered && !postSynapticLastSpikeRegistered)
		{
			presynapticLastSpikeRegistered = true;
			postSynapticLastSpikeRegistered = true;
			double deltaW = STDPFunction(postsynapticLastSpike-presynapticLastSpike);
			weight+=deltaW;
			if (weight > weightMax)
			{
				weight=weightMax;
			}
			if (weight < weightMin)
			{
				weight = weightMin;
			}

		}

	}

//W(x)
	private double STDPFunction(int x)
	{
		if (x>0)
		{
			return aPlus*Math.exp(-1*x/tauPlus);
		}
		else
		{
			return -1*aMinus*Math.exp(x/tauMinus);
		}
	}

	public double getWeight()
	{
		return weight;
	}

	public int getPresynapticLastSpike()
	{
		return presynapticLastSpike;
	}

	public int getPostsynapticLastSpike()
	{
		return postsynapticLastSpike;
	}
}
