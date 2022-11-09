package com.mianbrothersbooksellerandstationers.android.activities.customer;

import static com.mianbrothersbooksellerandstationers.android.firebase.RealTimeClass.getAllProducts;
import static com.mianbrothersbooksellerandstationers.android.firebase.RealTimeClass.getCategories;
import static com.mianbrothersbooksellerandstationers.android.firebase.RealTimeClass.requestFilterByNameProducts;
import static com.mianbrothersbooksellerandstationers.android.firebase.RealTimeClass.requestFilterProducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.mianbrothersbooksellerandstationers.android.R;

public class CustomerDashboardActivity extends AppCompatActivity{

    RecyclerView productRv;
    ImageView filter;
    EditText searchFilterEt;

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

        productRv = findViewById(R.id.productRv);
        filter = findViewById(R.id.filter);
        searchFilterEt = findViewById(R.id.searchFilterEt);

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

}