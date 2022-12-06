package com.softaai.realtimelocation.di.component;


import com.softaai.realtimelocation.di.module.AppModule;
import com.softaai.realtimelocation.sharelocation.ShareLocationActivity;
import com.softaai.realtimelocation.tracklocation.TrackLocationActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(ShareLocationActivity client);

    void inject(TrackLocationActivity client);

}
