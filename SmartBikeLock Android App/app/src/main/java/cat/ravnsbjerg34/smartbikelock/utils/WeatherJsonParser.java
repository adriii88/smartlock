package cat.ravnsbjerg34.smartbikelock.utils;

import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cat.ravnsbjerg34.smartbikelock.models.CityWeatherData;
import cat.ravnsbjerg34.smartbikelock.models.ParticleFuncResJson;
import cat.ravnsbjerg34.smartbikelock.models.ParticleVarResJson;
import cat.ravnsbjerg34.smartbikelock.models.WeatherJSON.CityWeatherJson;

/**
 * Created by guillemcat on 11/6/17.
 *
 */

public class WeatherJsonParser {

    private static final double TO_CELSIUS_FROM_KELVIN = -273.15;

    public static CityWeatherJson parseWeatherJsonWithGson(String jsonString){
        Log.d("DEBUG_topo", jsonString);
        Gson gson = new GsonBuilder().create();
        String ParticleJSON = (String) gson.fromJson(jsonString, ParticleVarResJson.class).result;
        CityWeatherJson test = gson.fromJson(ParticleJSON, CityWeatherJson.class);
        Log.d("DEBUG_topo", "parser " + test.name);
        return test;
    }

    public static CityWeatherData parseCityWeatherJsonWithGson(String jsonString){
        Log.d("DEBUG_topo", jsonString);
        Gson gson = new GsonBuilder().create();
        CityWeatherJson weatherInfo =  gson.fromJson(jsonString, CityWeatherJson.class);
        if(weatherInfo != null) {
            //TODO: Capitalize description
            return null;//new CityWeatherData(weatherInfo.name, weatherInfo.main.temp + TO_CELSIUS_FROM_KELVIN, weatherInfo.main.humidity, weatherInfo.weather.get(0).description, weatherInfo.weather.get(0).icon);
        } else {
            return null;
        }
    }
}
