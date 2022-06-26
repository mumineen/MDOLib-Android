# MDOLib-Android

This repo contains a precompiled implementation of the MDO Salaat algorithm.  The algorithm takes the date, latitude, longitude, and altitude as parameters, and returns all times as Unix timestamps (seconds since 1/1/1970 00:00 UTC).

If a time cannot be calculated on the given day at the given location, it is returned as -1.

Includes shared object files for 4 architectures: arm64, arm, x86, x86_64. 

The java source (declaring the methods that have been implemented in the shared object files) is included in MdoSalaat.java.  The java source and shared object files can be used to create the module in Android Studio.  Note that the package name "org.mumineen.MdoSalaat" must not be changed as the shared objects are compiled assuming that to be the package name.

A prebuilt AAR file has also been provided.  This can be added to an existing project, and then the library can be imported using `org.mumineen.MdoSalaat`


The Java source defines two methods which are implemented in the shared object files.  These methods are listed below:
```
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
```
