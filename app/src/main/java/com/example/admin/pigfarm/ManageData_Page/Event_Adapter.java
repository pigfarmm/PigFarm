package com.example.admin.pigfarm.ManageData_Page;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.pigfarm.R;

import java.util.List;


public class Event_Adapter extends RecyclerView.Adapter<Event_Adapter.EventViewHolder>{

    private Context Eventdata;
    private List<Event_items> itemsList;

    public Event_Adapter(Context Eventdata, List<Event_items> itemsList){
        this.Eventdata = Eventdata;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public Event_Adapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(Eventdata);
        View view = inflater.inflate(R.layout.listevent_content, null);
        return new Event_Adapter.EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Event_Adapter.EventViewHolder holder, int position) {
        Event_items event_items = itemsList.get(position);

        if (event_items.getEventname().equals("null") && event_items.getEvent_recorddate().equals("null")){
            holder.eventdata_name.setText("ไม่มีเกตุการณ์");
            holder.eventdata_recorddate.setText("");
        }else {

            holder.eventdata_name.setText(event_items.getEventname());
            holder.eventdata_recorddate.setText(event_items.getEvent_recorddate());
            holder.eventdata_bcs.setText("คะแนนหุ่น : "+event_items.getBcs_score());
        }


        Log.d("CHECK EVEVT"," "+event_items.getEventname()+"  "+event_items.getEvent_recorddate());

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder{
        TextView eventdata_name, eventdata_recorddate,eventdata_bcs;

    public EventViewHolder(View itemView){
        super(itemView);

        eventdata_name = itemView.findViewById(R.id.eventdata_name);
        eventdata_recorddate = itemView.findViewById(R.id.eventdata_recorddate);
        eventdata_bcs = itemView.findViewById(R.id.eventdata_bcs);

        if (eventdata_name != null && eventdata_recorddate != null && eventdata_bcs != null){
            eventdata_name.setVisibility(View.VISIBLE);
            eventdata_recorddate.setVisibility(View.VISIBLE);
            eventdata_bcs.setVisibility(View.VISIBLE);
        }else {
            eventdata_name.setVisibility(View.INVISIBLE);
            eventdata_recorddate.setVisibility(View.INVISIBLE);
            eventdata_bcs.setVisibility(View.INVISIBLE);
        }
    }
}
}
