package com.app.fevir.deligate.component;

import com.app.fevir.deligate.MyApplication;
import com.app.fevir.deligate.module.ApplicationModule;

import dagger.Component;

@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(MyApplication application);
}
