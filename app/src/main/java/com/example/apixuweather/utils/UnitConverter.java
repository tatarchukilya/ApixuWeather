package com.example.apixuweather.utils;


import com.example.apixuweather.R;
import com.example.apixuweather.repo.ISharePreference;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.threeten.bp.LocalTime;

import androidx.annotation.StringRes;

import static com.example.apixuweather.utils.Const.DEG;

public class UnitConverter {
    private UnitConverter() {
    }

    @Nullable
    public static String getWindSpeed(Double Mph, Double Kph) {
        switch (ISharePreference.getSpeedUnit()) {
            case Mph:
                return Mph == null ? null : INumberFormatter.UNSIGNED.get().format(Mph);
            case Kph:
                return Kph == null ? null : INumberFormatter.UNSIGNED.get().format(Kph);
            case Ms:
                return kmhToMs(Kph);
        }
        return null;
    }

    @Nullable
    public static String getPressure(Double inHgm, Double hPa) {
        switch (ISharePreference.getPressureUnit()) {
            case inHg:
                return inHgm == null ? null : INumberFormatter.UNSIGNED.get().format(inHgm);
            case hPa:
                return hPa == null ? null : INumberFormatter.UNSIGNED.get().format(hPa);
            case mmHg:
                return hPaToMmHg(hPa);
        }
        return null;
    }

    @Contract("null -> null")
    public static String getTemp(Double temp) {
        if (temp == null) return null;
        return Math.round(temp) == 0 ? "0" : INumberFormatter.SIGNED.get().format(temp) ;
    }

    @Contract("null -> null")
    private static String kmhToMs(Double kph) {
        return kph == null ? null : INumberFormatter.UNSIGNED.get().format(kph * 0.2777778);
    }

    @Contract("null -> null")
    private static String hPaToMmHg(Double hPa) {
        return hPa == null ? null : INumberFormatter.UNSIGNED.get().format(hPa * 0.750062);
    }

    @Contract("null -> null")
    @StringRes
    public static Integer degToDir(Integer deg) {
        if (deg == null) return null;
        int direction = (int) Math.round(((double) deg % 360) / 45) % 8;
        switch (direction) {
            case 0:
                return R.string.north;
            case 1:
                return R.string.northeast;
            case 2:
                return R.string.east;
            case 3:
                return R.string.southeast;
            case 4:
                return R.string.south;
            case 5:
                return R.string.southwest;
            case 6:
                return R.string.west;
            case 7:
                return R.string.northwest;
            default:
                return -1;
        }
    }

    @Contract(pure = true)
    public static PartOfDay getPartOfDay(@NotNull LocalTime time) {
        PartOfDay partOfDay = null;
        int hour = time.getHour();
        if (hour >= 0 && hour < 6) partOfDay = PartOfDay.Morning;

        return partOfDay;
    }

    @NotNull
    public static String getTempIconName(Double temp){
        int res = (int) Math.round(temp);
        return res < 0 ? "minus_" + Math.abs(res) : "_" + res;
    }
}
