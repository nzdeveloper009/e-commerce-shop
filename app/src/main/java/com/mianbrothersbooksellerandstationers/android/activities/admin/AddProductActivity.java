package com.mianbrothersbooksellerandstationers.android.activities.admin;

import static com.mianbrothersbooksellerandstationers.android.firebase.RealTimeClass.addToStorage;
import static com.mianbrothersbooksellerandstationers.android.firebase.RealTimeClass.uploadCategoryProduct;
import static com.mianbrothersbooksellerandstationers.android.utils.Constants.CATEGORY;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mianbrothersbooksellerandstationers.android.R;
import com.mianbrothersbooksellerandstationers.android.activities.BaseActivity;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddProductActivity extends BaseActivity {

    private CircleImageView imageIv;
    private TextInputLayout nameTIL, shortDescTIL, priceTIL, stockTIL, categoriesTIL;
    private TextInputEditText nameET, shortDescriptionET, priceET, stockET;
    private AppCompatButton addBtn;
    String nameStr, shortDescStr;
    Double priceDouble;
    int stockInt;

    private AppCompatEditText categoriesET;
    private String selectedCategory = "";
    private Boolean isSelect = false;

    private Uri fileUri;
    private String myUri = "";
    private String preSelectedCat;
    private boolean isPreSelectedCat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_add_product);
        initView();
        listeners();
        if (getIntent().hasExtra(CATEGORY)) {
            preSelectedCat = getIntent().getStringExtra(CATEGORY);
            isPreSelectedCat = true;
            categoriesET.setText(preSelectedCat);
            selectedCategory = preSelectedCat;
            categoriesTIL.setEnabled(false);
            categoriesET.setEnabled(false);
            isSelect = true;
        }

    }


    private void runtimePermission() {
        Dexter.withContext(AddProductActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        showProgressDialog(getResources().getString(R.string.please_click_back_again_to_exit));
                        Intent galleryIntent = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        );
                        startActivityForResult(galleryIntent, 438);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            Glide.with(AddProductActivity.this)
                    .load(fileUri)
                    .centerCrop() // Scale type of the image.
                    .placeholder(R.drawable.ic_place_holder) // A default place holder
                    .into(imageIv);
        }
    }


    private void listeners() {
        shortDescriptionET.addTextChangedListener(new TextFieldValidation(shortDescriptionET));
        nameET.addTextChangedListener(new TextFieldValidation(nameET));
        priceET.addTextChangedListener(new TextFieldValidation(priceET));
        stockET.addTextChangedListener(new TextFieldValidation(stockET));
        categoriesET.addTextChangedListener(new TextFieldValidation(categoriesET));

        imageIv.setOnClickListener(this::PickFile);

        addBtn.setOnClickListener(it -> {
            showProgressDialog(getResources().getString(R.string.please_wait));
            if (fileUri.toString().isEmpty()) {
                hideProgressDialog();
                showSnackBar("Please Select Image First", R.color.snackbar_warning_color);
            } else {
                if (isValidate()) {
                    nameStr = String.valueOf(nameET.getText());
                    shortDescStr = String.valueOf(shortDescriptionET.getText());
                    priceDouble = Double.parseDouble(String.valueOf(priceET.getText()));
                    stockInt = Integer.parseInt(String.valueOf(stockET.getText()));
                    selectedCategory = String.valueOf(categoriesET.getText());
                    addToStorage(AddProductActivity.this, selectedCategory, fileUri);
                }
            }

        });
    }

    private boolean isValidate() {
        return (validateCategory() && validateNameEt() && validateShortDescription() && validateET(priceET, priceTIL) && validateET(stockET, stockTIL));
    }

    private void PickFile(View view) {
        ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start(101);
    }

    private void initView() {
        imageIv = findViewById(R.id.iv_image);

        nameTIL = findViewById(R.id.nameTIL);
        shortDescTIL = findViewById(R.id.shortDescTIL);
        priceTIL = findViewById(R.id.priceTIL);
        stockTIL = findViewById(R.id.stockTIL);

        nameET = findViewById(R.id.nameET);
        shortDescriptionET = findViewById(R.id.shortDescriptionET);
        priceET = findViewById(R.id.priceET);
        stockET = findViewById(R.id.stockET);
        addBtn = findViewById(R.id.addBtn);

        categoriesET = findViewById(R.id.categoriesET);
        categoriesTIL = findViewById(R.id.categoriesTIL);
    }


    public void clearField() {
        nameET.getText().clear();
        shortDescriptionET.getText().clear();
        selectedCategory = "";
        preSelectedCat = "";
        isSelect = false;
        isPreSelectedCat = false;
        myUri = "";
        categoriesTIL.setEnabled(true);
        categoriesET.getText().clear();
        categoriesET.setEnabled(true);
        priceET.getText().clear();
        stockET.getText().clear();
        hideProgressDialog();
    }

    public void getStorageFirebaseUrl(String mUri) {
        myUri = mUri;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String key = String.valueOf(reference.push().getKey());

        HashMap<String, Object> data = new HashMap<>();
        String ts = "" + System.currentTimeMillis();

        data.put("desc", shortDescStr);
        data.put("image", mUri);
        data.put("inStock", true);
        data.put("name", nameStr);
        data.put("price", priceDouble);
        data.put("stock", stockInt);
        data.put("category", selectedCategory);
        data.put("key", key);
        data.put("timestamp", ts);
        uploadCategoryProduct(AddProductActivity.this, data);
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
                case R.id.shortDescriptionET:
                    validateShortDescription();
                    break;
                case R.id.nameET:
                    validateNameEt();
                    break;
                case R.id.stockET:
                    validateET(stockET, stockTIL);
                    break;
                case R.id.priceET:
                    validateET(priceET, priceTIL);
                    break;
                case R.id.categoriesET:
                    validateCategory();
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
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
        } else {
            categoriesTIL.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateET(TextInputEditText et, TextInputLayout til) {
        if (String.valueOf(et.getText()).isEmpty()) {
            til.setError("Required Field!");
            et.requestFocus();
            return false;
        } else if (Float.parseFloat(String.valueOf(et.getText())) <= 0) {
            til.setError("Please Enter more than 0");
            et.requestFocus();
            return false;
        } else {
            til.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateNameEt() {
        if (String.valueOf(nameET.getText()).isEmpty()) {
            nameTIL.setError("Required Field!");
            nameET.requestFocus();
            return false;
        } else if (String.valueOf(nameET.getText()).length() < 3) {
            nameTIL.setError("Please Enter Full Name");
            nameET.requestFocus();
            return false;
        } else if (String.valueOf(nameET.getText()).length() > 50) {
            nameTIL.setError("Entered name is too long!");
            nameET.requestFocus();
            return false;
        } else {
            nameTIL.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateShortDescription() {
        if (String.valueOf(shortDescriptionET.getText()).isEmpty()) {
            shortDescTIL.setError("Required Field!");
            shortDescriptionET.requestFocus();
            return false;
        } else if (String.valueOf(shortDescriptionET.getText()).length() < 10) {
            shortDescTIL.setError("Short Description is too short");
            shortDescriptionET.requestFocus();
            return false;
        } else if (String.valueOf(shortDescriptionET.getText()).length() > 80) {
            shortDescTIL.setError("Short Description can\'t more than 80 character long");
            shortDescriptionET.requestFocus();
            return false;
        } else {
            shortDescTIL.setErrorEnabled(false);
        }
        return true;
    }
}