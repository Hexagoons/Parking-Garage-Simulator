package nl.hanze.itv1l.parkingsimulation.views.sideTabs;

import nl.hanze.itv1l.parkingsimulation.controllers.DataController;
import nl.hanze.itv1l.parkingsimulation.models.GraphItem;
import nl.hanze.itv1l.parkingsimulation.views.View;

import java.awt.*;

/**
 * This is the base class of the graphs, it stores the graph item data and has some default methods
 *
 * @author Robin van Wijk
 * @since 1.0
 */
public abstract class Graph extends View<DataController> {
    GraphItem[] graphItems;

    /**
     * The constructor sets the controller and makes sure the graph items are set.
     * It also ensures the opaque setting is set to false to stop weird artifacting.
     *
     * @param controller The controller the view needs to function
     * @param graphItems The data that will be represented in the views
     */
    Graph(DataController controller, GraphItem[] graphItems) {
        super(controller);
        setGraphItems(graphItems);
        setOpaque(false);
    }

    /**
     * This method overrides the preferred dimension of the view
     *
     * @return The preferred dimension of this object
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(520, 500);
    }

    /**
     * The update method of the graph views
     */
    @Override
    public void update() {
        repaint();
    }

    /**
     * This method sets the graph items
     *
     * @param items Items represents the data the graph will visualize
     */
    public void setGraphItems(GraphItem[] items) {
        this.graphItems = items;
    }

    /**
     * This method calculated the total value based on the graph items
     *
     * @return The total value of the chart as a float
     */
    protected float calculateTotal() {
        float result = 0;

        for (GraphItem item : graphItems) {
            result += item.getValue();
        }
        return result;
    }

    /**
     * This method calculates the max value in the graph based on the grapg items
     *
     * @return The maximum value of the chart as a float
     */
    protected float getMax() {
        float result = 0;

        for (GraphItem item : graphItems) {
            if (item.getValue() > result)
                result = (float) item.getValue();
        }
        return result;
    }

    /**
     * This method calculates the min value in the graph based on the grapg items
     *
     * @return The minimum value of the chart as a float
     */
    protected float getMin() {
        float result = 0;

        for (GraphItem item : graphItems) {
            if (item.getValue() < result)
                result = (float) item.getValue();
        }
        return result;
    }

    /**
     * This method calculates the average value in the graph based on the grapg items
     *
     * @return The average value of the chart as a float
     */
    protected float getAverage() {
        return calculateTotal() / graphItems.length;
    }
}
