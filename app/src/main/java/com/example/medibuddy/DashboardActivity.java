package com.example.medibuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.medibuddy.getdoctor.GetDoctor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    ListView List_view;
    ProgressBar pb;
    int arr[]={R.drawable.general,R.drawable.cardio,R.drawable.ortho,R.drawable.gas,R.drawable.neuro,R.drawable.skin,R.drawable.child,R.drawable.gynae};
    String url="http://14.99.214.220/drbookingapp/bookingapp.asmx/GetDepartment";
    ArrayList<SetGet> alist= new ArrayList<SetGet>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        List_view=findViewById(R.id.List_view);
        pb=findViewById(R.id.pb);


            pb.setVisibility(View.VISIBLE);

            getDataFromServer(url);




    }
    private void getDataFromServer(String url)
    {
       JsonObjectRequest jobreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {
               pb.setVisibility(View.GONE);

               try {
                   JSONArray jr=response.getJSONArray("DepartmentDetails");
                   for(int i=0; i<jr.length(); i++)
                   {
                       JSONObject job=jr.getJSONObject(i);
                       String depid=job.getString("departmentid");
                       String dept=job.getString("departmentname");
                       SetGet st= new SetGet();

                          st.setDepartmentid(depid);
                          st.setDepartmentname(dept);
                          alist.add(st);
                   }
                   List_view.setAdapter(new Dweep());
                   List_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                       @Override
                       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           Intent obj=new Intent(DashboardActivity.this, GetDoctor.class);
                           obj.putExtra("departmentid",alist.get(position).getDepartmentid());
                           startActivity(obj);
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
       });
        Volley.newRequestQueue(DashboardActivity.this).add(jobreq);
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
            View row=lf.inflate(R.layout.dashboardchild,parent,false);
            TextView tv = row.findViewById(R.id.departmentid);
            TextView tv1 = row.findViewById(R.id.departmentname);
            tv.setText(alist.get(position).getDepartmentid());
            tv1.setText(alist.get(position).getDepartmentname());
            ImageView iv1=row.findViewById(R.id.iv1);
            iv1.setImageResource(arr[position]);
            return row;
        }
    }
}