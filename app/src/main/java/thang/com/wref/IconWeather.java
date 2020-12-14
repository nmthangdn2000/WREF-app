package thang.com.wref;

public class IconWeather {
    public int IconWeather(String desWeather){
        if(desWeather.equals("Rain"))
            return R.drawable.ic_rain_sun_small;
//        else if(desWeather.equals(""))
        else return 0;
    }
    public String Temp(float max, float min){
        float c_max = max - 273;
        float c_min = min - 273;
        return (int) c_max + "/"+ (int) c_min + "°C";
    }
    public String typeWeather(String des){
        if(des.equals("moderate rain"))
            return "Mưa vừa";
        else if(des.equals("heavy intensity rain"))
            return "Mưa lớn";
        else if(des.equals("light rain"))
            return "Mưa nhỏ";
        else return "";
    }
}
