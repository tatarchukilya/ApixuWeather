
package com.example.apixuweather.rest.response.apixu;

import com.example.apixuweather.repo.ISharePreference;
import com.example.apixuweather.utils.UnitConverter;
import com.example.apixuweather.utils.unit.TempUnit;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.room.Embedded;

import static com.example.apixuweather.utils.Const.DEG;

public class Day {

    @SerializedName("maxtemp_c")
    @Expose
    private Double maxtempC;
    @SerializedName("maxtemp_f")
    @Expose
    private Double maxtempF;
    @SerializedName("mintemp_c")
    @Expose
    private Double mintempC;
    @SerializedName("mintemp_f")
    @Expose
    private Double mintempF;
    @SerializedName("avgtemp_c")
    @Expose
    private Double avgtempC;
    @SerializedName("avgtemp_f")
    @Expose
    private Double avgtempF;
    @SerializedName("maxwind_mph")
    @Expose
    private Double maxwindMph;
    @SerializedName("maxwind_kph")
    @Expose
    private Double maxwindKph;
    @SerializedName("totalprecip_mm")
    @Expose
    private Double totalprecipMm;
    @SerializedName("totalprecip_in")
    @Expose
    private Double totalprecipIn;
    @SerializedName("avgvis_km")
    @Expose
    private Double avgvisKm;
    @SerializedName("avgvis_miles")
    @Expose
    private Double avgvisMiles;
    @SerializedName("avghumidity")
    @Expose
    private Double avghumidity;
    @SerializedName("condition")
    @Expose
    @Embedded
    private Condition condition;
    @SerializedName("uv")
    @Expose
    private Double uv;

    public Double getMaxtempC() {
        return maxtempC;
    }

    public void setMaxtempC(Double maxtempC) {
        this.maxtempC = maxtempC;
    }

    public Double getMaxtempF() {
        return maxtempF;
    }

    public void setMaxtempF(Double maxtempF) {
        this.maxtempF = maxtempF;
    }

    public Double getMintempC() {
        return mintempC;
    }

    public void setMintempC(Double mintempC) {
        this.mintempC = mintempC;
    }

    public Double getMintempF() {
        return mintempF;
    }

    public void setMintempF(Double mintempF) {
        this.mintempF = mintempF;
    }

    public Double getAvgtempC() {
        return avgtempC;
    }

    public void setAvgtempC(Double avgtempC) {
        this.avgtempC = avgtempC;
    }

    public Double getAvgtempF() {
        return avgtempF;
    }

    public void setAvgtempF(Double avgtempF) {
        this.avgtempF = avgtempF;
    }

    public Double getMaxwindMph() {
        return maxwindMph;
    }

    public void setMaxwindMph(Double maxwindMph) {
        this.maxwindMph = maxwindMph;
    }

    public Double getMaxwindKph() {
        return maxwindKph;
    }

    public void setMaxwindKph(Double maxwindKph) {
        this.maxwindKph = maxwindKph;
    }

    public Double getTotalprecipMm() {
        return totalprecipMm;
    }

    public void setTotalprecipMm(Double totalprecipMm) {
        this.totalprecipMm = totalprecipMm;
    }

    public Double getTotalprecipIn() {
        return totalprecipIn;
    }

    public void setTotalprecipIn(Double totalprecipIn) {
        this.totalprecipIn = totalprecipIn;
    }

    public Double getAvgvisKm() {
        return avgvisKm;
    }

    public void setAvgvisKm(Double avgvisKm) {
        this.avgvisKm = avgvisKm;
    }

    public Double getAvgvisMiles() {
        return avgvisMiles;
    }

    public void setAvgvisMiles(Double avgvisMiles) {
        this.avgvisMiles = avgvisMiles;
    }

    public Double getAvghumidity() {
        return avghumidity;
    }

    public void setAvghumidity(Double avghumidity) {
        this.avghumidity = avghumidity;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Double getUv() {
        return uv;
    }

    public void setUv(Double uv) {
        this.uv = uv;
    }

    public String getIconName(boolean isAnim){
        String code = "w_" + String.valueOf(condition.getCode());
        String isDay = "_day_" ;
        String type = isAnim ? "anim" : "static";
        return code + isDay + type;
    }

    public String getMaxTemp() {
        return UnitConverter.getTemp(ISharePreference.getTempUnit() == TempUnit.Fahrenheit ? maxtempF : maxtempC) + DEG;
    }

    public String getMinTemp(){
        return UnitConverter.getTemp(ISharePreference.getTempUnit() == TempUnit.Fahrenheit ? mintempF : mintempC) + DEG;
    }

    public String getWind(){
        return UnitConverter.getWindSpeed(maxwindMph, maxwindKph);
    }
}
