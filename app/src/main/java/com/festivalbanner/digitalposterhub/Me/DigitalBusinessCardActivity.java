package com.festivalbanner.digitalposterhub.Me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.festivalbanner.digitalposterhub.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DigitalBusinessCardActivity extends AppCompatActivity {
    String url = "https://adminapp.tech/brandmaker/userapi/templatelist.php";
    ArrayList<AllLogoPOJO> listItems = new ArrayList<>();
    RecyclerView recyclerView;
    AllLogoAdapter allLogoAdapter;
    TextView backPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_business_card);
        initMethod();
        setAction();
        try {
            getAllTemplates();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initMethod() {
        recyclerView = findViewById(R.id.recyclerView);
        backPress = findViewById(R.id.backPress);
    }

    private void setAction() {
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getAllTemplates() throws JSONException {
        listItems.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray1 = new JSONArray();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    jsonArray1 = jsonObject.getJSONArray("message");
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        String templatename = jsonObject1.getString("templatename");
                        String templateimage = jsonObject1.getString("templateimage");
                        String finalimage = "https://adminapp.tech/brandmaker/public/storage/" + templateimage;
                        AllLogoPOJO allLogoPOJO = new AllLogoPOJO(id, finalimage);
                        listItems.add(allLogoPOJO);
                    }
                    allLogoAdapter = new AllLogoAdapter(DigitalBusinessCardActivity.this, listItems);
                    recyclerView.setLayoutManager(new GridLayoutManager(DigitalBusinessCardActivity.this, 2));
                    recyclerView.setAdapter(allLogoAdapter);
                    allLogoAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        queue.add(request);
    }


    //Adapter for All Type Template------------------
    public class AllLogoAdapter extends RecyclerView.Adapter<AllLogoAdapter.MyViewHolder> {
        Context context;
        ArrayList<AllLogoPOJO> list;

        public AllLogoAdapter(Context context, ArrayList<AllLogoPOJO> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public AllLogoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_bussiness_template, parent, false);
            return new AllLogoAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AllLogoAdapter.MyViewHolder holder, int position) {
            AllLogoPOJO allLogoPOJO = list.get(position);

            Glide.with(context)
                    .load(allLogoPOJO.getImage())
                    .into(holder.imageView);

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ShowDigitalBusinessCard.class);
                    intent.putExtra("image", allLogoPOJO.getImage());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            RelativeLayout relativeLayout;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                relativeLayout = itemView.findViewById(R.id.layout);
            }
        }
    }
}