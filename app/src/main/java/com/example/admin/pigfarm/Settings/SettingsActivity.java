package com.example.admin.pigfarm.Settings;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.pigfarm.Home;
import com.example.admin.pigfarm.LoginActivity;
import com.example.admin.pigfarm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends PreferenceActivity {

    String member_id,farm_id,farm_name,url2;
    private String[] convert_to_string,convert_farmid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.pref_settings, false);
        setContentView(R.layout.settingsframelayout);


        FragmentManager manager = getFragmentManager();
        FragmentTransaction fragtrans = manager.beginTransaction();
        SettingsFragment settings = new SettingsFragment();
        fragtrans.replace(R.id.fragment_settings, settings);
        fragtrans.commit();

    }



}
