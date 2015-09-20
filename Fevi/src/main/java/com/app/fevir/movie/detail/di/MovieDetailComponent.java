package com.app.fevir.movie.detail.di;

import com.app.fevir.movie.detail.MovieDetailActivity;

import dagger.Component;

@Component(modules = {MovieDetailModule.class})
public interface MovieDetailComponent {
    void inject(MovieDetailActivity movieActivity);
}
