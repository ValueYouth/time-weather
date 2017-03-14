package com.valueyouth.timeweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * create the class Basic which mapped with the data of json.
 *
 * Created by cwang6 on 2017/3/13.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherID;

    public Update update;

    public class Update {

        @SerializedName("loc")
        public String updateTime;
    }
}
