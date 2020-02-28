package nl.hanze.itv1l.parkingsimulation.models.cars;
import nl.hanze.itv1l.parkingsimulation.models.Location;
import nl.hanze.itv1l.parkingsimulation.models.CarParkModel;
import nl.hanze.itv1l.parkingsimulation.models.TimeStamp;
import nl.hanze.itv1l.parkingsimulation.models.Time;

import java.awt.*;
import java.util.*;

/**
 * This class represents a reservedCar in the simulation.
 *
 * @author Shaquille Louisa
 * @since 1.0
 */
public class ReservedCar extends Car {
    
    public static final Color COLOR = new Color(179,222,105);
    private Location reservedLocation;
    private TimeStamp arrivalTime;
    private boolean willShowUp;

    /**
     * @param carParkModel the carPark model.
     * Constructor for objects of class ReservedCar.
     */
    public ReservedCar(CarParkModel carParkModel) {
        setArrivalTime();
        this.setHasToPay(true);
        setRandomReservedLocation(carParkModel);
    }

    /**
     * @param carParkModel the carPark model.
     * Reserve a random location in the garage.
     */
    private void setRandomReservedLocation(CarParkModel carParkModel) {
        Location location = carParkModel.getRandomFreeLocation();
        if(location == null)
            return;
        carParkModel.setReservedParkingSpot(location);
        this.reservedLocation = location;
    }

    /**
     * Set the time at which this reservedCar can enter the garage.
     */
    private void setArrivalTime() {
        Random random = new Random();
        willShowUp = (random.nextInt(5) != 0);
        int minutesLate = random.nextInt(60);
        int currentMonth = Time.getCurrentTimeStamp().getMonth();
        int currentWeek = Time.getCurrentTimeStamp().getWeek();
        int currentDay = Time.getCurrentTimeStamp().getDay();
        int currentHour = Time.getCurrentTimeStamp().getHour() + 1; //reservations can only be placed an hour in advance.
        arrivalTime = new TimeStamp(currentMonth, currentWeek,currentDay,currentHour,minutesLate);
    }

    /**
     * @return If this reservedCar will enter the garage or not.
     */
    public boolean CheckIfCarHasArrived() {
        return arrivalTime.equals(Time.getCurrentTimeStamp());
    }

    /**
     * @return If this reservedCar will enter the garage or not.
     */
    public boolean getWillShowUp() {
        return  willShowUp;
    }

    /**
     * @return The timestamp at which this reservedCar will arrive.
     */
    public TimeStamp getArrivalTimeStamp() {
        return arrivalTime;
    }

    /**
     * @return The reserved location.
     */
    public Location getReservedLocation() {
        return reservedLocation;
    }

    /**
     * @return The color of the ReservedCar.
     */
    public Color getColor(){
        return COLOR;
    }
}
