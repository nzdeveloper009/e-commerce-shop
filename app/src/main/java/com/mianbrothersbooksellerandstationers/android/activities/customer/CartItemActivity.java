package com.mianbrothersbooksellerandstationers.android.activities.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mianbrothersbooksellerandstationers.android.R;
import com.mianbrothersbooksellerandstationers.android.activities.admin.OrderListActivity;
import com.mianbrothersbooksellerandstationers.android.adapters.CartAdapter;
import com.mianbrothersbooksellerandstationers.android.adapters.OrderAdapter;
import com.mianbrothersbooksellerandstationers.android.models.CartModel;
import com.mianbrothersbooksellerandstationers.android.models.OrderModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartItemActivity extends AppCompatActivity {

    RecyclerView cartRv;
    TextView totalPriceTV;
    AppCompatButton checkOutBtn;
    ArrayList<CartModel> cartList;
    CartAdapter adapter;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    int ttprice=0;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date,uid;
    private int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_item);

        cartRv = findViewById(R.id.cartRv);
        totalPriceTV = findViewById(R.id.totalPriceTV);
        checkOutBtn = findViewById(R.id.checkOutBtn);

        uid = FirebaseAuth.getInstance().getUid();

        loadAllCart();
        checkOutBtn.setOnClickListener(it -> {
            if(ttprice!=0 && cartList.size() != 0)
            {
                ShowPlaceOrderDialog();
            }
        });
    }

    private void ShowPlaceOrderDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_place_order);
        EditText buyerName = dialog.findViewById(R.id.buyerName);
        EditText buyerAddress = dialog.findViewById(R.id.buyerAddress);
        EditText buyerPhoneNo = dialog.findViewById(R.id.buyerPhoneNo);
        TextView orderBtn = dialog.findViewById(R.id.orderBtn);



        orderBtn.setOnClickListener(it -> {
            String orderBy = String.valueOf(buyerName.getText());
            String buyerAdd = String.valueOf(buyerAddress.getText());
            String buyerPh = String.valueOf(buyerPhoneNo.getText());
            String orderID = "0";
            if (orderBy.isEmpty()){
                Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
            } else if (buyerAdd.isEmpty()){
                Toast.makeText(this, "Please Enter Your Address", Toast.LENGTH_SHORT).show();
            } else if(buyerPh.isEmpty())
            {
                Toast.makeText(this,"Please Enter Phone Number.",Toast.LENGTH_SHORT).show();
            } else {
                HashMap<String,Object> data = new HashMap<>();
                List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
                // https://medium.com/@shayma_92409/display-the-current-date-android-studio-f582bf14f908
                calendar = Calendar.getInstance();
                dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                date = dateFormat.format(calendar.getTime());
                for (i=0;i<cartList.size();i++)
                {
                    orderID = "" + (System.currentTimeMillis() + i);
                    String pId = cartList.get(i).getCartID();
                    data.put("productname",cartList.get(i).getProductname());
                    data.put("productcategory",cartList.get(i).getProductcategory());
                    data.put("productid",cartList.get(i).getCartID());
                    data.put("producttotalquantity",String.valueOf(cartList.get(i).getProducttotalquantity()));
                    data.put("productOrderQuantity",String.valueOf(cartList.get(i).getProductOrderQuantity()));
                    data.put("orderBy",orderBy);
                    data.put("buyerAddress",buyerAdd);
                    data.put("buyerPhoneNo",buyerPh);
                    data.put("buyerID",FirebaseAuth.getInstance().getUid());
                    data.put("price",String.valueOf(cartList.get(i).getTotalPrice()));
                    data.put("orderID",orderID);
                    data.put("salesTime",date);
                    dataList.add(i,data);

                    reference.child("Orders")
                            .child(orderID)
                            .setValue(data)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        DatabaseReference removeCart = FirebaseDatabase.getInstance().getReference();
                                        removeCart
                                                .child("Cart")
                                                .child(uid)
                                                .child(pId)
                                                    .removeValue();
                                        if(i==cartList.size()){
                                            startActivity(new Intent(CartItemActivity.this,CustomerDashboardActivity.class));
                                            finish();
                                        }
                                        Toast.makeText(CartItemActivity.this, "Place Order Done", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }



            }
        });



        dialog.show();
    }

    private void loadAllCart() {
        cartList = new ArrayList<>();
        reference.child("Cart")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        cartList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            CartModel model = ds.getValue(CartModel.class);
                            // add to list
                            cartList.add(model);

                            for(int i=0;i<cartList.size();i++)
                            {
                                ttprice += cartList.get(i).getTotalPrice();
                            }
                        }
                        //setup adapter
                        adapter = new CartAdapter(CartItemActivity.this, cartList);
                        //set adapter to recyclerview
                        cartRv.setAdapter(adapter);

                        String str = "Total Amount Rs." + ttprice;
                        totalPriceTV.setText(str);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}