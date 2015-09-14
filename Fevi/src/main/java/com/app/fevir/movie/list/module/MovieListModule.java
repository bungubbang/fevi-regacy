package com.app.fevir.movie.list.module;

import com.app.fevir.movie.list.presenter.MovieListPresenter;
import com.app.fevir.movie.list.presenter.MovieListPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class MovieListModule {

    private final MovieListPresenter.View view;

    public MovieListModule(MovieListPresenter.View view) {this.view = view;}

    @Provides
    public MovieListPresenter providePresenter() {
        return new MovieListPresenterImpl(view);
    }
}
