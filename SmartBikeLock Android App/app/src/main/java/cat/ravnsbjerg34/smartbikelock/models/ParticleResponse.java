package cat.ravnsbjerg34.smartbikelock.models;

/**
 * Created by guillemcat on 12/10/17.
 *
 */

public class ParticleResponse {

    private String id;
    private String last_app;
    private boolean connected;
    private int result;

    public ParticleResponse (String id, String last_app, boolean connected, int result) {
        this.id = id;
        this.last_app = last_app;
        this.connected = connected;
        this.result = result;
    }

    public String getId() {
        return this.id;
    }

    public String getLast_app() {
        return this.last_app;
    }

    public boolean getConnected() {
        return this.connected;
    }

    public int getResult() {
        return this.result;
    }
}
