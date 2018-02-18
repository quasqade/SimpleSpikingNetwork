package core;

/**
 * Created by user on 16-Feb-18.
 * Spike represents a voltage spike travelling along an axon
 */
public class Spike
{
	private int counter;
	private double voltage;

	public Spike(double voltage, int counter)
	{
		this.voltage = voltage;
		this.counter = counter;
	}

	//advance decrements a counter representing how many ticks the spike has left to travel and returns true
	//on successful decrement and false if counter has reached zero (since advancement technically fails)
	public boolean advance()
	{
		counter--;
		return (counter > 0); //if counter reaches zero, return false to signify arrival at target neuron
	}

	public int getCounter()
	{
		return counter;
	}

	public double getVoltage()
	{
		return voltage;
	}
}
