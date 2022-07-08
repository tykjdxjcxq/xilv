package com.gzf.xilv.ui.device;

import android.content.SharedPreferences;
import android.util.Log;

import com.ejlchina.okhttps.HttpResult;
import com.ejlchina.okhttps.HttpUtils;
import com.gzf.xilv.R;
import com.gzf.xilv.adapter.DeviceBannerAdapter;
import com.gzf.xilv.anim.RotateYTransformer;
import com.gzf.xilv.base.mvp.BaseMvpActivity;
import com.gzf.xilv.databinding.ActivityDeviceBinding;
import com.gzf.xilv.model.LEDInfo;
import com.gzf.xilv.ui.main.Config;
import com.youth.banner.indicator.CircleIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class DeviceActivity extends BaseMvpActivity<DevicePresenter> implements DeviceContract.View {
    private DeviceBannerAdapter bannerAdapter;
    private Config config=new Config();
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_device;
    }

    @Override
    protected void initView() {
        ActivityDeviceBinding binding = (ActivityDeviceBinding) dataBinding;
        bannerAdapter = new DeviceBannerAdapter(null);
        binding.banner.setIndicator(new CircleIndicator(this), false);
        binding.banner.setBannerGalleryEffect(50, 12, 0.8f);
        binding.banner.addPageTransformer(new RotateYTransformer());
        binding.banner.setAdapter(bannerAdapter, false);
        binding.banner.setOnBannerListener((data, position) -> {
            LEDInfo ledInfo = (LEDInfo) data;
            if (ledInfo.getType() == LEDInfo.TYPE_LED) {

            } else if (ledInfo.getType() == LEDInfo.TYPE_ADD_NEW) {

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        bannerAdapter.setDatas(presenter.getLEDInfos());
        SharedPreferences sp1 = getSharedPreferences("login", MODE_PRIVATE);
        HttpUtils.async(config.url_api1+"/api/scene")
                .addHeader("Authorization",sp1.getString("token", null) )
                .setOnResponse((HttpResult result1) -> {
                    String str=result1.getBody().toString();
                    try {
                        JSONObject jsonObject=new JSONObject(str);
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            Log.e("baidu", "123:" +jsonArray.getString(i));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                })
                .setOnException((IOException e) -> {
                    Log.e("baidu", "onCreate:" + e.toString());
                    //网络错误

                })
                .get();
        String value=getIntent().getStringExtra("OBOX_Device");
        Log.e("TAG", "initData: "+value );
    }

    @Override
    protected DevicePresenter createPresenter() {
        return new DevicePresenter();
    }
}