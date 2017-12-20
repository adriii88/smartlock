package cat.ravnsbjerg34.smartbikelock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import cat.ravnsbjerg34.smartbikelock.models.ParticleFuncResJson;
import cat.ravnsbjerg34.smartbikelock.models.ParticleVarResJson;
import cat.ravnsbjerg34.smartbikelock.utils.ParticleJsonParser;

import static cat.ravnsbjerg34.smartbikelock.utils.Globals.ALARM_OFF;
import static cat.ravnsbjerg34.smartbikelock.utils.Globals.GET_ALARM_FLAG;
import static cat.ravnsbjerg34.smartbikelock.utils.Globals.LOCK_ACTION;
import static cat.ravnsbjerg34.smartbikelock.utils.Globals.PARTICLE_URL;
import static cat.ravnsbjerg34.smartbikelock.utils.ParticleJsonParser.parseParticleResJsonWithGson;

public class SecurityService extends Service {

    //extend the Binder class - we will return and instance of this in the onBind()
    public class SecurityServiceBinder extends Binder {
        //return ref to service (or at least an interface) that activity can call public methods on
        SecurityService getService() {
            return SecurityService.this;
        }
    }

    //The IBinder instance to return
    private final IBinder binder = new SecurityServiceBinder();

    static RequestQueue queue;

    // fixed time until the next update
    private final static long SLEEP_MILISEC = 5000;

    final Handler handler = new Handler();

    //ParticleRes Json SecurRes;

    enum ReqMode {ACTION, ALARM_OFF, ALARM_REQ}

    private boolean AlarmFlag = false;

    private Runnable loop;

    private boolean Dead = false;

    ParticleFuncResJson LockRes;

    //TODO: remove
   // String next_state = "unlock";


    public SecurityService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //SharedPreferences StoredData = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        //last_update = StoredData.getString(LAST_UPDATE, "now");
        if (!Dead) {
            loopy();
        }
        return START_STICKY;
    }

    public void disableAlarmFlag() {
        this.AlarmFlag = false;
        sendReq(ReqMode.ALARM_OFF, null);
    }

    public boolean getAlarmFlag() {
        return this.AlarmFlag;
    }

    //TODO: remove
    /*public void setNext_state(String nxSt) {
        this.next_state = nxSt;
    }

    public String getNext_state() {
        return this.next_state;
    }*/

    private void loopy() {
        loop = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, SLEEP_MILISEC);
                sendReq(ReqMode.ALARM_REQ, null);
            }
        };
        handler.postDelayed(loop, SLEEP_MILISEC);
    }

    private void securityCheck(ParticleVarResJson data){
        if ((boolean)data.result) {
            AlarmFlag = true;
            sendNotification();
        }
    }

    public void sendReq(final ReqMode mode, final String param) {
        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }

        // configuring the city's URL
        String url;
        int req;
        if (mode==ReqMode.ALARM_REQ) {
            url = String.format(PARTICLE_URL, GET_ALARM_FLAG);
            req = Request.Method.GET;
        }/* else if (mode == ReqMode.ACTION) {
            url = String.format(PARTICLE_URL, LOCK_ACTION);
            req = Request.Method.POST;
        }*/ else if (mode == ReqMode.ALARM_OFF) {
            url = String.format(PARTICLE_URL, ALARM_OFF);
            req = Request.Method.POST;
        } else {
            //It should never go through here
            return;
        }
        // prepare the request, set up a listener which will save the new updated city
        StringRequest stringRequest = new StringRequest(req, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            Log.d("VOLLEY", "Renponse recived");
                            if (mode==ReqMode.ALARM_REQ) {
                                securityCheck((ParticleVarResJson)parseParticleResJsonWithGson(response, ParticleJsonParser.JsonType.Var));
                            } /*else if (mode == ReqMode.ACTION) {
                                LockRes = (ParticleFuncResJson) parseParticleResJsonWithGson(response, ParticleJsonParser.JsonType.Func);
                                //TODO: return it though broadcast
                            }*/ else if (mode == ReqMode.ALARM_OFF) {
                                Log.d("VOLLEY", "Alarm went off");
                            } else {
                                //It should never go through here
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "Response GET error");
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                if (mode == ReqMode.ALARM_OFF) {
                    params.put("arg", "");
                    return params;
                }/* else if (mode == ReqMode.ACTION) {
                    params.put("arg", param);
                    Log.d("DEBUG", "next_stateVOLLEY = " + param);
                    return params;
                }*/
                return null;
            }
        };

        queue.add(stringRequest);
    }

    public void sendNotification() {
        Log.d("WeaServ", "Send Notification");

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.bike_logo)
                        .setContentTitle(getString(R.string.NOTIFICATION_TITLE))
                        .setContentText(getString(R.string.NOTIFICATION_TEXT))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    public void stopMeNow() {
        Dead = true;
        //queue = null;
        handler.removeCallbacks(loop);
        stopSelf();
    }
}