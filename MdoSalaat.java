package org.mumineen;

import java.util.Arrays;

public class MdoSalaat{
    //load library
    static {
        System.loadLibrary("MDOSalaat");
    }

    public static native long[] getAllTimes(int year, int month, int day, double latitude, double longitude, double altitude);

    public static native long[] getAllTimesRounded(int year, int month, int day, double latitude, double longitude, double altitude);

    public static void main(String[] args){
        long[] times = MdoSalaat.getAllTimes(2020, 8, 9, 42.5112, -83.3466, 0);

        System.out.println(Arrays.toString(times));
    }
}