package com.app.fevir.movie.list.module;

import com.app.fevir.movie.list.presenter.MovieListPresenter;
import com.app.fevir.movie.list.presenter.MovieListPresenterImpl;
import com.app.fevir.network.ApiModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = ApiModule.class)
public class MovieListModule {

    private final MovieListPresenter.View view;

    public MovieListModule(MovieListPresenter.View view) {
        this.view = view;
    }

    @Provides
    public MovieListPresenter providePresenter(MovieListPresenterImpl movieListPresenter) {
        return movieListPresenter;
    }

    @Provides
    public MovieListPresenter.View provideView() {
        return view;
    }
}
