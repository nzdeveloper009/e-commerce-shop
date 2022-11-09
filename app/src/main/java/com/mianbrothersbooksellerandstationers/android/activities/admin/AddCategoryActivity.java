package com.mianbrothersbooksellerandstationers.android.activities.admin;

import static com.mianbrothersbooksellerandstationers.android.firebase.RealTimeClass.createCategory;
import static com.mianbrothersbooksellerandstationers.android.utils.Constants.PASSWORD_PATTERN;
import static com.mianbrothersbooksellerandstationers.android.utils.Constants.isValidPattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatCallback;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.mianbrothersbooksellerandstationers.android.R;
import com.mianbrothersbooksellerandstationers.android.activities.BaseActivity;

public class AddCategoryActivity extends BaseActivity {

    private AppCompatButton addCategoryBTN;
    private AppCompatEditText categoriesET;
    private TextInputLayout categoriesTIL;
    private String selectedCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_add_category);

        initView();
        listener();

    }

    private void listener() {
        addCategoryBTN.setOnClickListener(it -> {
            if(validateCategory())
            {
                selectedCategory = String.valueOf(categoriesET.getText());
                createCategory(AddCategoryActivity.this,selectedCategory);
            }
        });
    }

    private boolean validateCategory() {
        if (String.valueOf(categoriesET.getText()).isEmpty()) {
            categoriesTIL.setError("Required Field!");
            categoriesET.requestFocus();
            return false;
        } else if (String.valueOf(categoriesET.getText()).length() < 3) {
            categoriesTIL.setError("Please Enter Full Name of Category");
            categoriesET.requestFocus();
            return false;
        }  else {
            categoriesTIL.setErrorEnabled(false);
        }

        return true;
    }

    private void initView() {
        addCategoryBTN = findViewById(R.id.addCategoryBTN);
        categoriesTIL = findViewById(R.id.categoriesTIL);
        categoriesET = findViewById(R.id.categoriesET);
    }


}