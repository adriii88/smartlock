package cat.ravnsbjerg34.smartbikemultilockfrag.model;

import java.io.Serializable;

/**
 * Created by guillemcat on 12/7/17.
 *
 */

public class LockClass implements Serializable {
    private String lockName;
    private String Device;
    private int port;
    public enum lockState {locked, unlocked};
    private lockState state;
    String user;

    public LockClass(String name){
        this.lockName = name;
    }

    public String getLockName() {
        return this.lockName;
    }

    public String getDevice() {
        return this.Device;
    }

    public int getPort() {
        return this.port;
    }

    public lockState getState() {
        return this.state;
    }
}
