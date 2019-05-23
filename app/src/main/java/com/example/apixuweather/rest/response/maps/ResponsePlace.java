package com.example.apixuweather.rest.response.maps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponsePlace extends ResponseBaseMap {

    @SerializedName("predictions")
    @Expose
    private List<Prediction> predictions = null;

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }

}
