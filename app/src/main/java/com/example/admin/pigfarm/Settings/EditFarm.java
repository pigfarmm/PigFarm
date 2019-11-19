package com.example.admin.pigfarm.Settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.pigfarm.ManageData_Page.HttpParse;
import com.example.admin.pigfarm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditFarm extends AppCompatActivity {

    EditText edit_farm,edit_field,edit_addrnumber,edit_tumbon,edit_amphoe,edit_province,edit_code,edit_tel;
    Button btn_saveregis,btn_delfarm;
    ImageView img_back;
    String member_id,farm_id,url,farm_name,farm_field,farm_addrnumber,farm_tumbon,farm_amphoe,farm_province,farm_code,farm_tel;
    ProgressDialog progressDialog;
    private String finalResult;
    private HttpParse httpParse = new HttpParse();
    String UpdateURL = "https://pigaboo.xyz/Update_Farm.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editfarm);

        SharedPreferences shared = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        member_id = shared.getString("member_id", "");

        if (savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if (extras == null){
                farm_id = null;
            }else{
                farm_id = extras.getString("farm_id");
            }
        }

        edit_farm = findViewById(R.id.edit_farm);
        edit_field = findViewById(R.id.edit_field);
        edit_addrnumber = findViewById(R.id.edit_addrnumber);
        edit_tumbon = findViewById(R.id.edit_tumbon);
        edit_amphoe = findViewById(R.id.edit_amphoe);
        edit_province = findViewById(R.id.edit_province);
        edit_code = findViewById(R.id.edit_code);
        img_back = findViewById(R.id.img_back);
        edit_tel = findViewById(R.id.edit_tel);
        btn_saveregis = findViewById(R.id.btn_saveregis);
        btn_delfarm = findViewById(R.id.btn_delfarm);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditFarm.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        url = "https://pigaboo.xyz/EditFarm.php?farm_id=" + farm_id;
        StringRequest stringRequest2 = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                QueryFarmDetail(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditFarm.this, "เชื่อมต่อ Server ไม่ได้ โปรดลองอีกครั้ง", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue2 = Volley.newRequestQueue(this.getApplicationContext());
        requestQueue2.add(stringRequest2);




    }

    private void QueryFarmDetail(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");


            for (int i = 0; i < result.length(); i++) {
                JSONObject collectData = result.getJSONObject(i);
                farm_name = collectData.getString("farm_name");
                farm_field = collectData.getString("farm_field");
                farm_addrnumber = collectData.getString("farm_addrnumber");
                farm_tumbon = collectData.getString("farm_tumbon");
                farm_amphoe = collectData.getString("farm_amphoe");
                farm_province = collectData.getString("farm_province");
                farm_code = collectData.getString("farm_code");
                farm_tel = collectData.getString("farm_tel");

                edit_farm.setText(farm_name);
                edit_field.setText(farm_field);
                edit_addrnumber.setText(farm_addrnumber);
                edit_tumbon.setText(farm_tumbon);
                edit_amphoe.setText(farm_amphoe);
                edit_province.setText(farm_province);
                edit_code.setText(farm_code);
                edit_tel.setText(farm_tel);

            }

            btn_saveregis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetDataFromEditText();
                    update_data(farm_id,farm_name,farm_field,farm_addrnumber,farm_tumbon,farm_amphoe,farm_province,farm_code,farm_tel);
                }
            });




        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private void GetDataFromEditText(){
        farm_name = edit_farm.getText().toString();
        farm_field = edit_field.getText().toString();
        farm_addrnumber = edit_addrnumber.getText().toString();
        farm_tumbon = edit_tumbon.getText().toString();
        farm_amphoe = edit_amphoe.getText().toString();
        farm_province = edit_province.getText().toString();
        farm_code = edit_code.getText().toString();
        farm_tel = edit_tel.getText().toString();
    }

    private void update_data (String farm_id, String farm_name, String farm_field,String farm_addrnumber, String farm_tumbon, String farm_amphoe, String farm_province, String farm_code, String farm_tel){
        class update_dataClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(EditFarm.this,"กำลังอัพเดตข้อมูล...",null,true,true);
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("farm_id",params[0]);
                hashMap.put("farm_name",params[1]);
                hashMap.put("farm_field",params[2]);
                hashMap.put("farm_addrnumber",params[3]);
                hashMap.put("farm_tumbon",params[4]);
                hashMap.put("farm_amphoe",params[5]);
                hashMap.put("farm_province",params[6]);
                hashMap.put("farm_code",params[7]);
                hashMap.put("farm_tel",params[8]);

                finalResult = httpParse.postRequest(hashMap,UpdateURL);
                return finalResult;
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                Toast.makeText(EditFarm.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(EditFarm.this,SettingsActivity.class);
                startActivity(i);
                finish();

            }
        }

        update_dataClass update_dataclass = new update_dataClass();
        update_dataclass.execute(farm_id,farm_name,farm_field,farm_addrnumber,farm_tumbon,farm_amphoe,farm_province,farm_code,farm_tel);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditFarm.this,SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
