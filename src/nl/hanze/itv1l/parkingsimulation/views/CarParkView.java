package nl.hanze.itv1l.parkingsimulation.views;

import nl.hanze.itv1l.parkingsimulation.controllers.CarParkController;
import nl.hanze.itv1l.parkingsimulation.models.CarParkModel;
import nl.hanze.itv1l.parkingsimulation.models.Location;
import nl.hanze.itv1l.parkingsimulation.models.cars.Car;

import javax.swing.*;
import java.awt.*;

/**
 * View for the simulation view.
 *
 * @see nl.hanze.itv1l.parkingsimulation.controllers.CarParkController
 * @author Roy Voetman
 * @author Joey Marthé Behrens
 * @author Robin van Wijk
 * @author Shaquille Louisa
 * @since 1.0
 */
public class CarParkView extends View<CarParkController> {

    private Dimension size;
    private Image carParkImage;

    private JPanel runMenu = new JPanel();
    private JPanel actionMenu = new JPanel();
    private boolean runMenuEnabled = true;

    private JButton resumeButton;
    private JButton pauseButton;

    /**
     * Constructor for objects of class CarPark
     */
    public CarParkView(CarParkController controller) {
        super(controller);
        size = new Dimension(0, 0);
        setBorder(BorderFactory.createLineBorder(Color.BLACK,1));

        add(createRunMenu());
        add(createActionMenu());
    }

    /**
     * Tell the GUI manager how big we would like to be.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(720   , 540);
    }

    /**
     * The car park views component needs to be redisplayed. Copy the
     * internal image to screen.
     */
    @Override
    public void paintComponent(Graphics g) {
        if (carParkImage == null) {
            return;
        }

        Dimension currentSize = getSize();
        if (size.equals(currentSize)) {
            g.drawImage(carParkImage, 0, 0, null);
        }
        else {
            // Rescale the previous image.
            g.drawImage(carParkImage, 0, 0, currentSize.width, currentSize.height, null);
        }
    }

    @Override
    public void update() {
        CarParkModel model = controller.getModel();

        // Create a new car park image if the size has changed.
        if (!size.equals(getSize())) {
            size = getSize();
            carParkImage = createImage(size.width, size.height);
        }

        Graphics graphics = carParkImage.getGraphics();
        if(!model.getRunning())
            graphics.clearRect(0, 0, getWidth(), getHeight());

        for(int floor = 0; floor < model.getNumberOfFloors(); floor++) {
            for(int row = 0; row < model.getNumberOfRows(); row++) {
                for(int place = 0; place < model.getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Car car = model.getCarAt(location);
                    Color color;
                    if(car == null) {
                        color = model.getParkingSpotColor(floor, row, place);
                    }
                    else {
                        color = car.getColor();
                    }

                    drawPlace(graphics, location, color);
                }
            }
        }
        repaint();
    }

    /**
     * Paint a place on this car park views in a given color.
     */
    private void drawPlace(Graphics graphics, Location location, Color color) {
        graphics.setColor(color);
        graphics.fillRect(
                location.getFloor() * 220 + (1 + (int) Math.floor(location.getRow() * 0.5)) * 60 + (location.getRow() % 2) * 20,
                60 + location.getPlace() * 10,
                20 - 1,
                10 - 1); // TODO use dynamic size or constants
    }

    private JPanel createRunMenu() {
        runMenu.setLayout(new GridLayout(0,4));

        JButton dayButton = new JButton("1 Day");
        dayButton.addActionListener(e -> controller.startSimulation(1440));
        runMenu.add(dayButton);

        JButton weekButton = new JButton("1 Week");
        weekButton.addActionListener(e -> controller.startSimulation(10080));
        runMenu.add(weekButton);

        JButton monthButton = new JButton("1 Month");
        monthButton.addActionListener(e -> controller.startSimulation(43680));
        runMenu.add(monthButton);

        JButton customButton = new JButton("Custom");
        customButton.addActionListener(e -> controller.startSimulation());
        runMenu.add(customButton);

        for(Component component : runMenu.getComponents()) {
            if(component instanceof JButton)
                ((JButton) component).addActionListener(e -> toggleMenus());
        }

        return runMenu;
    }

    private JPanel createActionMenu() {
        actionMenu.setLayout(new GridLayout(0,3));

        resumeButton = new JButton("Resume");
        resumeButton.addActionListener(e -> {
            controller.resumeSimulation();
            toggleResumeAndPause();
        });
        actionMenu.add(resumeButton);

        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> {
            controller.pauseSimulation();
            toggleResumeAndPause();
        });
        actionMenu.add(pauseButton);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            controller.resetSimulation();
            toggleMenus();
        });
        actionMenu.add(resetButton);

        for(Component component : actionMenu.getComponents())
            component.setEnabled(false);

        return actionMenu;
    }

    private void toggleMenus() {
        for(Component component : actionMenu.getComponents())
            component.setEnabled(runMenuEnabled);

        for(Component component : runMenu.getComponents()) {
            component.setEnabled(!runMenuEnabled);
        }

        if(runMenuEnabled)
            resumeButton.setEnabled(false);

        runMenuEnabled = !runMenuEnabled;
    }

    private void toggleResumeAndPause() {
        resumeButton.setEnabled( !resumeButton.isEnabled() );
        pauseButton.setEnabled( !pauseButton.isEnabled() );
    }
}
