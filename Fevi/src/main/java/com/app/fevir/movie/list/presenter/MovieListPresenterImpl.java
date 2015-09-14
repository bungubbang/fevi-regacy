package com.app.fevir.movie.list.presenter;

import android.util.Log;

import javax.inject.Inject;

public class MovieListPresenterImpl implements MovieListPresenter {
    private final View view;

    @Inject
    public MovieListPresenterImpl(View view) {
        this.view = view;
    }

    @Override
    public void log() {
        Log.d("INFO", "hahahahaha");
    }
}
