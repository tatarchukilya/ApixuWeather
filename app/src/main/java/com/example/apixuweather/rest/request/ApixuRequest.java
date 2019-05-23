package com.example.apixuweather.rest.request;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;

public class ApixuRequest extends BaseRequest {

    private final static String sKey = "570631cf9b0c4393bce74848182112";
    private final static String TempKey = "570631cf9b0c4393bce7484818211";
    private final static String sCount = "11";
    private final String mLatLng;
    private final String mLang;

    public ApixuRequest(double lat, double lng) {
        mLatLng = latLngToString(lat, lng);
        mLang = Locale.getDefault().getLanguage().equals("ru") ? "ru" : "en"; }

    @Override
    public void onMapCreate(Map<String, String> map) {
        map.put("q", mLatLng);
        map.put("days", sCount);
        map.put("lang", mLang);
        map.put("key", sKey);
    }

    @NotNull
    @Contract(pure = true)
    private String latLngToString(Double lat, double lng){
        return lat + "," + lng;
    }
}
