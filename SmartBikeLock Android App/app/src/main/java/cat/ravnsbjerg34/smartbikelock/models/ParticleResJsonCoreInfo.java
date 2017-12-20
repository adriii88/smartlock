package cat.ravnsbjerg34.smartbikelock.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by guillemcat on 12/11/17.
 *
 */

public class ParticleResJsonCoreInfo {

    @SerializedName("last_app")
    @Expose
    public String last_app;
    @SerializedName("last_heard")
    @Expose
    public String last_heard;
    @SerializedName("connected")
    @Expose
    public boolean connected;
    @SerializedName("last_handshake_at")
    @Expose
    public String last_handshake_at;
    @SerializedName("deviceID")
    @Expose
    public String lockID;
    @SerializedName("product_id")
    @Expose
    public int product_id;
}