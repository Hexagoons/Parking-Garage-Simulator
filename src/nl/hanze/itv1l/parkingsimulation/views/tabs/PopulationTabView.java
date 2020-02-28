package nl.hanze.itv1l.parkingsimulation.views.tabs;

import nl.hanze.itv1l.parkingsimulation.controllers.SettingTabsController;
import nl.hanze.itv1l.parkingsimulation.models.CarParkModel;
import nl.hanze.itv1l.parkingsimulation.models.Time.DayOfTheWeek;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Tab view for the population (peak hours).
 *
 * @author Joey Marthé Behrens
 * @since 1.0
 */
public class PopulationTabView extends Tab {
    private CarParkModel model;

    private JComboBox selectedDay;
    private ArrayList<JLabel> weights;

    /**
     * Constructs a population tab view.
     *
     * @param controller Controller that handles all the logic of this view
     */
    public PopulationTabView(SettingTabsController controller) {
        super(controller);
        this.model = controller.getModel();

        this.tab.setPreferredSize(new Dimension(1220, 400));
        setupData();
    }

    /**
     * Method for initializing the population tab view.
     */
    private void setupData() {
        weights = new ArrayList<>();

        JLabel label1 = new JLabel("Selected day:");
        Font font = label1.getFont();
        label1.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        this.tab.add(label1);

        selectedDay = new JComboBox<>(DayOfTheWeek.values());
        selectedDay.addActionListener(e -> updateWeights());

        this.tab.add(selectedDay);

        JButton importButton = new JButton("Import");
        importButton.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(null);

            try {
                File file = fc.getSelectedFile();
                if (!file.getName().contains(".csv"))
                    JOptionPane.showMessageDialog(null, "File type must be of type .csv!", "Invalid file type", JOptionPane.ERROR_MESSAGE);
                this.model.importDaySettingsFile(file.getAbsolutePath());
            }
            catch (Exception ex) {
                System.out.println(ex);
            }
            updateWeights();
        });

        JButton exportButton = new JButton("Export");
        exportButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "We currently do not support exporting of peak data", "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            int value = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset your data?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (value == 0) {
                this.model.setDaySettingsDefault();
                updateWeights();
                JOptionPane.showMessageDialog(null, "Reset successful!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        this.tab.add(importButton);
        this.tab.add(exportButton);
        this.tab.add(resetButton);

        JPanel newPanel = new JPanel();
        newPanel.setLayout(new GridLayout(0, 25));
        newPanel.setPreferredSize(new Dimension(1220, 200));

        JLabel label2 = new JLabel("Hour:");
        label2.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        newPanel.add(label2);
        for (int i = 0; i < 24; i++)
            newPanel.add(new JLabel("" + i, JLabel.CENTER));

        newPanel.add(new JLabel(""));
        for (int i = 0; i < 24; i++) {
            int index = i;
            JButton addButton = new JButton("+");
            addButton.addActionListener(e -> {
                this.model.setDaySetting((DayOfTheWeek) selectedDay.getSelectedItem(), index, this.model.getDaySettings().get((DayOfTheWeek) selectedDay.getSelectedItem())[index] + 1);
                updateWeights();
            });
            newPanel.add(addButton);
        }

        JLabel label3 = new JLabel("Weight:");
        label3.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        newPanel.add(label3);
        for (int i = 0; i < 24; i++) {
            JLabel label = new JLabel("" + this.model.getDaySettings().get((DayOfTheWeek) selectedDay.getSelectedItem())[i], JLabel.CENTER);
            label.setForeground(Color.black);
            weights.add(label);
            newPanel.add(label);
        }

        newPanel.add(new JLabel(""));
        for (int i = 0; i < 24; i++) {
            int index = i;
            JButton subtractButton = new JButton("-");
            subtractButton.addActionListener(e -> {
                this.model.setDaySetting((DayOfTheWeek) selectedDay.getSelectedItem(), index, this.model.getDaySettings().get((DayOfTheWeek) selectedDay.getSelectedItem())[index] - 1);
                updateWeights();
            });
            newPanel.add(subtractButton);
        }

        this.tab.add(newPanel, BorderLayout.PAGE_START);
    }

    /**
     * Updates all the weight labels in the tab view
     */
    private void updateWeights() {
        for (int i = 0; i < weights.size(); i++) {
            Color color = Color.black;

            int value = this.model.getDaySetting((DayOfTheWeek) selectedDay.getSelectedItem())[i];

            if (value < 0)
                color = Color.red;

            weights.get(i).setText("" + value);
            weights.get(i).setForeground(color);
        }
    }
}
