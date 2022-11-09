package com.mianbrothersbooksellerandstationers.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mianbrothersbooksellerandstationers.android.R;
import com.mianbrothersbooksellerandstationers.android.models.OrderModel;

import java.util.ArrayList;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ViewHolder> {
    private Context context;
    public ArrayList<OrderModel> salesBuyerList;

    public SalesAdapter(Context context, ArrayList<OrderModel> salesBuyerList) {
        this.context = context;
        this.salesBuyerList = salesBuyerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get data at position
        OrderModel model = salesBuyerList.get(position);
        final String orderId = model.getOrderID();
        final String productName = "Product Name: " + model.getProductname();
        final String buyerName = "Buyer Name: " + model.getOrderBy();
        final String buyerAddress = "Address: " + model.getBuyerAddress();
        final String price = "Price: " + model.getPrice();

        holder.orderIdTv.setText(orderId);
        holder.productNameTv.setText(productName);
        holder.buyerNameTv.setText(buyerName);
        holder.buyerAddresTv.setText(buyerAddress);
        holder.amountTv.setText(price);

    }

    @Override
    public int getItemCount() {
        return salesBuyerList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //views of layout
        private TextView orderIdTv, productNameTv, buyerNameTv, amountTv, productDesTv, buyerAddresTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //init views of layout
            orderIdTv = itemView.findViewById(R.id.orderIdTv);
            productNameTv = itemView.findViewById(R.id.productNameTv);
            buyerNameTv = itemView.findViewById(R.id.buyerNameTv);
            amountTv = itemView.findViewById(R.id.amountTv);
            productDesTv = itemView.findViewById(R.id.productDesTv);
            buyerAddresTv = itemView.findViewById(R.id.buyerAddresTv);
        }
    }
}
