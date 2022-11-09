package com.mianbrothersbooksellerandstationers.android.activities;

import androidx.annotation.NonNull;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.mianbrothersbooksellerandstationers.android.R;
import com.mianbrothersbooksellerandstationers.android.activities.admin.AdminDashboardActivity;
import com.mianbrothersbooksellerandstationers.android.activities.customer.CustomerDashboardActivity;
import com.mianbrothersbooksellerandstationers.android.activities.customer.MainActivity;

public class LoginActivity extends BaseActivity {

    private TextInputLayout emailTIL, passwordTIL;
    private TextInputEditText passwordET, emailET;
    private TextView signInTv;

    SharedPreferences sh;
    SharedPreferences.Editor editor;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        initView();
        listeners();
        sh = getSharedPreferences("DAT", MODE_PRIVATE);
        editor = sh.edit();

    }


    @Override
    public void onBackPressed() {
        doubleBackToExit();
    }

    private void listeners() {
        signInTv.setOnClickListener(it -> {
            showProgressDialog(getResources().getString(R.string.please_wait));
            if(isValidate()){
                loginRegisteredUser();
            } else {
                hideProgressDialog();
            }
        });
        emailET.addTextChangedListener(new TextFieldValidation(emailET));
        passwordET.addTextChangedListener(new TextFieldValidation(passwordET));
    }

    private void loginRegisteredUser() {
        if(String.valueOf(emailET.getText()).equals("mianbrothers786@gmail.com"))
        {
            if(String.valueOf(passwordET.getText()).equals(getResources().getString(R.string.pwd)))
            {
                hideProgressDialog();
                editor.putString("E", String.valueOf(emailET.getText()));
                editor.putBoolean("IsLOGGEDIN", true);
                editor.apply();
                Toast.makeText(LoginActivity.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                startActivity(intent);
                finish();

            } else {
                hideProgressDialog();
                showSnackBar("Your password is incorrect",R.color.snackbar_error_color);
            }
        } else {
            customerLogin();
        }
    }

    private void customerLogin() {
        auth.signInWithEmailAndPassword(String.valueOf(emailET.getText()), String.valueOf(passwordET.getText()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Get Instance of the current user
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser.isEmailVerified()) {
                                Toast.makeText(getApplicationContext(), "You are logged in now", Toast.LENGTH_SHORT).show();
                                //open dashboard
                                Intent intent = new Intent(LoginActivity.this, CustomerDashboardActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_slideup, R.anim.anim_slidebottom);
                                finish();
                            } else {
                                hideProgressDialog();
                                firebaseUser.sendEmailVerification();
                                auth.signOut(); // sign out user
                                showAlertDialog();
                            }
                        } else {
                            hideProgressDialog();
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                emailTIL.setError("User does not exists or is no longer valid. Please register again");
                                emailET.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                emailTIL.setError("Invalid credentials. Kindly, check and re-enter.");
                                emailET.requestFocus();
                            } catch (Exception e) {
                                Log.e("MyTag", "onComplete: " + e.getMessage());
                                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(getApplicationContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showAlertDialog() {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email now. You can not login without email verification.");

        // open email apps if user clicks/taps continue button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //To email app in new window and not within our app
                startActivity(intent);
            }
        });

        //Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        //Show the AlertDialog
        alertDialog.show();
    }

    private void initView() {

        emailTIL = findViewById(R.id.emailTIL);
        passwordTIL = findViewById(R.id.passwordTIL);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        signInTv = findViewById(R.id.signInTv);

        emailTIL.setBoxStrokeColorStateList(setStrokeColorState());
        passwordTIL.setBoxStrokeColorStateList(setStrokeColorState());

    }

    private ColorStateList setStrokeColorState() {
        //Color from hex string
        int color = Color.parseColor("#0C90F1");

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_focused}, // focused
                new int[]{android.R.attr.state_hovered}, // hovered
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{}  //
        };

        int[] colors = new int[]{
                color,
                color,
                color,
                color
        };

        ColorStateList mColorList = new ColorStateList(states, colors);
        return mColorList;
    }

    private boolean isValidate() {
        return (validateEmail() | validatePassword());
    }

    private boolean validatePassword() {
        if (String.valueOf(passwordET.getText()).isEmpty()) {
            passwordTIL.setError("Required Field!");
            passwordET.requestFocus();
            return false;
        } else {
            passwordTIL.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        if (String.valueOf(emailET.getText()).isEmpty()) {
            emailTIL.setError("Required Field!");
            emailET.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(String.valueOf(emailET.getText())).matches()) {
            emailTIL.setError("Email is not valid");
            emailET.requestFocus();
            return false;
        } else {
            emailTIL.setErrorEnabled(false);
        }
        return true;
    }


    /**
     * applying text watcher on each text field
     */
    class TextFieldValidation implements TextWatcher {

        /*       TextInputEditText v;

               public TextFieldValidation(TextInputEditText view) {
                   this.v = view;
               }*/
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
                case R.id.emailET:
                    validateEmail();
                    break;
                case R.id.passwordET:
                    validatePassword();
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}