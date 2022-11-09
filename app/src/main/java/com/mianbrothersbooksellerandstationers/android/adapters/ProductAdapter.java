package com.mianbrothersbooksellerandstationers.android.adapters;

import static com.mianbrothersbooksellerandstationers.android.utils.Constants.CATEGORY;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mianbrothersbooksellerandstationers.android.R;
import com.mianbrothersbooksellerandstationers.android.models.ProductModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    public ArrayList<ProductModel> productList;

    public ProductAdapter(Context context, ArrayList<ProductModel> productList) {
        this.productList = productList;
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProductModel model = productList.get(position);

        Glide
                .with(context)
                .load(Uri.parse(model.getImage()))
                .placeholder(R.drawable.ic_place_holder)
                .into(holder.imgContactProductInfo);
        String name = "Name: " + model.getName();
        String stock = "Stock: " + model.getStock();
        String price = "Price: " + model.getPrice();
        String desc = "Desc:\n" + model.getDesc();

        holder.nameTv.setText(name);
        holder.shortDescTv.setText(desc);
        holder.priceTv.setText(price);
        holder.stockTv.setText(stock);


        holder.imgMaterialDelete.setOnClickListener(it -> {
            String setMessage = "Deleting this [" + model.getName() + "] will result in completely removing and won't be able to view?";
            AlertDialog.Builder alertbox = new AlertDialog.Builder(it.getRootView().getContext());
            alertbox.setMessage(setMessage);
            alertbox.setTitle("Are you sure");
            alertbox.setIcon(android.R.drawable.ic_dialog_alert);
            alertbox.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(CATEGORY).child(model.getCategory()).child(model.getKey());
                    ref.removeValue();
                    Toast.makeText(context, "Product Delete Successfully", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            });
            alertbox.show();
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgContactProductInfo;
        ImageView imgMaterialDelete;
        TextView stockTv, priceTv, shortDescTv, nameTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.nameTv);
            imgContactProductInfo = itemView.findViewById(R.id.imgContactProductInfo);
            imgMaterialDelete = itemView.findViewById(R.id.imgMaterialDelete);
            stockTv = itemView.findViewById(R.id.stockTv);
            priceTv = itemView.findViewById(R.id.priceTv);
            shortDescTv = itemView.findViewById(R.id.shortDescTv);
        }
    }
}
