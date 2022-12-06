package com.softaai.realtimelocation.di.module;

import android.app.Application;
import android.content.Context;


import com.softaai.realtimelocation.ViewModelFactory;
import com.softaai.realtimelocation.data.DeviceLocationDataStore;
import com.softaai.realtimelocation.sharelocation.ShareLocationViewModel;
import com.softaai.realtimelocation.tracklocation.TrackLocationViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application app) {
        mApplication = app;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    ShareLocationViewModel provideShareLocationViewModel() {
        return new ShareLocationViewModel(mApplication, new DeviceLocationDataStore());
    }

    @Provides
    @Singleton
    TrackLocationViewModel provideTrackLocationViewModel() {
        return new TrackLocationViewModel(new DeviceLocationDataStore());
    }

    @Provides
    @Singleton
    ViewModelFactory provideViewModelFactory(
            ShareLocationViewModel shareLocationViewModel,
            TrackLocationViewModel trackLocationViewModel) {
        return new ViewModelFactory(shareLocationViewModel, trackLocationViewModel);
    }

}
