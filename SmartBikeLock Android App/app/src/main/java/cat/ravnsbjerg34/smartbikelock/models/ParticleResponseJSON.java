package cat.ravnsbjerg34.smartbikelock.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by guillemcat on 12/9/17.
 *
 */

public class ParticleReqJSON {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("last_app")
    @Expose
    public String last_app;
    @SerializedName("connected")
    @Expose
    public boolean connected;
    @SerializedName("return_value")
    @Expose
    public int result;
}
