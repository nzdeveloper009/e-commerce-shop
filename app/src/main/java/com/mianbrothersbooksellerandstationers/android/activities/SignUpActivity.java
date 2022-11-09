package com.mianbrothersbooksellerandstationers.android.activities;

import static com.mianbrothersbooksellerandstationers.android.utils.Constants.PASSWORD_PATTERN;
import static com.mianbrothersbooksellerandstationers.android.utils.Constants.isValidPattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mianbrothersbooksellerandstationers.android.R;

import java.util.HashMap;

public class SignUpActivity extends BaseActivity {

    private  AppCompatButton btnSigUp;
    private  AppCompatEditText et_name,et_email,et_password,et_cnf_password;
    private  TextInputLayout til_name,til_email,til_password,til_cnf_password;
    private FirebaseAuth auth;
    String emailStr,passwordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        setupActionBar();
        initView();
        listener();
    }

    private void listener() {

        btnSigUp.setOnClickListener(it -> {
            emailStr = String.valueOf(et_email.getText());
            passwordStr = String.valueOf(et_password.getText());
            createUserInDatabase();
        });
    }

    private void createUserInDatabase() {
        showProgressDialog(getResources().getString(R.string.please_wait));
        auth.createUserWithEmailAndPassword(emailStr,passwordStr)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(useridd);

                            HashMap<String,Object> data = new HashMap<>();
                            data.put("email",emailStr);
                            data.put("id",useridd);

                            databaseReference.setValue(data)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                hideProgressDialog();
                                                firebaseUser.sendEmailVerification();
                                                auth.signOut();
                                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                overridePendingTransition(R.anim.anim_slideup, R.anim.anim_slidebottom);
                                                startActivity(intent);
                                                Toast.makeText(SignUpActivity.this, "User Register Successfully", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                hideProgressDialog();
                                                showSnackBar(task.getException().getMessage().toString(),R.color.snackbar_error_color);
                                            }
                                        }
                                    });
                        } else {
                            showSnackBar(task.getException().getMessage().toString(),R.color.snackbar_error_color);
                        }
                    }
                });
    }

    private void setupActionBar() {
        Toolbar toolbar_sign_up_activity = findViewById(R.id.toolbar_sign_up_activity);

        setSupportActionBar(toolbar_sign_up_activity);

         ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp);
        }
        toolbar_sign_up_activity.setNavigationOnClickListener(it ->{
            onBackPressed();
        });
    }

    private void initView() {
        btnSigUp  = findViewById(R.id.btn_sign_up);
        et_name  = findViewById(R.id.et_name);
        et_email  = findViewById(R.id.et_email);
        et_password  = findViewById(R.id.et_password);
        et_cnf_password  = findViewById(R.id.et_cnf_password);
        til_name  = findViewById(R.id.til_name);
        til_email  = findViewById(R.id.til_email);
        til_cnf_password  = findViewById(R.id.til_cnf_password);
        til_password  = findViewById(R.id.til_password);
    }

    /**
     * A function for Validation Setup.
     */
    private boolean isValidate() {
        return (validateFullName() && validateGmail() && validatePassword() && validateConfirmPassword());
    }

    /**
     * applying text watcher on each text field
     */
    class TextFieldValidation implements TextWatcher {

        View v;

        public TextFieldValidation(View view) {
            this.v = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // checking ids of each text field and applying functions accordingly.
            switch (v.getId()) {
                case R.id.et_name:
                    validateFullName();
                    break;
                case R.id.et_email:
                    validateGmail();
                    break;
                case R.id.et_password:
                    validatePassword();
                    break;
                case R.id.et_cnf_password:
                    validateConfirmPassword();
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private boolean validateConfirmPassword() {
        if (String.valueOf(et_cnf_password.getText()).isEmpty()) {
            til_cnf_password.setError("Required Field!");
            et_cnf_password.requestFocus();
            return false;
        } else if (!String.valueOf(et_cnf_password.getText()).equals(String.valueOf(et_password.getText()))) {
            til_cnf_password.setError("Password not Matched");
            et_cnf_password.requestFocus();
            return false;
        } else {
            til_cnf_password.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {

        if (String.valueOf(et_password.getText()).isEmpty()) {
            til_password.setError("Required Field!");
            et_password.requestFocus();
            return false;
        } else if (String.valueOf(et_password.getText()).length() < 8) {
            til_password.setError("Password must more then 8");
            et_password.requestFocus();
            return false;
        } else if (!isValidPattern(
                String.valueOf(et_password.getText()),
                PASSWORD_PATTERN
        )
        ) {
            til_password.setError("Please enter strong password including capital, lower case letter, special symbol and number");
            et_password.requestFocus();
            return false;
        } else {
            til_password.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateGmail() {
        if (String.valueOf(et_email.getText()).isEmpty()) {
            til_email.setError("Required Field!");
            et_email.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(String.valueOf(et_email.getText())).matches()) {
            til_email.setError("Email is not valid");
            et_email.requestFocus();
            return false;
        } else {
            til_email.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateFullName() {
        if (String.valueOf(et_name.getText()).isEmpty()) {
            til_name.setError("Required Field!");
            et_name.requestFocus();
            return false;
        } else if (et_name.getText().toString().length() < 3) {
            til_name.setError("Please Enter Full Name");
            et_name.requestFocus();
            return false;
        } else if( et_name.getText().toString().length() > 50){
            til_name.setError("Entered name is too long!");
            et_name.requestFocus();
            return false;
        }else {
            til_name.setErrorEnabled(false);
        }
        return true;
    }
}