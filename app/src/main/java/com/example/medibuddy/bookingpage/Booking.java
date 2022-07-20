package com.example.medibuddy.bookingpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.medibuddy.R;
import com.example.medibuddy.SplashActivity;
import com.example.medibuddy.util.Appdata;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Booking extends AppCompatActivity {

    ListView list_booking;
    String url = "http://14.99.214.220/drbookingapp/bookingapp.asmx/BookingDr";
    ArrayList<SetGet3> arrlist1 = new ArrayList<>();
    String doctorid,timeslotid,bookingdate;
    String val = Appdata.userid;
    TextView docid,bookdate,timeslot;
     Button logout_Button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        list_booking = findViewById(R.id.list_booking);

        docid=findViewById(R.id.docid);
        bookdate=findViewById(R.id.bookdate);
        timeslot=findViewById(R.id.timeslot);
        logout_Button=findViewById(R.id.logout_button);
        doctorid=getIntent().getExtras().getString("doctorid");
        timeslotid=getIntent().getExtras().getString("timeslotid");
        bookingdate=getIntent().getExtras().getString("date");
      //  final SharedPreferences sharedPreferences=getSharedPreferences("Data", Context.MODE_PRIVATE);
        docid.setText(doctorid);
        bookdate.setText(bookingdate);
        timeslot.setText(timeslotid);
        Postdatatoserver(url,doctorid,val,timeslotid,bookingdate);

        logout_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sf= getSharedPreferences("user",  MODE_PRIVATE);
                String u,p;
                u=sf.getString("uname","");
                p=sf.getString("pwd","");


                Toast.makeText(Booking.this, "logout successfully", Toast.LENGTH_SHORT).show();

                Intent i=new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(i);

            }
        });

    }


    private void Postdatatoserver(String url,String dctrid,String usid,String timeid,String bookdate){
        StringRequest strreq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobreq = new JSONObject(response);
                    String suc = jobreq.getString("Sucess");
                    if(suc.equalsIgnoreCase("1")){
                        String msg = jobreq.getString("Message");
                        SetGet3 st2 = new SetGet3();
                        st2.setMessage(msg);
                        arrlist1.add(st2);
                    }
                    list_booking.setAdapter(new Dweep());
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
                HashMap<String,String> map3 = new HashMap<>();
                map3.put("doctorid",doctorid);
                map3.put("userid",val);
                map3.put("timeslotid",timeslotid);
                map3.put("bookingdate",bookingdate);

                return map3;
            }
        };
        Volley.newRequestQueue(Booking.this).add(strreq);
    }
    class Dweep extends BaseAdapter {

        @Override
        public int getCount() {
            return arrlist1.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater lf = getLayoutInflater();
            View row = lf.inflate(R.layout.childbookingxml,null);
            TextView tv1 = row.findViewById(R.id.message);
            tv1.setText(arrlist1.get(position).getMessage());
            return row;
        }

    }
}