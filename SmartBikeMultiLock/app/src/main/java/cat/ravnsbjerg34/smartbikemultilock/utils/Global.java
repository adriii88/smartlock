package cat.ravnsbjerg34.smartbikemultilock.utils;

/**
 * Created by guillemcat on 11/30/17.
 *
 */

public class Global {

    private static final String ACCESS_TOKEN = "c8ace484073e145a7b1dee1fea49384d65abe354";
    private static final String FUNCTION = "switch";

    public static final String PARTICLE_URL = "https://api.particle.io/v1/devices/%s/" + FUNCTION + "?access_token=" + ACCESS_TOKEN;

    public static final String LOCK_EXTRA = "lockclass_from_locklistactivity";
    public static final String POSITION_EXTRA = "lockclass_position_from_locklistactivity";

}
