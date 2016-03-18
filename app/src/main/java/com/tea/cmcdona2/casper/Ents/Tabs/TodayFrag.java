package com.tea.cmcdona2.casper.Ents.Tabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.tea.cmcdona2.casper.Ents.EntItem;
import com.tea.cmcdona2.casper.Ents.EntsActivity;
import com.tea.cmcdona2.casper.Ents.EntsAdapter;
import com.tea.cmcdona2.casper.ParticularEnt.ParticularEntActivity;
import com.tea.cmcdona2.casper.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodayFrag extends android.support.v4.app.Fragment {

    List Ent_Cards;

    public static TodayFrag newInstance(String text) {
        TodayFrag fragment = new TodayFrag();
        Bundle args = new Bundle();
        args.putString("message", text);
        return fragment;
    }

    public TodayFrag() {
        // Required empty public constructor
    }
    int counter1 = 0;

    String loadedID;
    String[] stringIDs;
    int numOfEventsPassed;
    String[] societyName;
    String[] eventName ;
    String[] imageTemp ;
    String[] eventTimes;
    String[] eventDisplayDates ;
    String[] eventDisplayTimes ;
    String[] startTimes ;
    String[] endTimes ;
    String[] splitEventIDsAndTimes;
    String eventIDsAndTimes;
    String additive;
    String todayString;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.card_ents_activity, container, false);

        RecyclerView rv = (RecyclerView)v.findViewById(R.id.rv);
        final SharedPreferences appPrefs = this.getActivity().getSharedPreferences("appPrefs", 0);
        final SharedPreferences.Editor appPrefsEditor = appPrefs.edit();
        loadedID = appPrefs.getString("IDs", "null");
        stringIDs = loadedID.split(",");
        numOfEventsPassed = stringIDs.length;
        societyName = new String[numOfEventsPassed];
        eventName = new String[numOfEventsPassed];
        imageTemp = new String[numOfEventsPassed];
        eventTimes = new String[numOfEventsPassed];
        eventDisplayDates = new String[numOfEventsPassed];
        eventDisplayTimes = new String[numOfEventsPassed];
        startTimes = new String[numOfEventsPassed];
        endTimes = new String[numOfEventsPassed];


        final int[] EventId = new int[numOfEventsPassed];

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        int counter = 0;
        String activeIDs;
        activeIDs = appPrefs.getString("IDs", "null");
        String[] splitActiveIDs = activeIDs.split(",");

        int numOfEventsPassed = splitActiveIDs.length;
        Log.v("numOfEventsPassed", "" + numOfEventsPassed);


        for (String stringID : splitActiveIDs) {

            additive = stringID;
            eventIDsAndTimes = appPrefs.getString("eventIDsAndTimes" + additive, "");
            splitEventIDsAndTimes = eventIDsAndTimes.split(" ");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            todayString = dateFormat.format(date);




            if (splitEventIDsAndTimes[0].trim().equals(todayString.trim())) {
                societyName[counter] = appPrefs.getString("societyName" + additive, "");
                eventName[counter] = appPrefs.getString("eventName" + additive, "");
                imageTemp[counter] = appPrefs.getString("imageTemp" + additive, "");
                eventTimes[counter] = eventIDsAndTimes;
                eventDisplayTimes[counter] = eventTimes[counter].split(" ")[1];
                startTimes[counter] = eventDisplayTimes[counter].split("-")[0].trim();
                endTimes[counter] = eventDisplayTimes[counter].split("-")[1].trim();
                if(startTimes[counter].equals(endTimes[counter]))
                    eventDisplayTimes[counter] = eventDisplayTimes[counter].split("-")[0];
                Log.v("displayTime", "" + eventDisplayTimes[counter]);

                EventId[counter] = Integer.parseInt(stringID);
                counter++;
            }
        }

        counter1= counter;
        appPrefsEditor.putInt("todaySize", counter1).commit();
        initializeData();

        RVAdapter RVadapter = new RVAdapter(Ent_Cards);
        rv.setAdapter(RVadapter);

        return v;

    }

    public void initializeData() {
        Ent_Cards = new ArrayList<>();

        for (int i = 0; i < counter1; i++) {
            byte[] eventsData;
            Bitmap bm;

            eventsData = Base64.decode(imageTemp[i], Base64.DEFAULT);
            bm = BitmapFactory.decodeByteArray(eventsData, 0, eventsData.length);
            Ent_Cards.add(new Ent_CardItem(eventName[i], todayString.split("-")[2].split(" ")[0] + "/" + todayString.split("-")[1].trim() + '\n' + eventDisplayTimes[i], bm));
        }

    }

}
