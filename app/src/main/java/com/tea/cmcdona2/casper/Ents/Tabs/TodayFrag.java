package com.tea.cmcdona2.casper.Ents.Tabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import java.util.Date;

public class TodayFrag extends android.support.v4.app.Fragment {

    public static TodayFrag newInstance(String text) {
        TodayFrag fragment = new TodayFrag();
        Bundle args = new Bundle();
        args.putString("message", text);
        return fragment;
    }

    public TodayFrag() {
        // Required empty public constructor
    }
    int counter1=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View v = inflater.inflate(R.layout.ents_activity, container, false);
        EntsAdapter adapter;
        final ListView listView;
        listView = (ListView) v.findViewById(R.id.list_view);
        //listView.setEmptyView(v.findViewById(R.id.empty_list_item));
        adapter = new EntsAdapter(this.getContext(), R.layout.ent_item);

        listView.setAdapter(adapter);

        String activeIDs;
        final SharedPreferences appPrefs = this.getActivity().getSharedPreferences("appPrefs", 0);
        final SharedPreferences.Editor appPrefsEditor = appPrefs.edit();
        activeIDs = appPrefs.getString("IDs", "null");
        String[] splitActiveIDs = activeIDs.split(",");

        int numOfEventsPassed = splitActiveIDs.length;
        Log.v("numOfEventsPassed", "" + numOfEventsPassed);

        String[] societyName = new String[numOfEventsPassed];
        String[] eventName = new String[numOfEventsPassed];
        String[] imageTemp = new String[numOfEventsPassed];
        String[] eventTimings = new String[numOfEventsPassed];
        String[] eventDisplayTimes = new String[numOfEventsPassed];
        String[] startTimes = new String[numOfEventsPassed];
        String[] endTimes = new String[numOfEventsPassed];
        String[] splitEventIDsAndTimes;
        String eventIDsAndTimes;
        final int[] EventId = new int[numOfEventsPassed];
        String additive;
        String todayString;

        int counter = 0;

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
                eventTimings[counter] = eventIDsAndTimes;
                eventDisplayTimes[counter] = eventTimings[counter].split(" ")[1];
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

        //Make "No events to show" message appear if the tab has no events
        if(counter1 == 0)
            listView.setEmptyView(v.findViewById(R.id.empty_list_item));


        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < counter; i++){
            sb.append(Integer.toString(EventId[i])).append(',');
        }
        final String swipeEventId = sb.toString();



        Log.v("counter", "" + counter);

        for (int i = 0; i < counter; i++) {
            byte[] data;
            Bitmap bm;

            data = Base64.decode(imageTemp[i], Base64.DEFAULT);
            bm = BitmapFactory.decodeByteArray(data, 0, data.length);

            EntItem dataProvider = new EntItem(bm, eventName[i], eventDisplayTimes[i]);
            adapter.add(dataProvider);
        }




        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long ld) {

                        String Event = String.valueOf(parent.getItemAtPosition(position));
                        Intent intent = new Intent(getActivity(), ParticularEntActivity.class);
                        intent.putExtra("Event", Event);
                        intent.putExtra("swipeEventId", swipeEventId);
                        intent.putExtra("swipeCount", counter1);
                        intent.putExtra("swipePosition", position);
                        startActivity(intent);
                    }
                }
        );




        return v;

    }

}
