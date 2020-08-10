package org.mumineen;

public class MdoSalaat{
    //load library
    static {
        System.loadLibrary("MDOSalaat");
    }

    /*
     *
     * getAllTimes - Returns all times in Unix timestamp format, unrounded
     * @param year - The year
     * @param month - The month
     * @param day - The day
     * @param latitude - Latitude
     * @param longitude - Longitude
     * @param altitude - Altitude for adjustment estimate, in meters (note - this assumes that both observer and horizon are at this altitude)
     * @return long[] All times in Unix timestamp (long to avoid Y2038 problem), in the following order.
     *          [0] = Sihori end
     *          [1] = Fajr start
     *          [2] = Sunrise
     *          [3] = Zawaal
     *          [4] = Zohr End
     *          [5] = Asr End
     *          [6] = Maghrib
     *          [7] = Maghrib End (10m past maghrib)
     *          [8] = Nisful Layl Begin (and Isha End)
     *          [9] = Nisful Layl End
     */
    public static native long[] getAllTimes(int year, int month, int day, double latitude, double longitude, double altitude);

    /*
     *
     * getAllTimes - Returns all times in Unix timestamp format, rounded to ensure safe timings (e.g. Fajr end will round down, while maghrib will round up)
     * @param year - The year
     * @param month - The month
     * @param day - The day
     * @param latitude - Latitude
     * @param longitude - Longitude
     * @param altitude - Altitude for adjustment estimate, in meters (note - this assumes that both observer and horizon are at this altitude)
     * @return long[] All times in Unix timestamp (long to avoid Y2038 problem), in the following order.
     *          [0] = Sihori end
     *          [1] = Fajr start
     *          [2] = Sunrise
     *          [3] = Zawaal
     *          [4] = Zohr End
     *          [5] = Asr End
     *          [6] = Maghrib
     *          [7] = Maghrib End (10m past maghrib)
     *          [8] = Nisful Layl Begin (and Isha End)
     *          [9] = Nisful Layl End
     */
    public static native long[] getAllTimesRounded(int year, int month, int day, double latitude, double longitude, double altitude);
}