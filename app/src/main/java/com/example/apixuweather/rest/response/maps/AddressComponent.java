package com.example.apixuweather.rest.response.maps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressComponent {

    @SerializedName("long_name")
    @Expose
    private String longName;
    @SerializedName("short_name")
    @Expose
    private String shortName;
    @SerializedName("types")
    @Expose
    private List<String> types = null;

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public int getPriority(){
        for (String string : types){
            switch (string){
                case "administrative_area_level_1":    // Область
                    return 0;
                case "sublocality_level_1":            // Административный округ
                    return 1;
                case "sublocality_level_2":            // Район
                    return 2;
            }
        }
        return -1;
    }
}
