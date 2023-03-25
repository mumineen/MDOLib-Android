package org.mumineen;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;

public class MdoSalaat{
    //load library
    static {
        System.loadLibrary("MDOLib");
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


    /*
    *
    * roundedSalaatDictForDate - returns a dictionary of the salaat times for the day
    * @param year - The year
    * @param month - The month
    * @param day - The day
    * @param latitude - Latitude
    * @param longitude - Longitude
    * @param altitude - Altitude
    * @return Map<String, Long> - A map of the times in GMT as wrapped Long objects
    * Keys:
    *   sihori, fajr, sunrise, zawaal, zohr_end, asr_end, maghrib, maghrib_end, nisful_layl, nisful_layl_end
     */
    public static Map<String, Long> roundedSalaatDictForDate(int year, int month, int day, double latitude, double longitude, double altitude){
//        int year = dateIn.getYear();
//        int month = dateIn.getMonthValue();
//        int day = dateIn.getDayOfMonth();

        long timeArray[] = MdoSalaat.getAllTimesRounded(year, month, day, latitude, longitude, altitude);

        Map<String, Long> retDict = new HashMap<String, Long>();

        retDict.put("sihori",           timeArray[0] == -1 ? null : new Long(timeArray[0]));
        retDict.put("fajr",             timeArray[1] == -1 ? null : new Long(timeArray[1]));
        retDict.put("sunrise",          timeArray[2] == -1 ? null : new Long(timeArray[2]));
        retDict.put("zawaal",           timeArray[3] == -1 ? null : new Long(timeArray[3]));
        retDict.put("zohr_end",         timeArray[4] == -1 ? null : new Long(timeArray[4]));
        retDict.put("asr_end",          timeArray[5] == -1 ? null : new Long(timeArray[5]));
        retDict.put("maghrib",          timeArray[6] == -1 ? null : new Long(timeArray[6]));
        retDict.put("maghrib_end",      timeArray[7] == -1 ? null : new Long(timeArray[7]));
        retDict.put("nisful_layl",      timeArray[8] == -1 ? null : new Long(timeArray[8]));
        retDict.put("nisful_layl_end",  timeArray[9] == -1 ? null : new Long(timeArray[9]));

        return retDict;
    }

    /*
     *
     * roundedSalaatDictForDateWithExtraTimes - returns a dictionary of the salaat times for the day, with the option to add
     *      the previous day's nisful layl and nisful layl end times, and/or the next day's sihori
     * @param year - The year
     * @param month - The month
     * @param day - The day
     * @param latitude - Latitude
     * @param longitude - Longitude
     * @param altitude - Altitude
     * @param withPreviousNL - whether to include the previous day's nisful layl and nisful layl end
     * @param withNextSihori - whether to include the next day's sihori
     * @return Map<String, Long> - A map of the times in GMT as wrapped Long objects
     * Keys:
     *   sihori, fajr, sunrise, zawaal, zohr_end, asr_end, maghrib, maghrib_end, nisful_layl, nisful_layl_end
     *   Optional: next_sihori, prev_nisful_layl, prev_nisful_layl_end
     */
    public static Map<String, Long> roundedSalaatDictForDateWithExtraTimes(int year, int month, int day, double latitude, double longitude, double altitude, Boolean withPreviousNL, Boolean withNextSihori){
        Map<String, Long> currTimes = MdoSalaat.roundedSalaatDictForDate(year, month, day, latitude, longitude, altitude);


        if (withPreviousNL){
            Calendar cal = Calendar.getInstance();
            cal.set(year, month - 1, day);
            cal.add(Calendar.DATE, -1);
            Map<String, Long> prevTimes = MdoSalaat.roundedSalaatDictForDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE) , latitude, longitude, altitude);
            currTimes.put("prev_nisful_layl", prevTimes.get("nisful_layl"));
            currTimes.put("prev_nisful_layl_end", prevTimes.get("nisful_layl_end"));

        }

        if (withNextSihori){
            Calendar cal = Calendar.getInstance();
            cal.set(year, month - 1, day);
            cal.add(Calendar.DATE, 1);
            Map<String, Long> nextTimes = MdoSalaat.roundedSalaatDictForDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE) , latitude, longitude, altitude);
            currTimes.put("next_sihori", nextTimes.get("sihori"));
        }

        return currTimes;

    }

    /*
     * getNextTime - Returns the earliest upcoming time for a given date and time.
     * @param currTimeIn - Long containing the current time as Unix epoch seconds
     * @param latitude - Latitude
     * @param longitude - Longitude
     * @param altitude - Altitude
     * @return Map<String, Long> - A dictionary containing a single key-value pair of the name and time that
     * is the earliest upcoming.  Will return null if there are no upcoming times in the day
    */
    public static Map<String, Long> getNextTime(Long currTimeIn, double latitude, double longitude, double altitude){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(currTimeIn * 1000L);

        Map<String, Long> extraTimesDict = MdoSalaat.roundedSalaatDictForDateWithExtraTimes(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DATE), latitude, longitude, altitude, true, true);

        ArrayList<Long> timesArray = new ArrayList<Long>();
        ArrayList<String> namesArray = new ArrayList<String>();

        timesArray.add(extraTimesDict.get("prev_nisful_layl"));
        timesArray.add(extraTimesDict.get("prev_nisful_layl_end"));
        timesArray.add(extraTimesDict.get("sihori"));
        timesArray.add(extraTimesDict.get("sunrise"));
        timesArray.add(extraTimesDict.get("zawaal"));
        timesArray.add(extraTimesDict.get("zohr_end"));
        timesArray.add(extraTimesDict.get("asr_end"));
        timesArray.add(extraTimesDict.get("maghrib"));
        timesArray.add(extraTimesDict.get("nisful_layl"));
        timesArray.add(extraTimesDict.get("nisful_layl_end"));
        timesArray.add(extraTimesDict.get("next_sihori"));

        namesArray.add("prev_nisful_layl");
        namesArray.add("prev_nisful_layl_end");
        namesArray.add("sihori");
        namesArray.add("sunrise");
        namesArray.add("zawaal");
        namesArray.add("zohr_end");
        namesArray.add("asr_end");
        namesArray.add("maghrib");
        namesArray.add("nisful_layl");
        namesArray.add("nisful_layl_end");
        namesArray.add("next_sihori");

        for (int lcv = 0; lcv < 11; lcv++){
            if (timesArray.get(lcv) != null) {
                if (timesArray.get(lcv) > currTimeIn) {
                    Map<String, Long> retDict = new HashMap<String, Long>();
                    retDict.put(namesArray.get(lcv), timesArray.get(lcv));
                    return retDict;
                }
            }
        }

        return null;
    }
}
