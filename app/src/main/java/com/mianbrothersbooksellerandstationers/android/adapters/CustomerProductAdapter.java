package com.mianbrothersbooksellerandstationers.android.adapters;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mianbrothersbooksellerandstationers.android.R;
import com.mianbrothersbooksellerandstationers.android.models.ProductModel;
import com.mianbrothersbooksellerandstationers.android.utils.FilterProducts;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerProductAdapter extends RecyclerView.Adapter<CustomerProductAdapter.ViewHolder> implements Filterable {
    private Context context;
    public ArrayList<ProductModel> productList, filterList;
    private FilterProducts filter;

    FirebaseAuth auth;
    int qtByCustomer;
    String quantityByCustomerStr;


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


        /*holder.itemView.setOnClickListener(it -> {
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
        });*/

        holder.imgMaterialShop.setOnClickListener(it -> {
            ShowDialog(model);
        });
    }

    private void ShowDialog(ProductModel model) {
        AppCompatButton add_to_cart;
        ImageButton increaseQuantity, decreaseQuantity;
        EditText setQuantityEt;
        auth = FirebaseAuth.getInstance();

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_to_cart);
        add_to_cart = dialog.findViewById(R.id.add_to_cart);
        increaseQuantity = dialog.findViewById(R.id.increaseQuantity);
        decreaseQuantity = dialog.findViewById(R.id.decreaseQuantity);
        setQuantityEt = dialog.findViewById(R.id.setQuantityEt);

        int stockInt = Integer.parseInt(String.valueOf(model.getStock()));
        int originalPriceInt = Integer.parseInt(String.valueOf(model.getPrice()));
        quantityByCustomerStr = String.valueOf(setQuantityEt.getText());

        increaseQuantity.setOnClickListener(it -> {
            if (!quantityByCustomerStr.isEmpty()) {
                int qtByCustomerInt = Integer.parseInt(quantityByCustomerStr);
                if (qtByCustomerInt > stockInt) {
                    Toast.makeText(context, "Please enter valid Quantity", Toast.LENGTH_SHORT).show();
                } else if(qtByCustomerInt == stockInt){
                    Toast.makeText(context, "No more Stock Available", Toast.LENGTH_SHORT).show();
                }else {
                    qtByCustomer = Integer.parseInt(quantityByCustomerStr);
                    qtByCustomer += 1;
                    quantityByCustomerStr = String.valueOf(qtByCustomer);
                    setQuantityEt.setText(quantityByCustomerStr);
                }
            } else {
                Toast.makeText(context, "Please enter Quantity", Toast.LENGTH_SHORT).show();
            }
        });

        decreaseQuantity.setOnClickListener(it -> {
            if (!quantityByCustomerStr.isEmpty()) {
                int qtByCustomerInt = Integer.parseInt(quantityByCustomerStr);
                if (qtByCustomerInt == 1 ) {
                    Toast.makeText(context, "Minimum 1 is required", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    qtByCustomer = Integer.parseInt(quantityByCustomerStr);
                    qtByCustomer -= 1;
                    quantityByCustomerStr = String.valueOf(qtByCustomer);
                    setQuantityEt.setText(quantityByCustomerStr);
                }
            } else {
                Toast.makeText(context, "Please enter Quantity", Toast.LENGTH_SHORT).show();
            }
        });

        setQuantityEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!quantityByCustomerStr.isEmpty()) {
                    int qtByCustomerInt = Integer.parseInt(quantityByCustomerStr);
                    if (qtByCustomerInt == 1  || qtByCustomerInt == 0) {
                        Toast.makeText(context, "Minimum 1 is required", Toast.LENGTH_SHORT).show();
                        return;
                    } else if(qtByCustomerInt > stockInt)
                    {
                        Toast.makeText(context, "No More Stock Available", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(context, "Please enter Quantity", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!String.valueOf(setQuantityEt.getText()).isEmpty()) {
                    int ttPrice = model.getPrice() * Integer.parseInt(String.valueOf(setQuantityEt.getText()));
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    String cartID = String.valueOf(reference.push().getKey());
                    HashMap<String,Object> cartData = new HashMap<>();
                    cartData.put("productname",model.getName());
                    cartData.put("productcategory",model.getCategory());
                    cartData.put("customerId",FirebaseAuth.getInstance().getUid());
                    cartData.put("producttotalquantity",model.getStock());
                    cartData.put("productOrderQuantity",Integer.valueOf(String.valueOf(setQuantityEt.getText())));
                    cartData.put("orignalPrice",model.getPrice());
                    cartData.put("totalPrice",ttPrice);
                    cartData.put("cartID",cartID);
                    cartData.put("image",model.getImage());
                    reference.child("Cart").child(FirebaseAuth.getInstance().getUid())
                            .child(cartID)
                            .setValue(cartData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(context, "Add to Cart Successful", Toast.LENGTH_SHORT).show();
                                    } else{
                                        Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(context, "Please enter Quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

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
        TextView stockTv, priceTv, shortDescTv, nameTv, catTv;

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
