package com.example.cmcdona2.tea.Ents.Tabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.cmcdona2.tea.Ents.EntItem;
import com.example.cmcdona2.tea.Ents.EntsActivity;
import com.example.cmcdona2.tea.Ents.EntsAdapter;
import com.example.cmcdona2.tea.ParticularEnt.ParticularEntActivity;
import com.example.cmcdona2.tea.R;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TomorrowFrag extends android.support.v4.app.Fragment {

    public static TomorrowFrag newInstance(String text) {
        TomorrowFrag fragment = new TomorrowFrag();
        Bundle args = new Bundle();
        args.putString("message", text);
        return fragment;
    }

    public TomorrowFrag() {
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
        adapter = new EntsAdapter(this.getContext(), R.layout.ent_item);
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.activity_main_swipe_refresh_layout);
        listView.setAdapter(adapter);

        String loadedString;
        final SharedPreferences appPrefs = this.getActivity().getSharedPreferences("appPrefs", 0);
        final SharedPreferences.Editor appPrefsEditor = appPrefs.edit();
        loadedString = appPrefs.getString("IDs", "null");

        String[] stringIDs = loadedString.split(",");

        int numOfEventsPassed = stringIDs.length;

        String[] societyName = new String[numOfEventsPassed];
        String[] eventName = new String[numOfEventsPassed];
        String[] imageTemp = new String[numOfEventsPassed];
        String[] eventTimings = new String[numOfEventsPassed];
        String[] splitEventIDsAndTimes;
        String eventIDsAndTimes;
        String additive;
        final int[] EventId = new int[numOfEventsPassed];

        int counter = 0;

        for (int i = 0; i < numOfEventsPassed; i++) {

            additive = stringIDs[i];
            eventIDsAndTimes = appPrefs.getString("eventIDsAndTimes" + additive, "1");
            splitEventIDsAndTimes = eventIDsAndTimes.split(" ");

            //'day' now in the format yyyy-MM-dd

            DateTime tomorrow = DateTime.now().plusDays(1);
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
            String tomorrowString = fmt.print(tomorrow);

            if (splitEventIDsAndTimes[0].trim().equals(tomorrowString.trim())) {
                societyName[counter] = appPrefs.getString("societyName" + additive, "");
                eventName[counter] = appPrefs.getString("eventName" + additive, "");
                imageTemp[counter] = appPrefs.getString("imageTemp" + additive, "");
                eventTimings[counter] = eventIDsAndTimes;
                EventId[counter] = Integer.parseInt(stringIDs[i]);
                counter++;
            }
        }


        for (int i = 0; i < counter; i++) {
            byte[] data;
            Bitmap bm;

            data = Base64.decode(imageTemp[i], Base64.DEFAULT);
            bm = BitmapFactory.decodeByteArray(data, 0, data.length);

            EntItem dataProvider = new EntItem(bm, eventName[i], eventTimings[i]);
            adapter.add(dataProvider);
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                int position;
                position = appPrefs.getInt("position", 0);
                if (position == 0)
                    appPrefsEditor.putBoolean("allSocsFlag", true).commit();

                if (position == 1)
                    appPrefsEditor.putBoolean("allSocsFlag", false).commit();

                Intent intent = new Intent(TomorrowFrag.this.getContext(), EntsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("fromFrag", true);
                intent.putExtra("tabPosition", 1);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);

                getActivity().finish();

                mSwipeRefreshLayout.setRefreshing(false);

            }


        });

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long ld) {
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
