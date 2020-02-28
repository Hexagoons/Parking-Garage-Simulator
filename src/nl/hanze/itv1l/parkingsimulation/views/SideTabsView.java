package nl.hanze.itv1l.parkingsimulation.views;

import nl.hanze.itv1l.parkingsimulation.controllers.DataController;
import nl.hanze.itv1l.parkingsimulation.views.sideTabs.BarGraphView;
import nl.hanze.itv1l.parkingsimulation.views.sideTabs.PieGraphView;

import javax.swing.*;
import java.awt.*;

/**
 * This class is a tabbed view, it sets up its tabs and makes sure the tabs get their data
 *
 * @author Robin van Wijk
 * @since 1.0
 */
public class SideTabsView extends View<DataController> {
    private PieGraphView garagePieGraph;
    private BarGraphView queueBarGraph;
    private PieGraphView customersBarGraph;
    private PieGraphView financialPieChart;

    /**
     * The constructor creates the layout and passes data to the graph views
     * @param controller The controller of these views
     */
    public SideTabsView(DataController controller) {
        super(controller);

        //Create an outline border
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        //Create all the graphs and charts
        garagePieGraph     = new PieGraphView(controller,controller.getCarParkPieSlices());
        queueBarGraph      = new BarGraphView(controller,controller.getQueueSizesBars());
        customersBarGraph  = new PieGraphView(controller,controller.getCustomerBars());
        financialPieChart  = new PieGraphView(controller,controller.getRevenuePieSlices(), PieGraphView.ValueType.EURO);

        //Create the tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Occupation Overview", garagePieGraph);
        tabbedPane.addTab("Queue Sizes", queueBarGraph);
        tabbedPane.addTab("Customer Loss", customersBarGraph);
        tabbedPane.addTab("Finance", financialPieChart);
        add(tabbedPane);

        //Add all the views to the controller
        controller.addView(garagePieGraph);
        controller.addView(queueBarGraph);
        controller.addView(customersBarGraph);
        controller.addView(financialPieChart);
    }

    /**
     * This method overrides the preferred dimension of the view
     *
     * @return The preferred dimension of this object
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(540, 540);
    }

    /**
     * This update method makes sure the data of the views stays correct at all times.
     * This method also overrides the base update of View
     */
    @Override
    public void update() {
        garagePieGraph.setGraphItems(controller.getCarParkPieSlices());
        queueBarGraph.setGraphItems(controller.getQueueSizesBars());
        customersBarGraph.setGraphItems(controller.getCustomerBars());
        financialPieChart.setGraphItems(controller.getRevenuePieSlices());
    }
}
