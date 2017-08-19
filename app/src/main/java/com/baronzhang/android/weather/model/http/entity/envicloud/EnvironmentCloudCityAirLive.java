package com.baronzhang.android.weather.model.http.entity.envicloud;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 도시의 실시간 공기질
 *
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 *         2017/2/16
 */
public class EnvironmentCloudCityAirLive {


    /**
     * citycode : 101020100
     * PM25 : 33
     * time : 2017021614
     * rdesc : Success
     * PM10 : 43
     * SO2 : 12.25
     * o3 : 51.58
     * NO2 : 53.17
     * primary : 颗粒物(PM10)
     * rcode : 200
     * CO : 0.77
     * AQI : 46
     */

    @JSONField(name = "rcode")
    private int requestCode;//结果吗

    @JSONField(name = "rdesc")
    private String requestDesc;//결과 설명

    @JSONField(name = "citycode")
    private String cityId;//도시ID

    private String time;//시간(yyyyMMddHH)

    @JSONField(name = "AQI")
    private String aqi;//대기 질 지수

    @JSONField(name = "PM25")
    private String pm25;//PM2.5 농도(μg/m3)

    @JSONField(name = "PM10")
    private String pm10;//PM10 농도(μg/m3)

    @JSONField(name = "CO")
    private String co;//일산화탄소 농도(mg/m3)

    @JSONField(name = "SO2")
    private String so2;//이산화황 농도(μg/m3)

    @JSONField(name = "NO2")
    private String no2;//이산화질소 농도(μg/m3)

    private String o3;//오존 농도(μg/m3)

    private String primary;//1차 오염 물질

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String getRequestDesc() {
        return requestDesc;
    }

    public void setRequestDesc(String requestDesc) {
        this.requestDesc = requestDesc;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getPm10() {
        return pm10;
    }

    public void setPm10(String pm10) {
        this.pm10 = pm10;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getSo2() {
        return so2;
    }

    public void setSo2(String so2) {
        this.so2 = so2;
    }

    public String getNo2() {
        return no2;
    }

    public void setNo2(String no2) {
        this.no2 = no2;
    }

    public String getO3() {
        return o3;
    }

    public void setO3(String o3) {
        this.o3 = o3;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }
}
