package com.example.apixuweather.utils;

import java.text.DecimalFormat;

public class INumberFormatter {
    private INumberFormatter(){}

    public static final ThreadLocal<DecimalFormat> SIGNED;
    public static final ThreadLocal<DecimalFormat> UNSIGNED;

    static {
        SIGNED = new ThreadLocal<DecimalFormat>(){
            @Override
            protected DecimalFormat initialValue() {
                return new DecimalFormat("+0;-0");
            }
        };

        UNSIGNED = new ThreadLocal<DecimalFormat>(){
            @Override
            protected DecimalFormat initialValue() {
                return new DecimalFormat("0");
            }
        };
    }
}
