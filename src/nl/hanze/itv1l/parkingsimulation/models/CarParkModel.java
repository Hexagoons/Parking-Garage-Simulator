package nl.hanze.itv1l.parkingsimulation.models;

import nl.hanze.itv1l.parkingsimulation.models.Time.DayOfTheWeek;
import nl.hanze.itv1l.parkingsimulation.models.cars.AdHocCar;
import nl.hanze.itv1l.parkingsimulation.models.cars.Car;
import nl.hanze.itv1l.parkingsimulation.models.cars.CarQueue;
import nl.hanze.itv1l.parkingsimulation.models.cars.ParkingPassCar;
import nl.hanze.itv1l.parkingsimulation.models.cars.ReservedCar;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.awt.*;

/**
 * Model that holds all the data for the parking simulator
 *
 * @see nl.hanze.itv1l.parkingsimulation.models.Model
 * @author Roy Voetman
 * @author Joey Marthé Behrens
 * @author Robin van Wijk
 * @author Shaquille Louisa
 * @since 1.0
 */
public class CarParkModel extends Model {

    public enum CarType {
        AD_HOC,
        PASS,
        RESERVED,
    }

    public enum ParkingSpot {
        AD_HOC(Color.white),
        PASS(new Color(0.8f, 0.8f, 1)),
        RESERVED(new Color(1, 1, 0.6f));

        private final Color color;

        ParkingSpot(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }

    private Time time = new Time();

    private int tickSpeed = 100;
    private boolean running;
    private int ticks;
    private int customAmountOfTicks = 1440;    // One day

    private int weekDayArrivals = 100;         // average number of arriving cars per hour
    private int weekendArrivals = 200;         // average number of arriving cars per hour
    private int weekDayPassArrivals;           // average number of arriving cars per hour
    private int weekendPassArrivals;           // average number of arriving cars per hour
    private int weekDayPassPercentage = 80;    // percentage of arriving cars per day
    private int weekendPassPercentage = 5;     // percentage of arriving cars per day
    private int weekDayReservedArrivals = 50;
    private int weekendReservedArrivals = 5;

    private int enterSpeed = 3;                // number of cars that can enter per minute
    private int paymentSpeed = 7;              // number of cars that can pay per minute
    private int exitSpeed = 5;                 // number of cars that can leave per minute

    private HashMap<DayOfTheWeek, int[]> daySettings;

    private CarQueue entranceCarQueue;
    private CarQueue entrancePassQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;

    private int numberOfFloors = 3;
    private int numberOfRows = 6;
    private int numberOfPlaces = 30;
    private int numberOfTotalSpots;
    private int numberOfOpenSpots;
    private int numberOfReservedSpots = 60;

    private int amountOfCustomers;
    private int amountOfCustomerLoss;

    private int amountOfPassHolders = 1800;
    private double passholderPrice = 25;
    private double generalPrice = 5;
    private int generalCustomerAmount = 0;

    private Car[][][] cars;
    private ParkingSpot[][][] parkingSpots;
    private ArrayList<ReservedCar> reservedCars = new ArrayList<>();

    /**
     * Constructs a CarParkModel object and initializes all fields to
     * their default values.
     */
    public CarParkModel() {
        initializeFields();
    }

    /**
     * Initializes all the variables used in the simulation
     */
    private void initializeFields() {
        this.generalCustomerAmount = 0;
        this.numberOfTotalSpots = numberOfFloors * numberOfRows * numberOfPlaces;
        this.numberOfOpenSpots = this.numberOfTotalSpots;
        this.cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];
        this.parkingSpots = new ParkingSpot[numberOfFloors][numberOfRows][numberOfPlaces];

        this.ticks = 0;
        this.running = false;
        this.amountOfCustomers = 0;
        this.amountOfCustomerLoss = 0;

        this.entranceCarQueue = new CarQueue();
        this.entrancePassQueue = new CarQueue();
        this.paymentCarQueue = new CarQueue();
        this.exitCarQueue = new CarQueue();

        this.daySettings = new HashMap<DayOfTheWeek, int[]>() {{
            put(DayOfTheWeek.Monday, new int[24]);
            put(DayOfTheWeek.Tuesday, new int[24]);
            put(DayOfTheWeek.Wednesday, new int[24]);
            put(DayOfTheWeek.Thursday, new int[24]);
            put(DayOfTheWeek.Friday, new int[24]);
            put(DayOfTheWeek.Saturday, new int[24]);
            put(DayOfTheWeek.Sunday, new int[24]);
        }};

        this.calculatePassArrivals();
        this.setParkingPassReservedSpots(numberOfReservedSpots);
    }

    /**
     * Calculates the amount of arrivals with a parking pass.
     */
    public void calculatePassArrivals() {
        int weekDayArrivalsPerDay = amountOfPassHolders / 100 * weekDayPassPercentage;
        int weekEndArrivalsPerDay = amountOfPassHolders / 100 * weekendPassPercentage;
        this.weekDayPassArrivals = weekDayArrivalsPerDay / 24;
        this.weekendPassArrivals = weekEndArrivalsPerDay / 24;
    }

    /**
     * Returns the car at the specified location
     * @param location Location in the parking garage
     * @return Car at the specified location
     */
    public Car getCarAt(Location location) {
        return (locationIsValid(location)) ? cars[location.getFloor()][location.getRow()][location.getPlace()] : null;
    }

    /**
     * Sets a new car at a location in the parking garage
     * @param location Location in the parking garage
     * @param car Car object which is to be set
     * @return True if successful and false if unsuccessful
     */
    public boolean setCarAt(Location location, Car car) {
        if (!locationIsValid(location))
            return false;

        Car oldCar = getCarAt(location);

        if (oldCar != null)
            return false;

        cars[location.getFloor()][location.getRow()][location.getPlace()] = car;
        car.setLocation(location);
        numberOfOpenSpots--;

        return true;
    }

    /**
     * Removes a car at the specified location
     * @param location Location where a car has to be removed
     * @return The car that was removed
     */
    public Car removeCarAt(Location location) {
        if (!locationIsValid(location))
            return null;

        Car car = getCarAt(location);

        if (car == null)
            return null;

        cars[location.getFloor()][location.getRow()][location.getPlace()] = null;
        if (car instanceof ReservedCar) {
            if (getParkingSpotType(location) != ParkingSpot.PASS)
                parkingSpots[location.getFloor()][location.getRow()][location.getPlace()] = ParkingSpot.AD_HOC;
        }
        car.setLocation(null);
        numberOfOpenSpots++;

        return car;
    }

    /**
     * Searches the parking garage for the first empty spot.
     * @param isParkingPassCar Specifies whether or not to check for parking pass spots
     * @return The first free location
     */
    public Location getFirstFreeLocation(boolean isParkingPassCar) {
        int locationIndex = 0;
        for (int floor = 0; floor < numberOfFloors; floor++) {
            for (int row = 0; row < numberOfRows; row++) {
                for (int place = 0; place < numberOfPlaces; place++) {
                    Location location = new Location(floor, row, place);
                    locationIndex++;
                    if (!(!isParkingPassCar && locationIndex <= numberOfReservedSpots) && getParkingSpotType(location) != ParkingSpot.RESERVED)
                        if (getCarAt(location) == null)
                            return location;
                }
            }
        }

        return null;
    }

    /**
     * Gets the first leaving car
     * @return The leaving car
     */
    public Car getFirstLeavingCar() {
        for (int floor = 0; floor < numberOfFloors; floor++) {
            for (int row = 0; row < numberOfRows; row++) {
                for (int place = 0; place < numberOfPlaces; place++) {
                    Location location = new Location(floor, row, place);

                    Car car = getCarAt(location);

                    if (car != null && car.getMinutesLeft() <= 0 && !car.getIsPaying())
                        return car;
                }
            }
        }

        return null;
    }

    /**
     * Adds a new car to the queue
     * @param numberOfCars The amount of cars
     * @param type The type of the cars
     */
    public void addArrivingCars(int numberOfCars, CarType type) {
        // Add the cars to the back of the queue.
        switch (type) {
            case AD_HOC:
                for (int i = 0; i < numberOfCars; i++) {
                    addEntranceCarQueue(new AdHocCar());
                }
                break;
            case PASS:
                for (int i = 0; i < numberOfCars; i++) {
                    addEntrancePassQueue(new ParkingPassCar());
                }
                break;
            case RESERVED:
                for (int i = 0; i < numberOfCars; i++) {
                    reservedCars.add(new ReservedCar(this));
                }
                break;
        }
    }

    /**
     * Adds a reserverd car to the queue
     */
    public void addReservedCarsToQueue() {
        if (getNumberOfOpenSpots() <= 0)
            return;
        for (int i = 0; i < reservedCars.size(); i++) {
            ReservedCar reservedCar = reservedCars.get(i);
            if (reservedCar.getArrivalTimeStamp().getDay() == Time.getCurrentTimeStamp().getDay()) {
                if (reservedCar.getWillShowUp()) { //Check if the location was reserved for this day
                    if (reservedCar.CheckIfCarHasArrived()) {
                        addEntranceCarQueue(reservedCar);
                        reservedCars.remove(reservedCar);
                        i--;
                    }
                }
            } else {
                Location reservedLocation = reservedCar.getReservedLocation();
                if (reservedLocation != null && getParkingSpotType(reservedLocation) != CarParkModel.ParkingSpot.PASS) {
                    setParkingSpotType(reservedLocation, CarParkModel.ParkingSpot.AD_HOC);
                }
                reservedCars.remove(reservedCar);
                i--;
            }
        }
    }

    /**
     * Adds cars to the waiting queue
     * @param day     The day
     * @param weekDay The weekday
     * @param weekend The weekendday
     * @param type The car type
     */
    public void occupyQueue(int day, int weekDay, int weekend, CarType type) {
        int numberOfCars = getNumberOfCars(day, weekDay, weekend);
        int peakWeight = getDaySetting(DayOfTheWeek.values()[day])[time.getHour()] * 3;
        if (type == CarType.AD_HOC)
            numberOfCars = numberOfCars * (1 + (peakWeight / 10));

        amountOfCustomers += numberOfCars;

        int queueSize = (type == CarType.PASS) ? entrancePassQueue.carsInQueue() : entranceCarQueue.carsInQueue() * (peakWeight == 0 ? 1 : peakWeight);

        // Cars decide to drive away.
        if (queueSize > 10) {
            amountOfCustomerLoss += numberOfCars;
            numberOfCars = 0;
        }

        addArrivingCars(numberOfCars, type);
    }

    /**
     * Gets a random free location
     * @return A free random location
     */
    public Location getRandomFreeLocation() {
        ArrayList<Location> locations = new ArrayList<>();
        int locationIndex = 0;
        for (int floor = 0; floor < numberOfFloors; floor++) {
            for (int row = 0; row < numberOfRows; row++) {
                for (int place = 0; place < numberOfPlaces; place++) {
                    locationIndex++;
                    if (!(locationIndex <= numberOfReservedSpots)) {
                        Location location = new Location(floor, row, place);
                        if (getCarAt(location) == null) {
                            locations.add(location);
                        }
                    }
                }
            }
        }
        Random random = new Random();
        if (locations.size() == 0)
            return null;
        int randomLocationIndex = random.nextInt(locations.size());
        return locations.get(randomLocationIndex);
    }

    /**
     * Gets the number of arriving cars
     * @param day     The day
     * @param weekDay The weekday
     * @param weekend The weekendday
     * @return Amount of arriving cars
     */
    public int getNumberOfCars(int day, int weekDay, int weekend) {
        Random random = new Random();

        // Get the average number of cars that arrive per hour.
        int averageNumberOfCarsPerHour = day < 5 ? weekDay : weekend;

        DayOfTheWeek currentDay = null;

        for (DayOfTheWeek cd : DayOfTheWeek.values()) {
            if (day == cd.getValue()) {
                currentDay = cd;
                break;
            }
        }

        double deviation = 0.3f;

        // Calculate the number of cars that arrive this minute.
        double standardDeviation = averageNumberOfCarsPerHour * deviation;
        double numberOfCarsPerHour = averageNumberOfCarsPerHour + random.nextGaussian() * standardDeviation;
        return (int) Math.round(numberOfCarsPerHour / 60);
    }

    /**
     * Checks if a location is valid
     * @param location Specified location
     * @return True if valid and false if invalid
     */
    private boolean locationIsValid(Location location) {
        int floor = location.getFloor();
        int row = location.getRow();
        int place = location.getPlace();

        return !(floor < 0 || floor > numberOfFloors || row < 0 || row > numberOfRows || place < 0 || place > numberOfPlaces);
    }

    /**
     * Gets general pricing
     * @return The general pricing
     */
    public double getGeneralPricing() {
        return generalPrice;
    }

    /**
     * Gets passholder pricing
     * @return The passholder price
     */
    public double getPassholderPricing() {
        return passholderPrice;
    }

    /**
     * Sets a new general price
     * @param newPrice New Price
     */
    public void setGeneralPricing(double newPrice) {
        generalPrice = newPrice;
    }

    /**
     * Sets new passholder pricing
     * @param newPrice New price
     */
    public void setPassholderPricing(double newPrice) {
        passholderPrice = newPrice;
    }

    /**
     * Adds a new customer
     */
    public void addCustomer() {
        generalCustomerAmount++;
    }

    /**
     * Gets total general revenue
     * @return The total general revenue
     */
    public double getGeneralRevenue() {
        return getGeneralPricing() * generalCustomerAmount;
    }

    /**
     * Gets the total passholder revenue
     * @return The total passholder revenue
     */
    public double getPassholderRevenue() {
        return getPassholderPricing() * amountOfPassHolders * (time.getMonth() + 1);
    }

    /**
     * Gets the overal total revenue
     * @return Overal revenue
     */
    public double getTotalRevenue() {
        return getPassholderRevenue() + getGeneralRevenue();
    }

    /**
     * Adds car to the entrance queue
     * @param car Specified car
     */
    public void addEntranceCarQueue(Car car) {
        entranceCarQueue.addCar(car);
    }

    /**
     * Adds car to the pass entrance queue
     * @param car Specified car
     */
    public void addEntrancePassQueue(Car car) {
        entrancePassQueue.addCar(car);
    }

    /**
     * Add car to the payment queue
     * @param car Specified car
     */
    public void addPaymentCarQueue(Car car) {
        paymentCarQueue.addCar(car);
    }

    /**
     * Adds car to the exit queue
     * @param car Specified car
     */
    public void addExitCarQueue(Car car) {
        exitCarQueue.addCar(car);
    }

    /**
     * Removes a car from the payment queue
     * @return Car in queue
     */
    public Car removePaymentCarQueue() {
        return paymentCarQueue.removeCar();
    }

    /**
     * Remove a car form the exit queue
     * @return Car in queue
     */
    public Car removeExitCarQueue() {
        return exitCarQueue.removeCar();
    }

    /**
     * Gets the total number of parking spots
     * @return The total parkingspots
     */
    public int getNumberOfTotalSpots() {
        return numberOfTotalSpots;
    }

    /**
     * Gets the total amount of customers
     * @return Total customers
     */
    public int getAmountOfCustomers() {
        return amountOfCustomers;
    }

    /**
     * Gets amount of customer loss
     * @return Customer loss
     */
    public int getAmountOfCustomerLoss() {
        return amountOfCustomerLoss;
    }

    /**
     * Gets number of floors
     * @return The floors
     */
    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    /**
     * Gets number of rows
     * @return The rows
     */
    public int getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * Gets number of places
     * @return The places
     */
    public int getNumberOfPlaces() {
        return numberOfPlaces;
    }

    /**
     * Gets the number of open spots
     * @return The open spots
     */
    public int getNumberOfOpenSpots() {
        return numberOfOpenSpots;
    }

    /**
     * Gets the entrace queue
     * @return The entrance queue
     */
    public CarQueue getEntranceCarQueue() {
        return entranceCarQueue;
    }

    /**
     * Gets the entrance pass queue
     * @return The entrace pass queue
     */
    public CarQueue getEntrancePassQueue() {
        return entrancePassQueue;
    }

    /**
     * Gets the payment queue
     * @return The payment queue
     */
    public CarQueue getPaymentCarQueue() {
        return paymentCarQueue;
    }

    /**
     * Gets the exit queue
     * @return The exit queue
     */
    public CarQueue getExitCarQueue() {
        return exitCarQueue;
    }

    /**
     * Gets the weekday arrivals
     * @return The weekday arrivals
     */
    public int getWeekDayArrivals() {
        return weekDayArrivals;
    }

    /**
     * Gets the weekend arrivals
     * @return The weekend arrivals
     */
    public int getWeekendArrivals() {
        return weekendArrivals;
    }

    /**
     * Get the weekday pass arrivals
     * @return The weekday pass arrivals
     */
    public int getWeekDayPassArrivals() {
        return weekDayPassArrivals;
    }

    /**
     * Get the weekend pass arrivals
     * @return The weekend pass arrivals
     */
    public int getWeekendPassArrivals() {
        return weekendPassArrivals;
    }

    /**
     * Get the weekday reserved arrivals
     * @return The weekday reserved arrivalss
     */
    public int getWeekDayReservedArrivals() {
        return weekDayReservedArrivals;
    }

    /**
     * Gets the weekend reserved arrivals
     * @return The weekend reserverd arrivals
     */
    public int getWeekendReservedArrivals() {
        return weekendReservedArrivals;
    }

    /**
     * Gets the number of reserved spots
     * @return Amount of reserved spots
     */
    public int getNumberOfReservedSpots() {
        return numberOfReservedSpots;
    }

    /**
     * Gets all the peak hour settings
     * @return Hashmap of all the peak data per week
     */
    public HashMap<DayOfTheWeek, int[]> getDaySettings() {
        return daySettings;
    }

    /**
     * Gets the peak data for a specific day
     * @param day Specified day
     * @return Array of data
     */
    public int[] getDaySetting(DayOfTheWeek day) {
        return daySettings.get(day);
    }

    /**
     * Sets the current peak data for a specified day
     * @param day Specified day
     * @param index The hour
     * @param value The new value
     */
    public void setDaySetting(DayOfTheWeek day, int index, int value) {
        daySettings.get(day)[index] = value;
    }

    /**
     * Resets all the peak data for all days
     */
    public void setDaySettingsDefault() {
        for (Map.Entry<DayOfTheWeek, int[]> day : daySettings.entrySet()) {
            for (int weight :  day.getValue()) {
                day.setValue(new int[24]);
            }
        }
    }

    /**
     *  Imports a csv file with peak data
     */
    public void importDaySettingsFile(String path) {
        ArrayList<String[]> data = new ArrayList<>();
        BufferedReader bufferedReader = null;
        String line = "";
        String delimiter = ";";

        try {
            bufferedReader = new BufferedReader(new FileReader(path));

            while ((line = bufferedReader.readLine()) != null) {
                String[] lineData = line.split(delimiter);
                data.add(lineData);
            }

            for (int i = 0; i < data.size(); i++)
                for (int j = 0; j < data.get(i).length - 1; j++) {
                    DayOfTheWeek day = DayOfTheWeek.Monday;

                    if (data.get(i)[0].equals(DayOfTheWeek.Tuesday.toString()))
                        day = DayOfTheWeek.Tuesday;
                    else if (data.get(i)[0].equals(DayOfTheWeek.Wednesday.toString()))
                        day = DayOfTheWeek.Wednesday;
                    else if (data.get(i)[0].equals(DayOfTheWeek.Thursday.toString()))
                        day = DayOfTheWeek.Thursday;
                    else if (data.get(i)[0].equals(DayOfTheWeek.Friday.toString()))
                        day = DayOfTheWeek.Friday;
                    else if (data.get(i)[0].equals(DayOfTheWeek.Saturday.toString()))
                        day = DayOfTheWeek.Saturday;
                    else if (data.get(i)[0].equals(DayOfTheWeek.Sunday.toString()))
                        day = DayOfTheWeek.Sunday;

                    try {
                        daySettings.get(day)[j] = Integer.parseInt(data.get(i)[j + 1]);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "File could not be loaded!", "Invalid file!", JOptionPane.ERROR_MESSAGE);
                        setDaySettingsDefault();
                        return;
                    }
                }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            if (bufferedReader != null)
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            JOptionPane.showMessageDialog(null, "File successfully imported!", "Success!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Gets the enter speed
     * @return Enter speed
     */
    public int getEnterSpeed() {
        return enterSpeed;
    }

    /**
     * Gets the payment speed
     * @return Payment speed
     */
    public int getPaymentSpeed() {
        return paymentSpeed;
    }

    /**
     * Gets the exit speed
     * @return Exit speed
     */
    public int getExitSpeed() {
        return exitSpeed;
    }

    /**
     * Gets the time
     * @return The time
     */
    public Time getTime() {
        return time;
    }

    /**
     * Gets the tick speed
     * @return The tick speed
     */
    public int getTickSpeed() {
        return tickSpeed;
    }

    /**
     * Gets the amount of ticks
     * @return Amount of ticks
     */
    public int getTicks() {
        return ticks;
    }

    /**
     * Resets all the spots in the parking garage
     */
    public void resetSpots() {
        this.numberOfTotalSpots = numberOfFloors * numberOfRows * numberOfPlaces;
        this.numberOfOpenSpots = this.numberOfTotalSpots;
        this.cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];
        this.parkingSpots = new ParkingSpot[numberOfFloors][numberOfRows][numberOfPlaces];
    }

    /**
     * Gets the amount of pass holders
     * @return Amount of pass holders
     */
    public int getAmountOfPassHolders() {
        return amountOfPassHolders;
    }

    /**
     * Sets the amount of pass holders
     * @param amountOfPassHolders Amount of pass holders
     */
    public void setAmountOfPassHolders(int amountOfPassHolders) {
        this.amountOfPassHolders = amountOfPassHolders;
    }

    /**
     * Set the amount of ticks
     * @param ticks Amount of ticks
     */
    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    /**
     * Checks if simulation is running
     * @return Simulation state
     */
    public boolean getRunning() {
        return running;
    }

    /**
     * Sets the state of the simulation
     * @param running The state
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Resets the complete simulation
     */
    public void reset() {
        this.time.reset();

        initializeFields();
    }

    /**
     * Resets the current time
     */
    public void resetTime() {
        this.time.reset();
    }

    /**
     * Sets the tick speed
     * @param tickSpeed Specified speed
     */
    public void setTickSpeed(int tickSpeed) {
        this.tickSpeed = tickSpeed;
    }

    /**
     * Gets the amount of parking pass reserved spots
     * @return The amount of reserved parking pass spots.
     */
    public int getParkingPassReservedSpots() {
        return numberOfReservedSpots;
    }

    /**
     * Gets the custom amount of ticks
     * @return The custom amount of ticks
     */
    public int getCustomAmountOfTicks() {
        return customAmountOfTicks;
    }

    /**
     * Gets the weekday percentage
     * @return The weekday percentage
     */
    public int getWeekDayPassPercentage() {
        return weekDayPassPercentage;
    }

    /**
     * Sets the weekday pass percentage
     * @param weekDayPassPercentage The percentage
     */
    public void setWeekDayPassPercentage(int weekDayPassPercentage) {
        this.weekDayPassPercentage = weekDayPassPercentage;
    }

    /**
     * Gets the weekend pass percentage
     * @return The weekend pass percentage
     */
    public int getWeekendPassPercentage() {
        return weekendPassPercentage;
    }

    /**
     * Sets the weekendpass percentage
     * @param weekendPassPercentage The percentage
     */
    public void setWeekendPassPercentage(int weekendPassPercentage) {
        this.weekendPassPercentage = weekendPassPercentage;
    }

    /**
     * sets a custom amount of ticks
     * @param customAmountOfTicks Specified amount of ticks
     */
    public void setCustomAmountOfTicks(int customAmountOfTicks) {
        this.customAmountOfTicks = customAmountOfTicks;
    }

    /**
     *  Sets the amount of reserved parking pass spots
     */
    public void setParkingPassReservedSpots() {
        int locationIndex = 0;

        for (int floor = 0; floor < numberOfFloors; floor++) {
            for (int row = 0; row < numberOfRows; row++) {
                for (int place = 0; place < numberOfPlaces; place++) {
                    if (parkingSpots[floor][row][place] == ParkingSpot.PASS)
                        parkingSpots[floor][row][place] = ParkingSpot.AD_HOC;
                }
            }
        }

        if (numberOfReservedSpots <= 0)
            return;

        for (int floor = 0; floor < numberOfFloors; floor++) {
            for (int row = 0; row < numberOfRows; row++) {
                for (int place = 0; place < numberOfPlaces; place++) {
                    parkingSpots[floor][row][place] = ParkingSpot.PASS;
                    locationIndex++;
                    if (locationIndex == numberOfReservedSpots)
                        return;
                }
            }
        }

    }

    /**
     * Sets the amount of parking pass spots in the garage
     * @param numberOfReservedSpots Amount of spots
     */
    public void setParkingPassReservedSpots(int numberOfReservedSpots) {
        this.numberOfReservedSpots = numberOfReservedSpots;
        setParkingPassReservedSpots();
    }

    /**
     * Sets the amount of cars during the week
     * @param weekDayArrivals Amount of cars
     */
    public void setWeekDayArrivals(int weekDayArrivals) {
        this.weekDayArrivals = weekDayArrivals;
    }

    /**
     * Sets the amount of cars during the weekend
     * @param weekendArrivals Amount of cars
     */
    public void setWeekendArrivals(int weekendArrivals) {
        this.weekendArrivals = weekendArrivals;
    }

    /**
     * Sets the amount of parking pass cars during the week
     * @param weekDayPassArrivals Amount of cars
     */
    public void setWeekDayPassArrivals(int weekDayPassArrivals) {
        this.weekDayPassArrivals = weekDayPassArrivals;
    }

    /**
     * Sets the amount of parking pass cars during the weekend
     * @param weekendPassArrivals Amount of cars
     */
    public void setWeekendPassArrivals(int weekendPassArrivals) {
        this.weekendPassArrivals = weekendPassArrivals;
    }

    /**
     * Sets the amount of reserved cars during the week
     * @param weekDayReservedArrivals Amountof cars
     */
    public void setWeekDayReservedArrivals(int weekDayReservedArrivals) {
        this.weekDayReservedArrivals = weekDayReservedArrivals;
    }

    /**
     * Sets the amount of reserved cars arriving in the weekend
     * @param weekendReservedArrivals Amount of cars
     */
    public void setWeekendReservedArrivals(int weekendReservedArrivals) {
        this.weekendReservedArrivals = weekendReservedArrivals;
    }

    /**
     * Sets the enter speed
     * @param enterSpeed Specified enter speed
     */
    public void setEnterSpeed(int enterSpeed) {
        this.enterSpeed = enterSpeed;
    }

    /**
     * Sets the payment speed
     * @param paymentSpeed Specified payment speed
     */
    public void setPaymentSpeed(int paymentSpeed) {
        this.paymentSpeed = paymentSpeed;
    }

    /**
     * Sets the exit speed
     * @param exitSpeed Specified speed
     */
    public void setExitSpeed(int exitSpeed) {
        this.exitSpeed = exitSpeed;
    }

    /**
     * Sets the numbers of floors in the parking garage
     * @param numberOfFloors Amount of floors
     */
    public void setNumberOfFloors(int numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
        this.cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];
    }

    /**
     * Sets the number of rows in the parking garage
     * @param numberOfRows Amount of rows
     */
    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
        this.cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];
    }

    /**
     * Sets the number of places in the parking garage
     * @param numberOfPlaces Amount of places
     */
    public void setNumberOfPlaces(int numberOfPlaces) {
        this.numberOfPlaces = numberOfPlaces;
        this.cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];
    }

    /**
     * Sets the type of a parking spot
     * @param location Preferred location
     * @param parkingSpot New parking spot type
     */
    public void setParkingSpotType(Location location, ParkingSpot parkingSpot) {
        parkingSpots[location.getFloor()][location.getRow()][location.getPlace()] = parkingSpot;
    }

    /**
     * Gets the current type of the specified parking spot
     * @param location Location that has to be checked
     * @return The parking spot at specified location
     */
    public ParkingSpot getParkingSpotType(Location location) {
        return parkingSpots[location.getFloor()][location.getRow()][location.getPlace()];
    }

    /**
     * Gets the current color of a parking spot
     * @param floor The floor
     * @param row The row
     * @param place The place
     * @return The color of the parking spot
     */
    public Color getParkingSpotColor(int floor, int row, int place) {
        return (parkingSpots[floor][row][place] == null) ? Color.WHITE : parkingSpots[floor][row][place].getColor();
    }

    /**
     * Changes a parking spot to a reserved parking spot
     * @param location Location that has to be set
     */
    public void setReservedParkingSpot(Location location) {

        parkingSpots[location.getFloor()][location.getRow()][location.getPlace()] = ParkingSpot.RESERVED;
    }

    /**
     * Sets the current time
     * @param time Value at which the current time has to be set
     */
    public void setTime(Time time) {
        this.time = time;
    }
}
