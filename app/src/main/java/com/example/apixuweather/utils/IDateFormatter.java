package com.example.apixuweather.utils;

import com.example.apixuweather.R;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Locale;

import androidx.annotation.StringRes;


public class IDateFormatter {

    private static final String TIME_24 = "HH:mm";
    private static final String TIME_12 = "hh:mm a";
    private static final String DAY_OF_WEEK = "EEEE";
    private static final String DAY_AND_MONTH = "dd MMMM";
    private static final String DATE_TIME = "dd/MM/yy HH:mm";


    private static DateTimeFormatter sTime24;
    private static DateTimeFormatter sTime12;
    private static DateTimeFormatter sDayOfWeek;
    private static DateTimeFormatter sDayAndMonth;
    private static DateTimeFormatter sDateTime;

    static {
        sTime24 = DateTimeFormatter.ofPattern(TIME_24);
        sTime12 = DateTimeFormatter.ofPattern(TIME_12, Locale.US);
        sDateTime = DateTimeFormatter.ofPattern(DATE_TIME);
        sDayAndMonth = DateTimeFormatter.ofPattern(DAY_AND_MONTH);
        sDayOfWeek = DateTimeFormatter.ofPattern(DAY_OF_WEEK);
    }

    @Contract(pure = true)
    public static DateTimeFormatter time24() {
        return sTime24;
    }

    @Contract(pure = true)
    public static DateTimeFormatter time12() {
        return sTime12;
    }

    @Contract(pure = true)
    public static DateTimeFormatter dayOfWeek() {
        return sDayOfWeek;
    }

    @Contract(pure = true)
    public static DateTimeFormatter dayAndMonth() {
        return sDayAndMonth;
    }

    @Contract(pure = true)
    public static DateTimeFormatter dateTime() {
        return sDateTime;
    }

    @Contract(pure = true)
    public static DateTimeFormatter getTime12() {
        return sTime12;
    }

    @NotNull
    public static Long[] getPartsOfDay(long date, int offset) {
        OffsetDateTime dateTime = Instant.ofEpochSecond(date).atOffset(ZoneOffset.ofTotalSeconds(offset));
        return new Long[]{
                dateTime.withHour(7).toEpochSecond(),
                dateTime.withHour(14).toEpochSecond(),
                dateTime.withHour(20).toEpochSecond(),
                dateTime.plusDays(1).withHour(3).toEpochSecond()
        };
    }

    @Contract(pure = true)
    @StringRes
    public static int getPartOfDay(@NotNull OffsetDateTime date) {
        switch (date.getHour()) {
            case 7:
                return R.string.morning;
            case 14:
                return R.string.afternoon;
            case 20:
                return R.string.evening;
            case 3:
                return R.string.night;
        }
        return -1;
    }
}
