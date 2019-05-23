package com.example.apixuweather.rest.request.maps;

import java.util.Map;

public class DetailsRequest extends MapsRequest {

    private final String mFields = "geometry,utc_offset";
    private final String mPlaceId;

    public DetailsRequest(String placeId) {
        mPlaceId = placeId;
    }

    @Override
    public void onMapCreate(Map<String, String> map) {
        super.onMapCreate(map);
        map.put("fields", mFields);
        map.put("placeid", mPlaceId);
    }
}
