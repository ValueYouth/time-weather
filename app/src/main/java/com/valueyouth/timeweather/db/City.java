package com.valueyouth.timeweather.db;

import org.litepal.crud.DataSupport;

/**
 * 类说明：城市。
 * Created by cwang6 on 2017/1/3.
 */

public class City extends DataSupport
{
    private int id;

    private String cityName;

    private int cityCode;

    private int provinceID;

    public int getProvinceID()
    {
        return provinceID;
    }

    public void setProvinceID(int provinceID)
    {
        this.provinceID = provinceID;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public int getCityCode()
    {
        return cityCode;
    }

    public void setCityCode(int cityCode)
    {
        this.cityCode = cityCode;
    }
}
