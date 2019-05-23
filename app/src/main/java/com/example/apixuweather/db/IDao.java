package com.example.apixuweather.db;

import android.util.Log;

import com.example.apixuweather.rest.response.apixu.Astro;
import com.example.apixuweather.rest.response.apixu.DataLocation;
import com.example.apixuweather.rest.response.apixu.ForecastResponse;
import com.example.apixuweather.rest.response.apixu.Forecastday;
import com.example.apixuweather.rest.response.apixu.Hour;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import io.reactivex.Flowable;
import io.reactivex.Single;

import static com.example.apixuweather.utils.Const.I_TAG;

@Dao
public abstract class IDao {

    public static final String TAG = I_TAG + IDao.class.getSimpleName();

    /**
     * ForecastResponse
     */

    @Query("SELECT * FROM current WHERE locationId = :id")
    public abstract Flowable<List<ForecastResponse>> getForecastResponse(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract List<Long> insertCurrent(List<ForecastResponse> currents);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertCurrent(ForecastResponse response);

    @Query("DELETE FROM current WHERE locationId = :id")
    public abstract int deleteCurrent(String id);

    @Query("SELECT * FROM current")
    public abstract Flowable<List<ForecastResponse>> getAllLocation();

    @Query("SELECT * FROM current")
    public abstract Single<List<ForecastResponse>> getAllForecast();

    @Query("SELECT locationId, timeZone, name, region, lat, lon FROM current WHERE locationId = :id")
    public abstract Single<DataLocation> getLocation(String id);

    /**
     * Hourly
     */
    @Query("SELECT * FROM hourly WHERE locationId = :id")
    public abstract Flowable<List<Hour>> getHourly(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract List<Long> insertHourly(List<Hour> hours);

    @Query("DELETE FROM hourly WHERE locationId = :id")
    public abstract int deleteHourly(String id);

    @Query("SELECT * FROM hourly WHERE locationId = :id AND timeEpoch > :date ORDER BY timeEpoch LIMIT 24")
    public abstract Flowable<List<Hour>> getNext24(String id, long date);

    @Query("SELECT * FROM hourly WHERE locationId = :id AND timeEpoch >= :first ORDER BY timeEpoch LIMIT 24")
    public abstract Flowable<List<Hour>> get24(String id, long first);

    @Query("SELECT * FROM hourly WHERE locationId = :id AND (timeEpoch = :n1 OR timeEpoch = :n2 OR timeEpoch = :n3 OR timeEpoch = :n4)")
    public abstract Flowable<List<Hour>> getPartOfDay(String id, long n1, long n2, long n3, long n4);

    @Query("SELECT * FROM hourly WHERE locationId = :id AND timeEpoch = :time")
    public abstract Flowable<List<Hour>> getCurrentHour(String id, long time);

    @Query("SELECT * FROM hourly WHERE timeEpoch = :time")
    public abstract Flowable<List<Hour>> getLastHour(long time);

    /**
     * Daily
     */

    @Query("SELECT * FROM daily WHERE locationId = :id")
    public abstract Flowable<List<Forecastday>> getDaily(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract List<Long> insertDaily(List<Forecastday> forecastdays);

    @Query("DELETE FROM daily WHERE locationId = :id")
    public abstract int deleteDaily(String id);

    @Query("SELECT dateEpoch FROM daily WHERE locationId = :id AND dateEpoch >= :date")
    public abstract Flowable<List<Long>> getDate(String id, int date);

    @Query("SELECT timeZone FROM current WHERE locationId = :id")
    public abstract Flowable<Integer> getTimeZone(String id);

    @Query("SELECT sunrise, sunset, moonrise, moonset, moonPhase, moonIllumination FROM daily WHERE locationId = :id AND dateEpoch = :date")
    public abstract Flowable<List<Astro>> getAstro(String id, long date);

    /**
     * Transaction
     */

    @Transaction
    public void add(ForecastResponse response) {
        Log.i(TAG, "add: " + response.getLocation().getName() + " " + response.getLocation().getRegion());
        response.getCurrent().setLastUpdatedEpoch(OffsetDateTime.now(ZoneOffset.UTC).toEpochSecond());
        String locationId = response.getLocationId();
        deleteCurrent(locationId);
        deleteHourly(locationId);
        deleteDaily(locationId);
        insertCurrent(response);
        insertDaily(response.getForecast().getForecastday());
        for (Forecastday forecastday : response.getForecast().getForecastday()) {
            insertHourly(forecastday.getHour());
        }
    }

    @Transaction
    public void delete(String id) {
        Log.i(TAG, "delete");
        Log.i(TAG, "delete current: " + deleteCurrent(id));
        Log.i(TAG, "delete hourly: " + deleteHourly(id));
        Log.i(TAG, "delete daily: " + deleteDaily(id));
    }

    @Transaction
    public void update(ForecastResponse response) {
        Log.i(TAG, "update: " + response.getLocation().getName() + " " + response.getLocation().getRegion());
        response.getCurrent().setLastUpdatedEpoch(OffsetDateTime.now(ZoneOffset.UTC).toEpochSecond());

        deleteDaily(response.getLocationId());
        deleteHourly(response.getLocationId());
        deleteCurrent(response.getLocationId());

        insertCurrent(response);
        insertDaily(response.getForecast().getForecastday());
        for (Forecastday forecastday : response.getForecast().getForecastday()) {
            insertHourly(forecastday.getHour());
        }
    }

    /**Notification*/
    @Query("SELECT * FROM current WHERE locationId = 'current_location'")
    public abstract Flowable<List<ForecastResponse>> getCurrentLocationForecast();

    @Query("SELECT * FROM hourly WHERE locationId = 'current_location' AND timeEpoch = :time")
    public abstract Flowable<List<Hour>> getCurrentLocationLastHour(long time);
}
