package com.app.fevir.interfaces.web.di;

import com.app.fevir.interfaces.web.model.OpenModel;
import com.app.fevir.interfaces.web.presenter.OpenPresenter;
import com.app.fevir.interfaces.web.presenter.OpenPresenterImpl;
import com.app.fevir.network.ApiModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = ApiModule.class)
public class OpenModule {
    private OpenPresenter.View view;

    public OpenModule(OpenPresenter.View view) {
        this.view = view;
    }

    @Provides
    public OpenPresenter provideOpenPresenter(OpenPresenterImpl openPresenter) {
        return openPresenter;
    }

    @Provides
    public OpenPresenter.View provideView() {
        return view;
    }

    @Provides
    OpenModel provideOpenModel() {
        return new OpenModel();
    }
}
