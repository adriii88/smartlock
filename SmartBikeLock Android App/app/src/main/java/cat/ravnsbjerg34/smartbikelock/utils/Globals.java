package cat.ravnsbjerg34.smartbikelock.utils;

/**
 * Created by guillemcat on 12/9/17.
 *
 */

public class Globals {
    private static final String ACCESS_TOKEN = "c8ace484073e145a7b1dee1fea49384d65abe354";
    private static final String DEVICE_ID = "300041001047353138383138";
    public static final String LOCK_ACTION = "switch";
    public static final String ALARM_OFF = "alarmoff";
    public static final String GET_ALARM_FLAG = "alarmflag";
    public static final String WEATHER = "weather";

    public static final String PARTICLE_URL = "https://api.particle.io/v1/devices/" + DEVICE_ID + "/%s?access_token=" + ACCESS_TOKEN;
}
