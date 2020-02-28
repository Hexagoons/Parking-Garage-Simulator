package nl.hanze.itv1l.parkingsimulation.views.sideTabs;

import nl.hanze.itv1l.parkingsimulation.controllers.DataController;
import nl.hanze.itv1l.parkingsimulation.models.GraphItem;

import java.awt.*;

/**
 * This class will create a bar graph based on the data that is given to it.
 *
 * @author Robin van Wijk
 * @author Roy Voetman
 * @since 1.0
 */
public class BarGraphView extends Graph {
    /**
     * The constructor makes sure the parent gets the variables needed to function
     *
     * @param controller The controller of this bargraph
     * @param bars The bars are the GrapgItems that represent the data that this graph will visualize
     */
    public BarGraphView(DataController controller, GraphItem[] bars) {
        super(controller, bars);
    }

    /**
     * This method overrides the paintComponent method.
     * The method handles painting the graph in the way we would like
     *
     * @param g g is the graphics object we get from Swing
     */
    @Override
    public void paintComponent(Graphics g) {
        for (int i = 0; i < graphItems.length; i++) {
            //Calculate the variables needed to draw the current bar
            int height = (int) graphItems[i].getValue();
            int width = getWidth() / graphItems.length;
            int x = width * i;
            int y = getHeight() - height - 50;

            //Draw the current bar in the correct color
            g.setColor(graphItems[i].getColor());
            g.fillRect(x, y, width, height);

            //Draw the labels with the name and values of the bars
            FontMetrics metrics = g.getFontMetrics();
            String value = "" + (int) graphItems[i].getValue();
            g.drawString(value, x + (width - metrics.stringWidth(value)) / 2, y - 10);
            g.drawString(graphItems[i].getName(), x + (width - metrics.stringWidth(graphItems[i].getName())) / 2, 475);
        }
    }
}