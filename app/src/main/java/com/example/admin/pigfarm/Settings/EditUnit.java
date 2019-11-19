package com.example.admin.pigfarm.Settings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.pigfarm.Home;
import com.example.admin.pigfarm.ManageData_Page.HttpParse;
import com.example.admin.pigfarm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditUnit extends AppCompatActivity {

    String member_id,farm_id,unit_id,unit_name,typeunit_id,url,getUnit_id,getUnit_name;
    EditText edit_unit;
    Spinner spin_typeunit;
    Button btn_saveunit,btn_delunit;
    ImageView img_back;
    ProgressDialog progressDialog,progressDialog2;
    HashMap<String,String> hashMap = new HashMap<>();
    private String finalResult;
    private HttpParse httpParse = new HttpParse();
    String UpdateURL = "https://pigaboo.xyz/Update_Unit.php";
    private String DeleteUnit = "https://pigaboo.xyz/Delete_Unit.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editunit);

        SharedPreferences shared = this.getSharedPreferences("Farm", Context.MODE_PRIVATE);
        getUnit_id = shared.getString("unit_id", "");
        getUnit_name = shared.getString("unit_name", "");


        if (savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if (extras == null){
                unit_id = null;
            }else{
                unit_id = extras.getString("unit_id");
            }
        }

        edit_unit = findViewById(R.id.edit_unit);
        spin_typeunit = findViewById(R.id.spin_typeunit);
        btn_saveunit = findViewById(R.id.btn_saveunit);
        img_back = findViewById(R.id.img_back);
        btn_delunit = findViewById(R.id.btn_delunit);

        final String[] eventStr = getResources().getStringArray(R.array.type_of_unit);
        ArrayAdapter<String> adapterEvent = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, eventStr);
        spin_typeunit.setAdapter(adapterEvent);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditUnit.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        url = "https://pigaboo.xyz/EditUnit.php?unit_id=" + unit_id;
        StringRequest stringRequest2 = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                QueryUnitDetail(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditUnit.this, "เชื่อมต่อ Server ไม่ได้ โปรดลองอีกครั้ง", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue2 = Volley.newRequestQueue(this.getApplicationContext());
        requestQueue2.add(stringRequest2);

    }

    private void QueryUnitDetail(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");


            for (int i = 0; i < result.length(); i++) {
                JSONObject collectData = result.getJSONObject(i);
                unit_name = collectData.getString("unit_name");

                edit_unit.setText(unit_name);

            }

            btn_saveunit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetDataFromEditText();
                    update_data(unit_id,unit_name,typeunit_id);
                }
            });

            btn_delunit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem();
                }
            });




        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }




    private void GetDataFromEditText() {
        unit_name = edit_unit.getText().toString();
        if (spin_typeunit.getSelectedItem().toString().equals("พ่อพันธุ์")){
            typeunit_id = "1";
        }else if (spin_typeunit.getSelectedItem().toString().equals("แม่พันธุ์")){
            typeunit_id = "2";
        }else if (spin_typeunit.getSelectedItem().toString().equals("โรงขาย")){
            typeunit_id = "3";
        }else if (spin_typeunit.getSelectedItem().toString().equals("โรงอาหาร")){
            typeunit_id = "4";
        }else if (spin_typeunit.getSelectedItem().toString().equals("สุกรขุน")){
            typeunit_id = "5";
        }else if (spin_typeunit.getSelectedItem().toString().equals("สุกรทดแทน")){
            typeunit_id = "6";
        }else if (spin_typeunit.getSelectedItem().toString().equals("อนุบาล")){
            typeunit_id = "7";
        }
    }

    private void update_data(final String unit_id, final String unit_name, final String typeunit_id) {
        class update_dataClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(EditUnit.this,"กำลังอัพเดตข้อมูล...",null,true,true);
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("unit_id",params[0]);
                hashMap.put("unit_name",params[1]);
                hashMap.put("typeunit_id",params[2]);

                finalResult = httpParse.postRequest(hashMap,UpdateURL);
                return finalResult;
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);

                if (getUnit_id.equals(unit_id)){
                    SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = farm.edit();
                    editor.putString("unit_id",unit_id);
                    editor.putString("unit_name",unit_name);
                    editor.apply();
                    editor.commit();
                }

                progressDialog.dismiss();
                Toast.makeText(EditUnit.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                EditUnit.this.finish();
                Intent i = new Intent(EditUnit.this, Home.class);
                startActivity(i);

            }
        }

        update_dataClass update_dataclass = new update_dataClass();
        update_dataclass.execute(unit_id,unit_name,typeunit_id);
    }

    private void removeItem() {
        if (getUnit_id.equals(unit_id)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
            builder.setCancelable(true);
            builder.setMessage("ไม่สามารถลบยูนิตที่กำลังใช้งานอยู่");
            builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
            builder.setCancelable(true);
            builder.setMessage("คุณต้องการลบยูนิตใช่หรือไม่");
            builder.setPositiveButton("ใช่",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UnitDelete(unit_id);
                        }
                    });
            builder.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void UnitDelete(String getUnit_id) {
        class UnitDeleteClass extends AsyncTask<String,Void,String>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog2 = ProgressDialog.show(EditUnit.this, "กำลังลบยูนิต...",null,true,true);
            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("unit_id",params[0]);

                finalResult = httpParse.postRequest(hashMap, DeleteUnit);

                return finalResult;
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog2.dismiss();
                Toast.makeText(EditUnit.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(EditUnit.this,SettingsActivity.class);
                startActivity(i);
                finish();
            }

        }
        UnitDeleteClass unitDeleteClass = new UnitDeleteClass();
        unitDeleteClass.execute(getUnit_id);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditUnit.this,SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
