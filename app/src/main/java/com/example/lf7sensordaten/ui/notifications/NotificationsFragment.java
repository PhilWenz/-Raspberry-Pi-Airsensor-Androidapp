package com.example.lf7sensordaten.ui.notifications;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.lf7sensordaten.API;
import com.example.lf7sensordaten.R;
import com.example.lf7sensordaten.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);



        final SwipeRefreshLayout refreshLayout = root.findViewById(R.id.swiperefresh);

        final API api = new API();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_data(api, root);

            }
        });

        final TextView winStateText = root.findViewById(R.id.winstate_text);
        winStateText.setText("Waiting for Server...");

        get_data(api, root);

        final Button update = root.findViewById(R.id.update_btn);



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    send_update(api, root);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        final Button openWindow = root.findViewById(R.id.open_Window);
        openWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.toggleWindows(root.getContext(), new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) throws JSONException {
                        Toast.makeText(root.getContext(),"Updated!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(root.getContext(),"Error :X!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        return root;
    }

    public void get_data(API api, View root)
    {
        final SwipeRefreshLayout refreshLayout = root.findViewById(R.id.swiperefresh);
        refreshLayout.setRefreshing(true);
        api.get_settings(this.getContext(), new VolleyCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                final TextView winStateText = root.findViewById(R.id.winstate_text);
                if(result.getBoolean("state"))
                    winStateText.setText("Window is Open");
                else winStateText.setText("Window is Closed");
                final Button open_window =root.findViewById(R.id.open_Window);
                if(result.getBoolean("auto_toggle")) open_window.setVisibility(100);
                else open_window.setVisibility(0);
                if(result.getBoolean("state")) open_window.setText("Close Window!");
                else open_window.setText("Open Window!");


                final Switch tg_btn = root.findViewById(R.id.auto_toggle_btn);
                tg_btn.setChecked(result.getBoolean("auto_toggle"));

                final EditText temp = root.findViewById(R.id.temp_threshold_text);
                temp.setText(String.valueOf(result.getString("temp_threshold")));

                final EditText co2 = root.findViewById(R.id.co2_text);
                co2.setText(String.valueOf(result.getString("co2_threshold")));
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                final TextView winStateText = root.findViewById(R.id.winstate_text);
                winStateText.setText("Error :X");
                refreshLayout.setRefreshing(false);
            }
        });
    }

    public void send_update(API api, View root) throws JSONException {
        final SwipeRefreshLayout refreshLayout = root.findViewById(R.id.swiperefresh);
        refreshLayout.setRefreshing(true);

        JSONObject json = new JSONObject();

        final Switch tg_btn = root.findViewById(R.id.auto_toggle_btn);
        json.put("auto_toggle", tg_btn.isChecked());

        final EditText temp = root.findViewById(R.id.temp_threshold_text);
        json.put("temp_threshold", Double.valueOf(temp.getText().toString()));

        final EditText co2 = root.findViewById(R.id.co2_text);
        json.put("co2_threshold", Double.valueOf(co2.getText().toString()));


        api.send_settings(this.getContext(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                Toast t = Toast.makeText(root.getContext(),"Updated!",Toast.LENGTH_SHORT);
                t.show();
                refreshLayout.setRefreshing(false);

            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast t = Toast.makeText(root.getContext(),"Error :X!",Toast.LENGTH_SHORT);
                t.show();
                Log.v("FINDME",error.getMessage());
                refreshLayout.setRefreshing(false);
            }
        },json);
    }
}