package nl.hanze.itv1l.parkingsimulation.views.tabs;

import nl.hanze.itv1l.parkingsimulation.controllers.SettingTabsController;
import nl.hanze.itv1l.parkingsimulation.models.CarParkModel;
import nl.hanze.itv1l.parkingsimulation.models.Time;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

/**
 * Tab view for the general settings.
 *
 * @author Roy Voetman
 * @since 1.0
 */
public class GeneralTabView extends Tab {
    private JLabel timeLabel;

    private CarParkModel model;

    /**
     * Constructs the general tab view.
     *
     * @param controller Controller that handles all the logic of this view.
     */
    public GeneralTabView(SettingTabsController controller) {
        super(controller);
        this.model = controller.getModel();

        this.tab.setPreferredSize(new Dimension(1220, 90));

        this.tab.setLayout(new GridLayout(0,2));

        // Time panel
        this.tab.add(createTimePanel());

        // Tick speed
        JSlider tickSpeed = createSlider(new JSlider(JSlider.HORIZONTAL, 0, 300, Math.abs(model.getTickSpeed() - 250) ), "Simulation speed: ");
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(0, new JLabel("Slow") );
        labelTable.put(300, new JLabel("Fast") );
        tickSpeed.setLabelTable( labelTable );
        tickSpeed.addChangeListener(e -> controller.changeTickSpeed( Math.abs( ((JSlider) e.getSource()).getValue() - 250 ) ));

        // Starting at
        this.tab.add(createStartAtPanel());

        // Custom duration
        this.tab.add(createCustomDurationPanel());
    }

    /**
     * Creates a panel with the time data as label.
     *
     * @return A JPanel object with two labels, "Time: " and the current time
     *         inside of the simulation.
     */
    private JPanel createTimePanel() {
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new GridLayout(0, 2));

        JLabel label = new JLabel("Time:");
        timeLabel = new JLabel(model.getTime().toString());
        timePanel.add(label);
        timePanel.add(timeLabel);

        return timePanel;
    }

    private JPanel createStartAtPanel() {
        JPanel startAtPanel = new JPanel();
        startAtPanel.setLayout(new GridLayout(0, 2));

        JLabel label = new JLabel("Start at:");
        startAtPanel.add(label);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2));

        JComboBox daysOfTheWeek = new JComboBox<>(Time.DayOfTheWeek.values());
        daysOfTheWeek.addActionListener(e -> controller.changeStartDay( (Time.DayOfTheWeek) ((JComboBox) e.getSource()).getSelectedItem() ));
        inputPanel.add(daysOfTheWeek);

        String[] hours = new String[24];
        for (int i = 0; i < 24; i++)
            hours[i] = String.format("%02d", i) + ":00";
        JComboBox hoursOfTheDay = new JComboBox<>(hours);
        hoursOfTheDay.setSelectedItem("06:00");
        hoursOfTheDay.addActionListener(e -> controller.changeStartTime( ((JComboBox) e.getSource()).getSelectedIndex() ));
        inputPanel.add(hoursOfTheDay);

        startAtPanel.add(inputPanel);

        return startAtPanel;
    }

    /**
     * Creates a panel with the custom duration combo-boxes.
     *
     * @return A JPanel object with a label: "Custom duration: " and a panel with three combo-boxes.
     */
    private JPanel createCustomDurationPanel() {
        JPanel customDurationPanel = new JPanel();
        customDurationPanel.setLayout(new GridLayout(0, 2));

        JLabel label = new JLabel("Custom duration:");
        customDurationPanel.add(label);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 3));

        JComboBox days = new JComboBox<>(range(7, "Day(s)"));
        JComboBox weeks = new JComboBox<>(range(52, "Week(s)"));
        JComboBox months = new JComboBox<>(range(12, "Month(s)"));

        days.setSelectedIndex(1);
        days.addActionListener(e -> controller.changeCustomAmountOfTicks( ((JComboBox) e.getSource()).getSelectedIndex(), weeks.getSelectedIndex(), months.getSelectedIndex() ));
        inputPanel.add(days);

        weeks.addActionListener(e -> controller.changeCustomAmountOfTicks( days.getSelectedIndex(), ((JComboBox) e.getSource()).getSelectedIndex(), months.getSelectedIndex() ));
        inputPanel.add(weeks);

        months.addActionListener(e -> controller.changeCustomAmountOfTicks( days.getSelectedIndex(), weeks.getSelectedIndex(), ((JComboBox) e.getSource()).getSelectedIndex() ));
        inputPanel.add(months);

        customDurationPanel.add(inputPanel);

        return customDurationPanel;
    }

    /**
     * Creates an array of strings starting at 0 to the give integer max.
     * Every string is suffixed with the given suffix.
     *
     * @param max     Integer that indicates the max amount in the range
     * @param suffix  String that will be added at the end of each string in the array.
     * @return an array of strings starting at 0 to the give integer max.
     */
    private String[] range(int max, String suffix) {
        String[] array = new String[max+1];

        for (int i = 0; i <= max; i++)
            array[i] = "" + i + " " + suffix;

        return array;
    }

    /**
     * This update method makes sure the data of the views stays correct at all times.
     * This method also overrides the base update of View
     */
    @Override
    public void update() {
        timeLabel.setText(model.getTime().toString());
    }

}