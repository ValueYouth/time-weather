package com.valueyouth.timeweather.gson;

/**
 * AQI: Air Quality Index
 * Created by cwang6 on 2017/3/13.
 */

public class AQI {

    public AQICity city;

    public class AQICity {

        public String aqi;

        public String pm25;
    }

}
