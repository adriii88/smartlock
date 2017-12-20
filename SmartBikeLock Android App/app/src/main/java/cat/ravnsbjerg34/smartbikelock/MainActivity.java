package cat.ravnsbjerg34.smartbikelock;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

import java.util.Map;

import cat.ravnsbjerg34.smartbikelock.models.CityWeatherData;
import cat.ravnsbjerg34.smartbikelock.models.ParticleFuncResJson;
import cat.ravnsbjerg34.smartbikelock.models.WeatherJSON.CityWeatherJson;
import cat.ravnsbjerg34.smartbikelock.utils.ParticleJsonParser;

import static cat.ravnsbjerg34.smartbikelock.utils.Globals.LOCK_ACTION;
import static cat.ravnsbjerg34.smartbikelock.utils.Globals.PARTICLE_URL;
import static cat.ravnsbjerg34.smartbikelock.utils.Globals.WEATHER;
import static cat.ravnsbjerg34.smartbikelock.utils.ParticleJsonParser.parseParticleResJsonWithGson;
import static cat.ravnsbjerg34.smartbikelock.utils.WeatherJsonParser.parseCityWeatherJsonWithGson;
import static cat.ravnsbjerg34.smartbikelock.utils.WeatherJsonParser.parseWeatherJsonWithGson;

public class MainActivity extends AppCompatActivity {

    Switch alarmSwitch;
    TextView lockStatusTxt;
    ImageButton switchStatusBtn, refreshWeather;

    TextView LocName, Temp, Descr;

    String next_state;

    static RequestQueue queue;

    ParticleFuncResJson LockRes;

    private ServiceConnection conn;

    //BroadcastReceiver CityReceiver;

    SecurityService SecuServ;

    boolean AlarmFlagReg;
    private boolean Quiting = false;

    private Intent SecuServIntent;

    enum ReqMode {ACTION, WEATHER}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lockStatusTxt = findViewById(R.id.lock_state_txt);
        switchStatusBtn = findViewById(R.id.switch_icon_btn);

        LocName = findViewById(R.id.loc_name);
        Temp = findViewById(R.id.temp);
        Descr = findViewById(R.id.description);

        alarmSwitch = findViewById(R.id.alarm_switch);
        alarmSwitch.setClickable(false);

        //Status icon formatting
        switchStatusBtn.setAdjustViewBounds(true);
        switchStatusBtn.setBackgroundColor(0);

        refreshWeather = findViewById(R.id.refreshBtn);
        refreshWeather.setAdjustViewBounds(true);
        refreshWeather.setBackgroundColor(0);

        //SecurityService connections
        setupConnectionToSecurityService();

        switchStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOrder(ReqMode.ACTION);
            }
        });

        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (!checked) {
                    alarmSwitch.setTextColor(0xAAAAAA);
                    alarmSwitch.setClickable(false);
                    alarmSwitch.setText("Alarm");
                    sendOrder(ReqMode.ACTION);
                    SecuServ.disableAlarmFlag();
                }
            }
        });

        refreshWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOrder(ReqMode.WEATHER);
            }
        });
    }

    @Override
    protected void onStart() {
        if ( SecuServ != null && (AlarmFlagReg = SecuServ.getAlarmFlag())) {
            alarmSwitch.setChecked(true);
            alarmSwitch.setTextColor(getResources().getColor(R.color.error_text_color));
            alarmSwitch.setClickable(true);
            alarmSwitch.setText("Alarm");
            SecuServ.disableAlarmFlag();
        }
        restoreState();
        Log.d("DEBUG", "restore: " + next_state);
        sendOrder(ReqMode.WEATHER);
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //TODO: after an alarm the state isn't stored properly
        if (id == R.id.quit) {
            Quiting = true;
            SecuServ.stopMeNow();
            unbindService(conn);
            stopService(SecuServIntent);
            finishAndRemoveTask();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setNextState() {
        if (next_state.equals("lock")) {
            next_state = "unlock";
            lockStatusTxt.setText(R.string.statusTxtLocked);
            switchStatusBtn.setImageDrawable(getDrawable(R.drawable.locked));
            switchStatusBtn.setAdjustViewBounds(true);
            switchStatusBtn.setBackgroundColor(0);
            Log.d("DEBUG", "next_state = " + next_state);
        } else if (next_state.equals("unlock")) {
            next_state = "lock";
            lockStatusTxt.setText(R.string.statusTxtUnlocked);
            switchStatusBtn.setImageDrawable(getDrawable(R.drawable.unlocked));
            switchStatusBtn.setAdjustViewBounds(true);
            switchStatusBtn.setBackgroundColor(0);
            Log.d("DEBUG", "next_state = " + next_state);
        }
    }

    private void sendOrder(final ReqMode mode) {
        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }

        String url;
        int req;
        if (mode == ReqMode.ACTION) {
            url = String.format(PARTICLE_URL, LOCK_ACTION);
            req = Request.Method.POST;
        } else if (mode == ReqMode.WEATHER) {
            url = String.format(PARTICLE_URL, WEATHER);
            req = Request.Method.GET;
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


                            if (mode == ReqMode.ACTION) {
                                LockRes = (ParticleFuncResJson) parseParticleResJsonWithGson(response, ParticleJsonParser.JsonType.Func);
                                if (LockRes.return_value == -1) {
                                    Toast.makeText(MainActivity.this, "There is no bike in the lock!", Toast.LENGTH_LONG).show();
                                } else {
                                    setNextState();
                                }
                            } else if (mode == ReqMode.WEATHER) {
                                renderWeatherData(parseWeatherJsonWithGson(response));
                                Toast.makeText(MainActivity.this, "Weather updated", Toast.LENGTH_SHORT).show();
                            } else {
                                //It should never go through here
                                return;
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "Response GET error");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("arg", next_state);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    //Initialisation of the receiver who will catch the broadcast data sent from WeatherService
    /*public void initReceiver() {
        registerReceiver(CityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //TODO: refill
            }
        }, new IntentFilter(CITY_BROADCAST));
    }*/


    //Do all the configuration needed to bind to the SecurityService, including starting it if needed
    private void setupConnectionToSecurityService() { //TODO: update
        //Extracted from ServiceDemo app from the ITSMAP lessons, by Kasper from Leafcastle
        conn = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {

                SecuServ = ((SecurityService.SecurityServiceBinder) service).getService();
                /*if (SecuServ != null) {
                    initReceiver();
                }*/
                if (AlarmFlagReg = SecuServ.getAlarmFlag()) {
                    alarmSwitch.setChecked(true);
                    alarmSwitch.setTextColor(getResources().getColor(R.color.error_text_color));
                    alarmSwitch.setClickable(true);
                    alarmSwitch.setText("Alarm");
                }
            }

            public void onServiceDisconnected(ComponentName className) {
                SecuServ = null;
            }
        };
        SecuServIntent = new Intent(this, SecurityService.class);

        startService(SecuServIntent);
        bindService(SecuServIntent, conn, BIND_AUTO_CREATE);
    }

    private void renderWeatherData(CityWeatherJson data){
        if (data == null){
            LocName.setText("You fcked up somewhere, asshole!");
        }
        else {
            Log.d("DEBUG", data.name);
            LocName.setText(data.name);
            Temp.setText(String.valueOf(data.main.temp));
            String desc = data.weather.get(0).description;
            String Desc = desc.substring(0,1).toUpperCase()+desc.substring(1);
            Descr.setText(Desc);
        }
    }

    private void saveState() {
        SharedPreferences StoredData = getSharedPreferences("STATE", MODE_PRIVATE);
        SharedPreferences.Editor editor = StoredData.edit();

        editor.putString("next_state", next_state);
        editor.apply();
    }

    private void restoreState() {
        SharedPreferences StoredData = getSharedPreferences("STATE", MODE_PRIVATE);
        next_state = StoredData.getString("next_state", "lock");
        if (next_state.equals("unlock")) {
            //next_state = "unlock";
            lockStatusTxt.setText(R.string.statusTxtLocked);
            switchStatusBtn.setImageDrawable(getDrawable(R.drawable.locked));
            switchStatusBtn.setAdjustViewBounds(true);
            switchStatusBtn.setBackgroundColor(0);
        } else if (next_state.equals("lock")) {
            //next_state = "lock";
            lockStatusTxt.setText(R.string.statusTxtUnlocked);
            switchStatusBtn.setImageDrawable(getDrawable(R.drawable.unlocked));
            switchStatusBtn.setAdjustViewBounds(true);
            switchStatusBtn.setBackgroundColor(0);
        }
    }

    /*
    @Override
    protected void onPause() {
        unregisterReceiver(CityReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        registerReceiver(CityReceiver, new IntentFilter(CITY_BROADCAST));
        super.onResume();
    }*/

    @Override
    protected void onDestroy() {
        if (!Quiting) {
            unbindService(conn);
        }
        Log.d("DEBUG", "destroy: " + next_state);
        saveState();
        super.onDestroy();
    }
}