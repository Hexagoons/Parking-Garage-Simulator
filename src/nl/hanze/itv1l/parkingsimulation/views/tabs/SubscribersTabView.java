package nl.hanze.itv1l.parkingsimulation.views.tabs;

import nl.hanze.itv1l.parkingsimulation.controllers.SettingTabsController;
import nl.hanze.itv1l.parkingsimulation.models.CarParkModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * Tab view for the subscribers.
 *
 * @author Roy Voetman
 * @since 1.0
 */
public class SubscribersTabView extends Tab {
    private CarParkModel model;
    private JSlider parkingPassParkingSpots;

    /**
     * Constructs a subscriber tab view.
     *
     * @param controller Controller that handles all the logic of this view.
     */
    public SubscribersTabView(SettingTabsController controller) {
        super(controller);
        this.model = controller.getModel();

        this.tab.setPreferredSize(new Dimension(1220, 90));

        this.tab.setLayout(new GridLayout(0,2));

        createSliders();
        createPassHoldersTextField();
    }

    /**
     * Create all the arrival sliders.
     */
    private void createSliders() {
        JSlider weekDayPassArrivals = createSlider(new JSlider(JSlider.HORIZONTAL, 0, 100, model.getWeekDayPassPercentage()), "Week day percentage: ");
        weekDayPassArrivals.addChangeListener(e -> controller.changeWeekDayPassPercentage( ((JSlider) e.getSource()).getValue() ));

        JSlider weekendPassArrivals = createSlider(new JSlider(JSlider.HORIZONTAL, 0, 100, model.getWeekendPassPercentage()), "Weekend percentage: ");
        weekendPassArrivals.addChangeListener(e -> controller.changeWeekendPassPercentage( ((JSlider) e.getSource()).getValue() ));

        parkingPassParkingSpots = createSlider(new JSlider(JSlider.HORIZONTAL, 0, model.getNumberOfTotalSpots(), model.getParkingPassReservedSpots()), "Spots reserved for subscribers: ");
        parkingPassParkingSpots.addChangeListener(e -> controller.changeParkingPassReservedSpots( ((JSlider) e.getSource()).getValue() ));
    }

    /**
     * Creates the amount of pass-holders text field.
     */
    private void createPassHoldersTextField() {
        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new GridLayout(0, 2));
        JTextField textField = new JTextField("" + model.getAmountOfPassHolders());
        textFieldPanel.add(new JLabel("Amount of subscribers: "));
        textFieldPanel.add(textField);
        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { validate(); }
            public void removeUpdate(DocumentEvent e) { validate(); }
            public void insertUpdate(DocumentEvent e) { validate(); }

            void validate() {
                boolean valid = true;
                int value = 0;

                try {
                    value = Integer.parseInt(textField.getText());

                    if(value < 0)
                        valid = false;
                } catch(NumberFormatException e) {
                    valid = false;
                }

                if(!valid)
                    JOptionPane.showMessageDialog(null, "Error: Please enter a positive number", "Error Massage", JOptionPane.ERROR_MESSAGE);
                else
                    controller.changeAmountOfPassHolders( value );
            }
        });

        this.tab.add(textFieldPanel);
    }

    /**
     * This update method makes sure the data of the views stays correct at all times.
     * This method also overrides the base update of View
     */
    @Override
    public void update() {
        parkingPassParkingSpots.setMaximum(model.getNumberOfTotalSpots());
    }
}