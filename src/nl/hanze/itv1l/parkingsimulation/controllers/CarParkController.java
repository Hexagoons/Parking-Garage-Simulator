package nl.hanze.itv1l.parkingsimulation.controllers;

import nl.hanze.itv1l.parkingsimulation.models.*;
import nl.hanze.itv1l.parkingsimulation.models.cars.Car;
import nl.hanze.itv1l.parkingsimulation.models.cars.CarQueue;
import nl.hanze.itv1l.parkingsimulation.models.cars.ParkingPassCar;
import nl.hanze.itv1l.parkingsimulation.models.cars.ReservedCar;

/**
 * Controller for the simulation.
 *
 * @see nl.hanze.itv1l.parkingsimulation.views.CarParkView
 * @see nl.hanze.itv1l.parkingsimulation.models.CarParkModel
 * @author Roy Voetman
 * @author Joey Marthé Behrens
 * @author Robin van Wijk
 * @author Shaquille Louisa
 * @since 1.0
 */
public class CarParkController extends Controller<CarParkModel> {

    /**
     * Constructs a CarParkController object and registers the provided model.
     *
     * @param model A model that represents data and the rules that govern access to and updates of
     *              the simulation data.
     */
    public CarParkController(CarParkModel model) {
        super(model);
    }

    /**
     * First, occupies the pass-holder queue and normal entrance queue.
     * Second, handles the occupied queues.
     *
     * @param day Integer representing a day, where 0 equals Monday and 6 equals Sunday.
     */
    private void handleEntrance(int day) {
        carsArriving(day);
        model.addReservedCarsToQueue();
        carsEntering(model.getEntrancePassQueue());
        carsEntering(model.getEntranceCarQueue());
    }

    /**
     * Determines which cars have to leave and removes them from their spot. Determines which
     * cars how to pay (Ad Hoc cars, Reserved cars) and goes through payment process. Removes cars from
     * the exit queue, based on the queue speed.
     */
    private void handleExit() {
        carsReadyToLeave();
        carsPaying();
        carsLeaving();
    }

    /**
     * Occupies the pass-holder queue with pass-holders and normal entrance queue with reserved
     * and ad-hoc cars.
     *
     * @param day Integer representing a day, where 0 equals Monday and 6 equals Sunday.
     */
    private void carsArriving(int day) {
        model.occupyQueue(day, model.getWeekDayReservedArrivals(), model.getWeekendReservedArrivals(), CarParkModel.CarType.RESERVED);
        model.occupyQueue(day, model.getWeekDayPassArrivals(), model.getWeekendPassArrivals(), CarParkModel.CarType.PASS);
        model.occupyQueue(day, model.getWeekDayArrivals(), model.getWeekendArrivals(), CarParkModel.CarType.AD_HOC);
    }

    /**
     * Removes cars from the front of the queue and assigns the cars to the next free location, based on
     * the enter queue speed. In the case of a reserved car the car will be assigned to
     * their preselected location.
     *
     * @param queue The queue that will be handled.
     */
    private void carsEntering(CarQueue queue) {
        int i = 0;
        while (queue.carsInQueue() > 0 && model.getNumberOfOpenSpots() > 0 && i < model.getEnterSpeed()) {
            Car car = queue.getFirstCarInQueue();
            boolean isParkingPassCar = (car instanceof ParkingPassCar);
            Location freeLocation = model.getFirstFreeLocation(isParkingPassCar);
            if (freeLocation != null) {
                queue.removeCar();
                if (car instanceof ReservedCar) {
                    ReservedCar reservedCar = (ReservedCar) car;
                    Location reservedLocation = reservedCar.getReservedLocation();
                    if (reservedLocation != null) {
                        model.setCarAt(reservedLocation, reservedCar);
                    }
                } else {
                    model.setCarAt(freeLocation, car);
                }
            }
            i++;
        }
    }

    /**
     * Determines the first car that has to leave. If the car is a pass-holder the car will be added
     * to the exit queue other wise the car will first be added to the payment queue.
     */
    private void carsReadyToLeave() {
        // Add leaving cars to the payment queue.
        Car car = model.getFirstLeavingCar();
        while (car != null) {
            if (car.getHasToPay()) {
                car.setIsPaying(true);
                model.addPaymentCarQueue(car);
            } else {
                carLeavesSpot(car);
            }
            car = model.getFirstLeavingCar();
        }
    }

    /**
     * Handles the payment process for every car in the payment queue, based on the payment queue speed.
     */
    private void carsPaying() {
        int i = 0;
        while (model.getPaymentCarQueue().carsInQueue() > 0 && i < model.getPaymentSpeed()) {
            Car car = model.removePaymentCarQueue();
            handlePayment(car);
            carLeavesSpot(car);
            i++;
        }
    }

    /**
     * Add car to the amount of customers and toggle the `isPaying` boolean.
     *
     * @param car A Car object that has to go through the payment process.
     */
    private void handlePayment(Car car){
        model.addCustomer();
        car.setIsPaying(false);
    }

    /**
     * Handles the exit queue, based on the exit queue speed.
     */
    private void carsLeaving() {
        int i = 0;
        while (model.getExitCarQueue().carsInQueue() > 0 && i < model.getExitSpeed()) {
            Car car = model.removeExitCarQueue();
            if (car instanceof ReservedCar) {
                ReservedCar reservedCar = (ReservedCar) car;
                if (model.getParkingSpotType(reservedCar.getReservedLocation()) != CarParkModel.ParkingSpot.PASS) {
                    model.setParkingSpotType(reservedCar.getReservedLocation(), CarParkModel.ParkingSpot.AD_HOC);
                }
            }
            i++;
        }
    }

    /**
     * Removes car from his assigned spot and add him to the exit car queue.
     *
     * @param car A Car object that has to leave his spot.
     */
    private void carLeavesSpot(Car car) {
        model.removeCarAt(car.getLocation());
        model.addExitCarQueue(car);
    }

    /**
     * Pauses the simulation
     *
     * @see #resumeSimulation
     */
    public void pauseSimulation() {
        model.setRunning(false);
    }

    /**
     * Resumes the simulation
     *
     * @see #pauseSimulation
     */
    public void resumeSimulation() {
        model.setRunning(true);
    }

    /**
     * Resets the simulation
     *
     * @see #startSimulation
     * @see #startSimulation(int)
     */
    public void resetSimulation() {
        model.reset();
    }

    /**
     * Starts the simulation with the custom amount of time.
     * Provided under the general tab in the settings section.
     *
     * @see nl.hanze.itv1l.parkingsimulation.views.SettingTabsView
     */
    public void startSimulation() {
        startSimulation(model.getCustomAmountOfTicks());
    }

    /**
     * Starts the simulation with the given amount of ticks.
     *
     * @param ticks The amount of ticks the simulation has to run.
     */
    public void startSimulation(int ticks) {
        model.resetTime();
        model.setRunning(true);
        model.setTicks(ticks);
    }

    /**
     * Handles the updating of the model data and updates all the registered views.
     */
    @Override
    public void tick() {
        if (model.getRunning() && model.getTicks() > 0) {
            model.getTime().addOneMinute();
            handleEntrance(model.getTime().getDay());
            tickCars();
            handleExit();

            model.setTicks(model.getTicks() - 1);
        } else if (model.getRunning() && model.getTicks() == 0) {
            model.setRunning(false);
        }

        updateViews();

        try {
            Thread.sleep(model.getTickSpeed());
        } catch (InterruptedException e) {
            e.printStackTrace(); // TODO: Prevent printing stack trace in production
        }
    }

    /**
     * Updates all the cars that are currently parked inside the garage.
     */
    private void tickCars() {

        for (int floor = 0; floor < model.getNumberOfFloors(); floor++) {
            for (int row = 0; row < model.getNumberOfRows(); row++) {
                for (int place = 0; place < model.getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);

                    Car car = model.getCarAt(location);

                    if (car != null)
                        car.tick();
                }
            }
        }

    }
}
