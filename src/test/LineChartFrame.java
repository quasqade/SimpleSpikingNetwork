package test;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

/**
 * Created by user on 16-Feb-18.
 * A simple JFrame rendering a dataset
 */
public class LineChartFrame extends JFrame
{
	public LineChartFrame(DefaultCategoryDataset dataset, String title, String xLabel, String yLabel)
	{
		super("Result");
		JFreeChart chart = ChartFactory.createLineChart(title, xLabel, yLabel, dataset);
		ChartPanel panel = new ChartPanel(chart);
		setContentPane(panel);
		pack();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
