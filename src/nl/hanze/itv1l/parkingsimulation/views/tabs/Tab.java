package nl.hanze.itv1l.parkingsimulation.views.tabs;

import nl.hanze.itv1l.parkingsimulation.controllers.SettingTabsController;
import nl.hanze.itv1l.parkingsimulation.views.View;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract view for the tab views.
 *
 * @see nl.hanze.itv1l.parkingsimulation.controllers.SettingTabsController
 * @author Roy Voetman
 * @since 1.0
 */
public abstract class Tab extends View<SettingTabsController> {
    JPanel tab = new JPanel();

    /**
     * Constructs a tab by registering a controller and adding a JPanel to this view.
     *
     * @param controller The settings controller of these views.
     */
    Tab(SettingTabsController controller) {
        super(controller);

        add(tab);
    }

    /**
     * Creates a range slider.
     *
     * @param slider A JSlider object to be styled.
     * @param label  A String that will be used as the label.
     * @return A JSlider that has been styled by with major tick spacing
     *         and snap to ticks enabled.
     */
    JSlider createSlider(JSlider slider, String label) {
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new GridLayout(0, 2));

        slider.setMajorTickSpacing(slider.getMaximum() / 5);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);

        sliderPanel.add(new JLabel(label));
        sliderPanel.add(slider);

        tab.add(sliderPanel);

        return slider;
    }

    /**
     * Tell the GUI manager how big we would like to be.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1220, 250);
    }
}
