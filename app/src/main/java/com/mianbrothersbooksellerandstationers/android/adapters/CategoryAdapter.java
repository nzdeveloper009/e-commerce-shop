package com.mianbrothersbooksellerandstationers.android.adapters;

import static com.mianbrothersbooksellerandstationers.android.firebase.RealTimeClass.getProductsofCategory;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mianbrothersbooksellerandstationers.android.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    public ArrayList<String> categoryList;
    public RecyclerView productRv;
    boolean b;

    public CategoryAdapter(Context context, ArrayList<String> categoryList, RecyclerView productRv) {
        this.context = context;
        this.categoryList = categoryList;
        this.productRv = productRv;
    }

    public CategoryAdapter(Context context, ArrayList<String> categoryList, RecyclerView productRv, boolean b) {
        this.context = context;
        this.categoryList = categoryList;
        this.productRv = productRv;
        this.b = b;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String s = categoryList.get(position);
        holder.categoryTv.setText(categoryList.get(position));

        holder.catCv.setOnClickListener(it -> {
            Log.d("MaNahiToKounBeh", "MaNahiToKounBeh: " + s);
            getProductsofCategory(context, s, productRv, b);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTv;
        CardView catCv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTv = itemView.findViewById(R.id.categoryTv);
            catCv = itemView.findViewById(R.id.catCv);
        }
    }
}
