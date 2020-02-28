package nl.hanze.itv1l.parkingsimulation.models.cars;

import java.awt.*;

/**
 * This class represents an adHocCar in the simulation.
 *
 * @author unknown
 * @since 1.0
 */
public class AdHocCar extends Car {
    public static final Color COLOR = new Color(252,127,114);

    /**
     * Constructor for objects of class AdHocCar.
     */
    public AdHocCar() {
        this.setHasToPay(true);
    }

    /**
     * @return The color of the AdHocCar.
     */
    public Color getColor(){
    	return COLOR;
    }
}
