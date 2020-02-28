package nl.hanze.itv1l.parkingsimulation.models.cars;

import nl.hanze.itv1l.parkingsimulation.models.Location;

import java.awt.*;
import java.util.Random;

/**
 * This class represents a car in the simulation.
 *
 * @author unknown
 * @since 1.0
 */
public abstract class Car {

    private Location location;
    private int minutesLeft;
    private boolean isPaying;
    private boolean hasToPay;

    /**
     * Constructor for objects of class Car.
     */
    public Car() {
        Random random = new Random();
        int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
    }

    /**
     * @return the location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param location the new location of this car.
     * Set the location of this car.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * @return the minutesLeft.
     */
    public int getMinutesLeft() {
        return minutesLeft;
    }

    /**
     * @param minutesLeft how much minutes the car has left.
     * Set the minutesLeft.
     */
    public void setMinutesLeft(int minutesLeft) {
        this.minutesLeft = minutesLeft;
    }

    /**
     * @return isPaying.
     */
    public boolean getIsPaying() {
        return isPaying;
    }

    /**
     * @param isPaying if the car is to paying.
     * Set isPaying.
     */
    public void setIsPaying(boolean isPaying) {
        this.isPaying = isPaying;
    }

    /**
     * @return hasToPay.
     */
    public boolean getHasToPay() {
        return hasToPay;
    }

    /**
     * @param hasToPay if the car has to pay.
     * Set hasToPay.
     */
    public void setHasToPay(boolean hasToPay) {
        this.hasToPay = hasToPay;
    }

    /**
     * Decrease minutesLeft.
     */
    public void tick() {
        minutesLeft--;
    }

    /**
     * @return The color of the Car.
     */
    public abstract Color getColor();
}