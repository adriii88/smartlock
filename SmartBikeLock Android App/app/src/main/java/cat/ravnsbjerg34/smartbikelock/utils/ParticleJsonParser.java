package cat.ravnsbjerg34.smartbikelock.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cat.ravnsbjerg34.smartbikelock.models.ParticleFuncResJson;
import cat.ravnsbjerg34.smartbikelock.models.ParticleVarResJson;

import static cat.ravnsbjerg34.smartbikelock.utils.ParticleJsonParser.JsonType.Var;

/**
 * Created by guillemcat on 12/10/17.
 *
 */

public class ParticleJsonParser {

    public enum JsonType {Var, Func}

    public static Object parseParticleResJsonWithGson(String jsonString, JsonType mode){

        Gson gson = new GsonBuilder().create();
        if (mode == Var) {
             return gson.fromJson(jsonString, ParticleVarResJson.class);
        }
        else {
            return gson.fromJson(jsonString, ParticleFuncResJson.class);
        }
    }
}
