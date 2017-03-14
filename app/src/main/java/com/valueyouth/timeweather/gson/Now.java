package com.valueyouth.timeweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * show the temperature and the weather.
 * Created by cwang6 on 2017/3/13.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {

        @SerializedName("txt")
        public String info;

    }

}
