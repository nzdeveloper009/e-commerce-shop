package com.mianbrothersbooksellerandstationers.android.firebase;

import static com.mianbrothersbooksellerandstationers.android.utils.Constants.CATEGORY;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mianbrothersbooksellerandstationers.android.R;
import com.mianbrothersbooksellerandstationers.android.activities.admin.AddProductActivity;
import com.mianbrothersbooksellerandstationers.android.adapters.CategoryAdapter;
import com.mianbrothersbooksellerandstationers.android.adapters.CustomerProductAdapter;
import com.mianbrothersbooksellerandstationers.android.adapters.ProductAdapter;
import com.mianbrothersbooksellerandstationers.android.models.ProductModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class RealTimeClass {


    // Create a instance of Firebase RealTime Database
    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static Boolean ret = null;
    private static String myUri;
    private static CategoryAdapter categoryAdapter;
    private static ProductAdapter productAdapter;
    private static CustomerProductAdapter customerProductAdapter;
    private static ArrayList<ProductModel> productList = new ArrayList<>();

    public static void createCategory(Activity activity, String category) {
        Intent intent = new Intent(activity, AddProductActivity.class);
        intent.putExtra(CATEGORY, category);
        activity.startActivity(intent);
    }

    public static void getCategories(Activity activity, RecyclerView categoryRv, RecyclerView productRv, boolean b) {

        ArrayList<String> categoryList = new ArrayList<>();
        mDatabase.child(CATEGORY)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        categoryList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            categoryList.add(ds.getKey());
                            Log.d("ListCat", "onDataChange: " + ds.getKey());
                        }
                        if (b) {
                            categoryAdapter = new CategoryAdapter(activity.getApplicationContext(), categoryList, productRv, true);
                            categoryRv.setAdapter(categoryAdapter);
                        } else {
                            categoryAdapter = new CategoryAdapter(activity.getApplicationContext(), categoryList, productRv);
                            categoryRv.setAdapter(categoryAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    public static void addToStorage(AddProductActivity activity, String selectedCategory, Uri fileUri) {

        String timestamp = "" + System.currentTimeMillis();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        Log.d("URLEMPTY", "URLEMPTY: timestamp" + timestamp + "\n" + "CATEGORY: " + CATEGORY + "\nselectedCategory: " + selectedCategory + "\nfileUri: " + fileUri);
        storageReference.child(CATEGORY).child(selectedCategory).child(timestamp).putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                task.addOnCompleteListener(uri -> {
                    if (uri.isSuccessful()) {
                        myUri = uri.getResult().toString();
                        Log.d("AddToStorage", "AddToStorage: " + myUri);
                        activity.hideProgressDialog();
                        activity.getStorageFirebaseUrl(myUri);
                    } else {
                        activity.hideProgressDialog();
                        Toast.makeText(activity, "Error!! " + taskSnapshot.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public static void uploadCategoryProduct(AddProductActivity activity, HashMap<String, Object> data) {


        String cat = String.valueOf(data.get("category"));
        String key = String.valueOf(data.get("key"));
        activity.showProgressDialog("Uploading Product into Category, please wait");
        Log.d("INUploadCatProd", "INUploadCatProd: cat: " + cat + "\nkey: " + key);
        mDatabase.child("Products")
                .child(key)
                .setValue(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mDatabase.child(CATEGORY).child(cat)
                                    .child(key)
                                    .setValue(data)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(Task<Void> task) {
                                            Log.d("InCompleteSide", "InCompleteSide: " + task.isSuccessful());
                                            if (task.isSuccessful()) {
                                                activity.hideProgressDialog();
                                                Log.d("InSuccessSide", "InSuccessSide: ");
                                                activity.showSnackBar("Successful Add Product in the Category", R.color.snackbar_success_color);
                                                activity.clearField();
                                            } else {
                                                Log.d("InElseSide", "InElseSide: ");
                                                activity.hideProgressDialog();
                                                activity.showSnackBar("Error: " + task.getException().getMessage(), R.color.snackbar_error_color);
                                            }
                                        }
                                    });
                        } else {
                            activity.showSnackBar(task.getException().getMessage().toString(), R.color.snackbar_error_color);
                        }
                    }
                });

    }

    public static void getProductsofCategory(Context context, String s, RecyclerView productRv, boolean b) {
//        init Array list

        mDatabase.child(CATEGORY).child(s)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Clear list before adding new data in it
                        productList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ProductModel model = ds.getValue(ProductModel.class);
                            Log.d("PRODUCTMODEL", "onDataChange: " + model.getName());
                            // add to list
                            productList.add(model);
                        }
                        //setup adapter
                        if (b) {
                            customerProductAdapter = new CustomerProductAdapter(context, productList);
                            //set adapter to recyclerview
                            productRv.setAdapter(customerProductAdapter);
                        } else {
                            productAdapter = new ProductAdapter(context, productList);
                            //set adapter to recyclerview
                            productRv.setAdapter(productAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public static void getAllProducts(Activity activity, RecyclerView productRv) {

        mDatabase.child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Clear list before adding new data in it
                        productList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ProductModel model = ds.getValue(ProductModel.class);
                            Log.d("PRODUCTMODEL", "onDataChange: " + model.getName());
                            // add to list
                            productList.add(model);
                        }
                        //setup adapter
                        customerProductAdapter = new CustomerProductAdapter(activity, productList);
                        //set adapter to recyclerview
                        productRv.setAdapter(customerProductAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public static void requestFilterByNameProducts(String i) {
        customerProductAdapter.getFilter().filter(i);
    }

    public static void requestFilterProducts(Activity activity, int i, RecyclerView productRv) {
        if (i == 1) {
            Collections.sort(productList, new Comparator<ProductModel>() {
                @Override
                public int compare(ProductModel productModel, ProductModel t1) {
                    return productModel.getPrice() - t1.getPrice();
                }
            });
        } else {
            Collections.sort(productList, new Comparator<ProductModel>() {
                @Override
                public int compare(ProductModel productModel, ProductModel t1) {
                    return t1.getPrice() - productModel.getPrice();
                }
            });

        }

        customerProductAdapter = new CustomerProductAdapter(activity, productList);
        productRv.setAdapter(customerProductAdapter);

    }

    /**
     * A function for getting the user id of current logged user.
     */
    public static String getCurrentUserID() {
        // An Instance of currentUser using FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        String currentUserID = "";
        if (currentUser != null) {
            currentUserID = currentUser.getUid();
        }

        return currentUserID;
    }
}
