package thang.com.wref.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

import thang.com.wref.Models.Weather.currentWeather;
import thang.com.wref.Models.Weather.dailyWeather;

public class DetailWeatherModel {
    @SerializedName("current")
    private currentWeather current;
    @SerializedName("daily")
    private dailyWeather[] daily;

    public DetailWeatherModel(currentWeather current, dailyWeather[] daily) {
        this.current = current;
        this.daily = daily;
    }

    @Override
    public String toString() {
        return "DetailWeatherModel{" +
                "current=" + current +
                ", daily=" + Arrays.toString(daily) +
                '}';
    }

    public currentWeather getCurrent() {
        return current;
    }

    public void setCurrent(currentWeather current) {
        this.current = current;
    }

    public dailyWeather[] getDaily() {
        return daily;
    }

    public void setDaily(dailyWeather[] daily) {
        this.daily = daily;
    }
}
