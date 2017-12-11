package cat.ravnsbjerg34.smartbikelock.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Timestamp;

import cat.ravnsbjerg34.smartbikelock.models.ParticleReqJSON;
import cat.ravnsbjerg34.smartbikelock.models.ParticleResponse;

/**
 * Created by guillemcat on 12/10/17.
 *
 */

public class ParticleJsonParser {

    /*public static ParticleResponse parseParticleReqJSONWithGson(String jsonString){

        Gson gson = new GsonBuilder().create();
        ParticleReqJSON particleResponse =  gson.fromJson(jsonString, ParticleReqJSON.class);
        if(particleResponse != null) {
            return new ParticleResponse(particleResponse.id, particleResponse.last_app, particleResponse.connected, particleResponse.result);
        } else {
            return null;
        }
    }*/

    public static ParticleReqJSON parseParticleReqJSONWithGson(String jsonString){

        Gson gson = new GsonBuilder().create();
        ParticleReqJSON particleResponse =  gson.fromJson(jsonString, ParticleReqJSON.class);
        if(particleResponse != null) {
            return particleResponse;
        } else {
            return null;
        }
    }
}
