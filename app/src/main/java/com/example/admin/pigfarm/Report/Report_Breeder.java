package com.example.admin.pigfarm.Report;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.admin.pigfarm.R;

public class Report_Breeder extends AppCompatActivity {

    private String farm_id,gettextspn,sendtextspn,pdf;
    String getfarm_name,getunit_name,getfarm_id,m,d;
    TextView txt_farm, txt_unit;
    EditText edit_B1,edit_B2,pregnant_edt3_1,pregnant_edt3_2,pregnant_edt4_1,pregnant_edt4_2,edit_B5;
    Button btn_A1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_b8);


    }
}
