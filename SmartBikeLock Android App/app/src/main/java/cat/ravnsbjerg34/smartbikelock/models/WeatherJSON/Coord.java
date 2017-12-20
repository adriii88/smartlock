package cat.ravnsbjerg34.smartbikelock.models.WeatherJSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by guillemcat on 11/7/17.
 * Extracted from WeatherServiceDemo created by kasper on 30/04/16.
 */

public class Coord {

    @SerializedName("lon")
    @Expose
    public Double lon;
    @SerializedName("lat")
    @Expose
    public Double lat;
}
