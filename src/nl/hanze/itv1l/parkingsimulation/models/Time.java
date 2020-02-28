package nl.hanze.itv1l.parkingsimulation.models;

/**
 * This class controls the time of the simulation
 * @author Shaquille Louisa
 * @author Robin van Wijk
 *
 * @since 1.0
 */
public class Time {
    private int startDay = 0;
    private int startHour = 6;
    private static TimeStamp currentTimeStamp;

    /**
     * The constructor initializes the current timestamp
     */
    public Time() {
        currentTimeStamp = new TimeStamp(0,0,startDay,startHour,0);
    }

    /**
     * This method wil increment the time by a minute
     */
    public void addOneMinute(){
        currentTimeStamp.updateTimeStamp(0,0,0,0,1);
    }

    /**
     * This mehtod overrides the toString of Object and creates a new formatted string
     * @return A formatted string that represents this object
     */
    @Override
    public String toString() {
        return "Month: " + (currentTimeStamp.getMonth() + 1) + ", " + "Week: " + (currentTimeStamp.getWeek() + 1) + ", " + dayDigitToString() + " " + String.format("%02d", currentTimeStamp.getHour()) + ":" + String.format("%02d", currentTimeStamp.getMinute());
    }

    /**
     * Converts the day to a string
     * @return The current day in string
     */
    private String dayDigitToString() {
        switch (currentTimeStamp.getDay()) {
            case 0:
                return "Monday";

            case 1:
                return "Tuesday";

            case 2:
                return "Wednesday";

            case 3:
                return "Thursday";

            case 4:
                return "Friday";

            case 5:
                return "Saturday";

            case 6:
            default:
                return "Sunday";
        }
    }

    // The enum that represents a day of the week
    public enum DayOfTheWeek {
        Monday(0),
        Tuesday(1),
        Wednesday(2),
        Thursday(3),
        Friday(4),
        Saturday(5),
        Sunday(6);

        private int value;

        DayOfTheWeek(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * @return The current timestamps day
     */
    public int getDay() {
        return currentTimeStamp.getDay();
    }

    /**
     * @return The current timestamps hour
     */
    public int getHour() {
        return currentTimeStamp.getHour();
    }

    /**
     * @return The current timestamps month
     */
    public int getMonth() {
        return currentTimeStamp.getMonth();
    }

    /**
     * Static method that returns the current timestamp
     * @return The current timestamp
     */
    public static TimeStamp getCurrentTimeStamp() {
        return currentTimeStamp;
    }

    /**
     * Resets the current timestamp
     */
    public void reset() {
        currentTimeStamp = new TimeStamp(0,0,startDay,startHour,0);
    }

    /**
     * Sets the starting day
     * @param day The day which will be the starting day
     */
    public void setStartingDay(int day) {
        this.startDay = day;
    }

    /**
     * Sets the starting hour
     * @param hour The hour which will be the starting hour
     */
    public void setStartingHour(int hour) {
        this.startHour = hour;
    }
}
