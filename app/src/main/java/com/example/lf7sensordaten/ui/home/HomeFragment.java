package com.example.lf7sensordaten.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.example.lf7sensordaten.API;
import com.example.lf7sensordaten.MainActivity;
import com.example.lf7sensordaten.R;
import com.example.lf7sensordaten.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final SwipeRefreshLayout refreshLayout = root.findViewById(R.id.swiperefresh);
        final API api = new API();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update_data(root,api);

            }
        });









        update_data(root,api);


        return root;
    }

    private void update_data(View root, API api)
    {

        final TextView textViewDate = root.findViewById(R.id.text_date);
        final TextView textViewCo2 = root.findViewById(R.id.text_co2);
        final TextView textViewTemp = root.findViewById(R.id.text_temp);
        final TextView textViewTvoc = root.findViewById(R.id.text_tvoc);
        final SwipeRefreshLayout refreshLayout = root.findViewById(R.id.swiperefresh);
        refreshLayout.setRefreshing(true);
        if(textViewDate.getText() == "") textViewDate.setText("Waiting for Server...");

        api.get_data(this.getContext(), new VolleyCallback(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                        .withZone(ZoneId.of("UTC"));
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss");
                LocalDateTime date = LocalDateTime.parse(result.getString("_id").substring(0,19), formatter);

                textViewDate.setText(date.format(dtf));
                textViewCo2.setText("CO²:    "+result.getString("co2"));
                textViewTemp.setText("Temperatur:   "+result.getString("temp")+" C°");
                textViewTvoc.setText("Tvoc: "+result.getString("tvoc"));
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                textViewDate.setText("Error :C");
                textViewCo2.setText(error.toString());
                textViewTemp.setText("");
                textViewTvoc.setText("");
                refreshLayout.setRefreshing(false);
            }
        });
    }


}