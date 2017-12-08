package cat.ravnsbjerg34.smartbikemultilock;

import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

import cat.ravnsbjerg34.smartbikemultilock.model.LockClass;
import cat.ravnsbjerg34.smartbikemultilock.utils.Global;

import static cat.ravnsbjerg34.smartbikemultilock.utils.Global.LOCK_EXTRA;
import static cat.ravnsbjerg34.smartbikemultilock.utils.Global.POSITION_EXTRA;

public class LockDetailActivity extends AppCompatActivity {

    TextView lockName;
    ImageView statusIcon;
    Button switchBtn;

    LockClass Lock;
    int position;

    String next_state = "lock";

    static RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_detail);

        lockName = findViewById(R.id.lock_name);
        statusIcon = findViewById(R.id.status);
        switchBtn = findViewById(R.id.switch_btn);

        //Status icon formatting
        statusIcon.setAdjustViewBounds(true);
        statusIcon.setBackgroundColor(0);

        Intent CityListActivityIntent = getIntent();
        position = CityListActivityIntent.getIntExtra(POSITION_EXTRA, 0);
        Lock = (LockClass) CityListActivityIntent.getSerializableExtra(LOCK_EXTRA);

        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOrder();
            }
        });
    }

    private void setNextState(){
        if (next_state.equals("lock")) {
            next_state = "unlock";
            switchBtn.setText(next_state);
            statusIcon.setImageResource(R.drawable.locked);
            statusIcon.setAdjustViewBounds(true);
            statusIcon.setBackgroundColor(0);
            Log.d("DEBUG", "next_state = "+next_state);
        }
        else if (next_state.equals("unlock")) {
            next_state = "lock";
            switchBtn.setText(next_state);
            statusIcon.setImageResource(R.drawable.locked);
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
        String url = String.format(Global.PARTICLE_URL, Lock.getDevice());

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
                Log.d("Error", "Response POST error");
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                String param = next_state + Lock.getPort();
                Map<String, String> params = new HashMap<>();
                params.put("arg", param);
                Log.d("DEBUG", "next_stateVOLLEY = "+next_state);

                return params;
            }
        };

        queue.add(stringRequest);
    }
}
