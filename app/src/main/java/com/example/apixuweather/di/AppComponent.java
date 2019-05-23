package com.example.apixuweather.di;

import com.example.apixuweather.repo.DataBaseModel;
import com.example.apixuweather.repo.IconManager;
import com.example.apixuweather.repo.LocationModel;
import com.example.apixuweather.repo.NetModel;
import com.example.apixuweather.service.LocationService;
import com.example.apixuweather.ui.daily.DailyViewModel;
import com.example.apixuweather.ui.daily.DayViewModel;
import com.example.apixuweather.ui.navigation.NavigationViewModel;
import com.example.apixuweather.ui.root.RootViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RestModule.class, AppModule.class})
public interface AppComponent {

    // repo
    void inject(LocationModel model);
    void inject(NetModel model);
    void inject(DataBaseModel model);
    void inject(IconManager manager);

    // service
    void inject(LocationService service);

    // viewModel
    void inject(RootViewModel model);
    void inject(DayViewModel model);
    void inject(DailyViewModel model);
    void inject(NavigationViewModel model);
}
