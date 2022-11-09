package com.mianbrothersbooksellerandstationers.android.activities.customer;

import static com.mianbrothersbooksellerandstationers.android.firebase.RealTimeClass.getCategories;
import static com.mianbrothersbooksellerandstationers.android.firebase.RealTimeClass.requestFilterByNameProducts;
import static com.mianbrothersbooksellerandstationers.android.firebase.RealTimeClass.requestFilterProducts;
import static com.mianbrothersbooksellerandstationers.android.utils.Constants.MY_PROFILE_REQUEST_CODE;
import static com.mianbrothersbooksellerandstationers.android.utils.Constants.PROFILE;
import static com.mianbrothersbooksellerandstationers.android.utils.Constants.SIGN_OUT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mianbrothersbooksellerandstationers.android.R;
import com.mianbrothersbooksellerandstationers.android.activities.BaseActivity;
import com.mianbrothersbooksellerandstationers.android.activities.IntroActivity;
import com.mianbrothersbooksellerandstationers.android.adapters.CustomerProductAdapter;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth authProfile;
    DrawerLayout drawer_layout;

    RecyclerView categoryRv, productRv;
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
        setContentView(R.layout.activity_main);

        authProfile = FirebaseAuth.getInstance();
        setupActionBar();
        drawer_layout = findViewById(R.id.drawer_layout);
        categoryRv = findViewById(R.id.categoryRv);
        productRv = findViewById(R.id.productRv);
        filter = findViewById(R.id.filter);
        searchFilterEt = findViewById(R.id.searchFilterEt);

        getCategories(MainActivity.this, categoryRv, productRv, true);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                            requestFilterProducts(MainActivity.this,which,productRv);
                        }
                    }
                })
                .show();
    }


    /**
     * A function to setup action bar
     */
    private void setupActionBar() {

        Toolbar toolbar_main_activity = findViewById(R.id.toolbar_main_activity);
        toolbar_main_activity.setTitle("Mian Brothers");
        setSupportActionBar(toolbar_main_activity);
        toolbar_main_activity.setNavigationIcon(R.drawable.ic_action_navigation_menu);
        toolbar_main_activity.setNavigationOnClickListener(it -> {
            toggleDrawer();
        });
    }

    private void toggleDrawer() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            drawer_layout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            // A double back press function is added in Base Activity.
            doubleBackToExit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Toast.makeText(this, "Toast", Toast.LENGTH_SHORT).show();
        if(menuItem.getItemId() == PROFILE)
        {
            startActivityForResult(new Intent(this, MyProfileActivity.class), MY_PROFILE_REQUEST_CODE);

        } else if(menuItem.getItemId() == SIGN_OUT)
        {
            Toast.makeText(this, "This is sign out", Toast.LENGTH_LONG).show();
            // Here sign outs the user from firebase in this device.
            authProfile.signOut();
            // Send the user to the intro screen of the application.
            Intent intent = new Intent(MainActivity.this, IntroActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
       /* switch (menuItem.getItemId()) {
            case PROFILE: {
                break;
            }

            case SIGN_OUT: {
                Toast.makeText(this, "This is sign out", Toast.LENGTH_LONG).show();
                // Here sign outs the user from firebase in this device.
                authProfile.signOut();
                // Send the user to the intro screen of the application.
                Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            }

        }*/
        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }

}