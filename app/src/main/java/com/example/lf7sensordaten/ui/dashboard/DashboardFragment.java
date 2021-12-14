package com.example.lf7sensordaten.ui.dashboard;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.VolleyError;
import com.example.lf7sensordaten.API;
import com.example.lf7sensordaten.R;
import com.example.lf7sensordaten.VolleyCallback;
import com.example.lf7sensordaten.ui.DatePickerFragment;
import com.google.android.material.timepicker.TimeFormat;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    private interface CallbackDate{
        void getDateCallback(String date);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        final Button refreshBtn = (Button) root.findViewById(R.id.reloadBtn);

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw_graphs(root);
            }
        });

        draw_graphs(root);


        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void draw_graphs(View root){
        //Get Start Date
        getDate("Start", new CallbackDate() {
            @Override


            public void getDateCallback(String date) {
                String start = date;
                //Get end Date
                getDate("Ende", new CallbackDate() {
                    @Override
                    public void getDateCallback(String date) {
                        String ende = date;
                        API api = new API();
                        api.get_data(root.getContext(), new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject result) throws JSONException {
                                Log.v("FINDME",result.toString());

                                try {
                                    LineGraphSeries(root, result);
                                    final TextView dateText = (TextView) root.findViewById(R.id.dateText);
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");
                                    dateText.setText(sdf.format(parseDate(start))+" - "+sdf.format(parseDate(ende)));
                                }catch(Exception err){

                                }

                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.v("FINDME",error.toString());
                            }
                        },start,ende);
                    }
                });

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getDate(String name, CallbackDate dateCallback){


        //Get Start Date
        final DatePickerFragment startDatePicker = new DatePickerFragment(name +" Date");




        startDatePicker.listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                getTime(name,year+"-"+convertTwoDigits(month + 1)+"-"+convertTwoDigits(dayOfMonth),dateCallback);
            }
        };

        startDatePicker.show(super.getChildFragmentManager(),"asdf");





    }
    private void getTime(String name, String date, CallbackDate datecallback)
    {
        //Get Start Time

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        datecallback.getDateCallback(date+"T"+convertTwoDigits(hourOfDay)+":"+convertTwoDigits(minute)+":00.000000");

                    }
                }, mHour, mMinute, true);
        timePickerDialog.setMessage(name+" Time");
        timePickerDialog.show();
    }

    private String convertTwoDigits(int i)
    {
        String out = String.valueOf(i);
        if(i < 10) out = "0"+String.valueOf(i);

        return out;
    }

    private void LineGraphSeries(View root, JSONObject json) throws JSONException {



        //First Temp
        GraphView graph = (GraphView) root.findViewById(R.id.graph_temp);
        graph.removeAllSeries();

        LineGraphSeries<DataPoint> temp = new LineGraphSeries<>(new DataPoint[]{});
        // styling series
        temp.setTitle("Temp C°");
        temp.setColor(Color.RED);
        //temp.setDrawDataPoints(true);
        //temp.setDataPointsRadius(10);
        //temp.setThickness(8);

        JSONArray data = json.getJSONArray("data");
      ;
        for(int i = 0; i < data.length(); i++){
            JSONObject current = data.getJSONObject(i);
            Date date = parseDate(current.getString("_id"));
            double tempV = current.getDouble("temp");

            temp.appendData(new DataPoint(i,(int) tempV), true, 100000);
        }


    
        

        graph.addSeries(temp);


        GraphView graph_co2 = (GraphView) root.findViewById(R.id.graph_co2);
        graph_co2.removeAllSeries();


        LineGraphSeries<DataPoint> co2 = new LineGraphSeries<>(new DataPoint[]{});

        co2.setColor(Color.BLUE);


        for(int i = 0; i < data.length(); i++){
            JSONObject current = data.getJSONObject(i);

            double tempV = current.getDouble("co2");

            co2.appendData(new DataPoint(i,(int) tempV), true, 100000);
        }





        graph_co2.addSeries(co2);









    }


    private Date parseDate(String s){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = format.parse(s.substring(0,19));
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
       return new Date();
    }
}