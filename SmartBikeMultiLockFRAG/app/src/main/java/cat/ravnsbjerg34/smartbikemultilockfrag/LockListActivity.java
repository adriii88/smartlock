package cat.ravnsbjerg34.smartbikemultilockfrag;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import cat.ravnsbjerg34.smartbikemultilockfrag.adaptor.LockItemAdaptor;
import cat.ravnsbjerg34.smartbikemultilockfrag.model.LockClass;

public class LockListActivity extends AppCompatActivity {

    ListView LockListView;
    LockItemAdaptor listAdaptor;

    ArrayList<LockClass> LockList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_list);
        setContentView(R.layout.activity_lock_list);

        //Setting the listView adapter
        listAdaptor = new LockItemAdaptor(this, LockList);
        LockListView = findViewById(R.id.lockListView);
        LockListView.setAdapter(listAdaptor);

        LockListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                jumpToLockDetail(adapterView, position);
            }
        });
    }

    public void jumpToLockDetail(AdapterView<?> adapterView, int position) {
        /*Intent itemIntent = new Intent(this, LockDetailActivity.class);
        itemIntent.putExtra(POSITION_EXTRA, position);
        itemIntent.putExtra(LOCK_EXTRA, (LockClass) adapterView.getItemAtPosition(position));
        startActivity(itemIntent);*/
    }
}
