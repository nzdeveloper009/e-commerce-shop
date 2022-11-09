package com.mianbrothersbooksellerandstationers.android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mianbrothersbooksellerandstationers.android.R;

public class BaseActivity extends AppCompatActivity {

    private Boolean doubleBackToExitPressedOnce = false;
    /**
     * This is a progress dialog instance which we will initialize later on.
     */
    private Dialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * This function is used to show the progress dialog with the title and message to user.
     */
    public void showProgressDialog(String text) {
        mProgressDialog = new Dialog(this);

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        mProgressDialog.setContentView(R.layout.dialog_progress);

        TextView tv_progress_text = mProgressDialog.findViewById(R.id.tv_progress_text);

        tv_progress_text.setText(text);

        //Start the dialog and display it on screen.
        mProgressDialog.show();
    }

    /**
     * This function is used to dismiss the progress dialog if it is visible to user.
     */
    public void hideProgressDialog() {
        mProgressDialog.dismiss();
    }

    public void doubleBackToExit() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(
                this,
                getResources().getString(R.string.please_click_back_again_to_exit),
                Toast.LENGTH_SHORT
        ).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    public void showSnackBar(String message,int id) {
        Snackbar snackBar =
                Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackBarView = snackBar.getView();
        snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                        this,
                        id
                )
        );
        snackBar.show();
    }
}