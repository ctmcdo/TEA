package com.example.cmcdona2.tea.Splash;

import android.app.Activity;
import android.os.Bundle;

import com.example.cmcdona2.tea.R;

/**
 * Created by Paddy on 18/11/2015.
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1250);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally{
                    finish();
                }
            }
        };


/*
        String url = Constants.DATA_URL;

        StringRequest stringRequest;
        stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                
                finish();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SplashActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
*/
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
/*
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashActivity.this,socs_activity.class);
                    startActivity(intent);
                    finish();

                }
            }
        };
*/
        timerThread.start();
    }
}
