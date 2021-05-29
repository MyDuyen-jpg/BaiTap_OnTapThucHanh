package com.myduyen.ontap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnDel, btnCancel;
    StudentAdapter adt;
    ListView lsName;
    TextView tvDisplay,tvId,tvName;
    ArrayList<Student> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tvId = findViewById(R.id.tvName);
        tvName = findViewById(R.id.tvId);
        tvDisplay =findViewById(R.id.tvDisplay);
        btnDel = findViewById(R.id.btnDel);
        btnCancel = findViewById(R.id.btnCancel);
        lsName = findViewById(R.id.lsName);

        String urlGet="https://60aafeb366f1d0001777358a.mockapi.io/Student/";
        GetData(urlGet);
        lsName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student student =arrayList.get(position);
                id =student.getId();
                String name = student.getName();
                System.setProperty("ID", id+"");
                System.setProperty("Name",name);

                tvDisplay.setText(System.getProperty("Name"));
                btnDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String urlDel="https://60aafeb366f1d0001777358a.mockapi.io/Student";
                        DeleteAPI(urlDel);
                        GetData(urlDel);
                    }
                });
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }

    // Add library Volley
    public void GetData(String url) {
        arrayList = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                arrayList.add(new Student(object.getInt("id"), object.getString("name")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adt = new StudentAdapter(getApplicationContext(), arrayList, R.layout.item_listview);
                        lsName.setAdapter(adt);
                        adt.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error by Json Object....", Toast.LENGTH_SHORT).show();
            }

        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    public void DeleteAPI(String url) {
        StringRequest request = new StringRequest(
                Request.Method.DELETE, url+'/'+Integer.parseInt(System.getProperty("ID")),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, "Successfully Delete", Toast.LENGTH_SHORT).show();
                        adt.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Faild Delete", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);


    }
}