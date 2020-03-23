package com.example.admin.pigfarm.ManageData_Page;

public class Event_items {

    public String eventname ="";
    public String event_recorddate= "";
    public String bcs_score= "";

    public Event_items(String eventname, String event_recorddate,String bcs_score){
        this.eventname = eventname;
        this.event_recorddate = event_recorddate;
        this.bcs_score = bcs_score;
    }

    public String getEventname() {
        return eventname;
    }

    public String getEvent_recorddate() {
        return event_recorddate;
    }

    public String getBcs_score(){
        return bcs_score;
    }

}
