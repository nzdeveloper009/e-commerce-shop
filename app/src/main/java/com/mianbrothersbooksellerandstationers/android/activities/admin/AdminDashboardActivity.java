package com.mianbrothersbooksellerandstationers.android.activities.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.card.MaterialCardView;
import com.mianbrothersbooksellerandstationers.android.R;
import com.mianbrothersbooksellerandstationers.android.activities.IntroActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private MaterialCardView addCategories,product,sales,orders,logout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        sharedPreferences = getSharedPreferences("DAT",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        initView();
        setListeneres();
    }

    private void setListeneres() {
        addCategories.setOnClickListener(it -> {
            CharSequence options[] = new CharSequence[]
                    {
                            "Add Category",
                            "View Existing Category"
                    };
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboardActivity.this);
            builder.setTitle("Choose The Option");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(i==0)
                    {
                        startActivity(new Intent(AdminDashboardActivity.this, AddCategoryActivity.class));
                        overridePendingTransition(R.anim.anim_slideup, R.anim.anim_slidebottom);
                    }
                    if(i == 1)
                    {
                        startActivity(new Intent(AdminDashboardActivity.this, ExistCategoryActivity.class));
                        overridePendingTransition(R.anim.anim_slideup, R.anim.anim_slidebottom);
                    }
                }
            });
            builder.show();
        });
        product.setOnClickListener(it -> {
            CharSequence options[] = new CharSequence[]
                    {
                            "Add Product",
                            "View Existing Product"
                    };
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboardActivity.this);
            builder.setTitle("Choose The Option");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(i==0)
                    {
                        startActivity(new Intent(AdminDashboardActivity.this, AddProductActivity.class));
                        overridePendingTransition(R.anim.anim_slideup, R.anim.anim_slidebottom);
                    }
                    if(i == 1)
                    {
                        startActivity(new Intent(AdminDashboardActivity.this, ExistProductActivity.class));
                        overridePendingTransition(R.anim.anim_slideup, R.anim.anim_slidebottom);
                    }
                }
            });
            builder.show();
        });
        sales.setOnClickListener(it -> {
            startActivity(new Intent(AdminDashboardActivity.this, SaleActivity.class));
            overridePendingTransition(R.anim.anim_slideup, R.anim.anim_slidebottom);
        });
        orders.setOnClickListener(it -> {
            startActivity(new Intent(AdminDashboardActivity.this, OrderListActivity.class));
            overridePendingTransition(R.anim.anim_slideup, R.anim.anim_slidebottom);
        });
        logout.setOnClickListener(it -> {
            editor.clear();
            editor.apply();
            startActivity(new Intent(AdminDashboardActivity.this, IntroActivity.class));
            overridePendingTransition(R.anim.anim_slideup, R.anim.anim_slidebottom);
            finish();
        });
    }

    private void initView() {
        addCategories = findViewById(R.id.addCategories);
        product = findViewById(R.id.product);
        sales = findViewById(R.id.sales);
        orders = findViewById(R.id.orders);
        logout = findViewById(R.id.logout);
    }
}