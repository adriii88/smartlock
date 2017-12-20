package cat.ravnsbjerg34.smartbikelock.models;

import java.io.Serializable;
/**
 * Created by guillemcat on 11/3/17.
 *
 */

public class CityWeatherData implements Serializable {

    private String cityName;
    private Double temperature;
    private Double humidity;
    private String description;
    private String picture;

    public CityWeatherData(String name, Double temp, Double hum, String desc, String pic){
        this.cityName = name;
        this.temperature = temp;
        this.humidity = hum;
        this.description = desc;
        this.picture = pic;
    }

    public String getName() { return this.cityName;  }

    public Double getTemperature() { return this.temperature; }

    public void setTemperature(Double temp) { this.temperature = temp; }

    public Double getHumidity() { return this.humidity; }

    public void setHumidity(Double hum) { this.humidity = hum; }

    public String getDescription() { return this.description; }

    public void setDescription(String desc) { this.description = desc; }

    public String getPicture() { return this.picture; }

    public void setPicture(String pic) { this.picture = pic; }

    public boolean isEqual(CityWeatherData secondcity){return this.cityName.equals(secondcity.getName());}
}
