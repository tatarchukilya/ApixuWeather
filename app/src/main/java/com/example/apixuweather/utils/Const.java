package com.example.apixuweather.utils;

import java.util.concurrent.TimeUnit;

public class Const {

    public static final String I_TAG = "<><>";

    public static final String CURRENT_LOCATION_ID = "current_location";

    public static final String DEG = "\u00B0";

    public static final String UNKNOWN_TIME = "-:--";

    public final static long FRESH_PERIOD = TimeUnit.MINUTES.toSeconds(10);

    public final static long RELEVANCE_PERIOD = TimeUnit.MINUTES.toSeconds(30);

    public final static long VALIDITY_PERIOD = TimeUnit.HOURS.toSeconds(12);
}
