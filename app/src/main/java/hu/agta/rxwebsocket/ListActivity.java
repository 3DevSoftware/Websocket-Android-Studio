package hu.agta.rxwebsocket;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import hu.agta.rxwebsocket.model.Model;
import hu.agta.rxwebsocket.utils.AppGlobals;

public class ListActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Model> modelArrayList;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = findViewById(R.id.list_view);
        modelArrayList = new ArrayList<>();
        getAllData();
        listView.setOnTouchListener((v, event) -> {
            finish();
            return false;
        });
    }


    private void getAllData() {
        try {
            Model model = new Model();
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset());
            int lCount = jsonObject.getInt("lcount");
            int rCount = jsonObject.getInt("rcount");
            model.setLcount(lCount);
            model.setRcount(rCount);

            AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_L_COUNT, String.valueOf(lCount));
            AppGlobals.saveDataToSharedPreferences(AppGlobals.KEY_R_COUNT, String.valueOf(rCount));


            JSONArray jsonArray = jsonObject.getJSONArray("stats");
            for (int i = 0; i < jsonArray.length(); i++) {
                model = new Model();
                JSONObject object = jsonArray.getJSONObject(i);
                model.setType(object.getString("type"));
                model.setPend(object.getString("pend"));
                model.setInprog(object.getString("Inprog"));
                model.setApp(object.getString("app"));
                model.setColor(object.getString("color"));
                modelArrayList.add(model);
            }
            adapter = new ListAdapter(getApplicationContext(), modelArrayList);
            listView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("sample.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    class ListAdapter extends ArrayAdapter<Model> {
        private ViewHolder viewHolder;
        private ArrayList<Model> arrayList;
        LayoutInflater inflater;

        private ListAdapter(Context context, ArrayList<Model> lotteryList) {
            super(context, R.layout.delegate_list);
            this.arrayList = lotteryList;
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.delegate_list, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.type = convertView.findViewById(R.id.type);
                viewHolder.pend = convertView.findViewById(R.id.pend);
                viewHolder.inprog = convertView.findViewById(R.id.inprog);
                viewHolder.app = convertView.findViewById(R.id.app);
                viewHolder.linearLayout = convertView.findViewById(R.id.layout);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Model model = arrayList.get(position);
            viewHolder.type.setText(model.getType());
            viewHolder.pend.setText(model.getPend());
            viewHolder.inprog.setText(model.getInprog());
            viewHolder.app.setText(model.getApp());
            viewHolder.linearLayout.setBackgroundColor(Color.parseColor(model.getColor()));
            return convertView;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

    }

    class ViewHolder {
        TextView type;
        TextView pend;
        TextView inprog;
        TextView app;
        LinearLayout linearLayout;
    }
}
