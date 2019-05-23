package com.example.apixuweather.repo;

import android.content.Context;

import com.example.apixuweather.IApplication;
import com.example.apixuweather.db.IDao;
import com.example.apixuweather.db.IDataBase;
import com.example.apixuweather.rest.response.apixu.Astro;
import com.example.apixuweather.rest.response.apixu.DataLocation;
import com.example.apixuweather.rest.response.apixu.ForecastResponse;
import com.example.apixuweather.rest.response.apixu.Forecastday;
import com.example.apixuweather.rest.response.apixu.Hour;
import com.example.apixuweather.ui.base.BaseItem;
import com.example.apixuweather.ui.daily.recycler.PartDayItem;
import com.example.apixuweather.ui.daily.recycler.SunStateItem;
import com.example.apixuweather.ui.navigation.recycler.LocationItem;
import com.example.apixuweather.utils.IDateFormatter;
import com.example.apixuweather.utils.ILogger;

import org.threeten.bp.LocalTime;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import androidx.room.Room;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

import static com.example.apixuweather.utils.Const.CURRENT_LOCATION_ID;
import static com.example.apixuweather.utils.Const.I_TAG;

public class DataBaseModel {

    public static final String TAG = I_TAG + DataBaseModel.class.getSimpleName();

    @Inject
    Context mContext;

    private final IDao mDao;

    public DataBaseModel() {
        IApplication.getAppComponent().inject(this);
        mDao = Room.databaseBuilder(mContext, IDataBase.class, "weather_db").build().getDao();
    }

    public void add(ForecastResponse response) {
        Executors.newFixedThreadPool(Integer.MAX_VALUE).submit(() -> mDao.add(response));
    }

    public void delete(String id) {
        Executors.newFixedThreadPool(Integer.MAX_VALUE).submit(() -> mDao.delete(id));
    }

    public Flowable<List<ForecastResponse>> getForecastResponse(String id) {
        return mDao.getForecastResponse(id);
    }

    public Flowable<List<Forecastday>> getDaily(String id) {
        return mDao.getDaily(id);
    }

    public Flowable<List<Hour>> getNext24(String id) {
        return mDao.getNext24(id, OffsetDateTime.now(ZoneOffset.UTC).toEpochSecond());
    }

    /**
     * Список дат для ViewPager
     */
    public Flowable<List<Long>> getDate() {
        return mDao.getDate(ISharePreference.getLocationId(),
                (int) OffsetDateTime
                        .now(ZoneOffset.UTC)
                        .toLocalDate()
                        .atTime(LocalTime.MIDNIGHT)
                        .toEpochSecond(ZoneOffset.UTC));
    }

    public Flowable<Integer> getTimeZone() {
        return mDao.getTimeZone(ISharePreference.getLocationId());
    }

    /**
     * Данные для DayFragment: восзод, закат и прогноз на утро день и вечер
     */
    public Flowable<List<BaseItem>> getPartOfDay(long date) {
        String id = ISharePreference.getLocationId();
        return mDao.getTimeZone(ISharePreference.getLocationId()).flatMap(new Function<Integer, Flowable<List<Hour>>>() {
            @Override
            public Flowable<List<Hour>> apply(Integer integer) throws Exception {
                Long[] dates = IDateFormatter.getPartsOfDay(date, integer);
                return mDao.getPartOfDay(id, dates[0], dates[1], dates[2], dates[3]);
            }
        }, new BiFunction<Integer, List<Hour>, List<BaseItem>>() {
            @Override
            public List<BaseItem> apply(Integer integer, List<Hour> hours) throws Exception {
                List<BaseItem> result = new ArrayList<>();
                for (Hour hour : hours) {
                    result.add(new PartDayItem(hour, integer));
                }
                return result;
            }
        });
    }

    public Flowable<List<BaseItem>> getDayData(long date) {
        return Flowable.zip(getPartOfDay(date), mDao.getAstro(ISharePreference.getLocationId(), date), new BiFunction<List<BaseItem>, List<Astro>, List<BaseItem>>() {
            @Override
            public List<BaseItem> apply(List<BaseItem> baseItems, List<Astro> astros) throws Exception {
                baseItems.add(0, new SunStateItem(astros.get(0)));
                return baseItems;
            }
        });
    }

    /**
     * Данные для NavigationView: список лкаций с температурой и иконкой
     */

    public Flowable<List<ForecastResponse>> getCurrent() {
        return mDao.getAllLocation();
    }

    public Flowable<List<Hour>> getLastHours() {
        OffsetDateTime dateTime = OffsetDateTime.now(ZoneOffset.UTC);
        long time = dateTime.plusHours(1).withMinute(0).withSecond(0).withNano(0).toEpochSecond();
        return mDao.getLastHour(time);
    }

    /**
     * Данные для обновления погоды
     */
    public Single<DataLocation> getLocation(String id) {
        return mDao.getLocation(id).subscribeOn(Schedulers.io());
    }

    public void update(ForecastResponse response) {
        Executors.newFixedThreadPool(Integer.MAX_VALUE).submit(() -> mDao.update(response));
    }

    /**
     * При запуске приложения берет несвежие данные для обновления
     */
    public Observable<ForecastResponse> getNotFresh() {
        return mDao.getAllForecast()
                .subscribeOn(Schedulers.io())
                .toObservable()
                .flatMap(Observable::fromIterable)
                .filter(ForecastResponse::isNotCurrent)
                .filter(ForecastResponse::isNotFresh)
                .doOnNext(response -> ILogger.LogTimestamp(TAG, response));
    }

    // Метод возвращает погоду для ближайшего часа. Используеься, когда данные текущего прогноза старше 30 минут
    public Flowable<List<Hour>> getCurrentHour() {
        OffsetDateTime dateTime = OffsetDateTime.now(ZoneOffset.UTC);
        // Если прошло больше половины текущего часа, берет данные следующего часа
        long time = dateTime.plusHours(1).withMinute(0).withSecond(0).withNano(0).toEpochSecond();
        return mDao.getCurrentHour(ISharePreference.getLocationId(), time);
    }

    public Flowable<List<ForecastResponse>> getCurrentLocForecast() {
        return mDao.getCurrentLocationForecast();
    }

    public Flowable<List<Hour>> getCurrentLocLastHour() {
        OffsetDateTime dateTime = OffsetDateTime.now(ZoneOffset.UTC);
        long time = dateTime.plusHours(1).withMinute(0).withSecond(0).withNano(0).toEpochSecond();
        return mDao.getCurrentLocationLastHour(time);
    }
}
