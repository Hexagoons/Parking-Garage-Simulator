package nl.hanze.itv1l.parkingsimulation.models;

/**
 * This class represents a moment in time
 *
 * @author Shaquille Louisa
 * @author Robin van Wijk
 * @since 1.0
 */
public class TimeStamp {
    private int month;
    private int week;
    private int day;
    private int hour;
    private int minute;

    /**
     * Sets the initial values
     *
     * @param month  The initial month
     * @param week   The initial week
     * @param day    The initial day
     * @param hour   The initial hour
     * @param minute The initial minute
     */
    public TimeStamp(int month, int week, int day, int hour, int minute) {
        updateTimeStamp(month, week, day, hour, minute);
    }

    /**
     * Updates the month, week, day, hour and minute
     *
     * @param month  The update value of month
     * @param week   The update value of week
     * @param day    The update value of day
     * @param hour   The update value of hour
     * @param minute The update value of minute
     */
    public void updateTimeStamp(int month, int week, int day, int hour, int minute) {
        //Add the values to the variables
        this.month += month;
        this.week += week;
        this.day += day;
        this.hour += hour;
        this.minute += minute;

        //Update the all the variable based on conditions
        if (this.minute > 59) {
            this.minute -= 60;
            this.hour++;
        }
        if (this.hour > 23) {
            this.hour -= 24;
            this.day++;
        }
        if (this.day > 6) {
            this.day -= 7;
            this.week++;
        }
        if ((this.week + 1) % 2 == 0 && (this.week +1) / 2 != this.month) {
            this.month++;
        }
    }

    /**
     * @return The month
     */
    public int getMonth() {
        return month;
    }

    /**
     * @return The week
     */
    public int getWeek() {
        return week;
    }

    /**
     * @return The day
     */
    public int getDay() {
        return day;
    }

    /**
     * @return The hour
     */
    public int getHour() {
        return hour;
    }

    /**
     * @return The minute
     */
    public int getMinute() {
        return minute;
    }

    /**
     * This method overrides the default equals method
     * @param timeStamp The object we compare too
     * @return Boolean that represents if we are equal of not
     */
    @Override
    public boolean equals(Object timeStamp) {
        if (timeStamp == this) {
            return true;
        }
        if (!(timeStamp instanceof TimeStamp)) {
            return false;
        }
        TimeStamp time = (TimeStamp) timeStamp;
        return this.month == time.month && this.week == time.week && this.day == time.day && this.hour == time.hour;
    }

    /**
     * This method override the toString of Object
     * @return A formatted string representation of this object
     */
    @Override
    public String toString() {
        return month + "," + week + "," + day + "," + hour + "," + minute;
    }
}
