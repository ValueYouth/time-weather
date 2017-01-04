package com.valueyouth.timeweather;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.valueyouth.timeweather.db.City;
import com.valueyouth.timeweather.db.County;
import com.valueyouth.timeweather.db.Province;
import com.valueyouth.timeweather.util.HttpUtil;
import com.valueyouth.timeweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.valueyouth.timeweather.util.Constant.FIELD_TYPE_CITY;
import static com.valueyouth.timeweather.util.Constant.FIELD_TYPE_COUNTY;
import static com.valueyouth.timeweather.util.Constant.FIELD_TYPE_PROVINCE;
import static com.valueyouth.timeweather.util.Constant.LEVEL_CITY;
import static com.valueyouth.timeweather.util.Constant.LEVEL_COUNTY;
import static com.valueyouth.timeweather.util.Constant.LEVEL_PROVINCE;

/**
 * 类说明：遍历省市县数据。
 * Created by cwang6 on 2017/1/4.
 */
public class ChooseAreaFragment extends Fragment
        implements AdapterView.OnItemClickListener, View.OnClickListener
{
    private ProgressDialog progressDialog;

    private TextView titleText;

    private Button backButton;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<>();

    /**省列表*/
    private List<Province> provinceList;

    /**市列表*/
    private List<City> cityList;

    /**县列表*/
    private List<County> countyList;

    /**选中的省份*/
    private Province selectedProvince;

    /**选中的城市*/
    private City selectedCity;

    /**当前选中的级别*/
    private int currentLevel;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dataList);

        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(this);
        backButton.setOnClickListener(this);
        queryProvinces();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (currentLevel == LEVEL_PROVINCE) {
            selectedProvince = provinceList.get(position);
            queryCities();
        }
        else if (currentLevel == LEVEL_CITY) {
            selectedCity = cityList.get(position);
            queryCounties();
        }
    }

    @Override
    public void onClick(View v)
    {
        if (currentLevel == LEVEL_COUNTY){
            queryCities();
        }
        else if (currentLevel == LEVEL_CITY){
            queryProvinces();
        }
    }

    /**
     * 方法说明：查询全国所有的省份，优先从数据库查询，
     *          如果没有查到再去服务器上查询。
     */
    private void queryProvinces()
    {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);

        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }

            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }
        else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, FIELD_TYPE_PROVINCE);
        }
    }

    /**
     * 方法说明：查询全省所有市，优先从数据库查询，
     *          如果没有查到再去服务器上查询。
     */
    private void queryCities()
    {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?", String.valueOf(selectedProvince.getId()))
                .find(City.class);

        if (cityList.size() > 0){
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }

            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }
        else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, FIELD_TYPE_CITY);
        }
    }

    /**
     * 方法说明：查询全市所有县，优先从数据库查询，
     *          如果没有查到再去服务器上查询。
     */
    private void queryCounties()
    {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?", String.valueOf(selectedCity.getId()))
                .find(County.class);

        if (countyList.size() > 0){
            dataList.clear();
            for (County county : countyList){
                dataList.add(county.getCountyName());
            }

            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }
        else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, FIELD_TYPE_COUNTY);
        }
    }

    /**
     * 方法说明：根据传入的地址和类型从服务器上查询省市县数据。
     * @param address 地址。
     * @param type 类型：省、市和县。
     */
    private void queryFromServer(String address, final String type)
    {
        showProgressDialog(); // 显示进度条
        HttpUtil.sendOKHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                /*通过runOnUiThread()方法，回到主线程处理逻辑。*/
                getActivity().runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run()
                    {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                switch (type) {
                    case "province":
                        result = Utility.handleProvinceResponse(responseText);
                        break;
                    case "city":
                        result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                        break;
                    case "county":
                        result = Utility.handleCountyResponse(responseText, selectedCity.getId());
                        break;
                    default:
                        break;
                }

                if (result) {
                    showResult(type); // 显示结果
                }
            }
        });
    }

    /**
     * 方法说明：显示查询的结果。
     * @param type 类型：省、市和县。
     */
    private void showResult(final String type)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeProgressDialog();
                switch (type) {
                    case "province":
                        queryProvinces();
                        break;
                    case "city":
                        queryCities();
                        break;
                    case "county":
                        queryCounties();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 方法说明：显示进度对话框。
     */
    private void showProgressDialog()
    {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 方法说明：关闭进度对话框。
     */
    private void closeProgressDialog()
    {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


}
