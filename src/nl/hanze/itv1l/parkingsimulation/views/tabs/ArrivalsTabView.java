package nl.hanze.itv1l.parkingsimulation.views.tabs;

import nl.hanze.itv1l.parkingsimulation.controllers.SettingTabsController;
import nl.hanze.itv1l.parkingsimulation.models.CarParkModel;

import javax.swing.*;
import java.awt.*;

/**
 * Tab view for the ah doc and reserved arrivals.
 *
 * @author Roy Voetman
 * @since 1.0
 */
public class ArrivalsTabView extends Tab {
    private CarParkModel model;

    /**
     * Constructs a arrivals tab view.
     *
     * @param controller Controller that handles all the logic of this view.
     */
    public ArrivalsTabView(SettingTabsController controller) {
        super(controller);
        this.model = controller.getModel();

        this.tab.setPreferredSize(new Dimension(1220, 135));

        this.tab.setLayout(new GridLayout(0,2));

        createArrivalSliders();
    }

    /**
     * Creates the arrival sliders.
     */
    private void createArrivalSliders() {

        // Labels
        JLabel label1 = new JLabel("Week day arrivals:");
        JLabel label2 = new JLabel("Weekend arrivals:");
        Font font = label1.getFont();
        label1.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        label2.setFont(font.deriveFont(font.getStyle() | Font.BOLD));

        this.tab.add(label1);
        this.tab.add(label2);

        JSlider weekDayArrivals = createSlider(new JSlider(JSlider.HORIZONTAL, 0, 400, model.getWeekDayArrivals()), "Ad Hoc cars: ");
        weekDayArrivals.addChangeListener(e -> controller.changeWeekDayArrivals( ((JSlider) e.getSource()).getValue() ));

        JSlider weekendArrivals = createSlider(new JSlider(JSlider.HORIZONTAL, 0, 400, model.getWeekendArrivals()), "Ad Hoc cars: ");
        weekendArrivals.addChangeListener(e -> controller.changeWeekendArrivals( ((JSlider) e.getSource()).getValue() ));

        JSlider weekDayReservedArrivals = createSlider(new JSlider(JSlider.HORIZONTAL, 0, 400, model.getWeekDayReservedArrivals()), "Reserved cars: ");
        weekDayReservedArrivals.addChangeListener(e -> controller.changeWeekDayReservedArrivals( ((JSlider) e.getSource()).getValue() ));

        JSlider weekendReservedArrivals = createSlider(new JSlider(JSlider.HORIZONTAL, 0, 400, model.getWeekendReservedArrivals()), "Reserved cars: ");
        weekendReservedArrivals.addChangeListener(e -> controller.changeWeekendReservedArrivals( ((JSlider) e.getSource()).getValue() ));
    }
}