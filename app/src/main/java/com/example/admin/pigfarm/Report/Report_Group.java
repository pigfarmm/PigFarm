package com.example.admin.pigfarm.Report;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.pigfarm.R;
import com.example.admin.pigfarm.Home;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Report_Group extends AppCompatActivity {

    private String farm_id,pdf;
    Spinner spin_type_of_report;
    Button btn_submit_report;
    EditText edit_B2;
    Calendar StartDate = Calendar.getInstance();
    Calendar EndDate = Calendar.getInstance();
    Spinner spnlengthtime;
    String getfarm_name,getunit_name,getfarm_id,m,d,m2,d2;
    TextView txt_farm, txt_unit;
    ImageView img_back;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_group);

        img_back = findViewById(R.id.img_back);
        txt_farm = findViewById(R.id.txt_farm);
        txt_unit = findViewById(R.id.txt_unit);
        edit_B2 = findViewById(R.id.edit_B2);
        spnlengthtime = findViewById(R.id.spnlengthtime);


        SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
        getfarm_name = farm.getString("farm_name", "");
        getunit_name = farm.getString("unit_name", "");
        getfarm_id = farm.getString("farm_id","");
        txt_farm.setText(getfarm_name);
        txt_unit.setText(getunit_name);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Report_Group.this, Report_Home.class);
                startActivity(intent);
                finish();
            }
        });


        spin_type_of_report = findViewById(R.id.spin_type_of_report);
        btn_submit_report = findViewById(R.id.btn_submit_report);



        final String[] eventStr = getResources().getStringArray(R.array.type_of_report);
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, eventStr);
        adapterType.setDropDownViewResource(R.layout.spinner_item);
        spin_type_of_report.setAdapter(adapterType);


        final String[] eventStr2 = getResources().getStringArray(R.array.length_time_allperformance);
        ArrayAdapter<String> adapterType2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, eventStr2);
        adapterType.setDropDownViewResource(R.layout.spinner_item);
        spnlengthtime.setAdapter(adapterType2);


        btn_submit_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        if (String.valueOf(spin_type_of_report.getSelectedItem()).equals("ทั้งหมด")){

                            pdf = "https://pigaboo.xyz/Report/All_Report.php";
                            Intent intent = new Intent(Report_Group.this, PigData_Report.class);
                            intent.putExtra("url",pdf);
                            intent.putExtra("ip_number",edit_B2.getText().toString());
                            intent.putExtra("ip_type",spnlengthtime.getSelectedItem().toString());
                            startActivity(intent);
                        }

                        else if (String.valueOf(spin_type_of_report.getSelectedItem()).equals("หมูสาวยังไม่ได้ผสม")){

                            pdf = "https://pigaboo.xyz/Report/Momnotbreed_Report.php";
                            Intent intent = new Intent(Report_Group.this, PigData_Report.class);
                            intent.putExtra("url",pdf);
                            intent.putExtra("ip_number",edit_B2.getText().toString());
                            intent.putExtra("ip_type",spnlengthtime.getSelectedItem().toString());
                            startActivity(intent);
                            }


                        else if (String.valueOf(spin_type_of_report.getSelectedItem()).equals("ผสม")){

                            pdf = "https://pigaboo.xyz/Report/Mombreed_Report.php";
                            Intent intent = new Intent(Report_Group.this, PigData_Report.class);
                            intent.putExtra("url",pdf);
                            intent.putExtra("ip_number",edit_B2.getText().toString());
                            intent.putExtra("ip_type",spnlengthtime.getSelectedItem().toString());
                            startActivity(intent);

                        }

                        else if (String.valueOf(spin_type_of_report.getSelectedItem()).equals("ไม่ท้อง")){

                            pdf = "https://pigaboo.xyz/Report/NoPregnant_Report.php";
                            Intent intent = new Intent(Report_Group.this, PigData_Report.class);
                            intent.putExtra("url",pdf);
                            intent.putExtra("ip_number",edit_B2.getText().toString());
                            intent.putExtra("ip_type",spnlengthtime.getSelectedItem().toString());
                            startActivity(intent);

                        }

                        else if (String.valueOf(spin_type_of_report.getSelectedItem()).equals("กำหนดเข้าคลอด")){

                            pdf = "https://pigaboo.xyz/Report/DueDate_Pregnant_Report.php";
                            Intent intent = new Intent(Report_Group.this, PigData_Report.class);
                            intent.putExtra("url",pdf);
                            intent.putExtra("ip_number",edit_B2.getText().toString());
                            intent.putExtra("ip_type",spnlengthtime.getSelectedItem().toString());
                            startActivity(intent);

                        }

//                        else if (String.valueOf(spin_type_of_report.getSelectedItem()).equals("ต้องทำก่อนเข้าคลอด")){
//
//                        }

                        else if (String.valueOf(spin_type_of_report.getSelectedItem()).equals("คลอด")){

                            pdf = "https://pigaboo.xyz/Report/Maternity_but_notwean_Report.php";
                            Intent intent = new Intent(Report_Group.this, PigData_Report.class);
                            intent.putExtra("url",pdf);
                            intent.putExtra("ip_number",edit_B2.getText().toString());
                            intent.putExtra("ip_type",spnlengthtime.getSelectedItem().toString());
                            startActivity(intent);
                        }

                        else if (String.valueOf(spin_type_of_report.getSelectedItem()).equals("หย่านม")){

                            pdf = "https://pigaboo.xyz/Report/Wean_not_Breed_Report.php";
                            Intent intent = new Intent(Report_Group.this, PigData_Report.class);
                            intent.putExtra("url",pdf);
                            intent.putExtra("ip_number",edit_B2.getText().toString());
                            intent.putExtra("ip_type",spnlengthtime.getSelectedItem().toString());
                            startActivity(intent);
                        }

//                        else if (String.valueOf(spin_type_of_report.getSelectedItem()).equals("แม่ต้องทำวัคซีน")){
//
//                        }
                    }

        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }




}