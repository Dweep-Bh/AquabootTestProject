package com.example.medibuddy.getdoctor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.medibuddy.R;
import com.example.medibuddy.doctorschedule.GetAppointmentSchedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GetDoctor extends AppCompatActivity {
    EditText tv_select_date,etdepartmentid;
    String deptid,mydate;
    Button chooseherebutton, btnsubmit;
    ListView List_view;
    ArrayList<SetGetdoctor> alist= new ArrayList<SetGetdoctor>();
    int dd, mm, yy;
    String url="http://14.99.214.220/drbookingapp/bookingapp.asmx/GetDoctor";
    ProgressBar progressbar;
  //  String eph;
  String s1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getdoctor);
        tv_select_date = findViewById(R.id.tv_select_date);
      // etdepartmen tid=findViewById(R.id.etdepartmentid);
        chooseherebutton = findViewById(R.id.chooseherebutton);
        btnsubmit = findViewById(R.id.btnsubmit);
        List_view=findViewById(R.id.List_view);

        deptid=getIntent().getExtras().getString("departmentid");
      //  etdepartmentid.setText(eph);
    //    progressbar = findViewById(R.id.progressbar);
       // progressbar.setVisibility(View.VISIBLE);

        s1=tv_select_date.getText().toString();
        chooseherebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                dd = c.get(Calendar.DAY_OF_MONTH);
                mm = c.get(Calendar.MONTH);
                yy = c.get(Calendar.YEAR);
                DatePickerDialog dp = new DatePickerDialog(GetDoctor.this, new DatePickerDialog.OnDateSetListener() {
                    @Override

                    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
                        mydate =  (month + 1)+ "/" +dayofmonth + "/" + year;
                        tv_select_date.setText(mydate);

                        Toast.makeText(GetDoctor.this, "Date is" + mydate, Toast.LENGTH_SHORT).show();

                    }
                },yy, mm, dd);
                dp.show();

            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//              //  s2=etdepartmentid.getText().toString();

               post(url,deptid,mydate);
//                Intent intent=new Intent(GetappointmentscheduleActivity.this, Doctorschedule.class);
//                startActivity(intent);
            }
        });
    }
    private  void post (String url,String  tv_select_date,String etdepartmentid)
    {

        StringRequest sreq=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("resp",response);

                try {
                    JSONObject rootobj=new JSONObject(response);
                    JSONArray jr=rootobj.getJSONArray("DoctorSchedule");
                    String suc= rootobj.getString("Sucess");
                    if(suc.equalsIgnoreCase("1"))
                    {
                        for(int i=0; i<jr.length(); i++)
                        {
                            JSONObject job=jr.getJSONObject(i);
                            String drscheduleid= job.getString("drscheduleid");
                            String scheduleday= job.getString("scheduleday");
                            String scheduleid= job.getString("scheduleid");
                            String doctorid= job.getString("doctorid");

                            JSONObject innerobj= job.getJSONObject("doctorde");
                            String doctorname= innerobj.getString("doctorname");
                            String departmentid = innerobj.getString("departmentid");

                            JSONObject innerobj1= job.getJSONObject("schdet");
                            String starttime= innerobj1.getString("starttime");
                            String endtime = innerobj1.getString("endtime");


                            SetGetdoctor st= new SetGetdoctor();
                            st.setDrscheduleid(drscheduleid);
                            st.setScheduleday(scheduleday);
                            st.setScheduleid(scheduleid);
                            st.setDoctorid(doctorid);
                            st.setDoctorname(doctorname);
                            st.setDepartmentid(departmentid);
                            st.setStarttime(starttime);
                            st.setEndtime(endtime);
                            alist.add(st);




                        }
//                        Intent intent=new Intent(GetappointmentscheduleActivity.this,Doctorschedule.class);
//                        startActivity(intent);

                    }

                    List_view.setAdapter(new Dweep());//
                    //go to appointmentpage and carry schedule id doctor id date using put extra
                    List_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent i=new Intent(GetDoctor.this, GetAppointmentSchedule.class);

                            i.putExtra("scheduleid",alist.get(position).getScheduleid());
                            i.putExtra("doctorid",alist.get(position).getDoctorid());
                            i.putExtra("date",mydate);
                            startActivity(i);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<String,String>();
                map.put("departmentid",deptid);
                map.put("bookingdate",mydate);
                return map;
            }
        };
        Volley.newRequestQueue(GetDoctor.this).add(sreq);

    }

    class Dweep extends BaseAdapter
    {

        @Override
        public int getCount() {
            return alist.size();
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
            LayoutInflater lf= getLayoutInflater();// for xml calling
            View row=lf.inflate(R.layout.doctorchild,parent,false);
            TextView tv1 = row.findViewById(R.id.Drscheduleid);
            TextView tv2 = row.findViewById(R.id.Scheduleday);
            TextView tv3 = row.findViewById(R.id.Scheduleid);
            TextView tv4 = row.findViewById(R.id.Doctorid);
            TextView tv5 = row.findViewById(R.id.Doctorname);
            TextView tv6 = row.findViewById(R.id.Departmentid);
            TextView tv7 = row.findViewById(R.id.Starttime);
            TextView tv8 = row.findViewById(R.id.Endtime);

            tv1.setText(alist.get(position).getDrscheduleid());
            tv2.setText(alist.get(position).getScheduleday());
            tv3.setText(alist.get(position).getScheduleid());
            tv4.setText(alist.get(position).getDoctorid());
            tv5.setText(alist.get(position).getDoctorname());
            tv6.setText(alist.get(position).getDepartmentid());
            tv7.setText(alist.get(position).getStarttime());
            tv8.setText(alist.get(position).getEndtime());
            return row;
        }
    }


}




