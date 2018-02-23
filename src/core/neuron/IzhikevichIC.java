package core.neuron;

/*
* IzhikevichIC is a helper class to provide initial conditions to an IzhikevichNeuronModel constructor
* */
public class IzhikevichIC
{
	private final double prV, prU, prI;

	public IzhikevichIC(double v, double u, double I)
	{
		this.prV=v;
		this.prU=u;
		this.prI=I;
	}

	public double v()
	{
		return prV;
	}

	public double u()
	{
		return prU;
	}

	public double I()
	{
		return prI;
	}
}
