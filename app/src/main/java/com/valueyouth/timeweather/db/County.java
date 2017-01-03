package com.valueyouth.timeweather.db;

import org.litepal.crud.DataSupport;

/**
 * 类说明：区或县城。
 * Created by cwang6 on 2017/1/3.
 */

public class County extends DataSupport
{
    private int id;

    private String countyName;

    private String weatherID;

    private int cityID;

    public int getCityID()
    {
        return cityID;
    }

    public void setCityID(int cityID)
    {
        this.cityID = cityID;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCountyName()
    {
        return countyName;
    }

    public void setCountyName(String countyName)
    {
        this.countyName = countyName;
    }

    public String getWeatherID()
    {
        return weatherID;
    }

    public void setWeatherID(String weatherID)
    {
        this.weatherID = weatherID;
    }
}
