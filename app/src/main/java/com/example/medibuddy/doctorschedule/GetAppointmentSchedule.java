package com.example.medibuddy.doctorschedule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.medibuddy.bookingpage.Booking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetAppointmentSchedule extends AppCompatActivity {
    TextView tv_get_appointmentScheduleid, tv_get_appointmentDate, tv_get_appointmentDoctorid;
    Button btn_get_appointmentSubmit;
    String url = "http://14.99.214.220/drbookingapp/bookingapp.asmx/GetAppointmentSchedule";
    String mdate, scheduleid, doctorid;
    ListView listview_getappointment_schedule;
    ArrayList<SetGet2> arrlist = new ArrayList<SetGet2>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getappointmentschedule);
        tv_get_appointmentScheduleid = findViewById(R.id.tv_get_appointmentScheduleid);
        tv_get_appointmentDate = findViewById(R.id.tv_get_appointmentDate);
        tv_get_appointmentDoctorid = findViewById(R.id.tv_get_appointmentDoctorid);
        listview_getappointment_schedule = findViewById(R.id.listview_getappointment_schedule);
        //btn_get_appointmentSubmit=findViewById(R.id.btn_get_appointmentSubmit);

        scheduleid = getIntent().getExtras().getString("scheduleid");
        mdate = getIntent().getExtras().getString("date");
        doctorid = getIntent().getExtras().getString("doctorid");
        tv_get_appointmentScheduleid.setText(scheduleid);
        tv_get_appointmentDate.setText(mdate);
        tv_get_appointmentDoctorid.setText(doctorid);
        postDataToServer(url, scheduleid, mdate, doctorid);

//        btn_get_appointmentSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(GetAppointmentSchedule.this, "Submit Successfully", Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }

    private void postDataToServer(String url, String scheduleid, String mdate, String doctorid) {
        StringRequest srq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.i("resp",response);
                try {
                    JSONObject rootobj = new JSONObject(response);
                    JSONArray jar = rootobj.getJSONArray("availableschedule");
                    String suc = rootobj.getString("Sucess");

                    if (suc.equalsIgnoreCase("1")) {
                        for (int i = 0; i < jar.length(); i++) {
                            JSONObject job = jar.getJSONObject(i);
                            String timeslotid = job.getString("timeslotid");
                            String status = job.getString("status");
                            String slotfrom = job.getString("slotfrom");
                            String slotto = job.getString("slotto");

                            SetGet2 st = new SetGet2();
                            st.setTimeslotid(timeslotid);
                            st.setStatus(status);
                            st.setSlotfrom(slotfrom);
                            st.setSlotto(slotto);
                            arrlist.add(st);
                        }

                        Toast.makeText(GetAppointmentSchedule.this, "Login Successfully", Toast.LENGTH_SHORT).show();   //Go to dashboard if successfully login

                        //Intent intent=new Intent(GetAppointmentSchedule.this,Dashboard.class);
                        //startActivity(intent);
                    } else {
                        Toast.makeText(GetAppointmentSchedule.this, "Invalid User", Toast.LENGTH_SHORT).show();
                    }

                    listview_getappointment_schedule.setAdapter(new Dweep());
                    listview_getappointment_schedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intnt = new Intent(GetAppointmentSchedule.this, Booking.class);
                            intnt.putExtra("doctorid",doctorid);
                            intnt.putExtra("timeslotid",arrlist.get(position).getTimeslotid());
                            intnt.putExtra("date",mdate);
                            startActivity(intnt);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GetAppointmentSchedule.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {    //It is used to send parameter to the server
                HashMap<String, String> hmap = new HashMap<String, String>();
                hmap.put("scheduleid", scheduleid);
                hmap.put("date", mdate);
                hmap.put("doctorid", doctorid);
                //return super.getParams();
                return hmap;
            }
        };
        Volley.newRequestQueue(GetAppointmentSchedule.this).add(srq);
    }

    class Dweep extends BaseAdapter {

        @Override
        public int getCount() {
            return arrlist.size();
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
            LayoutInflater lf = getLayoutInflater();    // for xml calling
            View row = lf.inflate(R.layout.getappointmentchild, parent, false);
            TextView tv1 = row.findViewById(R.id.tv_timeslotid);
            TextView tv2 = row.findViewById(R.id.tv_status);
            TextView tv3 = row.findViewById(R.id.tv_slotfrom);
            TextView tv4 = row.findViewById(R.id.tv_slotto);

            tv1.setText(arrlist.get(position).getTimeslotid());
            tv2.setText(arrlist.get(position).getStatus());
            tv3.setText(arrlist.get(position).getSlotfrom());
            tv4.setText(arrlist.get(position).getSlotto());

            return row;
        }

    }
}