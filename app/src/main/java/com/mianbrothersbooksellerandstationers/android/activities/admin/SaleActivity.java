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
import com.mianbrothersbooksellerandstationers.android.adapters.SalesAdapter;
import com.mianbrothersbooksellerandstationers.android.models.OrderModel;

import java.util.ArrayList;

public class SaleActivity extends AppCompatActivity {

    RecyclerView salesRv;
    LottieAnimationView animActive;

    SalesAdapter adapter;
    ArrayList<OrderModel> salesist;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        salesRv = findViewById(R.id.salesRv);
        animActive = findViewById(R.id.animActive);

        getAllSales();
    }

    private void getAllSales() {
        salesist = new ArrayList<>();
        mDatabase.child("Sales")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Clear list before adding new data in it
                        salesist.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            OrderModel model = ds.getValue(OrderModel.class);
                            // add to list
                            salesist.add(model);
                        }
                        //setup adapter
                        adapter = new SalesAdapter(SaleActivity.this, salesist);
                        //set adapter to recyclerview
                        salesRv.setAdapter(adapter);

                        if(adapter.getItemCount() != 0 ){
                            animActive.setVisibility(View.GONE);
                            salesRv.setVisibility(View.VISIBLE);
                        }else{
                            animActive.setVisibility(View.VISIBLE);
                            salesRv.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}