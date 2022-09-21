package com.festivalbanner.digitalposterhub.Me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class AllLogoFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    String url = "https://adminapp.tech/yoyoiq/api/banner";
    ArrayList<AllLogoPOJO> listItems = new ArrayList<>();
    RecyclerView recyclerView;

    public AllLogoFragment() {
        // Required empty public constructor
    }

    public static AllLogoFragment newInstance(String param1, String param2) {
        AllLogoFragment fragment = new AllLogoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getAll();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    AllLogoAdapter allLogoAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_all_logo, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        return root;
    }

    private void getAll() throws JSONException {
        listItems.clear();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray1 = new JSONArray();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    jsonArray1 = jsonObject.getJSONArray("response");
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        String image = jsonObject1.getString("image");
                        AllLogoPOJO allLogoPOJO = new AllLogoPOJO(id, image);
                        listItems.add(allLogoPOJO);
                    }
                    allLogoAdapter = new AllLogoAdapter(getContext(), listItems);
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
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


    //Adapter------------------
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_image_logo, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AllLogoAdapter.MyViewHolder holder, int position) {
            AllLogoPOJO allLogoPOJO = list.get(position);

            holder.title.setText("TEXTILE");
            Glide.with(context)
                    .load(allLogoPOJO.getImage())
                    .into(holder.imageView);

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, LoGoEditActivity.class);
                    intent.putExtra("image", allLogoPOJO.getImage());
                    intent.putExtra("title", "TEXTILE");
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
            TextView title;
            RelativeLayout relativeLayout;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                relativeLayout = itemView.findViewById(R.id.layout);
                title = itemView.findViewById(R.id.title);
            }
        }
    }
}