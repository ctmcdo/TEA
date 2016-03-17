package com.tea.cmcdona2.casper.LogReg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tea.cmcdona2.casper.Other.Constants;
import com.tea.cmcdona2.casper.R;
import com.tea.cmcdona2.casper.Socs.SocsActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SocietyRegister extends AppCompatActivity implements View.OnClickListener {

    Button register;
    TextView login;
    EditText rSocName, etEmail, etPassword, etPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_society_register);

        Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"1", "2", "three"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        //rSocName = (EditText) findViewById(R.id.rName);
        etEmail = (EditText) findViewById(R.id.SocEmail);
        etPassword = (EditText) findViewById(R.id.SocPassword);
        etPassword2 = (EditText) findViewById(R.id.SocPassword2);

        register = (Button) findViewById(R.id.regbutton);
        login = (TextView) findViewById(R.id.logtext);


        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.regbutton:

                String email = etEmail.getText().toString();
                String password1 = etPassword.getText().toString();
                String password2 = etPassword2.getText().toString();


                if ( email.equals("") || password1.equals("")) {
                    Toast.makeText(this, "Incomplete User Details", Toast.LENGTH_SHORT).show();
                }

                if (!password1.equals(password2)) {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }

                //add code checking for unique email when databases are set-up

                if (!email.equals("") && !password1.equals("")) {
                    registerUser();

                }
                break;
            case R.id.logtext:
                Intent loginIntent = new Intent(this, LogIn.class);
                startActivity(loginIntent);

                break;
        }

    }

    private void registerUser(){
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String encryptedEmail = md5(email);
        final String encryptedPassword = md5(password);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("success")){

                            SharedPreferences appPrefs = SocietyRegister.this.getSharedPreferences("appPrefs", 0);
                            final SharedPreferences.Editor appPrefsEditor = appPrefs.edit();
                            boolean alreadyRegistered = true;
                            appPrefsEditor.putBoolean("alreadyRegistered", alreadyRegistered).commit();

                            Intent hIntent = new Intent(SocietyRegister.this, SocsActivity.class);
                            startActivity(hIntent);
                        }else{
                            Toast.makeText(SocietyRegister.this,response,Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SocietyRegister.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(Constants.KEY_EMAIL, encryptedEmail);
                params.put(Constants.KEY_PASSWORD,encryptedPassword);
                params.put(Constants.KEY_ACCTYPE, "soc");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
