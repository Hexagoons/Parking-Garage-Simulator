package nl.hanze.itv1l.parkingsimulation.views.tabs;

import nl.hanze.itv1l.parkingsimulation.controllers.SettingTabsController;
import nl.hanze.itv1l.parkingsimulation.models.CarParkModel;

import javax.swing.*;
import java.awt.*;

/**
 * Tab view for the architecture of the simulation.
 *
 * @author Roy Voetman
 * @since 1.0
 */
public class ArchitectureTabView extends Tab {
    private CarParkModel model;

    /**
     * Constructs a architecture tab view.
     *
     * @param controller Controller that handles all the logic of this view.
     */
    public ArchitectureTabView(SettingTabsController controller) {
        super(controller);
        this.model = controller.getModel();

        this.tab.setPreferredSize(new Dimension(1220, 90));

        this.tab.setLayout(new GridLayout(0,2));

        createArchitectureSliders();
    }

    /**
     * Creates the architecture sliders.
     */
    private void createArchitectureSliders() {
        JSlider numberOfFloors = createSlider(new JSlider(JSlider.HORIZONTAL, 1, 3, model.getNumberOfFloors()), "Number of floors: ");
        numberOfFloors.addChangeListener(e -> controller.changeNumberOfFloors( ((JSlider) e.getSource()).getValue() ));

        JSlider numberOfRows = createSlider(new JSlider(JSlider.HORIZONTAL, 1, 6, model.getNumberOfRows()), "Number of rows: ");
        numberOfRows.addChangeListener(e -> controller.changeNumberOfRows( ((JSlider) e.getSource()).getValue() ));

        JSlider numberOfPlaces = createSlider(new JSlider(JSlider.HORIZONTAL, 1, 41, model.getNumberOfPlaces()), "Number of places: ");
        numberOfPlaces.addChangeListener(e -> controller.changeNumberOfPlaces( ((JSlider) e.getSource()).getValue() ));
    }
}