package com.example.admin.pigfarm.ManageData_Page;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.pigfarm.R;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.chrono.ThaiBuddhistDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Fragment_datapig extends Fragment {

    private TextView text_recorddate,text_preglist, text_bd, text_breed, text_bredder, text_bredder1, text_from, text_reserveid,texttitle_preglist, text_pigid;
    String getpigno, getpigid, pig_no, pig_id, pig_preglist, pig_recorddate, pig_birthday, pig_breed, pig_idbreeder, pig_idbreeder2, pig_from, pig_idreserve,pig_amount_pregnant,
            event_name,event_recorddate,bcs_score,getfarm_id,getunit_id;
    List<Event_items> itemsList;
    RecyclerView recyclerView;
    ImageView editprofile,editevent;
    Event_Adapter adapter;


    public Fragment_datapig() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_datapig, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences farm = this.getActivity().getSharedPreferences("Farm", Context.MODE_PRIVATE);
        getfarm_id = farm.getString("farm_id","");
        getunit_id = farm.getString("unit_id","");

        Bundle bundle2 = getArguments();
        getpigid = bundle2.getString("pig_id");
        getpigno = bundle2.getString("pig_no");

        text_pigid = getView().findViewById(R.id.text_pigid);
        text_recorddate = getView().findViewById(R.id.text_recorddate);
        text_bd = getView().findViewById(R.id.text_bd);
        text_breed = getView().findViewById(R.id.text_breed);
        text_bredder = getView().findViewById(R.id.text_breeder);
        text_bredder1 = getView().findViewById(R.id.text_breeder1);
        text_from = getView().findViewById(R.id.text_from);
        text_reserveid = getView().findViewById(R.id.text_reserveid);
        text_preglist = getView().findViewById(R.id.text_preglist);
        texttitle_preglist = getView().findViewById(R.id.texttitle_preglist);
        editprofile = getView().findViewById(R.id.editprofile);
        editevent = getView().findViewById(R.id.editevent);

        recyclerView = getView().findViewById(R.id.recyclerview2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemsList = new ArrayList<>();

            loadDatapigs();

            editevent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DelEvent_Fragment delEvent_fragment = new DelEvent_Fragment();
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("pig_id",getpigid);
                    bundle3.putString("pig_no",getpigno);
                    delEvent_fragment.setArguments(bundle3);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                    ft.replace(R.id.container, delEvent_fragment);
                    ft.addToBackStack(null);
                    ft.commit();


                }
            });

            editprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Edit_ProfilePig edit_profilePig = new Edit_ProfilePig();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("pig_id",getpigid);
                    bundle2.putString("pig_no",getpigno);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                    edit_profilePig.setArguments(bundle2);
                    ft.replace(R.id.container, edit_profilePig);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

    }

    private void loadDatapigs(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "https://pigaboo.xyz/Query_DataPig.php?pig_id="+getpigid+"&farm_id="+getfarm_id+"&unit_id="+getunit_id;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                showJSON(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "ไม่สามารถดึงข้อมูลได้ โปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext().getApplicationContext());
        requestQueue.add(stringRequest);


    }
    public void showJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i<result.length(); i++){
                JSONObject collectData = result.getJSONObject(i);

                pig_no = collectData.getString("pig_no");
                pig_id = collectData.getString("pig_id");
                pig_preglist = collectData.getString("pig_preglist");
                pig_recorddate = collectData.getString("pig_recorddate");
                pig_birthday = collectData.getString("pig_birthday");
                pig_breed = collectData.getString("pig_breed");
                pig_idbreeder = collectData.getString("pig_idbreeder");
                pig_idbreeder2 = collectData.getString("pig_idbreeder2");
                pig_idreserve = collectData.getString("pig_idreserve");
                pig_from = collectData.getString("pig_from");
                event_name = collectData.getString("event_name");
                event_recorddate = collectData.getString("event_recorddate");
                pig_amount_pregnant = collectData.getString("pig_amount_pregnant");
                bcs_score = collectData.getString("bcs_score");

                Log.d("pig_id",pig_id);

                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat outputFormat = new SimpleDateFormat("d MMM yyyy", new Locale("th", "TH"));



                Date date_open = inputFormat.parse(pig_recorddate);
                String outputDateStr = outputFormat.format(date_open);

                Date date_bd = inputFormat.parse(pig_birthday);
                String outputDateStr2 = outputFormat.format(date_bd);


                text_pigid.setText(pig_id);
                text_recorddate.setText(outputDateStr);
                text_bd.setText(outputDateStr2);
                text_breed.setText(pig_breed);
                text_bredder.setText(pig_idbreeder);
                text_bredder1.setText(pig_idbreeder2);
                text_reserveid.setText(pig_idreserve);
                text_from.setText(pig_from);
                text_preglist.setText(pig_amount_pregnant);

                if(event_name.equals("null") && event_recorddate.equals("null")) {
                    Log.d("CHECK EVENT " , " "+event_name+"  "+event_recorddate);
                }else {

                    Date date_recorddate = inputFormat.parse(event_recorddate);
                    String outputDateStr3 = outputFormat.format(date_recorddate);

                    Event_items event_items = new Event_items(event_name, outputDateStr3,bcs_score);
                    itemsList.add(event_items);

                }
            }

            adapter = new Event_Adapter(getActivity(),itemsList);
            recyclerView.setAdapter(adapter);

            if (adapter.getItemCount() == 0 ){
                editevent.setVisibility(View.INVISIBLE);
            }

        }catch (JSONException ex) {
            ex.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
