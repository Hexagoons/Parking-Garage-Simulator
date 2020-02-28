package nl.hanze.itv1l.parkingsimulation.models.cars;

import java.awt.*;

/**
 * This class represents a parkingPassCar in the simulation.
 *
 * @author Shaquille Louisa
 * @since 1.0
 */
public class ParkingPassCar extends Car {
	public static final Color COLOR = new Color(128,177,211);

    /**
     * Constructor for objects of class ParkingPassCar.
     */
    public ParkingPassCar() {
        this.setHasToPay(false);
    }

    /**
     * @return the color of the ParkingPassCar.
     */
    public Color getColor(){
    	return COLOR;
    }
}
