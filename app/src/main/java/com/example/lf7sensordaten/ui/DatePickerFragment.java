package com.example.lf7sensordaten.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;



import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
       {
    public String name;
    public int month;
    public int day;

    public DatePickerDialog.OnDateSetListener listener;

    public DatePickerFragment(String name){
        this.name = name;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), listener, year, month, day);
        dialog.setMessage(this.name);

        // Create a new instance of DatePickerDialog and return it
        return dialog;
    }

    public void getData(DatePickerDialog.OnDateSetListener listener)
    {

    }

}
