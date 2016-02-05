package com.example.cmcdona2.tea.ParticularEnt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cmcdona2.tea.Other.Constants;
import com.example.cmcdona2.tea.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.GregorianCalendar;

public class ParticularEntActivity extends AppCompatActivity implements ParticularEntFrag.EventDescriptionListener {

    public Bitmap bm;
    public ProgressDialog loading;
    public String signal;
    public String EventDescription;
    public String EventName;
    public String Location;
    public String StartDate;
    public String EndDate;
    public int StartYear;
    public int StartMonth;
    public int StartDay;
    public int StartHour;
    public int StartMinute;
    public int EndYear;
    public int EndMonth;
    public int EndDay;
    public int EndHour;
    public int EndMinute;
    public String societyName;
    public Toolbar toolbar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        establishConnection(new ParticularEntActivity.VolleyCallback() {
            @Override
            public void handleData(String response) {

                JSONObject particularEntData;
                byte[] data;
                Bitmap bitmap;

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray(Constants.JSON_ARRAY);
                    String imageTemp;

                    particularEntData = result.getJSONObject(0);
                    imageTemp = particularEntData.getString(Constants.KEY_LOWRES);
                    EventName = particularEntData.getString(Constants.KEY_EVENTNAME);
                    EventDescription = particularEntData.getString(Constants.KEY_EVENTDESCRIPTION);
                    StartDate = particularEntData.getString(Constants.KEY_STARTDATE);
                    EndDate = particularEntData.getString(Constants.KEY_ENDDATE);
                    Location = particularEntData.getString(Constants.KEY_LOCATION);
                    societyName = particularEntData.getString(Constants.KEY_SOCIETYNAME);
                    data = Base64.decode(imageTemp, Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    bm = bitmap;
                    StartDay = Integer.parseInt(StartDate.substring(8, 10));
                    StartMonth = Integer.parseInt(StartDate.substring(5, 7));
                    StartYear = Integer.parseInt(StartDate.substring(0, 4));
                    StartHour = Integer.parseInt(StartDate.substring(11, 13));
                    StartMinute = Integer.parseInt(StartDate.substring(14, 16));
                    EndDay = Integer.parseInt(EndDate.substring(8, 10));
                    EndMonth = Integer.parseInt(EndDate.substring(5, 7));
                    EndYear = Integer.parseInt(EndDate.substring(0, 4));
                    EndHour = Integer.parseInt(EndDate.substring(11, 13));
                    EndMinute = Integer.parseInt(EndDate.substring(14, 16));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setContentView(R.layout.particular_ent_activity_frag);

//                toolbar = (Toolbar) findViewById(R.id.toolbar);
//                //Toolbar will now take on default Action Bar characteristics
//                setActionBar(toolbar);
//                //You can now use and reference the ActionBar

                //ActionBar actionBar;
                //actionBar.setIcon(R.drawable.app_icon);
                //actionBar.setDisplayUseLogoEnabled(true);
                //actionBar.setDisplayShowHomeEnabled(true);

                ImageView view = (ImageView) findViewById(R.id.myImgView);
                view.setImageBitmap(bm);
                TextView eventDescription = (TextView) findViewById(R.id.eventDescription);
                eventDescription.setText(EventDescription);
                TextView eventName = (TextView) findViewById(R.id.eventName);
                eventName.setText(EventName);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override

    public void GMAP() {

        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Location);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }

    }

    @Override
    public void Calendar() {
        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setType("vnd.android.cursor.item/event");
        calIntent.putExtra(CalendarContract.Events.TITLE, EventName + " by- " + societyName);
        calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, Location);
        calIntent.putExtra(CalendarContract.Events.DESCRIPTION, EventDescription);

        GregorianCalendar calstartDate = new GregorianCalendar(StartYear, StartMonth - 1, StartDay, StartHour, StartMinute);
        GregorianCalendar calendDate = new GregorianCalendar(EndYear, EndMonth - 1, EndDay, EndHour, EndMinute);
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calstartDate.getTimeInMillis());
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                calendDate.getTimeInMillis());

        startActivity(calIntent);
    }

    private String establishConnection(final VolleyCallback callback) {

        loading = ProgressDialog.show(this, "Please wait...", "Fetching data...", false, false);

        int eventID = getIntent().getIntExtra("ID", 0);
        String urlExtension = Integer.toString(eventID);
        String url = Constants.HIRES_URL + urlExtension;

        StringRequest stringRequest;
        stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                callback.handleData(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ParticularEntActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        return signal;
    }

    public interface VolleyCallback {
        void handleData(String response);
    }

}