package nl.hanze.itv1l.parkingsimulation.models.cars;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents a carQueue in the simulation.
 *
 * @author unknown
 * @since 1.0
 */
public class CarQueue {
    private Queue<Car> queue = new LinkedList<>();

    /**
     * Add a car to this queue.
     * @param car the new car.
     * @return if the car has been added.
     */
    public boolean addCar(Car car) {
        return queue.add(car);
    }

    /**
     * Remove the last car from this queue.
     * @return The removed car.
     */
    public Car removeCar() {
        return queue.poll();
    }

    /**
     * @return The first can in this queue.
     */
    public Car getFirstCarInQueue() {
        return queue.peek();
    }

    /**
     * @return The number of cars in this queue.
     */
    public int carsInQueue(){
    	return queue.size();
    }
}
