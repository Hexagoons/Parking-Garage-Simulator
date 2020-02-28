package nl.hanze.itv1l.parkingsimulation.models;

import java.awt.*;

/**
 * This class represents data in a graph of chart
 *
 * @author Robin van Wijk
 * @since 1.0
 */
public class GraphItem {
    private double value;
    private Color color;
    private String name;

    /**
     * The constructor sets the initial values of this class
     * @param value The value is the value of the data
     * @param color The color the chart item will be
     * @param name The name of the item
     */
    public GraphItem(double value, Color color, String name) {
        this.value = value;
        this.color = color;
        this.name = name;
    }

    /**
     * @return The value
     */
    public double getValue() {
        return value;
    }

    /**
     * @return The color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * This method sets the value to the parameters amount
     * @param value The new value of this item
     */
    public void setValue(double value) {
        this.value = value;
    }
}