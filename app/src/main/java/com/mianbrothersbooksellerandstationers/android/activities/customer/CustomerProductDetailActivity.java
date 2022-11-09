package com.mianbrothersbooksellerandstationers.android.activities.customer;

import static com.mianbrothersbooksellerandstationers.android.firebase.RealTimeClass.getCurrentUserID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mianbrothersbooksellerandstationers.android.R;
import com.mianbrothersbooksellerandstationers.android.activities.BaseActivity;
import com.mianbrothersbooksellerandstationers.android.adapters.CategoryAdapter;

import java.util.HashMap;

public class CustomerProductDetailActivity extends BaseActivity {

    String productIDStr,productPriceStr,productNameStr,productCategoryStr,productDescriptionStr,productQuantityStr;
    TextView brandName, productDescription,originalPrice,totalPrice,stock,orderBtn;

    EditText buyerName,buyerAddress,buyerPhoneNo,buyerNIC,quantity;
    FirebaseAuth auth;
    int quantityInt, totalPriceInt, originalPriceInt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_customer_product_detail);
        if(getIntent() != null){
            productIDStr = getIntent().getStringExtra("ProductID");
            productPriceStr = getIntent().getStringExtra("ProductPrice");
            productNameStr = getIntent().getStringExtra("ProductName");
            productCategoryStr = getIntent().getStringExtra("ProductCategory");
            productDescriptionStr = getIntent().getStringExtra("ProductDescription");
            productQuantityStr = getIntent().getStringExtra("ProductQuantity");
        }

        auth = FirebaseAuth.getInstance();

        initView();
        listeners();
    }

    private void listeners() {
        orderBtn.setOnClickListener(it ->{
            if(String.valueOf(quantity.getText()).isEmpty())
            {
                Toast.makeText(this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
            }else if(Integer.parseInt(String.valueOf(stock.getText()).trim()) < Integer.parseInt(String.valueOf(quantity.getText()).trim()))
            {
                Toast.makeText(this, "Range is Out", Toast.LENGTH_SHORT).show();
            }  else {
                PlaceOrderInDatabase();
            }
        });
    }

    private void PlaceOrderInDatabase() {
        showProgressDialog(getResources().getString(R.string.please_wait));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> data =new HashMap<>();
        String orderID = String.valueOf(reference.push().getKey());
        data.put("productname",productNameStr);
        data.put("productcategory",productCategoryStr);
        data.put("productid",productIDStr);
        data.put("producttotalquantity",productQuantityStr);
        data.put("productOrderQuantity",String.valueOf(quantity.getText()));
        data.put("orderBy",String.valueOf(buyerName.getText()));
        data.put("buyerAddress",String.valueOf(buyerAddress.getText()));
        data.put("buyerPhoneNo",String.valueOf(buyerPhoneNo.getText()));
        data.put("CashOnDelivery",String.valueOf(buyerNIC.getText()));
        data.put("buyerID",getCurrentUserID());
        data.put("orderID",orderID);
        data.put("price",String.valueOf(originalPrice.getText()));
        reference.child("Orders").child(orderID).setValue(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            hideProgressDialog();
                            showSnackBar("Order Place Successfully",R.color.snackbar_success_color);
                            startActivity(new Intent(CustomerProductDetailActivity.this,MainActivity.class));
                            finish();
                        } else {
                            hideProgressDialog();
                            showSnackBar("ERRoR! ",R.color.snackbar_error_color);
                        }
                    }
                });
    }

    private void initView() {
        brandName = findViewById(R.id.brandName);
        productDescription = findViewById(R.id.productDescription);
        originalPrice = findViewById(R.id.originalPrice);
        totalPrice = findViewById(R.id.totalPrice);
        stock = findViewById(R.id.stock);
        orderBtn = findViewById(R.id.orderBtn);

        buyerName = findViewById(R.id.buyerName);
        buyerAddress = findViewById(R.id.buyerAddress);
        buyerPhoneNo = findViewById(R.id.buyerPhoneNo);
        buyerNIC = findViewById(R.id.buyerNIC);
        quantity = findViewById(R.id.quantity);

        brandName.setText(productNameStr);
        productDescription.setText(productDescriptionStr);
        originalPrice.setText(productPriceStr);
        totalPrice.setText(productPriceStr);
        stock.setText(productQuantityStr);
    }
}