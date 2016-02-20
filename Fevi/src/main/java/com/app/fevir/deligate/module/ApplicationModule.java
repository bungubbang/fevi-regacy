package com.app.fevir.deligate.module;

import com.app.fevir.deligate.MyApplication;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private MyApplication myApplication;

    public ApplicationModule(MyApplication myApplication) {
        this.myApplication = myApplication;
    }

    @Provides
    public MyApplication myApplication() {
        return myApplication;
    }
}
