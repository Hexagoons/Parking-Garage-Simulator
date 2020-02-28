package nl.hanze.itv1l.parkingsimulation.views.tabs;

import nl.hanze.itv1l.parkingsimulation.controllers.SettingTabsController;
import nl.hanze.itv1l.parkingsimulation.models.CarParkModel;

import javax.swing.*;
import java.awt.*;

/**
 * Tab view for the queues.
 *
 * @author Roy Voetman
 * @since 1.0
 */
public class QueuesTabView extends Tab {
    private CarParkModel model;

    /**
     * Constructs a queues tab view.
     *
     * @param controller Controller that handles all the logic of this view.
     */
    public QueuesTabView(SettingTabsController controller) {
        super(controller);
        this.model = controller.getModel();

        this.tab.setPreferredSize(new Dimension(1220, 90));

        this.tab.setLayout(new GridLayout(0,2));

        createQueueSliders();
    }

    private void createQueueSliders() {
        JSlider enterSpeed = createSlider(new JSlider(JSlider.HORIZONTAL, 0, 15, model.getEnterSpeed()), "Enter queue speed: ");
        enterSpeed.addChangeListener(e -> controller.changeEnterSpeed( ((JSlider) e.getSource()).getValue() ));

        JSlider paymentSpeed = createSlider(new JSlider(JSlider.HORIZONTAL, 0, 15, model.getPaymentSpeed()), "Payment queue speed: ");
        paymentSpeed.addChangeListener(e -> controller.changePaymentSpeed( ((JSlider) e.getSource()).getValue() ));

        JSlider exitSpeed = createSlider(new JSlider(JSlider.HORIZONTAL, 0, 15, model.getExitSpeed()), "Exit queue speed: ");
        exitSpeed.addChangeListener(e -> controller.changeExitSpeed( ((JSlider) e.getSource()).getValue() ));
    }
}