package com.app.fevir.movie.list.component;

import com.app.fevir.movie.list.MovieListFragment;
import com.app.fevir.movie.list.module.MovieListModule;
import com.app.fevir.movie.list.presenter.MovieListPresenter;

import dagger.Component;

@Component(modules = {MovieListModule.class})
public interface MovieListComponent {

    void inject(MovieListFragment fragment);

    MovieListPresenter movieListPresenter();

}
