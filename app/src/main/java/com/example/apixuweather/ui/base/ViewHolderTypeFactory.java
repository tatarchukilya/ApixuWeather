package com.example.apixuweather.ui.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apixuweather.exeption.TypeNotSupportedException;
import com.example.apixuweather.ui.daily.recycler.PartDayViewHolder;
import com.example.apixuweather.ui.daily.recycler.SunStateViewHolder;
import com.example.apixuweather.ui.navigation.recycler.CurrentLocationViewHolder;
import com.example.apixuweather.ui.navigation.recycler.LocationViewHolder;
import com.example.apixuweather.ui.navigation.recycler.SearchViewHolder;
import com.example.apixuweather.ui.root.recycler.DailyViewHolder;
import com.example.apixuweather.ui.root.recycler.DetailNowViewHolder;
import com.example.apixuweather.ui.root.recycler.HourViewHolder;
import com.example.apixuweather.ui.root.recycler.HoursViewHolder;
import com.example.apixuweather.ui.root.recycler.WeatherNowViewHolder;
import com.example.apixuweather.ui.settings.recycler.NotificationItem;
import com.example.apixuweather.ui.settings.recycler.NotificationViewHolder;
import com.example.apixuweather.ui.settings.recycler.PressureViewHolder;
import com.example.apixuweather.ui.settings.recycler.TempViewHolder;
import com.example.apixuweather.ui.settings.recycler.WindViewHolder;

import androidx.annotation.LayoutRes;

public class ViewHolderTypeFactory {

    BaseViewHolder createViewHolder(ViewGroup parent, @LayoutRes int type, ViewHolderTouchListener listener) {

        View view = LayoutInflater.from(parent.getContext()).inflate(type, parent, false);

        switch (type) {
            case WeatherNowViewHolder.LAYOUT:
                return new WeatherNowViewHolder(view, listener);
            case DailyViewHolder.LAYOUT:
                return new DailyViewHolder(view, listener);
            case HourViewHolder.LAYOUT:
                return new HourViewHolder(view, listener);
            case DetailNowViewHolder.LAYOUT:
                return new DetailNowViewHolder(view, listener);
            case HoursViewHolder.LAYOUT:
                return new HoursViewHolder(view, listener);
            case PartDayViewHolder.LAYOUT:
                return new PartDayViewHolder(view, listener);
            case SunStateViewHolder.LAYOUT:
                return new SunStateViewHolder(view, listener);
            case SearchViewHolder.LAYOUT:
                return new SearchViewHolder(view, listener);
            case CurrentLocationViewHolder.LAYOUT:
                return new CurrentLocationViewHolder(view, listener);
            case LocationViewHolder.LAYOUT:
                return new LocationViewHolder(view, listener);
            case TempViewHolder.LAYOUT:
                return new TempViewHolder(view, listener);
            case WindViewHolder.LAYOUT:
                return new WindViewHolder(view, listener);
            case PressureViewHolder.LAYOUT:
                return new PressureViewHolder(view, listener);
            case NotificationViewHolder.LAYOUT:
                return new NotificationViewHolder(view, listener);
            default:
                throw TypeNotSupportedException.create(String.format("LayoutType: %d", type));
        }
    }
}
