import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class CombinedXYPlot extends JFrame {
	
	private int[][] stockHistory;
	private double[][] marketHistory;
	private int day;
	
	public CombinedXYPlot() {
		
		
	}
	public CombinedXYPlot(JInternalFrame internalFrame, int[][] stock, double[][] market, int cur) {

		stockHistory = stock;
        marketHistory = market;
        day = cur;
        JFreeChart chart = createCombinedChart();
        ChartPanel chartPanel = new ChartPanel(chart, true, true, true, false, true);
        internalFrame.setContentPane(chartPanel);
//      private int[][] stockHistory = new int[4][100];
//    	private double[][] marketHistory = new double[2][100];
        
//        System.out.println("1: " + day + " " + cur);
    }

    private JFreeChart createCombinedChart() {

        // create sub-plot 1...
        XYDataset data1 = createDataset1();
        XYItemRenderer renderer1 = new StandardXYItemRenderer();
        NumberAxis rangeAxis1 = new NumberAxis("Stock");
        rangeAxis1.setRange(800, 1300);
        rangeAxis1.setTickUnit(new NumberTickUnit(100));
        
        XYPlot subplot1 = new XYPlot(data1, null, rangeAxis1, renderer1);
        subplot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        
//        domain.setTickUnit(new NumberTickUnit(0.1));
//        domain.setVerticalTickLabels(true);
//        NumberAxis range = (NumberAxis) subplot1.getRangeAxis();
//        range.setRange(0.0, 1.0);
//        range.setTickUnit(new NumberTickUnit(0.1));
        
//        final XYTextAnnotation annotation = new XYTextAnnotation("Hello!", 50.0, 10000.0);
//        annotation.setFont(new Font("SansSerif", Font.PLAIN, 9));
//        annotation.setRotationAngle(Math.PI / 4.0);
//        subplot1.addAnnotation(annotation);
        
        // create sub-plot 2...
        XYDataset data2 = createDataset2();
        XYItemRenderer renderer2 = new StandardXYItemRenderer();
        NumberAxis rangeAxis2 = new NumberAxis("Performance");
        //rangeAxis2.setAutoRangeIncludesZero(false);
        rangeAxis2.setRange(-0.06, 0.06);
        rangeAxis2.setTickUnit(new NumberTickUnit(0.02));
        
        NumberAxis domainAxis = new NumberAxis("day");
        domainAxis.setRange(0, day);
        domainAxis.setTickUnit(new NumberTickUnit(1));
        
        XYPlot subplot2 = new XYPlot(data2, null, rangeAxis2, renderer2);
        subplot2.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        
        // parent plot...
        CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new NumberAxis("Day"));
        plot.setGap(10);
        
        // add the sub-plots...
        plot.add(subplot1, 1);
        plot.add(subplot2, 1);
        plot.setOrientation(PlotOrientation.VERTICAL);

        // return a new chart containing the overlaid plot...
        return new JFreeChart("",
                              JFreeChart.DEFAULT_TITLE_FONT, plot, true);

    }

    private XYDataset createDataset1() {

        // create data set 1...
    	//System.out.println("2: " + day);
		
        XYSeries series1 = new XYSeries("Amazon");
        XYSeries series2 = new XYSeries("Facebook");
        XYSeries series3 = new XYSeries("Google");
        XYSeries series4 = new XYSeries("Microsoft");
        series1.add(0, stockHistory[0][0]);
        series2.add(0, stockHistory[1][0]);
        series3.add(0, stockHistory[2][0]);
        series4.add(0, stockHistory[3][0]);
        for(int i = 0; i < day; i++) {
        	series1.add(i + 1, stockHistory[0][i]);
        	series2.add(i + 1, stockHistory[1][i]);
        	series3.add(i + 1, stockHistory[2][i]);
        	series4.add(i + 1, stockHistory[3][i]);
        }        

        XYSeriesCollection collection = new XYSeriesCollection();
        collection.addSeries(series1);
        collection.addSeries(series2);
        collection.addSeries(series3);
        collection.addSeries(series4);
        return collection;
    }

    private XYDataset createDataset2() {

        // create data set 2...
        XYSeries series1 = new XYSeries("Market");
        XYSeries series2 = new XYSeries("My Portfolio");
        series1.add(0, 0);
        series2.add(0, 0);
        for(int i = 0; i < day; i++) {
        	series1.add(i + 1, marketHistory[0][i]);
        	series2.add(i + 1, marketHistory[1][i]);
        }        

        XYSeriesCollection collection = new XYSeriesCollection();
        collection.addSeries(series1);
        collection.addSeries(series2);
        return collection;
    }
    
//    public static void main(final String[] args) {
//
//    	CombinedXYPlot demo = new CombinedXYPlot();
//        demo.pack();
//        RefineryUtilities.centerFrameOnScreen(demo);
//        demo.setVisible(true);
//
//    }
}
