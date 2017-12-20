package cat.ravnsbjerg34.smartbikelock.models.WeatherJSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by guillemcat on 11/7/17.
 * Extracted from WeatherServiceDemo created by kasper on 30/04/16.
 */

public class Main {

    @SerializedName("temp")
    @Expose
    public Double temp;
    @SerializedName("pressure")
    @Expose
    public Double pressure;
    @SerializedName("humidity")
    @Expose
    public Double humidity;
    @SerializedName("temp_min")
    @Expose
    public Double tempMin;
    @SerializedName("temp_max")
    @Expose
    public Double tempMax;
    @SerializedName("sea_level")
    @Expose
    public Double seaLevel;
    @SerializedName("grnd_level")
    @Expose
    public Double grndLevel;
}
