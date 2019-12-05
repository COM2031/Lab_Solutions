/**
 * @author Dr. Manal Helal
 * @date created: September 2019
 * @Module COM2031 Advanced Computer Algorithms - Computer Science Department
 * @University of Surrey
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.LogarithmicAxis;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.demo.BarChartDemo1;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.statistics.Regression;
import org.jfree.data.xy.*;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;



public class chartComparisons extends ApplicationFrame {
	
	public chartComparisons(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	
	
	
	
	private JFreeChart createChart(XYDataset dataset, String chartName)
	{
		 //create the chart...
		JFreeChart chart = ChartFactory.createXYLineChart(chartName,
				"Data Size",
				"Execution Time",
				dataset, // data
				PlotOrientation.VERTICAL, 
				true, // include legend
				true, // tooltips
				false // urls
				);
		
		
		
		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.lightGray);
		
		
		// get a reference to the plot for further customisation...
		XYPlot plot = (XYPlot) chart.getPlot();
		
		// change to logarithmic scale in the range axis
		//ValueAxis x = plot.getDomainAxis();
		//ValueAxis y = plot.getRangeAxis();
		
		//LogarithmicAxis ly = new LogarithmicAxis("Execution Time");
		//ly.setDefaultAutoRange(y.getDefaultAutoRange());
		//plot.setRangeAxis(ly);
		//LogAxis lx = new LogAxis("Data Size");
		//lx.setDefaultAutoRange(x.getDefaultAutoRange());
		//plot.setDomainAxis(lx);


		plot.setBackgroundPaint(Color.lightGray);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		// step render
		XYStepRenderer renderer = new XYStepRenderer();
		renderer.setBaseShapesVisible(true);
		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		//renderer.setSeriesStroke(1, new BasicStroke(5.0f));
		renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
		renderer.setDefaultEntityRadius(6);
		plot.setRenderer(renderer);

		//SymbolAxis rangeAxis = new SymbolAxis("", labels);
		//rangeAxis.setTickUnit(new NumberTickUnit(1));
		//rangeAxis.setRange(0, noprocess);
		//plot.setRangeAxis(rangeAxis);

		//NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		//rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// OPTIONAL CUSTOMISATION COMPLETED.
		return chart;
	}
	
	
	public  XYSeries createSeries (String seriesName) {
		XYSeries series = new XYSeries (seriesName);
		return series;
	}
	
	public void addPoint (XYSeries series, long x, long y) {
		series.add( x, y ,false);
	}
	
	public  void createDataset(List<XYSeries> seriesList, int simulations)  {
		

		XYSeriesCollection dataset = new XYSeriesCollection();
		
		List<XYSeries> trendsList = new ArrayList<>();
	    
	   
		for (int i = 0; i< seriesList.size(); i ++) { // for all series passed
			XYSeries series = seriesList.get(i);
			if (simulations > 2) { // add trend if more than 2 points 
				dataset.addSeries (series);
				
				double[] coefficients = Regression.getOLSRegression(dataset, 0);
		        double b = coefficients[0]; // intercept
		        double m = coefficients[1]; // slope
		        
		        XYSeries trend = new XYSeries("Trend " + series.getKey());
				//add here regression data
				
		        
		        // first point in the series
		        double x = series.getDataItem(0).getXValue();
		        //System.out.println("x:"+x+"m:"+m+"b:"+b+"ans:"+ m * x + b);
		        trend.add(x, m * x + b);
		        
		        // point point in the series
		        x = series.getDataItem(series.getItemCount() - 1).getXValue();
		        //System.out.println("x:"+x+"m:"+m+"b:"+b+"ans:"+ m * x + b);
		        trend.add(x, m * x + b);
				
		        dataset.removeAllSeries();
		        trendsList.add(trend);
			}
		}
		
		for (int i = 0; i< seriesList.size(); i ++) { // for all series passed
			XYSeries series = seriesList.get(i);
			dataset.addSeries(series);
		}
		
		for (int i = 0; i< trendsList.size(); i ++) { // for all trends created
			XYSeries series = trendsList.get(i);
			dataset.addSeries(series);
		}
		    
		    JFreeChart chart = createChart(dataset, "Execution Time Comparison");
			ChartPanel chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new java.awt.Dimension(750, 400));
			
			setContentPane(chartPanel);
		  }
	

}
