package com.app.fevir.movie.list.presenter;

import com.app.fevir.network.api.Cards;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class MovieListPresenterImpl implements MovieListPresenter {
    private final View view;
    private final Retrofit retrofit;
    private PublishSubject<String> requestPublisher;
    private int requestPage = 0;

    @Inject
    public MovieListPresenterImpl(View view, Retrofit retrofit) {
        this.view = view;
        this.retrofit = retrofit;

        initSubject();
    }

    private void initSubject() {
        requestPublisher = PublishSubject.create();
        requestPublisher
                .throttleWithTimeout(100, TimeUnit.MILLISECONDS)
                .onBackpressureBuffer()
                .observeOn(Schedulers.io())
                .subscribe(menuTitle -> retrofit.create(Cards.class)
                        .getCardsInfo(menuTitle, requestPage)
                        .doOnNext(cardInfo -> requestPage++)
                        .flatMap(cardInfo -> Observable.from(cardInfo.getContent()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(view::addCard, Throwable::printStackTrace), Throwable::printStackTrace);
    }

    @Override
    public void onLoadInfo(String menu_title) {
        requestPublisher.onNext(menu_title);
    }
}
