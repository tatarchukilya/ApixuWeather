package com.example.apixuweather.db;

import com.example.apixuweather.rest.response.apixu.ForecastResponse;
import com.example.apixuweather.rest.response.apixu.Forecastday;
import com.example.apixuweather.rest.response.apixu.Hour;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ForecastResponse.class, Forecastday.class, Hour.class}, version = 1, exportSchema = false)
public abstract class IDataBase extends RoomDatabase {
    public abstract IDao getDao();
}
