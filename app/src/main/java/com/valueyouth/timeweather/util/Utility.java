package com.valueyouth.timeweather.util;

import android.text.TextUtils;

import com.valueyouth.timeweather.db.City;
import com.valueyouth.timeweather.db.County;
import com.valueyouth.timeweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类说明：工具类，用来解析从服务器端获取的json格式的数据。
 * Created by cwang6 on 2017/1/4.
 */
public class Utility
{
    /**
     * 方法说明：解析和处理服务器返回的省级数据。
     * @param response 服务器返回的数据
     * @return 解析成功，则返回true；否则，返回false。
     */
    public static boolean handleProvinceResponse(String response)
    {
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++)
                {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();

                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));

                    province.save();
                }
                return true;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 方法说明：解析和处理服务器返回的市级数据。
     * @param response 服务器返回的数据。
     * @param provinceID 省级编号。
     * @return 解析成功，则返回true；否则，返回false。
     */
    public static boolean handleCityResponse(String response, int provinceID)
    {
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++)
                {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();

                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceID(provinceID);

                    city.save();
                }
                return true;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 方法说明：解析和处理服务器返回的县级数据。
     * @param response 服务器返回的数据。
     * @param cityID 城市编号
     * @return 解析成功，则返回true；否则，返回false。
     */
    public static boolean handleCountyResponse(String response, int cityID)
    {
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++)
                {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();

                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherID(countyObject.getString("weather_id"));
                    county.setCityID(cityID);

                    county.save();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            return true;
        }
        return false;
    }
}
