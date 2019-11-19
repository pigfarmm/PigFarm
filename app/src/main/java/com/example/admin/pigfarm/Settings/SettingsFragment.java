package com.example.admin.pigfarm.Settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.pigfarm.Home;
import com.example.admin.pigfarm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SettingsFragment extends PreferenceFragment {

    SharedPreferences sp;
    String member_id, farm_id, farm_name, url2,getfarm_id,unit_id,unit_name;
    private String[] convert_to_string, convert_farmid;
    ArrayList<String> stringList;
    ArrayList<String> stringIDFarm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_settings, false);
        addPreferencesFromResource(R.xml.pref_settings);

//        onPreferenceValueChanged();

        SharedPreferences shared = this.getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        member_id = shared.getString("member_id", "");

        SharedPreferences farm = this.getActivity().getSharedPreferences("Farm", Context.MODE_PRIVATE);
        getfarm_id = farm.getString("farm_id", "");

        url2 = "https://pigaboo.xyz/select_unit.php?farm_id=" + getfarm_id;
        StringRequest stringRequest2 = new StringRequest(url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showRadioButtonDialog(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "เชื่อมต่อ Server ไม่ได้ โปรดลองอีกครั้ง", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue2 = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue2.add(stringRequest2);


        Preference add_farm = (Preference) findPreference("add_unit");
        add_farm.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), AddUnit.class);
                startActivity(intent);
                return true;
            }
        });

//       EditFarm();
        EditUnit();
    }



//    private void onPreferenceValueChanged() {
//        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
//
//        pref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
//            @Override
//            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
//                String unit = sharedPreferences.getString("unit_settings", "");
//
//                Preference preference = findPreference("unit_settings");
//                preference.setSummary(unit);
//            }
//        });
//    }

    private void showRadioButtonDialog(String response) {
        stringList = new ArrayList<>();
        stringIDFarm = new ArrayList<>();

        final ListPreference select_farm = (ListPreference) findPreference("select_unit");

        try {
            JSONObject jsonObject2 = new JSONObject(response);
            JSONArray result2 = jsonObject2.getJSONArray("result");


            for (int i = 0; i < result2.length(); i++) {
                JSONObject collectData2 = result2.getJSONObject(i);
                unit_id = collectData2.getString("unit_id");
                unit_name = collectData2.getString("unit_name");

                stringList.add(unit_name);
                stringIDFarm.add(unit_id);

                convert_farmid = new String[stringIDFarm.size()];
                convert_farmid = stringIDFarm.toArray(convert_farmid);


                select_farm.setEntries(stringList.toArray(new String[stringList.size()]));
                select_farm.setEntryValues(stringList.toArray(new String[stringList.size()]));


//                delete_farm.setEntries(stringList.toArray(new String[stringList.size()]));
//                delete_farm.setEntryValues(stringList.toArray(new String[stringList.size()]));

            }


            select_farm.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String listValue = (String) newValue;
                    int index = select_farm.findIndexOfValue(listValue);

                    SharedPreferences farm = getActivity().getSharedPreferences("Farm", 0);
                    SharedPreferences.Editor editor2 = farm.edit();
                    editor2.putString("unit_id", convert_farmid[index]);
                    editor2.putString("unit_name", listValue);
                    editor2.apply();
                    editor2.commit();

                    getActivity().finish();
                    Intent i = new Intent(getActivity(), Home.class);
                    startActivity(i);
                    return true;

                }
            });


        } catch (JSONException ex) {
            ex.printStackTrace();
        }


    }

    private void EditFarm() {
        Preference pref_editfarm = findPreference("editdel_farm");
        pref_editfarm.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(getResources().getDrawable(R.drawable.deleteicon));
                builder.setTitle("เลือกฟาร์มที่ต้องการ");
                builder.setItems(stringList.toArray(new String[stringList.size()]), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent i = new Intent(getActivity(),EditFarm.class);
                        i.putExtra("farm_id",convert_farmid[item]);
                        startActivity(i);
                        getActivity().finish();

//                        Toast.makeText(getActivity(), "position"+convert_farmid[item], Toast.LENGTH_SHORT).show();
//                        new AlertDialog.Builder(getActivity())
//                                .setTitle("ยืนยันการลบ")
//                                .setMessage("ต้องการที่จะลบฟาร์มนี้ใช่หรือไม่ ?")
//                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // Continue with delete operation
//                                    }
//                                })
//
//                                // A null listener allows the button to dismiss the dialog and take no further action.
//                                .setNegativeButton(android.R.string.no, null)
//                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                .show();
                    }
                });
                builder.show();
                return false;
            }
        });
    }



    private void EditUnit() {
        Preference pref_editfarm = findPreference("editdel_unit");
        pref_editfarm.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(getResources().getDrawable(R.drawable.deleteicon));
                builder.setTitle("เลือกยูนิตที่ต้องการ");
                builder.setItems(stringList.toArray(new String[stringList.size()]), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent i = new Intent(getActivity(),EditUnit.class);
                        i.putExtra("unit_id",convert_farmid[item]);
                        startActivity(i);
                        getActivity().finish();

//                        Toast.makeText(getActivity(), "position"+convert_farmid[item], Toast.LENGTH_SHORT).show();
//                        new AlertDialog.Builder(getActivity())
//                                .setTitle("ยืนยันการลบ")
//                                .setMessage("ต้องการที่จะลบฟาร์มนี้ใช่หรือไม่ ?")
//                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // Continue with delete operation
//                                    }
//                                })
//
//                                // A null listener allows the button to dismiss the dialog and take no further action.
//                                .setNegativeButton(android.R.string.no, null)
//                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                .show();
                    }
                });
                builder.show();
                return false;
            }
        });
    }



}
