package nl.hanze.itv1l.parkingsimulation.main;

import nl.hanze.itv1l.parkingsimulation.controllers.CarParkController;
import nl.hanze.itv1l.parkingsimulation.controllers.Controller;
import nl.hanze.itv1l.parkingsimulation.controllers.DataController;
import nl.hanze.itv1l.parkingsimulation.models.CarParkModel;
import nl.hanze.itv1l.parkingsimulation.views.CarParkView;
import nl.hanze.itv1l.parkingsimulation.views.SideTabsView;
import nl.hanze.itv1l.parkingsimulation.controllers.SettingTabsController;
import nl.hanze.itv1l.parkingsimulation.views.SettingTabsView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Main entry point for the application
 *
 * @since 1.0
 */
public class Application {
    private static final JFrame APP_FRAME = new JFrame("Parking Simulator");
    private static final int SCREEN_WIDTH = 1260;
    private static final int SCREEN_HEIGHT = 900;
    private ArrayList<Controller> controllers;

    private static boolean isRunning = false;

    /**
     * Constructs the application class and initializes the styling.
     */
    public Application() {
        controllers = new ArrayList<>();

        //Initializing the styling
        InitializeStyling();
    }

    /**
     * Changes the default styling to the system default.
     */
    private void InitializeStyling(){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e){ System.err.println("Look and Feel given is unsupported");}
        catch (Exception e){ System.err.println("Couldn't use new look and feel, using the default");}
    }

    /**
     * Main entry point of the application. Defines a main panel and
     * initializes/registers all the Models, Controllers and Views
     */
    public void run() {
        //Create a main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        //Base Models
        CarParkModel carParkModel = new CarParkModel();

        //Base Controllers
        CarParkController carParkController = new CarParkController(carParkModel);
        DataController dataController = new DataController(carParkModel);
        SettingTabsController settingTabsController = new SettingTabsController(carParkModel);

        controllers.add(carParkController);
        controllers.add(dataController);
        controllers.add(settingTabsController);

        //Base Views
        CarParkView carParkView = new CarParkView(carParkController);
        SideTabsView sideTabsView = new SideTabsView(dataController);
        SettingTabsView settingTabsView = new SettingTabsView(settingTabsController);

        //Add Views to controllers
        carParkController.addView(carParkView);
        settingTabsController.addView(settingTabsView);
        dataController.addView(sideTabsView);

        //Add the views to the main panel
        mainPanel.add(carParkView, BorderLayout.CENTER);
        mainPanel.add(sideTabsView, BorderLayout.LINE_END);
        mainPanel.add(settingTabsView, BorderLayout.PAGE_END);

        //Set up window
        APP_FRAME.getContentPane().add(mainPanel);
        APP_FRAME.pack();
        APP_FRAME.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        APP_FRAME.setResizable(false);
        APP_FRAME.setLayout(null);
        APP_FRAME.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE | JFrame.EXIT_ON_CLOSE);
        APP_FRAME.setVisible(true);
        APP_FRAME.getContentPane().requestFocusInWindow();

        isRunning = true;
        while (isRunning) {
            for (Controller controller : controllers) {
                controller.tick();
            }
        }
    }

    /**
     * Handles the quiting of the application
     */
    public static void quit(){
        isRunning = false;
    }
}
