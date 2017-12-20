package cat.ravnsbjerg34.smartbikelock.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by guillemcat on 12/13/17.
 *
 */

public class ParticleVarResJson {

    @SerializedName("cmd")
    @Expose
    public String cmd;
    @SerializedName("name")
    @Expose
    public String var_name;
    @SerializedName("result")
    @Expose
    public Object result;
    @SerializedName("coreInfo")
    @Expose
    public ParticleResJsonCoreInfo coreInfo;
}
