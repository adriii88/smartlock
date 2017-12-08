package cat.ravnsbjerg34.smartbikemultilockfrag.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cat.ravnsbjerg34.smartbikemultilockfrag.R;
import cat.ravnsbjerg34.smartbikemultilockfrag.model.LockClass;

/**
 * Created by guillemcat on 12/7/17.
 *
 */

public class LockItemAdaptor extends BaseAdapter {
    private Context context;
    private ArrayList<LockClass> LockList;

    public LockItemAdaptor(Context c, ArrayList<LockClass> LockList) {
        this.context = c;
        this.LockList = LockList;
    }

    @Override
    public int getCount() {
        if (LockList != null) {
            return LockList.size();
        } else {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater LockInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = LockInflater.inflate(R.layout.lock_item, null);
        }

        LockClass LockInfo = LockList.get(position);
        if (LockInfo != null) {
            ImageView icon = convertView.findViewById(R.id.statusIcon);
            switch (LockInfo.getState()){
                case locked:
                    icon.setImageResource(R.drawable.locked);
                    break;
                case unlocked:
                    icon.setImageResource(R.drawable.unlocked);
                    break;
            }

            TextView txtTitle = convertView.findViewById(R.id.lockName);
            txtTitle.setText(LockInfo.getLockName());
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        if (LockList != null) {
            return LockList.get(position);
        } else {
            return null;
        }
    }
}
