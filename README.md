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
    
     /*
    *
    * roundedSalaatDictForDate - returns a dictionary of the salaat times for the day
    * @param dateIn - The current date as a LocalDate object
    * @param latitude - Latitude
    * @param longitude - Longitude
    * @param altitude - Altitude
    * @return Map<String, Instant> - A map of the times in GMT as LocalDateTime objects
    * Keys:
    *   sihori, fajr, sunrise, zawaal, zohr_end, asr_end, maghrib, maghrib_end, nisful_layl, nisful_layl_end
     */
    public static Map<String, Instant> roundedSalaatDictForDate(LocalDate dateIn, double latitude, double longitude, double altitude);
    
    /*
     *
     * roundedSalaatDictForDateWithExtraTimes - returns a dictionary of the salaat times for the day, with the option to add
     *      the previous day's nisful layl and nisful layl end times, and/or the next day's sihori
     * @param dateIn - The current date as a LocalDate object
     * @param latitude - Latitude
     * @param longitude - Longitude
     * @param altitude - Altitude
     * @param withPreviousNL - whether to include the previous day's nisful layl and nisful layl end
     * @param withNextSihori - whether to include the next day's sihori
     * @return Map<String, Instant> - A map of the times in GMT as Instant objects
     * Keys:
     *   sihori, fajr, sunrise, zawaal, zohr_end, asr_end, maghrib, maghrib_end, nisful_layl, nisful_layl_end
     *   Optional: next_sihori, prev_nisful_layl, prev_nisful_layl_end
     */
      public static Map<String, Instant> roundedSalaatDictForDateWithExtraTimes(LocalDate dateIn, double latitude, double longitude, double altitude, Boolean withPreviousNL, Boolean withNextSihori);
      
      /*
     * getNextTime - Returns the earliest upcoming time for a given date and time.
     * @param currTimeIn - Object containing the current time
     * @param latitude - Latitude
     * @param longitude - Longitude
     * @param altitude - Altitude
     * @return Map<String, Instant> - A dictionary containing a single key-value pair of the name and time that
     * is the earliest upcoming.  Will return null if there are no upcoming times in the day
    */
    public static Map<String, Instant> getNextTime(Instant currTimeIn, double latitude, double longitude, double altitude);
```
