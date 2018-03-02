package test.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by user on 18-Feb-18.
 */
public class XYChartFrame extends JFrame
{
	public enum MarkerType {SMALL_CROSS, TINY_DOT}

	public XYChartFrame(XYSeries series, String title, String xLabel, String yLabel, MarkerType markerType)
	{
		super("Result");
		XYSeriesCollection collection = new XYSeriesCollection();
		collection.addSeries(series);
		XYDataset dataset = (XYDataset) collection;

		JFreeChart chart = ChartFactory.createScatterPlot(title, xLabel, yLabel, dataset);
		chart.removeLegend();
		XYPlot plot = (XYPlot)chart.getPlot();
		plot.setBackgroundPaint(new Color(255,255,255));
		Shape marker = ShapeUtilities.createDiamond(1);
		switch (markerType)
		{
			case SMALL_CROSS:
				marker = ShapeUtilities.createDiagonalCross((float)0.2,(float)0.2);
				plot.getRenderer().setSeriesPaint(0, Color.red);
				break;
			case TINY_DOT:
				marker = ShapeUtilities.createDiamond((float)0.2);
				plot.getRenderer().setSeriesPaint(0, Color.gray);
				break;
		}
		plot.getRenderer().setSeriesShape(0, marker);


		//create Panel
		ChartPanel panel = new ChartPanel(chart);
		setContentPane(panel);

		setSize(new Dimension(800,400));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
