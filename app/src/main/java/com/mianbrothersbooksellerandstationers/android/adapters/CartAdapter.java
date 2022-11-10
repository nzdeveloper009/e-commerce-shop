package com.mianbrothersbooksellerandstationers.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mianbrothersbooksellerandstationers.android.R;
import com.mianbrothersbooksellerandstationers.android.models.CartModel;
import com.mianbrothersbooksellerandstationers.android.models.ProductModel;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    public ArrayList<CartModel> cartList;

    public CartAdapter(Context context, ArrayList<CartModel> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       final CartModel model = cartList.get(position);
        Glide
                .with(context)
                .load(model.getImage())
                .placeholder(R.drawable.logo_white_background)
                .into(holder.imageProductIV);
        String productName = model.getProductname();
        String category ="Category: " + model.getProductcategory();
        String stock = "Stock: " + model.getProducttotalquantity();
        String price = "Rs." +model.getOrignalPrice();
        String quantity = String.valueOf(model.getProductOrderQuantity());

        holder.nameTv.setText(productName);
        holder.catTv.setText(category);
        holder.stockTv.setText(stock);
        holder.priceTv.setText(price);
        holder.setQuantityEt.setText(quantity);

        holder.removeBtn.setOnClickListener(it ->{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Cart").child(FirebaseAuth.getInstance().getUid())
                    .child(model.getCartID())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Cat is Removed Successful", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProductIV;
        TextView nameTv,catTv,stockTv,priceTv;
        AppCompatButton removeBtn;
        ImageButton increaseQuantity,decreaseQuantity;
        EditText setQuantityEt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProductIV = itemView.findViewById(R.id.imageProductIV);
            nameTv = itemView.findViewById(R.id.nameTv);
            catTv = itemView.findViewById(R.id.catTv);
            stockTv = itemView.findViewById(R.id.stockTv);
            priceTv = itemView.findViewById(R.id.priceTv);
            removeBtn = itemView.findViewById(R.id.removeBtn);
            increaseQuantity = itemView.findViewById(R.id.increaseQuantity);
            decreaseQuantity = itemView.findViewById(R.id.decreaseQuantity);
            setQuantityEt = itemView.findViewById(R.id.setQuantityEt);
        }
    }

}
