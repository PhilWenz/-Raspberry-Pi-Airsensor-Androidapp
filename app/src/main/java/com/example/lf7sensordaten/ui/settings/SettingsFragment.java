package com.example.lf7sensordaten.ui.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.lf7sensordaten.API;
import com.example.lf7sensordaten.MainActivity;
import com.example.lf7sensordaten.R;
import com.example.lf7sensordaten.VolleyCallback;
import com.example.lf7sensordaten.ui.notifications.NotificationsViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        final EditText urlT = root.findViewById(R.id.url_text);
        urlT.setText(MainActivity.url);

        final Button urlB = root.findViewById(R.id.setUrl_btn);
        urlB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.url = urlT.getText().toString();
            }
        });





        return root;
    }
}