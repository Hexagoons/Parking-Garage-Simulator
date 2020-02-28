package nl.hanze.itv1l.parkingsimulation.views.sideTabs;

import nl.hanze.itv1l.parkingsimulation.controllers.DataController;
import nl.hanze.itv1l.parkingsimulation.models.GraphItem;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.text.DecimalFormat;

/**
 * This chart wil create a pie chart based on the data given to it.
 *
 * @author Robin van Wijk
 * @since 1.0
 */
public class PieGraphView extends Graph {

    private ValueType valueType = ValueType.INT;

    /**
     * The constructor passes parameters through
     *
     * @param controller The controller that this view needs
     * @param pieSlices  The pieslices are GraphItems that represent the data of the chart
     */
    public PieGraphView(DataController controller, GraphItem[] pieSlices) {
        super(controller, pieSlices);
    }

    /**
     * The constructor passes parameters through and sets the valuetype
     *
     * @param controller The controller that this view needs
     * @param pieSlices  The pieslices are GraphItems that represent the data of the chart
     * @param valueType  The valueType specifies how we render the value of the charts as a string
     */
    public PieGraphView(DataController controller, GraphItem[] pieSlices, ValueType valueType) {
        super(controller, pieSlices);
        this.valueType = valueType;
    }

    /**
     * This method overrides the paintComponent method.
     * The method handles painting the graph in the way we would like
     *
     * @param g g is the graphics object we get from Swing
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        //Set up the pie chart
        Arc2D.Float arc = new Arc2D.Float(Arc2D.PIE);
        arc.setFrame(80, 40, 360, 360);

        //The starting value of which we calculate the arcs
        float currentValue = 0.0f;
        float totalValue = calculateTotal();

        //Setup fonts
        FontMetrics metrics = g.getFontMetrics();
        Font normalFond = g.getFont();
        Font boldLargeFond = new Font("default", Font.BOLD, 16);

        //Draw the total amount
        g.setFont(boldLargeFond);
        g.drawString("Total: " + ((valueType == ValueType.EURO) ? "\u20ac " : "") + totalValue, (getWidth() / 2 - metrics.stringWidth(totalValue + "")), 450);

        //Reset the fond
        g.setFont(normalFond);

        for (int i = 0; i < graphItems.length; i++) {
            //Calculate the arc based on the current item and draw it
            arc.setAngleStart((currentValue * 360 / totalValue));
            arc.setAngleExtent((graphItems[i].getValue() * 360 / totalValue));
            g2.setColor(graphItems[i].getColor());
            g2.fill(arc);

            //Add the the value to update the start point of the next arc in the next iteration
            currentValue += graphItems[i].getValue();

            //Draw the labels with the name of the slice and the percentage
            int width = getWidth() / graphItems.length;
            int x = width * i;

            String values = "";

            //Check the valuetype for formatting
            switch (valueType) {
                case INT:
                    values = (int) graphItems[i].getValue() + " | ";
                    break;
                case FLOAT:
                    values = graphItems[i].getValue() + " | ";
                    break;
                case EURO:
                    DecimalFormat df = new DecimalFormat("0.00");
                    values = "\u20ac " + df.format(graphItems[i].getValue()) + " | ";
                    break;
            }

            //Draw the name and value strings
            g.drawString(graphItems[i].getName(), x + (width - metrics.stringWidth(graphItems[i].getName())) / 2, 475);
            values += +Math.round(graphItems[i].getValue() / totalValue * 100) + "%";
            g.drawString(values, x + (width - metrics.stringWidth(values)) / 2, 490);
        }
    }

    //The type of value represented by this chart
    public enum ValueType {
        INT,
        FLOAT,
        EURO,
    }
}

