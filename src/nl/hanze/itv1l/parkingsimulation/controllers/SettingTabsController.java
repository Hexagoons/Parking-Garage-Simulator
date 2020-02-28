package nl.hanze.itv1l.parkingsimulation.controllers;

import nl.hanze.itv1l.parkingsimulation.models.CarParkModel;
import nl.hanze.itv1l.parkingsimulation.models.Time;

/**
 * Controller for settings tab.
 *
 * @see nl.hanze.itv1l.parkingsimulation.views.SettingTabsView
 * @see nl.hanze.itv1l.parkingsimulation.models.CarParkModel
 * @author Roy Voetman
 * @since 1.0
 */
public class SettingTabsController extends Controller<CarParkModel> {

    /**
     * Constructs a DataController object and registers the provided model.
     *
     * @param model A model that represents data and the rules that govern access to and updates of
     *              the settings data.
     */
    public SettingTabsController(CarParkModel model) {
        super(model);
    }

    /**
     * Changes to ah hoc week day arrivals in the model.
     *
     * @param value Average of ad hoc week day arrivals per hour.
     */
    public void changeWeekDayArrivals(int value) {
        model.setWeekDayArrivals(value);
    }

    /**
     * Changes to ah hoc weekend arrivals in the model.
     *
     * @param value Average of ad hoc weekend arrivals per hour.
     */
    public void changeWeekendArrivals(int value) {
        model.setWeekendArrivals(value);
    }

    /**
     * Changes the weekend pass-holder arrivals in the model
     *
     * @param value Average percentage of pass-holders per day.
     */
    public void changeWeekendPassPercentage(int value) {
        model.setWeekendPassPercentage(value);
        model.calculatePassArrivals();
    }

    /**
     * Changes the weekend pass-holder arrivals in the model
     *
     * @param value Average percentage of pass-holders per day.
     */
    public void changeWeekDayPassPercentage(int value) {
        model.setWeekDayPassPercentage(value);
        model.calculatePassArrivals();
    }

    /**
     * Changes to amount of reserved spots on week days in the model.
     *
     * @param value Average amount of reserved spots hour.
     */
    public void changeWeekDayReservedArrivals(int value) {
        model.setWeekDayReservedArrivals(value);
    }

    /**
     * Changes to amount of reserved spots in the weekends in the model.
     *
     * @param value Average amount of reserved spots hour.
     */
    public void changeWeekendReservedArrivals(int value) {
        model.setWeekendReservedArrivals(value);
    }

    /**
     * Changes to speed of the simulation in the model.
     *
     * @param value The amount of milliseconds between each tick.
     */
    public void changeTickSpeed(int value) {
        model.setTickSpeed(value);
    }

    /**
     * Changes to speed of the entrance queue in the model.
     *
     * @param value Amount of cars that can be handled per minute.
     */
    public void changeEnterSpeed(int value) {
        model.setEnterSpeed(value);
    }

    /**
     * Changes to speed of the payment queue in the model.
     *
     * @param value Amount of cars that can be handled per minute.
     */
    public void changePaymentSpeed(int value) {
        model.setPaymentSpeed(value);
    }

    /**
     * Changes to speed of the exit queue in the model.
     *
     * @param value Amount of cars that can be handled per minute.
     */
    public void changeExitSpeed(int value) {
        model.setExitSpeed(value);
    }

    /**
     * Changes the amount of spots reserved for pass-holders in the model.
     *
     * @param value Amount of spots that will be reserved for pass-holders.
     */
    public void changeParkingPassReservedSpots(int value) {
        model.setParkingPassReservedSpots(value);
    }

    /**
     * Changes the number of floors the garage has, defined in the model.
     * Resets the cars and parkingSpots array, totalAmountOfSpots. numberOfOpenSpots
     * and the number of reserved spots to match the new architecture.
     *
     * @param value Amount of floors.
     */
    public void changeNumberOfFloors(int value) {
        model.setNumberOfFloors(value);
        model.resetSpots();
        model.setParkingPassReservedSpots(model.getNumberOfReservedSpots());
    }

    /**
     * Changes the number of rows the garage has, defined in the model.
     * Resets the cars and parkingSpots array, totalAmountOfSpots. numberOfOpenSpots
     * and the number of reserved spots to match the new architecture.
     *
     * @param value Amount of rows.
     */
    public void changeNumberOfRows(int value) {
        model.setNumberOfRows(value);
        model.resetSpots();
        model.setParkingPassReservedSpots(model.getNumberOfReservedSpots());
    }

    /**
     * Changes the number of places per row the garage has, defined in the model.
     * Resets the cars and parkingSpots array, totalAmountOfSpots. numberOfOpenSpots
     * and the number of reserved spots to match the new architecture.
     *
     * @param value Amount of places per row.
     */
    public void changeNumberOfPlaces(int value) {
        model.setNumberOfPlaces(value);
        model.resetSpots();
        model.setParkingPassReservedSpots(model.getNumberOfReservedSpots());
    }

    /**
     * Changes the total amount of pass-holders and recalculates the average amount
     * of pass-holders per hour.
     *
     * @param value Amount of total pass-holders.
     */
    public void changeAmountOfPassHolders(int value) {
        model.setAmountOfPassHolders(value);
        model.calculatePassArrivals();
    }

    /**
     * Changes the starting day of the simulation.
     *
     * @param day Integer representing the day where 0 equals Monday and 6 equals Sunday.
     */
    public void changeStartDay(Time.DayOfTheWeek day) {
        Time time = model.getTime();
        time.setStartingDay(day.getValue());

        model.setTime(time);
    }

    /**
     * Changes the starting hour of the simulation.
     *
     * @param hour Integer representing the day where 0 equals 00:00 and 23 equals 23:00.
     */
    public void changeStartTime(int hour) {
        Time time = model.getTime();
        time.setStartingHour(hour);

        model.setTime(time);
    }

    /**
     * Changes the custom duration of the simulation in the model.
     *
     * @param days   Integer representing the day where 0 equals Monday and 6 equals Sunday.
     * @param weeks  Integer representing the day where 0 equals 00:00 and 23 equals 23:00.
     * @param months Integer representing the amount of months.
     */
    public void changeCustomAmountOfTicks(int days, int weeks, int months) {
        model.setCustomAmountOfTicks((days * 1440) + (weeks * 10080) + (months * 43680));
    }

}
