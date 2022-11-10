package com.mianbrothersbooksellerandstationers.android.activities.customer;

import static com.mianbrothersbooksellerandstationers.android.firebase.RealTimeClass.getAllProducts;
import static com.mianbrothersbooksellerandstationers.android.firebase.RealTimeClass.getCategories;
import static com.mianbrothersbooksellerandstationers.android.firebase.RealTimeClass.requestFilterByNameProducts;
import static com.mianbrothersbooksellerandstationers.android.firebase.RealTimeClass.requestFilterProducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mianbrothersbooksellerandstationers.android.R;
import com.mianbrothersbooksellerandstationers.android.activities.BaseActivity;
import com.mianbrothersbooksellerandstationers.android.activities.IntroActivity;

public class CustomerDashboardActivity extends BaseActivity {

    RecyclerView productRv;
    ImageView filter,cartIv;
    EditText searchFilterEt;

    DrawerLayout drawerLayout;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_customer_dashboard);

        auth = FirebaseAuth.getInstance();

        if(auth == null)
        {
            SignOut();
        }

        drawerLayout = findViewById(R.id.drawerLayout);
        productRv = findViewById(R.id.productRv);
        filter = findViewById(R.id.filter);
        searchFilterEt = findViewById(R.id.searchFilterEt);
        cartIv = findViewById(R.id.cartIv);

        getAllProducts(CustomerDashboardActivity.this,productRv);

        filter.setOnClickListener(it -> {
            FilterDialog();
        });

        searchFilterEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                requestFilterByNameProducts(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cartIv.setOnClickListener(it -> {
            startActivity(new Intent(CustomerDashboardActivity.this,CartItemActivity.class));
        });

    }

    private void SignOut() {
        startActivity(new Intent(CustomerDashboardActivity.this, IntroActivity.class));
        finish();

    }

    private void FilterDialog() {
        //options to display in dialog
        String[] options = {"All", "Low Price", "High Price"};
        //dialog
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDashboardActivity.this);
        builder.setTitle("Filter Products:")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle item clicks
                        if (which == 0) {
                            //All clicked
                            String str = "All Products";
                            searchFilterEt.setHint(str);
                            requestFilterByNameProducts(""); // show all products
                        } else {
                            String optionClicked = options[which];
                            String clicked = "Search by " + optionClicked + " Product";
                            searchFilterEt.setHint(clicked);
                            requestFilterProducts(CustomerDashboardActivity.this,which,productRv);
                        }
                    }
                })
                .show();
    }

    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }

    private void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view) {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void ClickProductByCategory(View view) {
        drawerLayout.closeDrawer(GravityCompat.START);
        startActivity(new Intent(CustomerDashboardActivity.this,MainActivity.class));
    }

    public void ClickCart(View view) {
        startActivity(new Intent(CustomerDashboardActivity.this,CartItemActivity.class));
    }

    public void ClickLogOut(View view) {
        drawerLayout.closeDrawer(GravityCompat.START);
        auth.signOut();
        SignOut();

    }
}