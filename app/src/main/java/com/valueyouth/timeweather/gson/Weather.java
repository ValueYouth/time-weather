package com.valueyouth.timeweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cwang6 on 2017/3/13.
 */

public class Weather {

    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecasts;


}
