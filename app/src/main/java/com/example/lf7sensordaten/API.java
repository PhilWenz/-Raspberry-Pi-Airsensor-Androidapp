package com.example.lf7sensordaten;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lf7sensordaten.ui.home.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class API{
    
    protected String url = MainActivity.url;

    public void get_data(Context con,final VolleyCallback callback){



        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(con);
        String url_req = this.url+"?start_date=123&end_date=123&last=True";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url_req, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(error);
            }
        });


        queue.add(jsonObjReq);

    }

    public void get_data(Context con,final VolleyCallback callback, String start, String end){



        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(con);
        String url_req = this.url+"?start_date="+start+"&end_date="+end+"&last=false";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url_req, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(error);
            }
        });


        queue.add(jsonObjReq);

    }

    public void send_settings(Context con, final VolleyCallback callback, JSONObject payload){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(con);
        String url_req = this.url+"/windows";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PATCH,
                url_req, payload,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(error);
            }
        });


        queue.add(jsonObjReq);
    }

    public void get_settings(Context con, final VolleyCallback callback){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(con);
        String url_req = this.url+"/windows";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url_req,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(error);
            }
        });


        queue.add(jsonObjReq);
    }

    public void toggleWindows(Context con, final VolleyCallback callback){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(con);
        String url_req = this.url+"/windows/toggle";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PATCH,
                url_req,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(error);
            }
        });


        queue.add(jsonObjReq);
    }

}


