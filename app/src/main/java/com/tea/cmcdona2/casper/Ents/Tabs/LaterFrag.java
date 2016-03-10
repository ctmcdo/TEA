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
import com.tea.cmcdona2.casper.Ents.EntsAdapter;
import com.tea.cmcdona2.casper.ParticularEnt.ParticularEntActivity;
import com.tea.cmcdona2.casper.R;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Paddy on 03/12/2015.
 */

public class LaterFrag extends android.support.v4.app.Fragment {

    public static LaterFrag newInstance(String text) {
        LaterFrag fragment = new LaterFrag();
        Bundle args = new Bundle();
        args.putString("message", text);
        return fragment;
    }

    public LaterFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.ents_activity, container, false);
        EntsAdapter adapter;
        ListView listView;
        listView = (ListView) v.findViewById(R.id.list_view);
        listView.setEmptyView(v.findViewById(R.id.empty_list_item));
        adapter = new EntsAdapter(this.getContext(), R.layout.ent_item);

        listView.setAdapter(adapter);

        String loadedID;
        final SharedPreferences appPrefs = this.getActivity().getSharedPreferences("appPrefs", 0);
        final SharedPreferences.Editor appPrefsEditor = appPrefs.edit();
        loadedID = appPrefs.getString("IDs", "null");
        String[] stringIDs = loadedID.split(",");
        int numOfEventsPassed = stringIDs.length;
        String[] societyName = new String[numOfEventsPassed];
        String[] eventName = new String[numOfEventsPassed];
        String[] imageTemp = new String[numOfEventsPassed];
        String[] eventTimes = new String[numOfEventsPassed];
        String[] eventDisplayDates = new String[numOfEventsPassed];
        String[] eventDisplayTimes = new String[numOfEventsPassed];
        String[] startTimes = new String[numOfEventsPassed];
        String[] endTimes = new String[numOfEventsPassed];
        String[] splitEventIDsAndTimes;
        String eventIDsAndTimes;
        String additive;
        final int[] EventId = new int[numOfEventsPassed];



        int counter = 0;

        for (int i = 0; i < numOfEventsPassed; i++) {

            additive = stringIDs[i];
            eventIDsAndTimes = appPrefs.getString("eventIDsAndTimes" + additive, "");
            splitEventIDsAndTimes = eventIDsAndTimes.split(" ");

            //the interval is exclusive, so not including tomorrow (1)
            DateTime fiveDays = DateTime.now().plusDays(5);
            DateTime tomorrow = DateTime.now().plusDays(1);
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
            if(eventIDsAndTimes != "") {
                DateTime particularDate = fmt.parseDateTime(splitEventIDsAndTimes[0].trim());


                if (new Interval(tomorrow, fiveDays).contains(particularDate)) {
                    societyName[counter] = appPrefs.getString("societyName" + additive, "");
                    eventName[counter] = appPrefs.getString("eventName" + additive, "");
                    imageTemp[counter] = appPrefs.getString("imageTemp" + additive, "");
                    eventTimes[counter] = eventIDsAndTimes;
                    eventDisplayDates[counter] = eventTimes[counter].split("-")[2].split(" ")[0] + "/" + eventTimes[counter].split("-")[1].trim();
                    Log.v("DisplayDates", ""+eventDisplayDates[counter]);
                    eventDisplayTimes[counter] = eventTimes[counter].split(" ")[1];
                    startTimes[counter] = eventDisplayTimes[counter].split("-")[0].trim();
                    endTimes[counter] = eventDisplayTimes[counter].split("-")[1].trim();
                    if(startTimes[counter].equals(endTimes[counter]))
                        eventDisplayTimes[counter] = eventDisplayTimes[counter].split("-")[0];
                    Log.v("displayTime", "" + eventDisplayTimes[counter]);
                    EventId[counter] = Integer.parseInt(stringIDs[i]);
                    counter++;
                }
            }
        }

        appPrefsEditor.putInt("swipeCount", counter).commit();

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < counter; i++){
            sb.append(Integer.toString(EventId[i])).append(',');
        }

        appPrefsEditor.putString("swipeEventId", sb.toString()).commit();


        for (int i = 0; i < counter; i++) {
            byte[] eventsData;
            Bitmap bm;

            eventsData = Base64.decode(imageTemp[i], Base64.DEFAULT);
            bm = BitmapFactory.decodeByteArray(eventsData, 0, eventsData.length);

            EntItem dataProvider = new EntItem(bm, eventName[i], eventDisplayDates[i] + '\n' + eventDisplayTimes[i]);
            adapter.add(dataProvider);
        }



        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long ld) {
                        String pos = Integer.toString(position);
                        appPrefsEditor.putInt("swipePosition", position).commit();
                        String Event = String.valueOf(parent.getItemAtPosition(position));
                        Intent intent = new Intent(getActivity(), ParticularEntActivity.class);
                        intent.putExtra("Event", Event);
                        intent.putExtra("ID", EventId[position]);
                        startActivity(intent);
                    }
                }
        );

        return v;
    }

}
