package nl.hanze.itv1l.parkingsimulation.controllers;

import nl.hanze.itv1l.parkingsimulation.models.GraphItem;
import nl.hanze.itv1l.parkingsimulation.models.CarParkModel;
import nl.hanze.itv1l.parkingsimulation.models.Location;
import nl.hanze.itv1l.parkingsimulation.models.cars.*;

import java.awt.*;

/**
 * Controller for graph data.
 *
 * @see nl.hanze.itv1l.parkingsimulation.views.SideTabsView
 * @see nl.hanze.itv1l.parkingsimulation.models.CarParkModel
 * @author Robin van Wijk
 * @since 1.0
 */
public class DataController extends Controller<CarParkModel> {

    /**
     * Constructs a DataController object and registers the provided model.
     *
     * @param model A model that represents data and the rules that govern access to and updates of
     *              the graph data.
     */
    public DataController(CarParkModel model) {
        super(model);
    }

    /**
     * Determines the occupation of the amount and kind of cars.
     *
     * @return An array of GraphItem objects based on the current occupation.
     */
    public GraphItem[] getCarParkPieSlices() {
        GraphItem[] graphItems = new GraphItem[4];

        graphItems[0] = new GraphItem(0, AdHocCar.COLOR, "AdHocCar");
        graphItems[1] = new GraphItem(0, ParkingPassCar.COLOR, "ParkingPassCar");
        graphItems[2] = new GraphItem(0, ReservedCar.COLOR, "ReservedCar");
        graphItems[3] = new GraphItem(0, Color.gray, "Empty");

        for (int floor = 0; floor < model.getNumberOfFloors(); floor++) {
            for (int row = 0; row < model.getNumberOfRows(); row++) {
                for (int place = 0; place < model.getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Car car = model.getCarAt(location);

                    int index = 3; // Empty

                    if(car instanceof AdHocCar)
                        index = 0;
                    else if(car instanceof ParkingPassCar)
                        index = 1;
                    else if(car instanceof  ReservedCar)
                        index = 2;

                    graphItems[index].setValue(graphItems[index].getValue() + 1);
                }
            }
        }

        return graphItems;
    }

    /**
     * Determines the size of the queues and adds them to and array of graph items.
     *
     * @return An array of GraphItem objects based on the current size of the queues.
     */
    public GraphItem[] getQueueSizesBars() {
        return new GraphItem[] {
            new GraphItem(model.getEntranceCarQueue().carsInQueue(), new Color(128,177,211), "Normal Entrance"),
            new GraphItem(model.getEntrancePassQueue().carsInQueue(), new Color(252,127,114), "Passholder Entrance"),
            new GraphItem(model.getPaymentCarQueue().carsInQueue(), new Color(253,180,98), "Payment"),
            new GraphItem(model.getExitCarQueue().carsInQueue(), new Color(179,222,105), "Exit")
        };
    }

    /**
     * Determines the amount of customer loss based on the amount of customers and the
     * amount of customers lost by driving away due to too long waiting queues.
     *
     * @return An array of GraphItem objects based on the current size of the queues.
     */
    public GraphItem[] getCustomerBars() {
        int amountOfCustomers = model.getAmountOfCustomers();
        int amountOfCustomerLoss = model.getAmountOfCustomerLoss();

        return new GraphItem[] {
            new GraphItem(amountOfCustomers - amountOfCustomerLoss, new Color(128,177,211), "Customers"),
            new GraphItem(amountOfCustomerLoss, new Color(253,180,98), "Customer Loss"),
        };
    }

    /**
     * Creates an array of GraphItem objects to use in a subclass of Graph.
     * This is based on the currently earned revenue.
     *
     * @return An array of GraphItem objects based on the revenue calculated in the model
     */
    public GraphItem[] getRevenuePieSlices(){
        return new GraphItem[]{
                new GraphItem(model.getGeneralRevenue(), Color.BLUE, "General Customer Revenue" ),
                new GraphItem(model.getPassholderRevenue(), Color.MAGENTA, "Passholder Revenue" ),
        };
    }
}
