package com.app.fevir.movie.detail.di;

import com.app.fevir.movie.detail.presenter.MovieDetailPresenter;
import com.app.fevir.movie.detail.presenter.MovieDetailPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class MovieDetailModule {

    private MovieDetailPresenter.View view;

    public MovieDetailModule(MovieDetailPresenter.View view) {
        this.view = view;
    }

    @Provides
    public MovieDetailPresenter provideMovieDetailPresenter(MovieDetailPresenterImpl movieDetailPresenter) {
        return movieDetailPresenter;
    }

    @Provides
    public MovieDetailPresenter.View provideView() {
        return view;
    }


}
