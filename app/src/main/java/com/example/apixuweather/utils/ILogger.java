package com.example.apixuweather.utils;

import android.util.Log;

import com.example.apixuweather.rest.response.apixu.ForecastResponse;

import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.List;

public class ILogger {

    public static void LogTimestamp(String tag, List<ForecastResponse> responses) {
        for (ForecastResponse response : responses) {
            LogTimestamp(tag, response);
        }
    }

    public static void LogTimestamp(String tag, ForecastResponse response){
        OffsetDateTime dateTime = Instant.ofEpochSecond(response.getCurrent().getLastUpdatedEpoch()).atOffset(ZoneOffset.UTC);
        Log.i(tag, response.getLocation().getName() + " " + response.getLocation().getRegion() + " updated: " + dateTime.format(IDateFormatter.dateTime()));
    }
}

