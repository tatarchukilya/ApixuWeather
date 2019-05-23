package com.example.apixuweather.utils.unit;

import org.jetbrains.annotations.Contract;

public enum TempUnit {

    Celsius(0), Fahrenheit(1);

    private int mId;

    TempUnit(int id) {
        mId = id;
    }

    @Contract(pure = true)
    public int getId() {
        return mId;
    }

    @Contract(pure = true)
    public static TempUnit toEnum(int id){
        switch (id){
            case 1: return Fahrenheit;
            default: return Celsius;
        }
    }
}
