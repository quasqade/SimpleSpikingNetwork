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

	public STDPSynapse(Neuron presynaptic, Neuron postsynaptic, int delay)
	{
		super(presynaptic, postsynaptic, delay);
		tauPlus = 20.0;
		tauMinus = 20.0;
		aPlus = 0.01;
		aMinus = 0.012;

		weightMin=0;
		weightMax = 0.0000001;

		weight=0.00000005;
	}

	@Override
	public void addSpike(Spike spike)
	{
		super.addSpike(new Spike(spike.getVoltage()*weight, super.getDelay()));
	}

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
			updateWeights=true;
		}
		if (super.getPresynaptic().isSpiking())
		{
			presynapticLastSpike = counter;
			updateWeights=true;
		}

		if (updateWeights)
		{
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
			return aMinus*Math.exp(x/tauMinus);
		}
	}

	public double getWeight()
	{
		return weight;
	}
}
