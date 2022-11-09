package com.mianbrothersbooksellerandstationers.android.activities.admin;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mianbrothersbooksellerandstationers.android.R;
import com.mianbrothersbooksellerandstationers.android.adapters.OrderAdapter;
import com.mianbrothersbooksellerandstationers.android.models.OrderModel;

import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity {


    RecyclerView ordersRv;
    LottieAnimationView animActive;

    OrderAdapter adapter;
    ArrayList<OrderModel> orderList;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        ordersRv = findViewById(R.id.ordersRv);
        animActive = findViewById(R.id.animActive);

        getAllOders();
    }

    private void getAllOders() {
        orderList = new ArrayList<>();
        mDatabase.child("Orders")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Clear list before adding new data in it
                        orderList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            OrderModel model = ds.getValue(OrderModel.class);
                            // add to list
                            orderList.add(model);
                        }
                        //setup adapter
                        adapter = new OrderAdapter(OrderListActivity.this, orderList);
                        //set adapter to recyclerview
                        ordersRv.setAdapter(adapter);

                        if(adapter.getItemCount() != 0 ){
                            animActive.setVisibility(View.GONE);
                            ordersRv.setVisibility(View.VISIBLE);
                        }else{
                            animActive.setVisibility(View.VISIBLE);
                            ordersRv.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}