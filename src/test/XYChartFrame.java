package test;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

/**
 * Created by user on 18-Feb-18.
 */
public class XYChartFrame extends JFrame
{
	public XYChartFrame(XYSeries series)
	{
		super("Result");
		XYSeriesCollection collection = new XYSeriesCollection();
		collection.addSeries(series);
		XYDataset dataset = (XYDataset) collection;

		JFreeChart chart = ChartFactory.createScatterPlot("Simulation", "Time(ms)", "Neuron", dataset);
		XYPlot plot = (XYPlot)chart.getPlot();
		plot.setBackgroundPaint(new Color(255,228,196));

		//create Panel
		ChartPanel panel = new ChartPanel(chart);
		setContentPane(panel);

		setSize(new Dimension(800,400));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
