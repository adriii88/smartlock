package cat.ravnsbjerg34.smartbikelock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.devicesetup.ParticleDeviceSetupLibrary;

public class MainActivity extends AppCompatActivity {

    TextView lockName;
    ImageView statusIcon;
    Button switchBtn;

    int resultCode;

    ParticleDevice Lock;

    String next_state = "lock";

    static RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lockName = findViewById(R.id.lock_name);
        statusIcon = findViewById(R.id.status);
        switchBtn = findViewById(R.id.switch_btn);

        //Status icon formatting
        statusIcon.setAdjustViewBounds(true);
        statusIcon.setBackgroundColor(0);

        /*ParticleDeviceSetupLibrary.startDeviceSetup(this, MainActivity.class);

        ParticleDeviceSetupLibrary.initWithSetupOnly(this.getApplicationContext());

        try {
            ParticleCloudSDK.getCloud().logIn("guillem.cat96@gmail.com", "Ravnsbjerg34");
        } catch (ParticleCloudException e) {
            Log.d("Error",String.format("LogIn failed %s %s",e.getResponseData(), e.getBestMessage()));
        }

        ParticleCloudSDK.getCloud().setAccessToken("c8ace484073e145a7b1dee1fea49384d65abe354");

        try {
            Lock = ParticleCloudSDK.getCloud().getDevice("300041001047353138383138");
        } catch (ParticleCloudException e) {
            Log.d("Error", String.format("getDevice failed %s %s",e.getResponseData(), e.getBestMessage()));
        }
        lockName.setText(Lock.getName());*/

        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*List<String> param = new ArrayList<>();
                param.add(next_state);
                try {
                    resultCode = Lock.callFunction("switch", param);
                } catch (ParticleCloudException e) {
                    Log.d("Error", "callFunction cloud error");
                } catch (ParticleDevice.FunctionDoesNotExistException f){
                    Log.d("Error", "Error 404: callFunction callback parameter not found");
                } catch (IOException ioError) {
                    Log.d("Error", "IO error");
                }*/
                sendOrder();
            }
        });
    }

    private void setNextState(){
        if (next_state.equals("lock")) {
                next_state = "unlock";
                switchBtn.setText(next_state);
                statusIcon.setImageDrawable(getDrawable(R.drawable.locked));
                statusIcon.setAdjustViewBounds(true);
                statusIcon.setBackgroundColor(0);
                Log.d("DEBUG", "next_state = "+next_state);
        }
        else if (next_state.equals("unlock")) {
                next_state = "lock";
                switchBtn.setText(next_state);
                statusIcon.setImageDrawable(getDrawable(R.drawable.unlocked));
                statusIcon.setAdjustViewBounds(true);
                statusIcon.setBackgroundColor(0);
                Log.d("DEBUG", "next_state = "+next_state);
        }
    }

    private void sendOrder() {
        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }
        // configuring the city's URL
        String url = "https://api.particle.io/v1/devices/300041001047353138383138/switch?access_token=c8ace484073e145a7b1dee1fea49384d65abe354";

        // prepare the request, set up a listener which will save the new updated city
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            Log.d("VOLLEY", "Renponse recived");
                            setNextState();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "Response GET error");
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("arg", next_state);
                Log.d("DEBUG", "next_stateVOLLEY = "+next_state);

                return params;
            }
        };

        queue.add(stringRequest);
    }
}