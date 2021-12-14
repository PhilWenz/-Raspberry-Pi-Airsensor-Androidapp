package com.example.lf7sensordaten;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public interface VolleyCallback {
    void onSuccess(JSONObject result) throws JSONException;
    void onErrorResponse(VolleyError error);
}
