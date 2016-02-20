package com.app.fevir.interfaces.web.di;

import com.app.fevir.interfaces.web.OpenActivity;

import dagger.Component;

@Component(modules = {OpenModule.class})
public interface OpenComponent {

    void inject(OpenActivity openActivity);
}
