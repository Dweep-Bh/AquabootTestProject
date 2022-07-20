package com.example.medibuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.medibuddy.util.Appdata;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SigninActivity extends AppCompatActivity {
    TextInputLayout login_username_layout, login_password_layout;
    Button login_button, login_register_button;
    String url = "http://14.99.214.220/drbookingapp/bookingapp.asmx/UserLogin";

    ProgressDialog mLoginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mLoginProgress = new ProgressDialog(this);
     //   final SharedPreferences sharedPreferences=getSharedPreferences("Data", Context.MODE_PRIVATE);


        login_username_layout = findViewById(R.id.login_username_layout);
        login_password_layout = findViewById(R.id.login_password_layout);
        login_button = findViewById(R.id.login_button);
        login_register_button = findViewById(R.id.login_register_button);


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String username = login_username_layout.getEditText().getText().toString();
                String password = login_password_layout.getEditText().getText().toString();

                SharedPreferences sf= getSharedPreferences("user",  MODE_PRIVATE);
                SharedPreferences.Editor editor= sf.edit();
                editor.putString("uname",username);
                editor.putString("pwd",password);
                editor.commit();

                if (username.length() == 0 && password.length() == 0) {
                    Toast.makeText(SigninActivity.this, "Please enter valid username and password", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (username.length() > 0) {
                        if (password.length() > 0) {
                            loginData(url,username,password);

                        } else {
                            Toast.makeText(SigninActivity.this, "Please enter valid password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SigninActivity.this, "Please enter valid user name", Toast.LENGTH_SHORT).show();
                    }


                }


            }

        });


        login_register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent registerpatient = new Intent(SigninActivity.this, RegisterPatientActivity.class);
                startActivity(registerpatient);

            }

        });
    }

    private void loginData(String url, String username, String password) {
        StringRequest str = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    JSONObject jr=root.getJSONObject("UserDetails");
                    String suc = root.getString("Sucess");

                    if(suc.equalsIgnoreCase("1")){
                        String userid=jr.getString("userid");
                        Appdata.userid=userid;
                        Toast.makeText(SigninActivity.this, "Login sucessfull", Toast.LENGTH_SHORT).show();


                        Intent obj = new Intent(SigninActivity.this, DashboardActivity.class);
                        startActivity(obj);
                    }
                    else{
                        Toast.makeText(SigninActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map1 = new HashMap<>();
                map1.put("username",username);
                map1.put("pwd",password);

                return map1;
            }
        };
        Volley.newRequestQueue(SigninActivity.this).add(str);
    }



}
