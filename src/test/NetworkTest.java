package test;

import core.Neuron;
import core.NeuronModel;
import core.Spike;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;

import javax.swing.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by user on 16-Feb-18.
 * NetworkTest is a way to test a network using models defined in core package
 */
public class NetworkTest
{
	public static void main(String[] args)
	{
		//TODO reimplement this in NeuralNetwork if implemented correctly

		//create a set of 1000 randomly interconnected neurons
		List<Neuron> neuronList = new ArrayList<>();
		for (int i = 0; i< 1000; i++)
		{
			neuronList.add(new Neuron());
		}

		//randomly interconnect them
		//TODO check if loops are allowed (currently assumed yes)
		Random random = new Random();
		int connectionChance = 0; //chance (in percentages) that a given neuron pair will interconnect
		for (int i = 0; i < neuronList.size(); i++)
		{
			for (int j = 0; j < neuronList.size(); j++)
			{
				if (i!=j)
					continue; //do not connect neurons to themselves

				if (random.nextInt(100)<connectionChance)
				{
					//connect neuron i to j
					neuronList.get(i).addPostsynapticNeuron(neuronList.get(j));
				}
			}
		}


		//simulate network for 1000 ms
		XYSeries dataset = new XYSeries("Simulation");
		Instant before = Instant.now();

		//add random stimulations
		int initialSpikeProbability = 20; //chance that a given neuron will be stimulated initially
		for (Neuron neuron: neuronList
			 )
		{
			if (random.nextInt(100) < 20)
			{
				//neuron.addIncomingSpike(new Spike(30, 2));
			}
		}
		for (int i = 0; i < 1000; i++)
		{
			//calculate thalamic input
			double I = 0;
			for (int j = 0; j < neuronList.size(); j++)
			{
				Neuron neuron = neuronList.get(j);
				if (neuron.isSpiking())
				{
					I+=0.5*random.nextGaussian();
				}
			}

			//add random thalamic input
			for (int j = 0; j < neuronList.size(); j++)
			{
				Neuron neuron = neuronList.get(j);
				double kek = 5*random.nextGaussian()+I+10;
				neuron.setI(kek);
			}

			//simulate each neuron
			for (int j = 0; j < neuronList.size(); j++)
			{
				Neuron neuron = neuronList.get(j);
				neuron.simulateTick();
				if (neuron.isSpiking())
				{
					dataset.add(j,i); //X axis charts neurons by number and Y axis charts their spike timings
				}
			}

		}
		Instant after = Instant.now();
		Duration elapsed = Duration.between(before, after);
		System.out.println("Execution took " + elapsed.toMillis() + " milliseconds");

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				new XYChartFrame(dataset);
			}
		});

	}
}
