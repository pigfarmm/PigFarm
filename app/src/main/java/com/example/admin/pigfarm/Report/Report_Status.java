package com.example.admin.pigfarm.Report;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.pigfarm.R;
import com.example.admin.pigfarm.Home;

public class Report_Status extends AppCompatActivity {

    private String farm_id;
    String getfarm_name,getunit_name,getfarm_id;
    TextView txt_farm, txt_unit;
    ImageView img_back;
    CardView card_dadmom,card_NewMom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_status);

        img_back = findViewById(R.id.img_back);
        txt_farm = findViewById(R.id.txt_farm);
        txt_unit = findViewById(R.id.txt_unit);

        SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
        getfarm_name = farm.getString("farm_name", "");
        getunit_name = farm.getString("unit_name", "");
        getfarm_id = farm.getString("farm_id","");
        txt_farm.setText(getfarm_name);
        txt_unit.setText(getunit_name);
        card_dadmom = findViewById(R.id.card_dadmom);

        card_dadmom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Report_Status_a1_Fragment report_status_a1_fragment = new Report_Status_a1_Fragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left, R.anim.left_to_right, R.anim.right_to_left);
                transaction.replace(R.id.report_status_fragment, report_status_a1_fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Report_Status.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
