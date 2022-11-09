package com.mianbrothersbooksellerandstationers.android.utils;

import com.mianbrothersbooksellerandstationers.android.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {
    // digit + lowercase char + uppercase char + punctuation + symbol
    public static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

    public static final String CATEGORY = "Category";

    //A unique code for starting the activity for result
    public static final int MY_PROFILE_REQUEST_CODE = 11;

    public static final int PROFILE = R.id.nav_my_profile;
    public static final int SIGN_OUT = R.id.nav_sign_out;
    public static final int CART = R.id.nav_my_cart;

    public static Boolean isValidPattern(String input, String CHECK_PATTERN) {
        Pattern pattern = Pattern.compile(CHECK_PATTERN);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
