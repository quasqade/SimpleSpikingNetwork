package core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by user on 16-Feb-18.
 * NeuralNetwork represents a connected neural network of neurons and provides a way to run simulations with supplied
 * parameters
 */
public class NeuralNetwork
{

	public enum Architecture{RANDOM, FEEDFORWARD}

	private Set<Neuron> neuronSet;
	private List<List<Neuron>> layers;

	public NeuralNetwork(Architecture arch)
	{
		this(arch, 100);
	}

	public NeuralNetwork(Architecture arch, int size)
	{
		switch (arch)
		{
			case RANDOM:
				init(arch, size, 10);
				break;
			case FEEDFORWARD:
				init(arch, size, 100);
				break;
		}
	}

	public NeuralNetwork(Architecture arch, int size, int probability)
	{

	}

	public NeuralNetwork(Architecture arch, int layers, int inputSize, int outputSize)
	{
		neuronSet = new HashSet<>();
	}

	private void init(Architecture arch, int size, int probability)
	{
		neuronSet = new HashSet<>();
		for (int i = 0; i < size; i++)
		{
			neuronSet.add(new Neuron(Neuron.NeuronType.IZHIKEVICH, 5));
		}
	}

	public boolean addLayer(int size, int index, int probability)
	{
		return true;
	}


	public void simulate()
	{

	}
}


