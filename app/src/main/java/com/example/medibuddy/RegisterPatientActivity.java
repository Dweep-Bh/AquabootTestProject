package com.example.medibuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterPatientActivity extends AppCompatActivity {
    TextInputLayout signup_fname_layout,signup_lname_layout,signup_username_layout,signup_pwd_layout,signup_address_layout,signup_phno_layout,signup_email_layout;
    Button login_signup_button,signup_button;
    String url = "http://14.99.214.220/drbookingapp/bookingapp.asmx/UserRegistration";
     ProgressDialog mRegProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient);


        mRegProgress = new ProgressDialog(this);

        signup_fname_layout=findViewById(R.id.signup_fname_layout);
        signup_lname_layout=findViewById(R.id.signup_lname_layout);
        signup_username_layout=findViewById(R.id.signup_username_layout);
        signup_pwd_layout=findViewById(R.id.signup_pwd_layout);
        signup_address_layout=findViewById(R.id.signup_address_layout);
        signup_phno_layout=findViewById(R.id.signup_phno_layout);
        signup_email_layout=findViewById(R.id.signup_email_layout);
        signup_button=findViewById(R.id.signup_button);
        login_signup_button=findViewById(R.id.login_signup_button);



        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fname= signup_fname_layout.getEditText().getText().toString();
                String lname= signup_lname_layout.getEditText().getText().toString();
                String uname= signup_username_layout.getEditText().getText().toString();
                String pwd= signup_pwd_layout.getEditText().getText().toString();
                String address=signup_address_layout.getEditText().getText().toString();
                String phno=signup_phno_layout.getEditText().getText().toString();
                String email=signup_email_layout.getEditText().getText().toString();

                if (fname.length()!=0 && lname.length()!=0 && uname.length()!=0 && pwd.length()!=0 && address.length()!=0 && phno.length()!=0 && email.length()!=0){
                    CreateAccount(url,fname,lname,uname,pwd,address,phno,email);
                }
                else{
                    Toast.makeText(RegisterPatientActivity.this, "Please fill all field", Toast.LENGTH_SHORT).show();
                }
            }
        });
      login_signup_button .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(RegisterPatientActivity.this,SigninActivity.class);
                startActivity(intent);
                Toast.makeText(RegisterPatientActivity.this, "Login Page", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void  CreateAccount(String url,String fname,String lname,String uname,String pass,String add,String phno,String email){
        StringRequest str = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    String suc = root.getString("Sucess");
                    if (suc.equalsIgnoreCase("1")){
                        Toast.makeText(RegisterPatientActivity.this, "Registration Sucessfull", Toast.LENGTH_SHORT).show();
                        Intent obj = new Intent(RegisterPatientActivity.this,SigninActivity.class);
                        startActivity(obj);
                    }
                    else{
                        Toast.makeText(RegisterPatientActivity.this, "Already registered", Toast.LENGTH_SHORT).show();
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
                HashMap<String,String> map = new HashMap<>();
                map.put("fname",fname);
                map.put("lname",lname);
                map.put("username",uname);
                map.put("pwd",pass);
                map.put("address",add);
                map.put("phoneno",phno);
                map.put("email",email);

                return map;
            }
        };
        Volley.newRequestQueue(RegisterPatientActivity.this).add(str);
    }


}