package com.softaai.realtimelocation;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.softaai.realtimelocation.di.component.AppComponent;
import com.softaai.realtimelocation.di.component.DaggerAppComponent;
import com.softaai.realtimelocation.di.module.AppModule;


public class App extends MultiDexApplication {

    private AppComponent mAppComponent;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AppModule appModule = new AppModule(this);
        mAppComponent = DaggerAppComponent.builder().appModule(appModule).build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
