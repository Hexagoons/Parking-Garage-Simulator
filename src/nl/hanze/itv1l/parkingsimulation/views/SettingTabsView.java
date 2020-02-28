package nl.hanze.itv1l.parkingsimulation.views;

import nl.hanze.itv1l.parkingsimulation.controllers.SettingTabsController;
import nl.hanze.itv1l.parkingsimulation.models.CarParkModel;
import nl.hanze.itv1l.parkingsimulation.views.tabs.*;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * View for the settings tabs.
 *
 * @see nl.hanze.itv1l.parkingsimulation.controllers.SettingTabsController
 * @author Roy Voetman
 * @since 1.0
 */
public class SettingTabsView extends View<SettingTabsController> {
    private LinkedHashMap<String, Tab> tabs;
    private JTabbedPane tabbedPane;

    /**
     * The constructor creates the layout and passes data to the tab views
     * @param controller The controller of these views
     */
    public SettingTabsView(SettingTabsController controller) {
        super(controller);

        this.tabs = new LinkedHashMap<>();

        tabs.put("General", new GeneralTabView(controller));
        tabs.put("Arrivals", new ArrivalsTabView(controller));
        tabs.put("Subscribers", new SubscribersTabView(controller));
        tabs.put("Queues", new QueuesTabView(controller));
        tabs.put("Architecture", new ArchitectureTabView(controller));
        tabs.put("Population", new PopulationTabView(controller));

        tabbedPane = new JTabbedPane();

        for(Map.Entry<String, Tab> entry : tabs.entrySet())
            tabbedPane.addTab(entry.getKey(), entry.getValue());

        tabbedPane.setEnabledAt(4, true);

        add(tabbedPane);
    }

    /**
     * This update method makes sure the data of the views stays correct at all times.
     * This method also overrides the base update of View
     */
    @Override
    public void update() {
        for(Map.Entry<String, Tab> entry : tabs.entrySet())
            (entry.getValue()).update();

        CarParkModel model = controller.getModel();

        if(model.getRunning())
            tabbedPane.setEnabledAt(4, false);
        else
            tabbedPane.setEnabledAt(4, true);
    }

    /**
     * Tell the GUI manager how big we would like to be.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1260, 300);
    }
}