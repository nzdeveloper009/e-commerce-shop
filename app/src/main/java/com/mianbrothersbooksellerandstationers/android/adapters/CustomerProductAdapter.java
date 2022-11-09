package com.mianbrothersbooksellerandstationers.android.adapters;

import static com.mianbrothersbooksellerandstationers.android.utils.Constants.CATEGORY;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mianbrothersbooksellerandstationers.android.R;
import com.mianbrothersbooksellerandstationers.android.activities.customer.CustomerProductDetailActivity;
import com.mianbrothersbooksellerandstationers.android.models.ProductModel;
import com.mianbrothersbooksellerandstationers.android.utils.FilterProducts;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import android.widget.Filter;

public class CustomerProductAdapter extends RecyclerView.Adapter<CustomerProductAdapter.ViewHolder> implements Filterable {
    private Context context;
    public ArrayList<ProductModel> productList, filterList;
    private FilterProducts filter;


    public CustomerProductAdapter(Context context, ArrayList<ProductModel> productList) {
        this.context = context;
        this.productList = productList;
        this.filterList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_customer_product, parent, false);

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
        String desc = "Desc:\n\t" + model.getDesc();
        String category = "Category:\n\t" + model.getCategory();

        holder.nameTv.setText(name);
        holder.shortDescTv.setText(desc);
        holder.priceTv.setText(price);
        holder.stockTv.setText(stock);
        holder.catTv.setText(category);


        holder.itemView.setOnClickListener(it -> {
            Intent intent = new Intent(context, CustomerProductDetailActivity.class);
            intent.putExtra(CATEGORY, model.getCategory());
            intent.putExtra("ProductID", model.getKey());
            intent.putExtra("ProductPrice", String.valueOf(model.getPrice()));
            intent.putExtra("ProductName", model.getName());
            intent.putExtra("ProductCategory", model.getCategory());
            intent.putExtra("ProductDescription", model.getDesc());
            intent.putExtra("ProductQuantity", String.valueOf(model.getStock()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            //init filter
            filter = new FilterProducts(this, filterList);
        }
        return filter;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgContactProductInfo;
        ImageView imgMaterialShop;
        TextView stockTv, priceTv, shortDescTv, nameTv,catTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.nameTv);
            imgContactProductInfo = itemView.findViewById(R.id.imgContactProductInfo);
            imgMaterialShop = itemView.findViewById(R.id.imgMaterialShop);
            stockTv = itemView.findViewById(R.id.stockTv);
            priceTv = itemView.findViewById(R.id.priceTv);
            catTv = itemView.findViewById(R.id.catTv);
            shortDescTv = itemView.findViewById(R.id.shortDescTv);
        }
    }
}
