package com.example.finalprojectgymapp.util;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Filter out special characters, only allow numbers
public class DecimalInputFilter implements InputFilter {
    private Pattern pattern;

    public DecimalInputFilter(int maxDigitsBeforeDecimal, int maxDigitsAfterDecimal) {
        pattern = Pattern.compile("^-?\\d{0," + maxDigitsBeforeDecimal + "}(\\.\\d{0," + maxDigitsAfterDecimal + "})?$");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String result = dest.subSequence(0, dstart) + source.toString() + dest.subSequence(dend, dest.length());
        Matcher matcher = pattern.matcher(result);
        if (!matcher.matches()) {
            return "";
        }
        return null;
    }
}
